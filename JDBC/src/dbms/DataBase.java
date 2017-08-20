package dbms;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.JResultSet;
import utilities.Logs;
import filesHandle.IFileReader;
import filesHandle.IFileWriter;
import validator.FileValidator;

public class DataBase implements IDataBase {

	private String currentDB;
	private Table table;
	private Path homeDir;
	private IFileWriter writer;
	private Logs logger;
	public DataBase(IFileWriter writer, IFileReader reader,FileValidator fileValidator, Path homeDir) {
		this.homeDir = homeDir;
		this.writer = writer;
		currentDB = new String();
		logger = new Logs();
		table = new Table(writer, reader, fileValidator, homeDir);
	}

	public boolean setDataBase(String dbName) {
		Path currentPath = Paths.get(homeDir.toString() + File.separator + dbName);
		if (Files.isDirectory(currentPath)) {

			currentDB = new String(dbName);
			return true;

		} else {
			return false;
		}
	}

	public boolean changeDBStructure(int mode, ArrayList<String> query) throws SQLException {
		if (mode == Constants.CREATE_TABLE) {
			return createTable(query.get(0), new ArrayList<String>(query.subList(1, query.size())));
			
		} else {
			return dropTable(query.get(0));
			
		}
		
	}

	public boolean createTable(String tableName, ArrayList<String> fields) throws SQLException {
		 
        Path tablePath = Paths.get(homeDir.toString() + File.separator + currentDB + File.separator + tableName);
 
        try {
            if (Files.exists(tablePath) && Files.isDirectory(tablePath)) {
               logger.error("Table already exists !");
            	throw new SQLException("Error:Existing Table");
            } else {
                Files.createDirectory(tablePath);
                
                writer.creatTable(tablePath, fields);
                return true;
            }
 
        } catch (NoSuchFileException x) {
        	logger.error("There is no used Database !");
            throw new SQLException("Error:No DB selected");
        } catch (IOException e) {
        	logger.error("Failed to create table !");
        	 throw new SQLException("Error in Creating Table");
        }
 
    }

	public boolean dropTable(String tableName) throws SQLException {
		Path path = Paths.get(homeDir.toString() + File.separator + currentDB + File.separator + tableName);
		try {
			if (Files.isDirectory(path)) {
				File[] innerFiles = path.toFile().listFiles();
				for (int j = 0; j < innerFiles.length; j++) {
					innerFiles[j].delete();
				}
				Files.delete(path);
				logger.info("Table deleted successfully !");
				return true;
			} else {
				logger.error("There is no used Database !");
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to create table !");
			throw new SQLException("Error in Creating Table");

		}
		
	}

	public int updateTables(int mode, ArrayList<String> query) throws SQLException {

		try {
			if (mode == Constants.DELETE) {
				return table.deleteTable(currentDB, query);
			} else if (mode == Constants.INSERT_REC) {
				return table.insertTable(currentDB, query);
			} else  {
				return table.updateTable(currentDB, query);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		
	}

	public JResultSet selectQuery(ArrayList<String> query) throws SQLException {
		return table.selectTable(currentDB, query);
	}

	public void changeTableStructure(int mode, ArrayList<String> query) throws SQLException {
		if(mode == Constants.ALTER_ADD){
    		table.alterAdd(currentDB, query);
    	}else if(mode == Constants.ALTER_DROP){
    		table.alterDrop(currentDB, query);
    	}
		
	}

}