package dbms;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import jdbc.JResultSet;
import utilities.Logs;
import filesHandle.IFileReader;
import filesHandle.IFileWriter;
import validator.FileValidator;
import validator.QueryFormatter;
import validator.QueryValidator;
public class Table implements ITable {
	private IFileWriter writer;
	private IFileReader reader;
	private FileValidator fileValidator;
	private Path homeDir;
	private Comparator comparator;
	private QueryValidator qValidator;
	private Logs logger;

	public Table(IFileWriter writer, IFileReader reader, FileValidator fileValidator, Path homeDir) {
		this.writer = writer;
		this.reader = reader;
		this.fileValidator = fileValidator;
		this.homeDir = homeDir;
		logger = new Logs();
		comparator = new Comparator();
		qValidator = new QueryValidator();
	}

	private int insert(ArrayList<String> cols, ArrayList<String> type, ArrayList<String> values,
			ArrayList<String> fields, ArrayList<String> typesFields, Path path) throws SQLException {
		if (qValidator.validateInsert(type, cols, values, fields, typesFields, path)) {
			ArrayList<Boolean> existCol = qValidator.getExistFields();
			ArrayList<String> value = qValidator.getArrangedValues();
			ArrayList<LinkedHashMap<String, String>> tableMap = new ArrayList<LinkedHashMap<String, String>>(
					reader.read(path));
			LinkedHashMap<String, String> newMap = new LinkedHashMap<String, String>();
			int pointer = 0;
			for (int i = 0; i < fields.size(); i++) {
				String val = !existCol.get(i) ? "null" : value.get(pointer++);
				newMap.put(fields.get(i), val);
			}
			tableMap.add(newMap);
			writer.write(tableMap, path);
			// print query has been excuted
			return 1;
		} else {
			logger.error("Invalid Insert parameters !");
			throw new SQLException("ERROR: Invalid parameters.");
		}
	}

	private int insert(ArrayList<String> type, ArrayList<String> values, ArrayList<String> fields,
			ArrayList<String> typesFields, Path path) throws SQLException {
		if (qValidator.validateInsert(type, values, typesFields, path)) {
			ArrayList<LinkedHashMap<String, String>> tableMap = reader.read(path);
			LinkedHashMap<String, String> newMap = new LinkedHashMap<String, String>();
			for (int i = 0; i < fields.size(); i++) {
				String value = i < values.size() ? values.get(i) : "null";
				newMap.put(fields.get(i), value);
			}
			tableMap.add(newMap);
			writer.write(tableMap, path);
			// print query has been excuted
			return 1;
		} else {
			logger.error("Invalid Insert parameters !");
			throw new SQLException("ERROR: Invalid parameters.");
		}
	}

	public int insertTable(String currentDB, ArrayList<String> query) throws SQLException {
		
		QueryFormatter queryFormatter = new QueryFormatter(query, Constants.INSERT_REC);
		int mode = queryFormatter.format();
		String tableName = queryFormatter.getTableName();
		Path path = Paths.get(homeDir + File.separator + currentDB + File.separator + tableName);
		if (fileValidator.validatePath(path)) {
			ArrayList<String> fields = fileValidator.getFields();// newwwwwwwwwwwwwww
			ArrayList<String> typesfields = fileValidator.getTypesFields();// newwwwwwwwwwwwwwwwwwww
			ArrayList<String> values = queryFormatter.getValues();
			ArrayList<String> type = queryFormatter.getType();
			if (mode == Constants.INSERT_VALUES) {
				
				return insert(type, values, fields, typesfields, path);
				
				
				
			} else {
				
				ArrayList<String> cols = queryFormatter.getColumns();
				return insert(cols, type, values, fields, typesfields, path);
			}
			
		} else {
			logger.error("Invalid path !");
			throw new SQLException("ERROR: Invalid path.");
		}
	}

	private int delete(Path path) throws SQLException {
		ArrayList<LinkedHashMap<String, String>> oldTable = reader.read(path);
		ArrayList<LinkedHashMap<String, String>> tableMap = new ArrayList<LinkedHashMap<String, String>>();
		writer.write(tableMap, path);
		// print query has been excuted
		return oldTable.size();
	}

	private int delete(ArrayList<String> condition, ArrayList<String> fields, ArrayList<String> typesFields, Path path)
			throws SQLException {
		if (qValidator.validateDelete(condition, fields, typesFields, path)) {
			
			int rowsAffeced = 0;
			ArrayList<LinkedHashMap<String, String>> tableMap = reader.read(path);
			for (int i = 0; i < tableMap.size(); i++) {
				String key = new String(condition.get(0));
				String colVal = new String(tableMap.get(i).get(key));
				 String typeFile = qValidator.getTypeFile();
	               condition.set(2, typeFile);           
				if (comparator.compare(new ArrayList<String>(condition.subList(1, condition.size())), colVal)) {
					rowsAffeced++;
					tableMap.remove(i);
					i--;
				}
			}
			writer.write(tableMap, path);
			// print query has been excuted
			return rowsAffeced;
		} else {
			logger.error("Invalid Delete parameter !");
			throw new SQLException("ERROR: Invalid parameter.");
		}
	}

	public int deleteTable(String currentDB, ArrayList<String> query) throws SQLException {
		QueryFormatter queryFormatter = new QueryFormatter(query, Constants.DELETE);
		int mode = queryFormatter.format();
		String tableName = queryFormatter.getTableName();
		Path path = Paths.get(homeDir + File.separator + currentDB + File.separator + tableName);
		if (fileValidator.validatePath(path)) {
			ArrayList<String> fields = fileValidator.getFields();
			ArrayList<String> typesfields = fileValidator.getTypesFields();
			if (mode == Constants.DELETE_ALL) {
				return delete(path);
			} else {
				ArrayList<String> condtion = queryFormatter.getCondition();
				return delete(condtion, fields, typesfields, path);
			}
		} else {
			logger.error("Invalid path");
			throw new SQLException("ERROR: Invalid path.");
		}
	}

	private ArrayList<LinkedHashMap<String, String>> selectDistinct(ArrayList<LinkedHashMap<String, String>> tableMap){
        HashSet<LinkedHashMap<String, String>> distinctRows = new HashSet<LinkedHashMap<String, String>>();
           for (int i = 0; i < tableMap.size(); i++) {
               distinctRows.add(tableMap.get(i));
           }
           List<LinkedHashMap<String, String>> temp = new ArrayList<LinkedHashMap<String, String>>(
                   distinctRows);
           return new ArrayList<LinkedHashMap<String, String>>(temp);
    }
   public JResultSet selectTable(String currentDB, ArrayList<String> query) throws SQLException {
       QueryFormatter queryFormatter = new QueryFormatter(query, Constants.SELECT_TABLE);
       int mode = queryFormatter.format();
       String tableName = queryFormatter.getTableName();
       ArrayList<LinkedHashMap<String, String>> tableMap = null;
       Path path = Paths.get(homeDir + File.separator + currentDB + File.separator + tableName);
       if (fileValidator.validatePath(path)) {
           ArrayList<String> typesFields = fileValidator.getTypesFields();
           ArrayList<String> fields = fileValidator.getFields();
           if (mode == Constants.SELECT_ALL) {
               tableMap = new ArrayList<LinkedHashMap<String, String>>(select(path));
           } else if (mode == Constants.SELECT_COLS) {
               ArrayList<String> cols = queryFormatter.getColumns();
               tableMap = new ArrayList<LinkedHashMap<String, String>>(select(path, fields, cols));
           } else if (mode == Constants.SELECT_ROWS) {
               ArrayList<String> condition = queryFormatter.getCondition();
               tableMap = new ArrayList<LinkedHashMap<String, String>>(
                       selectRows(path, fields, typesFields, condition));
           } else if (mode == Constants.SELECT_ITEMS) {
               ArrayList<String> cols = queryFormatter.getColumns();
               ArrayList<String> condition = queryFormatter.getCondition();
               tableMap = new ArrayList<LinkedHashMap<String, String>>(
                       select(path, fields, typesFields, cols, condition));
           }
          
           if (queryFormatter.isDistinct()){
               tableMap = selectDistinct(tableMap);
           }
           LinkedHashMap<String, String> tabName = new LinkedHashMap<String, String>();
           tabName.put("tableName", tableName);
           tableMap.add(0, tabName);
           LinkedHashMap<String, String> tabValues = new LinkedHashMap<String, String>();
           for (int colDTDSize = 0; colDTDSize < typesFields.size(); colDTDSize++) {
               tabValues.put(fields.get(colDTDSize), typesFields.get(colDTDSize));
           }
           tableMap.add(1, tabValues);
           JResultSet temp2= new JResultSet(tableMap);
          
           return temp2;
       }
       logger.info("Invalid Path !");
       throw new SQLException("ERROR: Invalid path.");
   }

   private ArrayList<LinkedHashMap<String, String>> select(Path path) throws SQLException {
       return reader.read(path);
   }

   private ArrayList<LinkedHashMap<String, String>> select(Path path, ArrayList<String> fields, ArrayList<String> cols)
           throws SQLException {

       ArrayList<LinkedHashMap<String, String>> currentTable = reader.read(path);
       ArrayList<LinkedHashMap<String, String>> newQuery = new ArrayList<LinkedHashMap<String, String>>();

       if (qValidator.validateSelect(cols, fields, path)) {
           for (int rowIndex = 0; rowIndex < currentTable.size(); rowIndex++) {
               LinkedHashMap<String, String> rowMap = new LinkedHashMap<String, String>();
               for (int colIndex = 0; colIndex < cols.size(); colIndex++) {
                   String value = new String(currentTable.get(rowIndex).get(cols.get(colIndex)));
                   rowMap.put(cols.get(colIndex), value);
               }
               newQuery.add(rowMap);
           }
           return newQuery;
       }
       logger.info("Invalid Select paramters !");
       throw new SQLException("ERROR: Invalid parameters.");

   }

   private ArrayList<LinkedHashMap<String, String>> select(Path path, ArrayList<String> fields,
           ArrayList<String> typesFields, ArrayList<String> cols, ArrayList<String> condition) throws SQLException {

       ArrayList<LinkedHashMap<String, String>> currentTable = reader.read(path);
       ArrayList<LinkedHashMap<String, String>> newQuery = new ArrayList<LinkedHashMap<String, String>>();
       if (qValidator.validateSelect(cols, condition, fields, typesFields, path)) {
           for (int rowIndex = 0; rowIndex < currentTable.size(); rowIndex++) {

               ArrayList<String> compQuery = new ArrayList<String>(condition.subList(1, condition.size()));
               String col = new String(condition.get(0));
               String value = new String(currentTable.get(rowIndex).get(col));
                String typeFile = qValidator.getTypeFile();
               compQuery.set(1, typeFile);
               if (comparator.compare(compQuery, value)) {
                   LinkedHashMap<String, String> rowMap = new LinkedHashMap<String, String>();
                   for (int colIndex = 0; colIndex < cols.size(); colIndex++) {
                       String colValue = new String(currentTable.get(rowIndex).get(cols.get(colIndex)));
                       rowMap.put(cols.get(colIndex), colValue);
                   }
                   newQuery.add(rowMap);
               }
           }
           return newQuery;

       }
       logger.info("Invalid Select paramters !");
       throw new SQLException("ERROR: Invalid parameters.");
   }

   private ArrayList<LinkedHashMap<String, String>> selectRows(Path path, ArrayList<String> fields,
           ArrayList<String> typesFields, ArrayList<String> condition) throws SQLException {
       ArrayList<LinkedHashMap<String, String>> currentTable = reader.read(path);
       ArrayList<LinkedHashMap<String, String>> newQuery = new ArrayList<LinkedHashMap<String, String>>();
       if (qValidator.validateSelectRows(condition, fields, typesFields, path)) {
           for (int rowIndex = 0; rowIndex < currentTable.size(); rowIndex++) {
               String col = new String(condition.get(0));
               String value = new String(currentTable.get(rowIndex).get(col));
               String typeFile = qValidator.getTypeFile();
               condition.set(2, typeFile);
               ArrayList<String> compQuery = new ArrayList<String>(condition.subList(1, condition.size()));
               if (comparator.compare(compQuery, value)) {
                   LinkedHashMap<String, String> rowMap = new LinkedHashMap<String, String>(
                           currentTable.get(rowIndex));
                   newQuery.add(rowMap);
               }

           }
           return newQuery;
       }
       logger.info("Invalid Select paramters !");
       throw new SQLException("ERROR: Invalid parameters.");
   }

	public int updateTable(String currentDB, ArrayList<String> query) throws SQLException {
		QueryFormatter queryFormatter = new QueryFormatter(query, Constants.UPDATE_REC);
		int mode = queryFormatter.format();
		ArrayList<String> values = queryFormatter.getValues();
		ArrayList<String> cols = queryFormatter.getColumns();
		ArrayList<String> type = queryFormatter.getType();
		String tableName = queryFormatter.getTableName();
		Path path = Paths.get(homeDir + File.separator + currentDB + File.separator + tableName);
		if (fileValidator.validatePath(path)) {
			ArrayList<String> fields = fileValidator.getFields();// newwwwwwwwwwwwwww
			ArrayList<String> typesfields = fileValidator.getTypesFields();// newwwwwwwwwwwwwwwwwwww
			if (mode == Constants.updateAllRows) {
				return update(type, cols, values, fields, typesfields, path);
			} else {
				ArrayList<String> condition = queryFormatter.getCondition();
				return update(type, cols, values, condition, fields, typesfields, path);
			}
		} else {
			 logger.info("Invalid path!");
			throw new SQLException("ERROR: Invalid path.");
		}
	}

	private int update(ArrayList<String> type, ArrayList<String> cols, ArrayList<String> values,
            ArrayList<String> fields, ArrayList<String> typesFields, Path path) throws SQLException {
        if (qValidator.validateUpate(type, cols, values, fields, typesFields, path)) {
            ArrayList<Boolean> existCol = qValidator.getExistFields();
            ArrayList<String> value = qValidator.getArrangedValues();
            ArrayList<LinkedHashMap<String, String>> tableMap = reader.read(path);
            ArrayList<LinkedHashMap<String, String>> newtableMap = new ArrayList<LinkedHashMap<String, String>>();
            LinkedHashMap<String, String> map;
            for (int i = 0; i < tableMap.size(); i++) {
                map = new LinkedHashMap<String, String>();
                int pointer = 0;
                for (int j = 0; j < fields.size(); j++) {
                    String key = fields.get(j);
                    String val = existCol.get(j) ? value.get(pointer++) : tableMap.get(i).get(key);
                    map.put(key, val);
                }
                newtableMap.add(map);
            }
            writer.write(newtableMap, path);
            return tableMap.size();
            // print query has been excuted
        } else {
        	 logger.info("Invalid Update paramters !");
            throw new SQLException("ERROR: Invalid parameter.");
        }
    }

	 private int update(ArrayList<String> type, ArrayList<String> cols, ArrayList<String> values,
			 
	            ArrayList<String> condtion, ArrayList<String> fields, ArrayList<String> typesFields, Path path)
	            throws SQLException {
		      
	        if (qValidator.validateUpate(type, cols, values, condtion, fields, typesFields, path)) {
	        	
	            int rowsAffeced = 0;
	            ArrayList<Boolean> existCol = qValidator.getExistFields();
	            ArrayList<String> value = qValidator.getArrangedValues();
	            ArrayList<LinkedHashMap<String, String>> tableMap = reader.read(path);
	            ArrayList<LinkedHashMap<String, String>> newTableMap = new ArrayList<LinkedHashMap<String, String>>();
	            LinkedHashMap<String, String> map;
	            String key = new String(condtion.get(0));
	            String typeFile = qValidator.getTypeFile();
	            condtion.set(2, typeFile);
	            
	            condtion = new ArrayList<String>(condtion.subList(1, condtion.size()));
	            for (int i = 0; i < tableMap.size(); i++) {
	                map = new LinkedHashMap<String, String>();
	                int pointer = 0;
	                boolean updateRow = false;
	                String colVal = new String(tableMap.get(i).get(key));
	                for (int j = 0; j < fields.size(); j++) {
	                    if (comparator.compare(condtion, colVal) && existCol.get(j)) {
	                        map.put(fields.get(j), value.get(pointer++));
	                        updateRow = true;
	                    } else {
	                        map.put(fields.get(j), tableMap.get(i).get(fields.get(j)));
	 
	                    }
	                }
	                if (updateRow) {
	                    rowsAffeced++;
	                }
	                newTableMap.add(map);
	            }
	            writer.write(newTableMap, path);
	            // print query has been excuted
	            return rowsAffeced;
	        } else {
	        	 logger.info("Invalid Update paramters !");
	            throw new SQLException("ERROR: Invalid parameter.");
	        }
	    }

	public void alterAdd(String currentDB, ArrayList<String> query) throws SQLException {
		String tableName = new String(query.get(0));
		String key = new String(query.get(1));
		Path path = Paths.get(homeDir + File.separator + currentDB + File.separator + tableName);
		if (fileValidator.validatePath(path)) {
			ArrayList<String> fields = fileValidator.getFields();// newwwwwwwwwwwwwww
			ArrayList<String> typesfields = fileValidator.getTypesFields();// newwwwwwwwwwwwwwwwwwww
			ArrayList<String> Query = qValidator.changeTableFields(Constants.ALTER_ADD, query, fields, typesfields,
					path);
			ArrayList<LinkedHashMap<String, String>> tableMap = reader.read(path);
			writer.creatTable(path, Query);
			for (int i = 0; i < tableMap.size(); i++) {
				tableMap.get(i).put(key, "null");
			}
			writer.write(tableMap, path);
		} else {
			 logger.info("Invalid path !");
			throw new SQLException("ERROR: Invalid path.");
		}
	}

	public void alterDrop(String currentDB, ArrayList<String> query) throws SQLException {
		String tableName = new String(query.get(0));
		String key = new String(query.get(1));
		Path path = Paths.get(homeDir + File.separator + currentDB + File.separator + tableName);
		if (fileValidator.validatePath(path)) {
			ArrayList<String> fields = fileValidator.getFields();// newwwwwwwwwwwwwww
			ArrayList<String> typesfields = fileValidator.getTypesFields();// newwwwwwwwwwwwwwwwwwww
			ArrayList<String> Query = qValidator.changeTableFields(Constants.ALTER_DROP, query, fields, typesfields,
					path);
			ArrayList<LinkedHashMap<String, String>> tableMap = reader.read(path);
			writer.creatTable(path, Query);
			for (int i = 0; i < tableMap.size(); i++) {
				tableMap.get(i).remove(key);
			}
			writer.write(tableMap, path);
		} else {
			 logger.info("Invalid path !");
			throw new SQLException("ERROR: Invalid path.");
		}
	}

	/*
	 * public static void main(String args[]) throws Exception {
	 * 
	 * FileWriter fileWriter = new XmlWriter(); FileReadr filerd = new
	 * XmlReader(); AQueryValidator qValidator = new XmlValidator(); Path
	 * homeDir = Paths.get(System.getProperty("user.home") + File.separator +
	 * "DataBases"); Table table = new Table
	 * (fileWriter,filerd,qValidator,homeDir); ArrayList<String> query = new
	 * ArrayList<String>(); //"zew" query.add("4"); query.add("age");
	 * 
	 * try { table.selectTable("zew", query); } catch (SQLException e){
	 * System.out.print(e.toString()); } }
	 */

}
