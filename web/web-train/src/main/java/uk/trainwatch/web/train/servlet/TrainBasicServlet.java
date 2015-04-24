/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.servlet;

import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.train.Train;
import uk.trainwatch.web.train.TrainMovement;

/**
 * Basic view of a train service
 * <p>
 * @author peter
 */
@WebServlet(name = "TrainBasicServlet", urlPatterns = "/train/basic/*")
public class TrainBasicServlet
        extends AbstractTrainServlet
{

    @Override
    protected void show( ApplicationRequest request, String rid, Train train )
    {
        getDetails( request, rid, train );
        getDelay( request, rid, train );
        getStatus( request, rid, train );

        // For basic we will remove details for those locations have been suppressed
        // and all passes
        train.getMovement().removeIf( TrainMovement.isNotOriginOrDestination.and( TrainMovement.isSuppressed.or( TrainMovement::isPass ) ) );

        request.renderTile( "train.basic" );
    }

}
