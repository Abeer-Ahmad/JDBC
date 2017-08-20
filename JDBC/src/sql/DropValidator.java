package sql;

import java.sql.SQLException;
import java.util.ArrayList;

import dbms.Constants;
import utilities.Logs;

public class DropValidator {

	private ArrayList<String> query;
    private int mode;
    private Logs logger;
    
    public DropValidator() {
    	logger = new Logs();
    	initialize();
    }
    
    public int getMode() {
    	return mode;
    }
    
    private void initialize() {
        query = new ArrayList<String>();
        mode = Constants.INVALID;
    }
    
 // validates DROP syntax  
    public ArrayList<String> validateDrop(String[] keys) throws SQLException {
    	initialize();
        if (keys.length != Constants.DROP_MIN) {
            declareError("DROP", "Missing Information!");
        } else if (keys[1].equalsIgnoreCase("database")) {
            query.add(keys[2].toLowerCase());//database to lower
            mode = Constants.DROP_DB;
        } else if (keys[1].equalsIgnoreCase("table")) {
            query.add(keys[2].toLowerCase());//table to lower
            mode = Constants.DROP_TABLE;
        } else {
            declareError("DROP", "Neither DATABASE Nor TABLE Choosen!");
        }
        return query;
    }
    
    private void declareError(String order, String message) throws SQLException {
    	logger.error("Invalid " + order + " Statement!");
    	logger.error(message);
        mode = Constants.INVALID;
        throw new SQLException("Invalid " + order + " Statement! >> " + message);
    }
}
