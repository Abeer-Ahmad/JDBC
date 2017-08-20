package jdbc;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import utilities.driverLoader;

public class Main {
	private driverLoader loader;
	private Driver driver;
	private String username;
	private String password;
	private String protocol1;
	private String protocol2;
	  

	public Main() {
		loader = new driverLoader();
		File driverJar = new File("C:/Users/user/git/oop/oop/jars/JDriver.jar");
		loader.loadDriver(driverJar);
		driver = loader.instansiateDriver();
		readConfig();
	}

	private void readConfig() {
		File configFile = new File("CLIConfiguration.properties");
		try {
			FileReader reader = new FileReader(configFile);
			Properties prop = new Properties();
			prop.load(reader);
			username = prop.getProperty("dbuserName");
			password = prop.getProperty("dbpassword");
			protocol1 = prop.getProperty("protocol1");
			protocol2 = prop.getProperty("protocol2");
		} catch (Exception e) {
			throw new RuntimeException("error in config file");
		}
	}

	private String queryScanner(Scanner scan) {
		StringBuilder queryStatement = new StringBuilder();
		String line = new String();
		while (true) {
			line = scan.nextLine();
			queryStatement.append(line + " ");
			char lastchar = line.charAt(line.length() - 1);
			if (line.equals(";") || lastchar == ';') {
				break;
			}
		}
		return queryStatement.toString();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public static void main(String[] args) throws SQLException {
		Main CLI = new Main();
		Scanner scan = new Scanner(System.in);
		// JDriver driver = new JDriver();
		Properties info = new Properties();
		String sqlStat;
		boolean exit = false;
		System.out.println("Please enter URL :");
		String URL = scan.nextLine();
		Path pathDB = Paths.get(System.getProperty("user.home") + File.separator + "DataBases");
		try {
			while(!CLI.driver.acceptsURL(URL)){
				System.out.println("Please enter URL again :");
				URL = scan.nextLine();
			}
		} catch (Exception e) {
           
		}
		info.put("path", pathDB);
		String username, password;
		System.out.println("Please enter (user name AND password):");
		username = scan.nextLine();
		password = scan.nextLine();
		boolean validNameAndPassword = false;
		
		if (username.equals(CLI.getUsername()) && password.equals(CLI.getPassword()))
			validNameAndPassword = true;
		while (!validNameAndPassword) {
			System.out.println("INVALID! Please enter (user name AND password) again ");
			username = scan.nextLine();
			password = scan.nextLine();
			if (username.equals(CLI.getUsername()) && password.equals(CLI.getPassword()))
				validNameAndPassword = true;
		}
		Connection connection = CLI.driver.connect(URL, info);
		Statement statement = connection.createStatement();
		while (!exit) {
			statement = connection.createStatement();
			System.out.println("Enter SQL Statement OR close; to exit : ");
			sqlStat = new String(CLI.queryScanner(scan));
			if (sqlStat.equals("close;")) {
				break;
			}
			statement.executeUpdate(sqlStat);

		}

	}

}
