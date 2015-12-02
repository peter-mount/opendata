/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.cms.core.CmsCore;
import uk.trainwatch.cms.core.mediawiki.MediaWiki;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class CmsContextListener
        implements ServletContextListener
{

    @Inject
    private CmsCore cmsCore;

    @Inject
    private MediaWiki mediaWiki;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        cmsCore.init();
        mediaWiki.init();
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

}
