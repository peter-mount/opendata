/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.tag;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import uk.trainwatch.util.CDIUtils;
import uk.trainwatch.web.ldb.cache.TrainCache;

/**
 *
 * @author peter
 */
public class TrainTag
        extends BodyTagSupport
{

    private static final Logger LOG = Logger.getLogger( TrainTag.class.getName() );

    private String rid;
    private String var;

    @Inject
    private TrainCache trainCache;

    @SuppressWarnings("LeakingThisInConstructor")
    public TrainTag()
    {
        CDIUtils.inject( this );
    }

    @Override
    public int doStartTag()
            throws JspException
    {
        if( var != null && rid != null ) {
            try {
                pageContext.setAttribute( var, trainCache.get( rid ) );
            }
            catch( SQLException ex ) {
                LOG.log( Level.SEVERE, ex, () -> "Failed to retrieve " + rid );
                pageContext.setAttribute( var, null );
            }
        }

        return SKIP_BODY;
    }

    public void setRid( String rid )
    {
        this.rid = rid;
    }

    public void setVar( String var )
    {
        this.var = var;
    }

}
