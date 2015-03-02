/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.osgb.codepoint.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.osgb.codepoint.PostCode;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.KeyValue;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author peter
 */
@MetaInfServices(Utility.class)
public class CodePointImport
        extends DBUtility {

    private static final Logger LOG = Logger.getLogger(CodePointImport.class.getName());
    private static final String SCHEMA = "gis";

    private static final String CP_SQL = "INSERT INTO " + SCHEMA + ".codepoint "
            + "(postcode,pqi,eastings,northings,country,county,district,ward,nhsregion,nhsha)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?)";

    private Consumer<Path> consumer;
    private List<Path> cifFiles;
    private int lineCount;
    private File codeFile;
    private File nhsCodeFile;

    private Map<String, Integer> codeLookup = new HashMap<>();
    private Map<String, Integer> nhsLookup = new HashMap<>();

    public CodePointImport() {
        super();
        getOptions().
                addOption("codes", "c", true, "Import codes from file").
                addOption("nhs", "n", true, "Import nhs codes from file");
    }

    @Override
    public boolean parseArgs(CommandLine cmd) {
        super.parseArgs(cmd);

        codeFile = new File(cmd.getOptionValue('c'));
        nhsCodeFile = new File(cmd.getOptionValue('n'));

        cifFiles = Utility.getArgFileList(cmd);

        return !cifFiles.isEmpty();
    }

    @Override
    public void runUtility()
            throws Exception {
        importFiles(cifFiles, this::importer);

        LOG.log(Level.INFO, () -> "Imported " + lineCount + " postcodes.");
    }

    @Override
    protected void initDB(Connection con)
            throws SQLException {
        LOG.log(Level.INFO, "Clearing down CodePoint database");

        SQL.deleteIdTable(con, SCHEMA, "codepoint");

        lineCount = 0;

        SQL.deleteIdTable(con, SCHEMA, "codepoint_code");

        LOG.log(Level.INFO, () -> "Importing " + codeFile + " into code table");
        con.setAutoCommit(false);

        importCode(con, "codepoint_code", r -> r.get(1), r -> r.get(0), codeFile);
        importCode(con, "codepoint_nhs", r -> r.get(0), r -> r.get(1), nhsCodeFile);

        try (Statement s = con.createStatement()) {
            LOG.log(Level.INFO, "Loading lookup table");
            codeLookup = SQL.stream(s.executeQuery("SELECT code,id FROM " + SCHEMA + ".codepoint_code"), rs -> new KeyValue<>(rs.getString(1), rs.getInt(2))).
                    collect(KeyValue.toMap());

            nhsLookup = SQL.stream(s.executeQuery("SELECT code,id FROM " + SCHEMA + ".codepoint_nhs"), rs -> new KeyValue<>(rs.getString(1), rs.getInt(2))).
                    collect(KeyValue.toMap());
        }
    }

    private void importCode(Connection con,
            String table,
            Function<CSVRecord, String> code,
            Function<CSVRecord, String> name,
            File file) throws SQLException {

        try (CSVParser p = new CSVParser(new FileReader(file), CSVFormat.EXCEL)) {
            LOG.log(Level.INFO, () -> "Clearing down " + table);

            SQL.deleteIdTable(con, SCHEMA, table);
            try (Statement s = con.createStatement()) {
                s.executeUpdate("INSERT INTO " + SCHEMA + "." + table + " VALUES (0,'','')");
            }

            try (PreparedStatement ps = con.prepareStatement("INSERT INTO " + SCHEMA + "." + table + " (code,name) VALUES(?,?)")) {

                p.getRecords().forEach(
                        SQLConsumer.guard(r -> SQL.executeUpdate(ps, code.apply(r), name.apply(r)))
                );

                con.commit();

                LOG.log(Level.INFO, () -> "Imported " + p.getRecordNumber() + " entries into code table");
            }
        } catch (SQLException ex) {
            con.rollback();
            throw new UncheckedSQLException(ex);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void importer(Connection con, Path path) throws SQLException {
        LOG.log(Level.INFO, () -> "Importing " + path);

        try {
            try (CSVParser parser = new CSVParser(new FileReader(path.toFile()), CSVFormat.DEFAULT)) {
                List<CSVRecord> records = parser.getRecords();

                // Do the import in one massive transaction
                con.setAutoCommit(false);

                try (PreparedStatement ps = con.prepareStatement(CP_SQL)) {
                    records.stream().
                            map(r -> new PostCode(
                                            r.get(0),
                                            Integer.parseInt(r.get(1)),
                                            Integer.parseInt(r.get(2)),
                                            Integer.parseInt(r.get(3)),
                                            r.get(4), r.get(5), r.get(6), r.get(7), r.get(8), r.get(9))).
                            forEach(SQLConsumer.guard(pc -> {
                                SQL.executeUpdate(ps,
                                        pc.getPostCode(),
                                        pc.getPqi(),
                                        pc.getEastings(), pc.getNorthings(),
                                        codeLookup.getOrDefault(pc.getCountry(), 0),
                                        codeLookup.getOrDefault(pc.getCounty(), 0),
                                        codeLookup.getOrDefault(pc.getDistrict(), 0),
                                        codeLookup.getOrDefault(pc.getWard(), 0),
                                        nhsLookup.getOrDefault(pc.getNhsRegion(), 0),
                                        nhsLookup.getOrDefault(pc.getNhs(), 0)
                                );
                            }));
                }

                con.commit();

                int parseCount = records.size();
                lineCount += parseCount;
                LOG.log(Level.INFO, () -> "Parsed " + parseCount);
            }

        } catch (IOException ex) {
            con.rollback();
            LOG.log(Level.SEVERE, null, ex);
            throw new UncheckedIOException(ex);
        } catch (UncheckedSQLException ex) {
            con.rollback();
            LOG.log(Level.SEVERE, null, ex);
            throw ex.getCause();
        } catch (Exception ex) {
            con.rollback();
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

}
