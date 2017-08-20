package dbms;

import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.JResultSet;

public interface ITable {

	
	public int insertTable(String currentDB, ArrayList<String> query) throws SQLException;
	public int deleteTable(String currentDB, ArrayList<String> query) throws SQLException;
	public JResultSet selectTable(String currentDB, ArrayList<String> query) throws SQLException;
	public int updateTable (String currentDB,ArrayList<String> query) throws SQLException;
	public void alterAdd(String currentDB, ArrayList<String> query) throws SQLException;
}
