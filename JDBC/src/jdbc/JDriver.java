package jdbc;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import dbms.Constants;
import dbms.DBMS;

public class JDriver implements Driver {

	
	private String userName;
	private String password;
	private String protocolEntered;
	

	// Constructor and registering the driver through the driver manager
	public JDriver() {

		
		try {
			DriverManager.registerDriver(this);
		} catch (SQLException e) {
			// print can not register driver.
			
		}
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		String[] urlInfo = url.split(":");
		if (urlInfo.length != 3) {
			return false;
		} else {
			if (!urlInfo[0].equals("jdbc")) {
				return false;
			}
			if (!urlInfo[1].equals("xmldb") && !urlInfo[1].equals("altdb")) {
				return false;
			} 
			else
			{
				protocolEntered = new String(urlInfo[1]);
			}
			if (!urlInfo[2].equals("//localhost"))
				return false;
			char[] localHost = urlInfo[2].toCharArray();
			if (localHost[0] != '/' || localHost[1] != '/') {
				return false;
			}

			return true;
		}
		
	}
   
	

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		Path path = Paths.get(info.get("path").toString());
		// initialize a new DBMS per connection
		int protocol = -1;
		if (acceptsURL(url)){
			if (protocolEntered.equals("xmldb") ) {
				protocol= Constants.XML_TYPE;
			} 
			else if (protocolEntered.equals("altdb")){
				protocol= Constants.JSON_TYPE;
			}
		DBMS dbms = new DBMS(protocol,path);
		/// validate user name and password from configuration file
		JConnection connection = new JConnection(dbms);
		return connection;
		}else {
			// logger
			throw new SQLException("Invalid URL");
		}
	}

	@Override
	public int getMajorVersion() {
		throw new java.lang.UnsupportedOperationException();
		
	}

	
	@Override
	public int getMinorVersion() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		DriverPropertyInfo[] propertyInfo = new DriverPropertyInfo[1];
		DriverPropertyInfo property;
		property = new DriverPropertyInfo(userName, password);
		propertyInfo[0] = property;
		return propertyInfo;
	}

	@Override
	public boolean jdbcCompliant() {
		throw new java.lang.UnsupportedOperationException();
	}
}
