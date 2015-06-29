/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.servlet;

import java.util.Map;
import javax.inject.Inject;
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

    @Inject
    protected TrainLocationFactory trainLocationFactory;
    
    protected final void showHome( ApplicationRequest request )
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "stations", trainLocationFactory.getStations() );

        request.renderTile( "train.home" );
    }
}
