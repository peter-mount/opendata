/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * A Wrapper around {@link Connection} used with {@link ResultSetWrapper}.
 * <p>
 * @author peter
 */
public final class ConnectionWrapper
        implements Connection
{

    private final Connection con;
    private final ResultSetWrapper w;
    private PreparedStatement ps;

    ConnectionWrapper( ResultSetWrapper w, Connection con )
    {
        this.con = con;
        this.w = w;
    }

    /**
     * Set the ResultSet to persist
     * <p>
     * @param rs
     * <p>
     * @return
     * <p>
     * @throws SQLException
     */
    public ResultSet setResultSet( ResultSet rs )
            throws SQLException
    {
        w.setResultSet( rs );
        return rs;
    }

    @Override
    public void close()
            throws SQLException
    {
        try {
            if( ps != null ) {
                ps.close();
            }
        }
        finally {
            con.close();
        }
    }

    /**
     * Persist a PreparedStatement to close when we close this wrapper
     * <p>
     * @param ps
     * <p>
     * @return
     */
    public PreparedStatement setPreparedStatement( PreparedStatement ps )
    {
        this.ps = ps;
        return ps;
    }

    //<editor-fold defaultstate="collapsed" desc="Wrapper">
    @Override
    public boolean isWrapperFor( Class<?> iface )
            throws SQLException
    {
        return Connection.class.isAssignableFrom( iface ) || con.isWrapperFor( iface );
    }

    @Override
    public <T> T unwrap( Class<T> iface )
            throws SQLException
    {
        if( Connection.class.isAssignableFrom( iface ) ) {
            return (T) con;
        }
        return con.unwrap( iface );
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Connection">
    @Override
    public Statement createStatement()
            throws SQLException
    {
        return con.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement( String sql )
            throws SQLException
    {
        return con.prepareStatement( sql );
    }

    @Override
    public CallableStatement prepareCall( String sql )
            throws SQLException
    {
        return con.prepareCall( sql );
    }

    @Override
    public String nativeSQL( String sql )
            throws SQLException
    {
        return con.nativeSQL( sql );
    }

    @Override
    public void setAutoCommit( boolean autoCommit )
            throws SQLException
    {
        con.setAutoCommit( autoCommit );
    }

    @Override
    public boolean getAutoCommit()
            throws SQLException
    {
        return con.getAutoCommit();
    }

    @Override
    public void commit()
            throws SQLException
    {
        con.commit();
    }

    @Override
    public void rollback()
            throws SQLException
    {
        con.rollback();
    }

    @Override
    public boolean isClosed()
            throws SQLException
    {
        return con.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData()
            throws SQLException
    {
        return con.getMetaData();
    }

    @Override
    public void setReadOnly( boolean readOnly )
            throws SQLException
    {
        con.setReadOnly( readOnly );
    }

    @Override
    public boolean isReadOnly()
            throws SQLException
    {
        return con.isReadOnly();
    }

    @Override
    public void setCatalog( String catalog )
            throws SQLException
    {
        con.setCatalog( catalog );
    }

    @Override
    public String getCatalog()
            throws SQLException
    {
        return con.getCatalog();
    }

    @Override
    public void setTransactionIsolation( int level )
            throws SQLException
    {
        con.setTransactionIsolation( level );
    }

    @Override
    public int getTransactionIsolation()
            throws SQLException
    {
        return con.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings()
            throws SQLException
    {
        return con.getWarnings();
    }

    @Override
    public void clearWarnings()
            throws SQLException
    {
        con.clearWarnings();
    }

    @Override
    public Statement createStatement( int resultSetType, int resultSetConcurrency )
            throws SQLException
    {
        return con.createStatement( resultSetType, resultSetConcurrency );
    }

    @Override
    public PreparedStatement prepareStatement( String sql, int resultSetType, int resultSetConcurrency )
            throws SQLException
    {
        return con.prepareStatement( sql, resultSetType, resultSetConcurrency );
    }

    @Override
    public CallableStatement prepareCall( String sql, int resultSetType, int resultSetConcurrency )
            throws SQLException
    {
        return con.prepareCall( sql, resultSetType, resultSetConcurrency );
    }

    @Override
    public Map<String, Class<?>> getTypeMap()
            throws SQLException
    {
        return con.getTypeMap();
    }

    @Override
    public void setTypeMap(
            Map<String, Class<?>> map )
            throws SQLException
    {
        con.setTypeMap( map );
    }

    @Override
    public void setHoldability( int holdability )
            throws SQLException
    {
        con.setHoldability( holdability );
    }

    @Override
    public int getHoldability()
            throws SQLException
    {
        return con.getHoldability();
    }

    @Override
    public Savepoint setSavepoint()
            throws SQLException
    {
        return con.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint( String name )
            throws SQLException
    {
        return con.setSavepoint( name );
    }

    @Override
    public void rollback( Savepoint savepoint )
            throws SQLException
    {
        con.rollback( savepoint );
    }

    @Override
    public void releaseSavepoint( Savepoint savepoint )
            throws SQLException
    {
        con.releaseSavepoint( savepoint );
    }

    @Override
    public Statement createStatement( int resultSetType, int resultSetConcurrency, int resultSetHoldability )
            throws SQLException
    {
        return con.createStatement( resultSetType, resultSetConcurrency, resultSetHoldability );
    }

    @Override
    public PreparedStatement prepareStatement( String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability )
            throws SQLException
    {
        return con.prepareStatement( sql, resultSetType, resultSetConcurrency, resultSetHoldability );
    }

    @Override
    public CallableStatement prepareCall( String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability )
            throws SQLException
    {
        return con.prepareCall( sql, resultSetType, resultSetConcurrency, resultSetHoldability );
    }

    @Override
    public PreparedStatement prepareStatement( String sql, int autoGeneratedKeys )
            throws SQLException
    {
        return con.prepareStatement( sql, autoGeneratedKeys );
    }

    @Override
    public PreparedStatement prepareStatement( String sql, int[] columnIndexes )
            throws SQLException
    {
        return con.prepareStatement( sql, columnIndexes );
    }

    @Override
    public PreparedStatement prepareStatement( String sql, String[] columnNames )
            throws SQLException
    {
        return con.prepareStatement( sql, columnNames );
    }

    @Override
    public Clob createClob()
            throws SQLException
    {
        return con.createClob();
    }

    @Override
    public Blob createBlob()
            throws SQLException
    {
        return con.createBlob();
    }

    @Override
    public NClob createNClob()
            throws SQLException
    {
        return con.createNClob();
    }

    @Override
    public SQLXML createSQLXML()
            throws SQLException
    {
        return con.createSQLXML();
    }

    @Override
    public boolean isValid( int timeout )
            throws SQLException
    {
        return con.isValid( timeout );
    }

    @Override
    public void setClientInfo( String name, String value )
            throws SQLClientInfoException
    {
        con.setClientInfo( name, value );
    }

    @Override
    public void setClientInfo( Properties properties )
            throws SQLClientInfoException
    {
        con.setClientInfo( properties );
    }

    @Override
    public String getClientInfo( String name )
            throws SQLException
    {
        return con.getClientInfo( name );
    }

    @Override
    public Properties getClientInfo()
            throws SQLException
    {
        return con.getClientInfo();
    }

    @Override
    public Array createArrayOf( String typeName, Object[] elements )
            throws SQLException
    {
        return con.createArrayOf( typeName, elements );
    }

    @Override
    public Struct createStruct( String typeName, Object[] attributes )
            throws SQLException
    {
        return con.createStruct( typeName, attributes );
    }

    @Override
    public void setSchema( String schema )
            throws SQLException
    {
        con.setSchema( schema );
    }

    @Override
    public String getSchema()
            throws SQLException
    {
        return con.getSchema();
    }

    @Override
    public void abort( Executor executor )
            throws SQLException
    {
        con.abort( executor );
    }

    @Override
    public void setNetworkTimeout( Executor executor, int milliseconds )
            throws SQLException
    {
        con.setNetworkTimeout( executor, milliseconds );
    }

    @Override
    public int getNetworkTimeout()
            throws SQLException
    {
        return con.getNetworkTimeout();
    }

    //</editor-fold>
}
