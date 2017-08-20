package dbms;

import java.sql.SQLException;

import java.util.ArrayList;

import jdbc.JResultSet;

public interface IDataBase {

	
	public boolean setDataBase (String dbName);
	public boolean changeDBStructure (int mode, ArrayList<String> query) throws SQLException;
	public void changeTableStructure(int mode, ArrayList<String> query) throws SQLException;
	public boolean createTable(String tableName,ArrayList<String> fields) throws SQLException;
	public boolean dropTable(String tableName) throws SQLException;
	public int updateTables(int mode,ArrayList <String> query) throws SQLException;
	public JResultSet selectQuery (ArrayList<String> query) throws SQLException;
}
