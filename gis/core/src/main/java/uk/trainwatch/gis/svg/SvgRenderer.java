/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.svg;

import java.sql.SQLException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author peter
 */
public interface SvgRenderer
{

    void render( XMLStreamWriter w, SvgBounds tileBounds )
            throws SQLException,
                   XMLStreamException,
                   ClassNotFoundException;

}
