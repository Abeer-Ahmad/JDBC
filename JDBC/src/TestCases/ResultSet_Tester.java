package TestCases;
 
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 
public class ResultSet_Tester {
 
    private String protocol = "xmldb";
    private String tmp = System.getProperty("java.io.tmpdir");
    private Statement statement;
    private ResultSet resultSet;
 
    public Connection connect(String databaseName) throws SQLException {
        Driver driver = new JDriver(); // creates a new driver
        Properties info = new Properties();
        File dbDir = new File(tmp + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
        info.put("path", dbDir.getAbsoluteFile());
        Connection connection = driver.connect("jdbc:" + protocol + "://localhost", info);
        statement = connection.createStatement();
        statement.execute("CREATE DATABASE " + databaseName);
        statement.execute("USE " + databaseName);
        statement.execute("CREATE TABLE table (name varchar, age int, tax float, salaryDate date)");
        statement.execute("INSERT INTO table VALUES ('abeer', 20, 3.3, '2011-11-05')");
        statement.execute("INSERT INTO table VALUES ('yasmin', 21, 5.2, '2014-12-05')");
        statement.execute("INSERT INTO table VALUES ('salma', 22, -6, '2016-01-12')");
        statement.execute("SELECT * FROM table");
        //statement.close();
        return connection;
    }
   
    @Test
    // tests cursor manipulation methods
    public void manipulateCursor() throws SQLException {
        try {
            Connection connection = connect("Test_Manipulate_Cursor");
            resultSet = statement.getResultSet();
           
            boolean bool;
            bool = resultSet.isBeforeFirst();
            assertTrue(bool);
           
            bool = resultSet.absolute(-1);
            assertTrue(bool);
            assertTrue(resultSet.isLast());
           
            bool = resultSet.next();
            assertFalse(bool);
           
            bool = resultSet.isAfterLast();
            assertTrue(bool);
           
            int counter = 0;
            while (resultSet.previous()) {
                counter++;
            }
            assertEquals(3, counter);
           
            bool = resultSet.isBeforeFirst();
            assertTrue(bool);
           
            resultSet.next();
            bool = resultSet.isFirst();
            assertTrue(bool);
           
        } catch (SQLException e) {
            Assert.fail("Unexpected SQLException Was Thrown!");
        } catch (Throwable e) {
            Assert.fail("Unexpected Exception Was Thrown!");
        }
        // fail??
    }
   
    @Test
    // tests getInt method
    public void testGetInt() throws SQLException {
        Connection connection = connect("Test_Get_Int");
        resultSet = statement.getResultSet();
        resultSet.first();
        resultSet.next();
        int x = resultSet.getInt(2);
        assertEquals(21, x);
    }
   
    @Test
    // tests getString method
    public void testGetString() throws SQLException {
        Connection connection = connect("Test_Get_String");
        resultSet = statement.getResultSet();
        resultSet.last();
        String s = resultSet.getString("name");
        assertEquals("salma", s);
    }
   
    @Test
    // tests getFloat method
    public void testGetFloat() throws SQLException {
        Connection connection = connect("Test_Get_Float");
        resultSet = statement.getResultSet();
        resultSet.first();
        float f = resultSet.getFloat(3);
        f += 0.7;
        assertEquals(4, (int) f);
       
    }
   
    
   
    @Test
    // tests close method
    public void testClose() throws SQLException {
        Connection connection = connect("Test_Close");
        resultSet = statement.getResultSet();
        assertFalse(resultSet.isClosed());
        resultSet.close();
        assertTrue(resultSet.isClosed());
    }
   
    // tests getMetaData method
}
