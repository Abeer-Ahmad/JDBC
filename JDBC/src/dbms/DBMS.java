package dbms;
 
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;


import filesHandle.IFileReader;
import filesHandle.IFileWriter;
import filesHandle.JsonReader;
import filesHandle.JsonWriter;
import filesHandle.XmlReader;
import filesHandle.XmlWriter;
import jdbc.JResultSet;
import utilities.Logs;
import validator.FileValidator;
import validator.JsonValidator;
import validator.XmlValidator;
 
public class DBMS implements IDBMS{
 
    private DataBase db;
    //private int fileType;
    private Path homeDir;
    private IFileWriter writer;
    private IFileReader reader;
    private FileValidator fileValidator;
    private Logs logger;
     public DBMS (int fileType, Path homeDir) throws SQLException{
    	// this.fileType = fileType;
    	 logger = new Logs();
    	 this.homeDir = homeDir;
    	 if(fileType == Constants.JSON_TYPE){
 			writer = new JsonWriter();
 			reader = new JsonReader();
 			fileValidator = new JsonValidator();
 		}else if(fileType == Constants.XML_TYPE){
 			writer = new XmlWriter();
 			reader = new XmlReader();
 			fileValidator = new XmlValidator();
 		}
    	 db = new DataBase (writer, reader,fileValidator, homeDir);
        try {
            if (!Files.exists(homeDir)){
            Files.createDirectory(homeDir);
            logger.info("DataBases Directory has been successfuly created !");
            }
        } catch (IOException e) {
        	logger.error("DataBases Directory is already created !");
            e.printStackTrace();
        }
    }
   
   
    public boolean createDB(String dbName) throws SQLException {
        
        Path dbPath = Paths.get(homeDir.toString() + File.separator + dbName);
        //if directory exists?
        if (!Files.exists(dbPath)) {
            try {
                Files.createDirectory(dbPath);
                logger.info("DB has been successfuly created !");
                return true;
                
            } catch (IOException e) {
                //fail to create directory
            	logger.error("failed to create DataBase !");
            	throw new SQLException("failed to create DataBase");
              
            }
        }
        else{
        	logger.error("Already created DataBase!");
        	return false;
		     //throw new SQLException("Already created DataBase");
        }

    }
   
    public boolean useDB (String dbName){
    	Path dbPath = Paths.get(homeDir.toString()+File.separator +dbName);
        if (Files.isDirectory(dbPath)){
           db.setDataBase(dbName);
           logger.info("Current DataBase is "+dbName);
            return true;
        }
        else
          {
        	logger.error("Failed to find DataBase !");
        	return false;
          }
        }
 
    public boolean dropDB(String dbName) throws SQLException{
            Path dbPath = Paths.get(homeDir.toString()+File.separator +dbName);
            try{
                if (Files.isDirectory(dbPath)){
                    File[] dbTables = dbPath.toFile().listFiles();
                    for (int i=0; i<dbTables.length;i++){
                        File[] innerFiles = dbTables[i].listFiles();
                        for (int j=0; j< innerFiles.length;j++){
                            innerFiles[j].delete();
                        }
                        dbTables[i].delete();
                    }
                    Files.delete(dbPath);
                    logger.info("DataBase deleted successfully !");
                    return true;
                }
                else{
                	logger.error("Failed to find DataBase !");
                	return false;
                    //throw new SQLException("DB Not Found");
                }
            } catch (IOException e){
            	logger.error("ERROR in Drop Database ! ");
            	throw new SQLException("ERROR in Drop DB");
            }  
        }
   
    public boolean changeDBStructure(int mode, ArrayList<String> query) throws SQLException{
           return db.changeDBStructure(mode, query);
        }
   
  
    public void changeTableStructure(int mode, ArrayList<String> query) throws SQLException{
    	 db.changeTableStructure(mode, query);
    }
    
     public int excuteUpdateQuery(int mode, ArrayList<String> query) throws SQLException{
         
    	 return db.updateTables(mode, query);
    	 
             
        }
       
       
        public JResultSet selectQuery(ArrayList<String> query) throws SQLException{
        	
        	return db.selectQuery(query);
        	
        	
         
       }
 
     
}