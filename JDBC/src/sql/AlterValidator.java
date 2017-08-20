package sql;

import java.sql.SQLException;
import java.util.ArrayList;

import dbms.Constants;
import utilities.Logs;

public class AlterValidator {

	private String[] keys;
	private ArrayList<String> query;
    private int mode;
    private Logs logger;
    
    public AlterValidator() {
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
    
 // validates ALTER syntax
    public ArrayList<String> validateAlter(String[] keys) throws SQLException {
    	initialize();
    	setKeys(keys);
    	if (keys.length < Constants.ALTER_MIN) {
    		declareError("ALTER TABLE", "Missing Information!");
    	} else {
    		try {
    			if (keys[1].equalsIgnoreCase("table")) {
    				query.add(keys[2].toLowerCase()); // table to lower
    				chooseAlter(3);
    			} else {
    				declareError("ALTER TABLE", "\"TABLE\" Keyword Is Missing!");
    			}
    		} catch (Exception e) {
    			declareError("ALTER TABLE", e.getMessage());
    			throw new SQLException(e.getMessage());
    		}
    	}
    	return query;
    }
     
    // decides whether to add or drop column
    private void chooseAlter(int i) throws SQLException {
    	if (keys[i].equalsIgnoreCase("DROP")) {
    		if (keys[i + 1].equalsIgnoreCase("COLUMN")) {
    			query.add(keys[i + 2].toLowerCase()); // toLowerCase()
    			test(i + 3, Constants.ALTER_DROP, "ALTER TABLE");
    		} else {
    			declareError("ALTER TABLE", "\"COLUMN\" Keyword Is Missing!!");
    		}
    	} else if (keys[i].equalsIgnoreCase("ADD")) {
    		if (keys[i + 1].equalsIgnoreCase("COLUMN")) {
    			i++;
    		}
    		query.add(keys[i + 1].toLowerCase()); // toLowerCase()
    		query.add(keys[i + 2]);
    		test(i + 3, Constants.ALTER_ADD, "ALTER TABLE");
    	} else {
    		declareError("ALTER TABLE", "Undefined Keyword!");
    	}
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
