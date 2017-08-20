package dbms;



public abstract class Constants {


	public static final int SELECT_ALL = 0;
	public static final int SELECT_COLS = 1;
	public static final int SELECT_ROWS = 2;
	public static final int SELECT_ITEMS=3;
	public static final int SELECT_DISTINCT=4;
	
	public static final int pathError = 11;
	public static final int paraError = 12;
	public static final int EXCUTED = 13;
	
	public static final int IN_VALID_SELECT= -1;
	public static final int NO_MODE= -2;
	
	public static final int updateAllRows=20;
	public static final int updateRows=21;
	
	public static final int INVALID = 0;
	public static final int CREATE_DB = 1;
	public static final int DROP_DB = 2;
	public static final int USE_DB = 3;
	public static final int CREATE_TABLE = 4;
	public static final int DROP_TABLE = 5;
	public static final int DELETE = 6;
	public static final int INSERT_REC = 7;
	public static final int UPDATE_REC = 8;
	public static final int SELECT_TABLE = 9;
	public static final int JSON_TYPE = 14;
	public static final int XML_TYPE = 15;

	public static final int ALTER_DROP = 10;
    public static final int ALTER_ADD = 11;
    
	
	public static final int BEGIN = 0;
	public static final int NEXT_KEY = 2;
	public static final int UPDATE_MIN = 7;
	public static final int DROP_MIN = 4;
	public static final int DELETE_MIN = 4;
	public static final int SELECT_MIN = 5;
	public static final int CREATE_MIN = 4;
	public static final int INSERT_VALUES = 50;
	public static final int INSERT_COLUMNS = 51;
	public static final int DELETE_ALL = 52;
	public static final int DELETE_ROWS = 53;
	 public static final int ALTER_MIN = 7; // new constant
//	public static final Path homeDir = Paths.get(System.getProperty("user.home") + File.separator + "DataBases");

}
