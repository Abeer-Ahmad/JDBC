package dbms;


import java.sql.SQLException;

import java.util.ArrayList;

import jdbc.JResultSet;

public interface IDBMS {

	public boolean createDB(String dbName) throws SQLException;
    public boolean useDB(String tableName);
    public boolean dropDB(String dbName) throws SQLException;
    public int excuteUpdateQuery(int mode,ArrayList<String> query) throws Exception;
    public boolean changeDBStructure(int mode, ArrayList<String> query) throws SQLException;
    public JResultSet selectQuery(ArrayList<String> query) throws SQLException;
    public void changeTableStructure(int mode, ArrayList<String> query) throws SQLException;
}

