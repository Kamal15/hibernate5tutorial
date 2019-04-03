package org.hibernate.id;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.naming.ObjectNameNormalizer;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class CustomIncrementGenerator extends IncrementGenerator {

    private static final CoreMessageLogger LOG = CoreLogging.messageLogger( CustomIncrementGenerator.class );

    private Class returnClass;
    private String sql;

    private IntegralDataTypeHolder previousValueHolder;

    @Override
    public synchronized Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        if ( sql != null ) {
            initializePreviousValueHolder( session );
        }
        return previousValueHolder.makeValueThenIncrement();
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        returnClass = type.getReturnedClass();

        final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService( JdbcEnvironment.class );
        final ObjectNameNormalizer normalizer =
                (ObjectNameNormalizer) params.get( PersistentIdentifierGenerator.IDENTIFIER_NORMALIZER );

        String column = params.getProperty( "column" );
        if ( column == null ) {
            column = params.getProperty( PersistentIdentifierGenerator.PK );
        }
        column = normalizer.normalizeIdentifierQuoting( column ).render( jdbcEnvironment.getDialect() );

        String tableList = params.getProperty( "tables" );
        if ( tableList == null ) {
            tableList = params.getProperty( PersistentIdentifierGenerator.TABLES );
        }
        String[] tables = StringHelper.split( ", ", tableList );

        final String schema = normalizer.toDatabaseIdentifierText(
                params.getProperty( PersistentIdentifierGenerator.SCHEMA )
        );
        final String catalog = normalizer.toDatabaseIdentifierText(
                params.getProperty( PersistentIdentifierGenerator.CATALOG )
        );

        StringBuilder buf = new StringBuilder();
        for ( int i = 0; i < tables.length; i++ ) {
            final String tableName = normalizer.toDatabaseIdentifierText( tables[i] );
            if ( tables.length > 1 ) {
                buf.append( "select max(" ).append( column ).append( ") as mx from " );
            }
            buf.append( Table.qualify( catalog, schema, tableName ) );
            if ( i < tables.length - 1 ) {
                buf.append( " union " );
            }
        }
        if ( tables.length > 1 ) {
            buf.insert( 0, "( " ).append( " ) ids_" );
            column = "ids_.mx";
        }

        sql = "select max(" + column + ") from " + buf.toString();
    }

    private void initializePreviousValueHolder(SessionImplementor session) {
        previousValueHolder = IdentifierGeneratorHelper.getIntegralDataTypeHolder( returnClass );

        long offset = Long.getLong("HIBERNATE_ID_OFFSET", 1);
        final boolean debugEnabled = LOG.isDebugEnabled();
        if ( debugEnabled ) {
            LOG.debugf( "Fetching initial value: %s", sql );
        }
        try {
            PreparedStatement st = session.getJdbcCoordinator().getStatementPreparer().prepareStatement( sql );
            try {
                ResultSet rs = session.getJdbcCoordinator().getResultSetReturn().extract( st );
                try {
                    if ( rs.next() ) {
                        previousValueHolder.initialize( rs, 0L );
                        if (previousValueHolder.eq(0L))
                            previousValueHolder.increment();
                        else 
                            previousValueHolder.add(offset);
                    }
                    else {
                        previousValueHolder.initialize( 1L );
                    }
                    sql = null;
                    if ( debugEnabled ) {
                        LOG.debugf( "First free id: %s", previousValueHolder.makeValue() );
                    }
                }
                finally {
                    session.getJdbcCoordinator().getResourceRegistry().release( rs, st );
                }
            }
            finally {
                session.getJdbcCoordinator().getResourceRegistry().release( st );
                session.getJdbcCoordinator().afterStatementExecution();
            }
        }
        catch (SQLException sqle) {
            final JdbcServices services = session.getFactory().getServiceRegistry().getService(JdbcServices.class);
            throw services.getSqlExceptionHelper().convert(
                    sqle,
                    "could not fetch initial value for increment generator",
                    sql
            );
        }
    }

}
