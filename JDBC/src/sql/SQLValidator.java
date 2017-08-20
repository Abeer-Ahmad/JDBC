package sql;
 
import java.sql.SQLException;
import java.util.ArrayList;

import dbms.Constants;
import utilities.Logs;
 
public class SQLValidator {
 
    private String statement, order;
    private String[] keys;
    private ArrayList<String> query;
    private int mode;
    private Logs logger;
    private Formatter formatter;
    private CreateValidator createValidator;
    private UseValidator useValidator;
    private InsertValidator insertValidator;
    private UpdateValidator updateValidator;
    private AlterValidator alterValidator;
    private SelectValidator selectValidator;
    private DeleteValidator deleteValidator;
    private DropValidator dropValidator;
    
    public SQLValidator() {
    	initialize(null);
    	logger = new Logs();
    	formatter = new Formatter();
        createValidator = new CreateValidator();
        useValidator = new UseValidator();
        insertValidator = new InsertValidator();
        updateValidator = new UpdateValidator();
        alterValidator = new AlterValidator();
        selectValidator = new SelectValidator();
        deleteValidator = new DeleteValidator();
        dropValidator = new DropValidator();
    }
 
    // initial values of each field
    private void initialize(String s) {
        statement = s;
        order = null;
        keys = null;
        query = new ArrayList<String>();
        mode = Constants.INVALID;
    }
 
    // sets an ID of current query
    private void setMode(int mode) {
        this.mode = mode;
    }
    
    // returns an ID of current query
    public int getMode() throws SQLException {
    	if(mode==Constants.INVALID){
    		throw new SQLException("Syntax Error!");/////change later;
    	}
        return mode;
    }
 
    // edits the statement format
    private void formatStatement() {
        statement = formatter.format(statement);
        keys = statement.split("\\s+");
        order = keys[0];
    }
   
    // determines required query
    public ArrayList<String> validateOrder(String s) throws SQLException {
        try {
            initialize(s);
            formatStatement();
            if (order.equalsIgnoreCase("USE")) {
            	query = useValidator.validateUse(keys);
                setMode(useValidator.getMode());
            } else if (order.equalsIgnoreCase("CREATE")) {
                query = createValidator.validateCreate(keys);
                setMode(createValidator.getMode());
            } else if (order.equalsIgnoreCase("INSERT")) {
            	query = insertValidator.validateInsert(keys);
                setMode(insertValidator.getMode());
            } else if (order.equalsIgnoreCase("UPDATE")) {
            	query = updateValidator.validateUpdate(keys);
                setMode(updateValidator.getMode());
            } else if (order.equalsIgnoreCase("ALTER")) {
            	query = alterValidator.validateAlter(keys);
                setMode(alterValidator.getMode());
            } else if (order.equalsIgnoreCase("SELECT")) {
            	query = selectValidator.validateSelect(keys);
                setMode(selectValidator.getMode());
            } else if (order.equalsIgnoreCase("DELETE")) {
            	query = deleteValidator.validateDelete(keys);
                setMode(deleteValidator.getMode());
            } else if (order.equalsIgnoreCase("DROP")) {
            	query = dropValidator.validateDrop(keys);
                setMode(dropValidator.getMode());
            } else {
                declareError("", "Unsupported Query!");
            } 
        } catch (Exception e) {
        	throw new SQLException(e.getMessage());
        }
        formatQuery();
        return query;
    }   
    
	private void formatQuery() {
		for (int i = 0; i < query.size(); i++) {
			query.set(i, query.get(i).replaceAll("~", " "));
		}
	}
    
    private void declareError(String order, String message) throws SQLException {
       logger.error("Invalid " + order + " Statement!");
       logger.error(message);
    	mode = Constants.INVALID;
    	throw new SQLException("Invalid " + order + " Statement! >> " + message);
    }     
}