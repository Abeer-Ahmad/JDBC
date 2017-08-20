package TestCases;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import jdbc.JDriver;

public class Statement_Tester {

	private String protocol = "xmldb";
	private String tmp = System.getProperty("java.io.tmpdir");
	
	private Connection createUseDatabase(String databaseName) throws SQLException {
		Driver driver = new JDriver(); // creates a new driver
		Properties info = new Properties();
		File dbDir = new File(tmp + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
		info.put("path", dbDir.getAbsoluteFile());
		Connection connection = driver.connect("jdbc:" + protocol + "://localhost", info);
		Statement statement = connection.createStatement(); 
		statement.execute("CREATE DATABASE " + databaseName);
		statement.execute("USE " + databaseName);
		statement.close();
		return connection;
	}

	@Test
	// tests Statement behavior after closing
	public void testStatementExistance() throws SQLException {
		Connection connection = createUseDatabase("Test_Statement_Existance");
		try {
			Statement statement = connection.createStatement();
			statement.close();
			statement.execute("CREATE TABLE table1(col1 varchar, col2 int, col3 date)");
			Assert.fail("Expected SQLException Was Not Thrown!\nInValid Statement Was Executed!");
		} catch (SQLException e) {
			// >> expected exception
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!");
		}
		connection.close();	
	}
	
	@Test
	// tests creating multiple Statements
	public void testMultipleStatements() throws SQLException {
		Connection connection = createUseDatabase("Test_Multiple_Statements");
		try {
			Statement stmt1 = connection.createStatement();
			stmt1.execute("CREATE TABLE table1(col1 varchar, col2 int, col3 date)"); // creates new table
			stmt1.close();
			stmt1 = connection.createStatement(); //uses a new statement without closing the previous one
			stmt1.execute("Drop TABLE table1"); // dropping the created table
			Statement stmt2 = connection.createStatement(); //uses a new statement without closing the previous one
			stmt2.execute("CREATE TABLE table1(col1 varchar, col2 int, col3 date)"); // creates the same previous table
			stmt1.close();
			stmt1.close(); // closing the same statement twice
			stmt2.close();
		} catch (SQLException e) {
			Assert.fail("Error When Closing Statements!");
		}
		connection.close();
	}
	
	@Test
	//tests Execute
	public void testExecute() throws SQLException {
		Connection connection = createUseDatabase("Test_Execute");
		try {
			Statement statement = connection.createStatement();		
			boolean bool1 = statement.execute("CREATE TABLE table1(col1 varchar, col2 int, col3 date)");
			boolean bool2 = statement.execute("ALTER TABLE table1 ADD col4 float");
			assertFalse(bool1 || bool2);
			boolean bool3 = statement.execute("SELECT * FROM table1");
			assertFalse(bool3);
			statement.close();
		} catch (SQLException e) {
			Assert.fail("Unexpected SQLException Was Thrown!\nValid Statement Was Rejected!");
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!");
		}
		connection.close();
	}
	
	@Test
	//tests excuteUpdate with a matching query
	public void testExcuteUpdate1() throws SQLException {
		Connection connection = createUseDatabase("Test_ExecuteUpdate1");
		try{
			Statement statement = connection.createStatement();	
			statement.execute("CREATE TABLE table1(name varchar, age int, city varchar)");
			int counter = 0;
			counter += statement.executeUpdate("INSERT INTO table1 VALUES ('Khalaf', '20', 'El_Sa'eed')");
			counter += statement.executeUpdate("INSERT INTO table1 VALUES ('El_Dahshoury',60 , 'El_Sa'eed')");
			assertEquals(2, counter);
			statement.executeUpdate("INSERT INTO table1 VALUES 'El_Dahshoury',60 , 'El_Sa'eed'"); // missing () after VALUES >> invalid
			Assert.fail("Expected SQLException Was Not Thrown!\nInValid Statement Was Executed!");
			statement.close();
		} catch (SQLException e) {
			// >> expected exception
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!"); 
		}
		connection.close();
	}
	
	@Test
	//tests excuteUpdate with a non-matching query
	public void testExcuteUpdate2() throws SQLException {
		Connection connection = createUseDatabase("Test_ExecuteUpdate2");
		try{
			Statement statement = connection.createStatement();	
			statement.execute("CREATE TABLE table1(name varchar, age int, city varchar)");
			statement.executeUpdate("SELECT * FROM table1");
			Assert.fail("Expected SQLException Was Not Thrown!\nInValid Statement Was Executed!");
			statement.close();
		} catch (SQLException e) {
			// >> expected exception
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!"); 
		}
		connection.close();
	}
	
	@Test
	//tests excuteQuery with updateCount
	public void testExcuteQueryWithUpdateCount() throws SQLException {
		Connection connection = createUseDatabase("Test_ExcuteQuery_With_UpdateCount");
		try{
			Statement statement = connection.createStatement();	
			statement.execute("CREATE TABLE table1(name varchar, age int, city varchar)");
			statement.executeUpdate("INSERT INTO table1 VALUES ('Khalaf', '20', 'El_Sa'eed')");
			statement.executeUpdate("INSERT INTO table1 VALUES ('El_Dahshoury',60 , 'El_Sa'eed')");
			ResultSet result = statement.executeQuery("SELECT * FROM table1");
			int updateCount = statement.getUpdateCount();
			assertEquals(-1, updateCount);
			result.close();
			statement.close();
		} catch (SQLException e) {
			Assert.fail("Unexpected SQLException Was Thrown!\nValid Statement Was Rejected!");
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!");
		}
		connection.close();
	}
	
	@Test
	//tests getConnection
	public void testGetConnection() throws SQLException {
		Connection connection = createUseDatabase("Test_GetConnection");
		try{
			Statement statement = connection.createStatement();
			connection.close(); // also closes the statement
			statement.close(); // already closed >> no effect
			statement.getUpdateCount();
			Assert.fail("Using Closed Statement!");
		} catch (SQLException e) {
			// expected exception
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!"); 
		}
		connection.close();
	}
	
	@Test
	//tests getConnection
	public void testBatch() throws SQLException {
		Connection connection = createUseDatabase("Test_Batch");
		try{
			Statement statement = connection.createStatement();
			statement.addBatch("CREATE TABLE table1(name varchar, age int, city varchar)");
			statement.addBatch("INSERT INTO table1 VALUES ('Khalaf', '20', 'El_Sa'eed')");
			statement.addBatch("INSERT INTO table1 VALUES ('El_Dahshoury',60 , 'El_Sa'eed')");
			statement.addBatch("ALTER TABLE table1 ADD col4 float");
			statement.addBatch("SELECT * FROM table1");
			statement.addBatch("Delete");
			int[] returned = statement.executeBatch();
			int[] expected = {0, 1, 1, 0, Statement.SUCCESS_NO_INFO, Statement.EXECUTE_FAILED};
			for (int i = 0; i < returned.length; i++) {
				assertEquals(expected[i], returned[i]);
			}
		} catch (SQLException e) {
			Assert.fail("Unexpected SQLException Was Thrown!");
		} catch (Throwable e) {
			Assert.fail("Unexpected Exception Was Thrown!");
		}
		connection.close();
	}
}