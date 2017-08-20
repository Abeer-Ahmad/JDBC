package sql;

import java.sql.SQLException;
import java.util.ArrayList;

import dbms.Constants;
import utilities.Logs;

public class UseValidator {

	private String[] keys;
	private ArrayList<String> query;
    private int mode;
    private Logs logger;
    
    public UseValidator() {
    	logger = new Logs();
    	initialize();
    }
    
    private void setKeys(String[] keys) {
    	this.keys = keys;
    }
    
    public int getMode() {
    	return mode;
    }
    
    private void initialize() {
        keys = null;
        query = new ArrayList<String>();
        mode = Constants.INVALID;
    }
    
 // validates USE syntax
    public ArrayList<String> validateUse(String[] keys) throws SQLException {
    	initialize();
    	setKeys(keys);
        try {
            query.add(keys[1].toLowerCase());//database to lower
            test(Constants.NEXT_KEY, Constants.USE_DB, "USE");
        } catch (Exception e) {
            declareError("USE", e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return query;
    }
    
    private void test(int i, int cur, String order) throws SQLException {
		if (keys[i].equals(";")) {
			if (i == keys.length - 1) {
				mode = cur;
			} else {
				declareError(order, "Multiple Semicolons!");
			}
		} else {
			declareError(order, "Undefined!");
		}
	}
	
    private void declareError(String order, String message) throws SQLException {
    	logger.error("Invalid " + order + " Statement!");
    	logger.error(message);
        mode = Constants.INVALID;
        throw new SQLException("Invalid " + order + " Statement! >> " + message);
    }
}
