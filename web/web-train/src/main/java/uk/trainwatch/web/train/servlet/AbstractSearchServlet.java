/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.servlet;

import java.util.Map;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
public abstract class AbstractSearchServlet
        extends AbstractServlet
{

    protected final void showHome( ApplicationRequest request )
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "stations", TrainLocationFactory.INSTANCE.getStations() );

        request.renderTile( "train.home" );
    }
}
