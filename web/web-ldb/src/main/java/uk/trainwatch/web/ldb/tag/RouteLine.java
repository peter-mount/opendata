/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.tag;

import java.sql.ResultSet;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class RouteLine
{

    /**
     * (SPHEADJ,GRVSEND,0,9,0,10,t)
     * <p>
     */
    public static final SQLFunction<ResultSet, RouteLine> fromSQL = rs -> new RouteLine(
            rs.getFloat( 3 ),
            rs.getFloat( 4 ),
            rs.getFloat( 5 ),
            rs.getFloat( 6 ),
            rs.getBoolean( 7 ),
            rs.getBoolean( 8 ),
            rs.getBoolean( 9 )
    );

    private final float sx;
    private final float sy;
    private final float ex;
    private final float ey;
    private final boolean canc;
    private final boolean pass;
    private final boolean stop;

    RouteLine( float sx, float sy, float ex, float ey, boolean canc, boolean pass, boolean stop )
    {
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
        this.canc = canc;
        this.pass = pass;
        this.stop = stop;
    }

    public float getSx()
    {
        return sx;
    }

    public float getSy()
    {
        return sy;
    }

    public float getEx()
    {
        return ex;
    }

    public float getEy()
    {
        return ey;
    }

    public boolean isCanc()
    {
        return canc;
    }

    public boolean isPass()
    {
        return pass;
    }

    public boolean isStop()
    {
        return stop;
    }

}
