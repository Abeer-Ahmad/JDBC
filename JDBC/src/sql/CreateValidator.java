package sql;

import java.sql.SQLException;
import java.util.ArrayList;

import dbms.Constants;
import utilities.Logs;

public class CreateValidator {

	private String[] keys;
	private ArrayList<String> query;
    private int mode, colName, colType;
    private Logs logger;
    
    public CreateValidator() {
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
        colName = 0;
        colType = 0;
    }
    
	// validates CREATE syntax
    public ArrayList<String> validateCreate(String[] keys) throws SQLException {
    	initialize();
    	setKeys(keys);
        try {
            if (keys.length < Constants.CREATE_MIN) {
                declareError("CREATE", "Missing Information!");
            } else {
                if (keys[1].equalsIgnoreCase("DATABASE")) {
                    validateDB();
                } else if (keys[1].equalsIgnoreCase("TABLE")) {
                    validateTABLE();
                } else {
                    declareError("CREATE", "Undefined CREATE Query!");
                }
            }
        } catch (Exception e) {
            declareError("CREATE", e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return query;
    }
    
    // validates DATABASE creation
    private void validateDB() throws SQLException {
       /* if (!validateName(keys[2])) {
            declareError("CREATE", "Invalid DATABASE Name!");
        }*/ //else {
            query.add(keys[2].toLowerCase());//database to lower
            int i = 3;
            test(i, Constants.CREATE_DB, "CREATE");
        //}
    }
 
    // validates TABLE creation
    private void validateTABLE() throws SQLException{
        /*if (!validateName(keys[2])) {
            declareError("CREATE", "Invalid TABLE Name!");
        } *///else {
            if (!(keys[3].equals("(") && keys[keys.length - 2].equals(")"))) {
                declareError("CREATE", "Incorect Format!");
            } else {
                query.add(keys[2].toLowerCase());//to lower table
                int i = checkColType(4);
                if (colName != colType) {
                    declareError("CREATE", "Missing Columns Types!");
                } else {
                    test(i, Constants.CREATE_TABLE, "CREATE");
                }
            }
        //}       
    }
    
    // checks that DB or TABLE has a valid name
    /*private boolean validateName(String s) {
        if (Character.isLetter(s.charAt(0))) {
            return true;
        }
        return false;
    }*/
   
    // validates TABLE fields
    private int checkColType(int i) throws SQLException{
        query.add(keys[i].replace("'", "").toLowerCase()); // toLowerCase()
        colName++;
        if (checkType(keys[i + 1])) {
            query.add(keys[++i].toLowerCase());
            colType++;
            i = validateComma(++i);
            return i + 1;
        } else {
            logger.error("Unsupported DataType!");
            throw new SQLException();
        }
    }
 
    // validates TABLE types
    private boolean checkType(String type) {
        if (type.equalsIgnoreCase("varchar")) {
            return true;
        } else if (type.equalsIgnoreCase("int")) {
            return true;
        } else if (type.equalsIgnoreCase("float")) {
            return true;
        } else if (type.equalsIgnoreCase("date")) {
            return true;
        }
        return false;
    }
    
	private int validateComma(int i) {
		while (true) {
			if (keys[i].equals(",")) {
				query.add(keys[++i].replace("'", "").toLowerCase()); // toLowerCase()
				colName++;
				if (checkType(keys[i + 1])) {
					query.add(keys[++i].toLowerCase());
					colType++;
				} else {
					break;
				}
				i++;
			} else {
				break;
			}
		}
		return i;
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
