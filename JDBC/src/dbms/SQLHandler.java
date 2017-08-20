package dbms;

import java.util.ArrayList;



import jdbc.JResultSet;
import sql.SQLValidator;

public class SQLHandler {
    
	private String statement;
	private int printMode;
	private SQLValidator sqlV = new SQLValidator();
	private DBMS dataBase;
	private ArrayList<String> query = new ArrayList<String>();
	
	SQLHandler(DBMS dbms){
		this.dataBase = dbms;
	}
	
	public void setStatement(String s){
		statement = new String(s);
	}
	public void excuteQuery(int mode) throws Exception{
		if(mode == Constants.CREATE_DB){
			dataBase.createDB(query.get(0));
		}else if(mode == Constants.USE_DB){
			dataBase.useDB(query.get(0));
		}else if(mode == Constants.DROP_DB){
			dataBase.dropDB(query.get(0));
		}else if(mode == Constants.CREATE_TABLE){
			dataBase.changeDBStructure(Constants.CREATE_TABLE, query);
		}else if(mode == Constants.DROP_TABLE){
			dataBase.changeDBStructure(Constants.DROP_TABLE, query);
		}else if(mode == Constants.DELETE){
			printMode = dataBase.excuteUpdateQuery(Constants.DELETE, query);
		}else if(mode == Constants.INSERT_REC){
			printMode = dataBase.excuteUpdateQuery(Constants.INSERT_REC, query);
		}else if(mode == Constants.UPDATE_REC){
			printMode = dataBase.excuteUpdateQuery(Constants.UPDATE_REC, query);
		}else if(mode == Constants.SELECT_TABLE){
	  			JResultSet resultSet = dataBase.selectQuery(query);
			    if(printMode != Constants.pathError && printMode != Constants.paraError ){
			    }
		}
	}
	public void excute() throws Exception{
		query = sqlV.validateOrder(statement);
		int mode = sqlV.getMode();
		excuteQuery(mode);
	}
	public static void main(String args[]) {
      
	}
}
