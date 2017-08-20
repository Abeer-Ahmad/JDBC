package sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import dbms.Constants;
import utilities.Logs;

public class InsertValidator {

	private String[] keys;
	private ArrayList<String> query;
	private int mode, insertTo, vals;
	private Logs logger;
    
	public InsertValidator() {
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
		insertTo = 0;
		vals = 0;
	}

	// validates INSERT syntax
	public ArrayList<String> validateInsert(String[] keys) throws SQLException {
		initialize();
    	setKeys(keys);
		try {
			if (!keys[1].equalsIgnoreCase("into")) {
				declareError("INSERT", "\"INTO\" Keyword Is Missing!");
			} else {
				query.add(keys[2].toLowerCase());//table to lower
				int i = checkCol(3);
				if (keys[i].equalsIgnoreCase("values")) {
					if (insertTo > 0) {
						query.add("values");
					}
					i = checkVal(++i);
					if (insertTo > 0 && vals != insertTo) {
						declareError("INSERT",
								"Columns Number Doesnot Match Values Number!");
					} else {
						test(i, Constants.INSERT_REC, "INSERT");
					}
				} else {
					declareError("INSERT", "No Values Entered!");
				}
			}
		} catch (Exception e) {
			declareError("INSERT", e.getMessage());
			throw new SQLException(e.getMessage());
		}
		return query;
	}

	// checks whether to insert into specific columns
	private int checkCol(int i) throws SQLException {
		if (keys[i].equals("(")) {
			query.add("columns");
			query.add(keys[++i].toLowerCase()); // toLowerCase()
			insertTo++;
			i = validateComma(++i);
			if (!keys[i].equals(")")) {
				logger.error("Columns Format Incorrect!");
				throw new SQLException("Columns Format Incorrect!");
			}
			return i + 1;
		} else {
			return i;
		}
	}

	// validates INSERT values
	private int checkVal(int i) throws SQLException {
		if (keys[i].equals("(")) {
			appendType(++i);
			query.add(keys[i].replace("'", ""));
			vals++;
			i = validateComma(++i);
			if (!keys[i].equals(")")) {
				logger.error("Values Format Incorrect!");
				throw new SQLException("Values Format Incorrect!");
			}
		} else {
			logger.error("Values Format Incorrect!");
			throw new SQLException("Values Format Incorrect!");
		}
		return i + 1;
	}

	// validates comma syntax
	private int validateComma(int i) throws SQLException {
		while (true) {
			if (keys[i].equals(",")) {
				i++;
				if (vals > 0) {
					appendType(i);
					vals++;
					query.add(keys[i].replace("'", ""));
				}			
				if (insertTo > 0 && vals == 0) { // indication for inserting into specific columns
					insertTo++;
					query.add(keys[i].toLowerCase()); // toLowerCase()
				}
				i++;
			} else {
				break;
			}
		}
		return i;
	}

	private void appendType(int i) throws SQLException {
        if (keys[i].startsWith("'")) {
            query.add("varchar");
        } else if (Pattern.matches("[+-]?[0-9]+", keys[i])) {
            query.add("int");
        } else if (Pattern.matches(
                "((?:19|20)\\d\\d)-(0?[1-9]|1[012])-([12][0-9]|3[01]|0?[1-9])",
                keys[i])) {
            query.add("date");
        }else if(Pattern.matches("[+-]?([0-9]*[.])?[0-9]+",keys[i])) {
            query.add("float");
        }
        else {
        	logger.error("The Value You have Entered Doesnot Match Any DataType!");
            throw new SQLException("The Value You have Entered Doesnot Match Any DataType!");
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
