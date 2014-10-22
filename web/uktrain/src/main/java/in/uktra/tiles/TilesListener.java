/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.tiles;

import javax.servlet.annotation.WebListener;
import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.web.startup.AbstractTilesListener;

/**
 *
 * @author Peter T Mount
 */
@WebListener
public class TilesListener
        extends AbstractTilesListener
{

    @Override
    protected TilesInitializer createTilesInitializer()
    {
        return new CompleteAutoloadTilesInitializer();
    }

}
