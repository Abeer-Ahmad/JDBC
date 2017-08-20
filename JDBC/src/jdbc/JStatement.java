/**
 *
 */
package jdbc;
 
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
 
import dbms.Constants;
import dbms.DBMS;
import sql.SQLValidator;
import utilities.Logs;
 
public class JStatement implements Statement {
    private SQLValidator sqlV; 
    private JConnection connection;
    private JResultSet resultSet;
    private DBMS dbms;
    private ArrayList<String> query;
    private Queue<String> batch;
    private int mode;
    private int updated;
    private int updateCount;
    private boolean isClosed;
     private Logs logger;
 
 
    public JStatement(JConnection connection) {
        this.connection = connection;
        dbms = connection.getDBMS();
        sqlV = new SQLValidator();
        query = new ArrayList<String>();
        batch = new LinkedList<String>();
        updateCount = -1;
        updated = 0;
        mode = Constants.INVALID;
        isClosed = false;
         logger = new Logs();
    }
 
 
    // 1. Batch Group
    @Override
    public void addBatch(String sql) throws SQLException {
        checkExistance();
        batch.add(sql);
        logger.info("Query Is Added To The Batch Successfully!");
    }
 
    @Override
    public int[] executeBatch() throws SQLException {
        checkExistance();
        int[] counts = new int[batch.size()];
        int i = 0;
        while (!batch.isEmpty()) {
            try {
                String sql = batch.poll();
                execute(sql);
                if (updateCount >= 0) {
                    counts[i++] = updateCount; // executeUpdate
                } else {
                    counts[i++] = Statement.SUCCESS_NO_INFO; // executeQuery
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
                counts[i++] = Statement.EXECUTE_FAILED;
            }
        }
        logger.info("Batch Has Been Executed!");
        return counts;
    }
 
    @Override
    public void clearBatch() throws SQLException {
        checkExistance();
        batch.clear();
        logger.info("Batch Is Cleared!");
    }
 
    // 2. Execute Group
    @Override
    public boolean execute(String sql) throws SQLException {
        checkExistance();
        query = sqlV.validateOrder(sql);
        mode = sqlV.getMode();
        if (mode == Constants.SELECT_TABLE) {
            executeQuery(sql);
            if (resultSet.getSize() == 0)
                return false;
            return true;
        } else {
            executeUpdate(sql);
            return false;
        }
    }
 
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        checkExistance();
        try {
            query = sqlV.validateOrder(sql);
            mode = sqlV.getMode();
            if (mode != Constants.SELECT_TABLE) {
                logger.error("ExecuteQuery Is Not Applicable For This Statement");
                throw new SQLException("ExecuteQuery Is Not Applicable For This Statement");
            }
            updateCount = -1; // reset updateCount
            resultSet = dbms.selectQuery(query);
            resultSet.setStatement(this);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        logger.info("Query Has Been Executed!");
        return resultSet;
    }
 
    @Override
    public int executeUpdate(String sql) throws SQLException {
        checkExistance();
        try {      
            query = sqlV.validateOrder(sql);
            mode = sqlV.getMode();         
            updateCount = -1; // reset updateCount
            updated = 0;
            switch (mode) {
            case Constants.CREATE_DB: {
                dbms.createDB(query.get(0)); // return ??
                break;
            }
            case Constants.USE_DB: {
                dbms.useDB(query.get(0)); // return ??
                break;
            }
            case Constants.DROP_DB: {
                dbms.dropDB(query.get(0)); // return ??
                break;
            }
            case Constants.CREATE_TABLE: {             
                dbms.changeDBStructure(Constants.CREATE_TABLE, query); // return ??
                break;
            }
            case Constants.DROP_TABLE: {
                dbms.changeDBStructure(Constants.DROP_TABLE, query); // return ??
                break;
            }
            case Constants.DELETE: {
                updated = dbms.excuteUpdateQuery(Constants.DELETE, query);
                break;
            }
            case Constants.INSERT_REC: {               
                updated = dbms.excuteUpdateQuery(Constants.INSERT_REC, query);
                break;
            }
            case Constants.UPDATE_REC: {
                updated = dbms.excuteUpdateQuery(Constants.UPDATE_REC, query);
                break;
            }
            case Constants.ALTER_ADD: {
                dbms.changeTableStructure(Constants.ALTER_ADD, query);
                break;
            }
            case Constants.ALTER_DROP: {
                dbms.changeTableStructure(Constants.ALTER_DROP, query);
                break;
            }
            default: {
                logger.error("ExecuteUpdate Is Not Applicable For This Statement");
                throw new SQLException("ExecuteUpdate Is Not Applicable For This Statement");
            }
            }          
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
        logger.info("Query Has Been Executed!");
        updateCount = updated;
        return updateCount;
    }
 
    // 3. Get Group
    @Override
    public Connection getConnection() throws SQLException {
        checkExistance();
        return connection;
    }
 
    @Override
    public ResultSet getResultSet() throws SQLException {
        checkExistance();
        return resultSet;
    }
 
    @Override
    public int getUpdateCount() throws SQLException {
        checkExistance();
        return updateCount;
    }
 
    // 4. close
    @Override
    public void close() throws SQLException {
        isClosed = true;
        logger.info("Statement Has Been Closed!");
 
    }
 
    // 5. isClosed()
    @Override
    public boolean isClosed() throws SQLException {
        return isClosed;
    }
 
    // 6. checkExistance
    private void checkExistance() throws SQLException{
        if (connection.isClosed()) {
            logger.error("The Connection That Created This Statement Has Been Closed!");
            throw new SQLException("The Connection That Created This Statement Has Been Closed!");
        }
        if (isClosed) {
            logger.error("The Statement Has Been Closed!");
            throw new SQLException("The Statement Has Been Closed!");
        }
    }
 
    // 6. Unimplemented Methods
    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public void cancel() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void clearWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void closeOnCompletion() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public boolean execute(String arg0, int arg1) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public boolean execute(String arg0, int[] arg1) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public boolean execute(String arg0, String[] arg1) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int executeUpdate(String arg0, int arg1) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int executeUpdate(String arg0, int[] arg1) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int executeUpdate(String arg0, String[] arg1) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getFetchDirection() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getFetchSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getMaxFieldSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getMaxRows() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public boolean getMoreResults() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public boolean getMoreResults(int arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getQueryTimeout() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getResultSetConcurrency() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getResultSetHoldability() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public int getResultSetType() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public boolean isPoolable() throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public void setCursorName(String arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public void setEscapeProcessing(boolean arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void setFetchDirection(int arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void setFetchSize(int arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
    }
 
    @Override
    public void setMaxFieldSize(int arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void setMaxRows(int arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void setPoolable(boolean arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
    @Override
    public void setQueryTimeout(int arg0) throws SQLException {
        throw new java.lang.UnsupportedOperationException();
 
    }
 
}