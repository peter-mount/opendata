/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.tools;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.Line;
import uk.trainwatch.nrod.signalmapeditor.map.Node;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class SignalCaptureImporter
        extends AbstractAction
{

    public SignalCaptureImporter()
    {
        super( "Import Signal Capture Logs" );
        putValue( SHORT_DESCRIPTION, "Imports a map from a Signal capture log file" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( MainFrame.fileChooser.showOpenDialog( MainFrame.frame ) == JFileChooser.APPROVE_OPTION ) {
            File file = MainFrame.fileChooser.getSelectedFile();
            MainFrame.setStatus( "Importing file " + file );
            // Run the import in a background thread, leaving Swing to run the UI
            ThreadQueue.executeLater( () -> importFile( file ) );
        }
    }

    private void importFile( File file )
    {
        try( BufferedReader r = new BufferedReader( new FileReader( file ) ) ) {
            // Reset the map with nothing in it
            Project.INSTANCE.newMap();

            SignalMap signalMap = Project.INSTANCE.getMap();

            // Visit each parsed line building the new map
            Map<String, Node> map = new HashMap<>();
            r.lines().
                    map( this::decodeLine ).
                    filter( Objects::nonNull ).
                    // Note: Always update the map in the Swing Thread
                    forEach( c -> c.visit( map, signalMap ) );

            // Bulk load into the map - this saves excessive events being issued
            signalMap.addAll( map.values() );

            MainFrame.setStatus( "Import complete" );
        }
        catch( IOException ex ) {
            MainFrame.setStatus( "Import failed: " + ex.getMessage() );
            ex.printStackTrace();
        }
    }

    private boolean ignore( String id )
    {
        return "COUT".equals( id ) || "STIN".equals( id );
    }

    private LogEntry decodeLine( String l )
    {
        if( l.length() > 26 ) {
            String dt = l.substring( 0, 19 );
            String s[] = l.substring( 26 ).
                    replaceAll( "([ ]+)", " " ).
                    split( " " );
            switch( s[0] ) {
                case "Initialising":
                    return new Area( dt, s[s.length - 1] );

                case "Move":
                    if( ignore( s[2] ) ) {
                        return new Put( s[4] );
                    }
                    else if( ignore( s[4] ) ) {
                        return new Put( s[2] );
                    }
                    return new Move( s[2], s[4] );

                case "Put":
                case "Canc":
                    return ignore( s[2] ) ? null : new Put( s[2] );

                default:
                    return null;
            }
        }
        return null;
    }

    private static interface LogEntry
    {

        void visit( Map<String, Node> map, SignalMap signalMap );
    }

    private static class Area
            implements LogEntry
    {

        private final String dt;
        private final String area;

        public Area( String dt, String area )
        {
            this.dt = dt;
            this.area = area;
        }

        @Override
        public void visit( Map<String, Node> map, SignalMap signalMap )
        {
            signalMap.setArea( area );
            signalMap.setTitle( "Imported signal map for " + area );
            signalMap.setNotes( signalMap.getTitle() + "\n\nCapture started " + dt );
        }
    }

    public static boolean present( Map<String, Node> map, int x, int y )
    {
        return map.values().
                stream().
                anyMatch( b -> b.getX() == x && b.getY() == y );
    }

    private static Berth newBerth( String id, int x, Map<String, Node> map, SignalMap signalMap )
    {
        Node n = map.computeIfAbsent( id,
                                      k -> {
                                          int y = 0;
                                          while( present( map, x, y ) ) {
                                              y++;
                                          }
                                          return new Berth( id, x, y, signalMap );
                                      } );
        return n instanceof Berth ? (Berth) n : null;
    }

    private static class Move
            implements LogEntry
    {

        private final String from;
        private final String to;

        public Move( String from, String to )
        {
            this.from = from;
            this.to = to;
        }

        @Override
        public void visit( Map<String, Node> map, SignalMap signalMap )
        {
            Berth fromBerth = newBerth( from, 0, map, signalMap );
            fromBerth.join( new Line( fromBerth, newBerth( to, fromBerth.getX() + 1, map, signalMap ) ) );
        }

    }

    private static class Put
            implements LogEntry
    {

        private final String berth;

        public Put( String berth )
        {
            this.berth = berth;
        }

        @Override
        public void visit( Map<String, Node> map, SignalMap signalMap )
        {
            newBerth( berth, 0, map, signalMap );
        }

    }

}
