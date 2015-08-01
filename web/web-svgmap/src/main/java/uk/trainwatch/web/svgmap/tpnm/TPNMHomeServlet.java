/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.AbstractHomeServlet;

/**
 *
 * @author peter
 */
@WebServlet(name = "TPNMHomeServlet",
            urlPatterns = {"/tpnm", "/tpnm/", "/tpnm/map"},
            initParams = {
                @WebInitParam(name = "/tpnm", value = "tpnm.home"),
                @WebInitParam(name = "/tpnm/", value = "tpnm.home"),
                @WebInitParam(name = "/tpnm/map", value = "tpnm.map")
            })
public class TPNMHomeServlet
        extends AbstractHomeServlet
{

}
