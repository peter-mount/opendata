/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.util.DaemonThreadFactory;

/**
 * Ensure we cleanup any resources on shutdown
 * <p>
 * @author Peter T Mount
 */
@WebListener
public class ContextListener
        implements ServletContextListener
{

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
        DaemonThreadFactory.INSTANCE.shutdown();
    }

}
