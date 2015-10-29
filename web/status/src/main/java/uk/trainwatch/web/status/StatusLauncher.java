/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.status;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author peter
 */
@ApplicationScoped
@WebListener
public class StatusLauncher
        implements ServletContextListener
{

    @Inject
    private JvmMemoryMonitor jvmMemoryMonitor;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        jvmMemoryMonitor.start();
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

}
