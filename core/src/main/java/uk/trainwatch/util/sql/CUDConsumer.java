/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * A consumer which will perform Create, Update or Delete operations on a Database
 * <p>
 * @author Peter T Mount
 * @param <T>
 */
public abstract class CUDConsumer<T>
        implements SQLConsumer<T>
{

    private final Connection con;
    private final PreparedStatement insert;
    private final PreparedStatement update;
    private final PreparedStatement delete;
    private int inserted, updated, deleted, total;

    public CUDConsumer( Connection con, String insert, String update, String delete )
    {
        try
        {
            this.con = Objects.requireNonNull( con );

            this.insert = insert == null ? null : con.prepareStatement( insert );
            this.update = update == null ? null : con.prepareStatement( update );
            this.delete = delete == null ? null : con.prepareStatement( delete );
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    protected final Connection getConnection()
    {
        return con;
    }

    @Override
    public String toString()
    {
        return getTotal() + ", " + getInserted() + " new, " + getUpdated() + " amended, " + getDeleted() + " deleted.";
    }

    protected final PreparedStatement getInsert()
    {
        return insert;
    }

    protected final PreparedStatement getUpdate()
    {
        return update;
    }

    protected final PreparedStatement getDelete()
    {
        return delete;
    }

    public final int getInserted()
    {
        return inserted;
    }

    protected final void inserted()
    {
        inserted++;
    }

    public final int getUpdated()
    {
        return updated;
    }

    protected final void updated()
    {
        updated++;
    }

    public final int getDeleted()
    {
        return deleted;
    }

    protected final void deleted()
    {
        deleted++;
    }

    public final int getTotal()
    {
        return total;
    }

    protected final void totaled()
    {
        total++;
    }

}
