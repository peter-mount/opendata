/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.svg;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import uk.trainwatch.gis.PostGISUtils;

/**
 *
 * @author peter
 */
//@MetaInfServices(Utility.class)
public class SVGMapGenerator
//        extends DBUtility
{

    private static final Logger LOG = Logger.getLogger( SVGMapGenerator.class.getName() );

//    @SuppressWarnings("ThrowableInstanceNeverThrown")
//    public boolean parseArgs( CommandLine cmd )
//    {
//  //      super.parseArgs( cmd );
//
//        //cifFiles = Utility.getArgFileList( cmd );
//        //return !cifFiles.isEmpty();
//        return true;
//    }

    //@Override
    public void runUtility()
            throws Exception
    {
//        try( Connection con = PostGISUtils.postgisEnable( getConnection() ) ) {
//
//            SQL.deleteTable( con, "tpnm", "tile" );
//            exportMap( con );
//        }
//        catch( IOException ex ) {
//            throw new UncheckedIOException( ex );
//        }
//        catch( ParserConfigurationException ex ) {
//            throw new IllegalStateException( ex );
//        }
    }

//    private void exportMap( Connection con )
//            throws SQLException,
//                   IOException,
//                   ParserConfigurationException,
//                   XMLStreamException
//    {
//        LOG.log( Level.INFO, "Determining map bounds" );
//        Rectangle2D bounds = PostGISUtils.getMapBounds( con,
//                                                        "tpnm",
//                                                        "feat_graphictext",
//                                                        "feat_graphicvector",
//                                                        "feat_signal",
//                                                        "feat_track"
//        );
//
//        // Size of each tile in map units
//        final int tileSize = 256;
//
//        // ox = offset x
//        final int ox = (int) bounds.getMinX();
//
//        // nx = number tile on x axis
//        final int nx = (int) ((bounds.getMaxX() - bounds.getMinX()) / tileSize);
//
//        // ox = offset x
//        final int oy = (int) bounds.getMinY();
//
//        // nx = number tile on x axis
//        final int ny = (int) ((bounds.getMaxY() - bounds.getMinY()) / tileSize);
//
//        // Total tile count
//        final int total = nx * ny;
//        final int logstep = Math.max( 1, total / 100 );
//
//        LOG.log( Level.INFO, () -> "Tile size " + tileSize + " total (" + nx + "," + ny + ") = " + total + " tiles." );
//        LOG.log( Level.INFO, () -> "ox=" + ox + ", oy=" + oy );
//
////        TileWriter tileWrier = new TileWriter( con, nx );
////        TileToSVG toSVG = new TileToSVG( con, nx, ox, oy, tileSize );
////
////        IntStream.range( 0, total ).
////                boxed().
////                forEach( SQLConsumer.guard( tileId -> tileWrier.accept( tileId, toSVG.apply( tileId ) ) ) );
//    }
}
