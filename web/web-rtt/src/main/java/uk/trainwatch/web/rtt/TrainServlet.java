/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rtt;

import javax.servlet.annotation.WebServlet;

/**
 *
 * @author peter
 */
@WebServlet( name = "TrainServlet", urlPatterns = "/rtt/train/*" )
public class TrainServlet
        extends AbstractTrainServlet
{

    @Override
    protected String getTile()
    {
        return "rtt.details";
    }

}
