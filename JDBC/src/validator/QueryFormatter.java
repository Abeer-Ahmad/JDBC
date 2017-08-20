package validator;
 
import java.util.ArrayList;
 
import dbms.Constants;
 
public class QueryFormatter {
 
    private String tableName;
    private ArrayList<String> query;
    private ArrayList<String> columns ;
    private ArrayList<String> values ;
    private ArrayList<String> types ;
    private ArrayList<String> condition ;
    private int currentFunction;
    private boolean Distinct =false;
    public QueryFormatter(ArrayList<String> query, int function){
   
         tableName = new String();
         this.query = query;
         columns = new ArrayList<String>();
         values = new ArrayList<String>();
         types = new ArrayList<String>();
         condition = new ArrayList<String>();
         currentFunction =function;
    }
    public ArrayList<String> getColumns(){
        return columns;
    }
    public ArrayList<String> getValues(){
        return values;
    }
    public ArrayList<String> getType(){
        return types;
    }
    public ArrayList<String> getCondition(){
        return condition;
    }
    public String getTableName(){
        return tableName;
    }
    public boolean isDistinct(){
        return Distinct;
    }
     public int format (){
         if (currentFunction == Constants.SELECT_TABLE){
             return formatSelect(query);
         }
         else if (currentFunction == Constants.UPDATE_REC){
             return formatUpdate(query);
         }
         else if (currentFunction == Constants.INSERT_REC){
             return formatInsert(query);
         }
         else if (currentFunction == Constants.DELETE){
             return  formatDelete(query);
         }
         else return 0;// el mfroud ytshal
     }
   
    private int formatSelect(ArrayList<String> query){
       
        tableName = new String( query.get(0));
        Distinct = query.contains("DISTINCT");
        int selectMode  =-3;
        if (query.size()==1){
            tableName = new String( query.get(0));
            selectMode =Constants.SELECT_ALL;
        }
        else {
             int indexWhere = query.indexOf("where");
             int colEnd = (indexWhere!=-1) ? indexWhere : query.size();
             int colStart = (Distinct) ?      2:1;
            if (indexWhere ==-1){
                columns = new ArrayList<String> (query.subList(colStart, colEnd));
                selectMode= Constants.SELECT_COLS;
               
            }
            else if (indexWhere==1){      
                condition = new ArrayList<String>(query.subList(indexWhere+1,query.size()));
             
                selectMode=Constants.SELECT_ROWS;
            }
            else if (indexWhere >1){
                // where found but after columns
                columns = new ArrayList<String> (query.subList(colStart, colEnd));
                condition = new ArrayList<String>(query.subList(indexWhere+1,query.size()));
                selectMode= Constants.SELECT_ITEMS;
               
            }
           
         
        }
        return selectMode;
    }
    private int formatUpdate (ArrayList<String> query){
        int indexWhere = query.indexOf("where");
        tableName = new String( query.get(0));
        int colEnd = (indexWhere!=-1) ? indexWhere : query.size();
        /*el mfroud mlhomsh lzma
        columns.clear();
        types.clear();
        values.clear();*/
        for (int i=1;i<colEnd;i+=3){
            columns.add(query.get(i));
            types.add(query.get(i+1));
            values.add(query.get(i+2));
        }
        if (indexWhere ==-1){
       
            return Constants.updateAllRows;
        }
        else {
            condition =new ArrayList<String>(query.subList(indexWhere+1,query.size()));
            return Constants.updateRows;
        }
    }
   
    private int formatInsert(ArrayList<String> query){
        tableName = new String( query.get(0));
        int indexCols = query.indexOf("columns");
        int indexValues= query.indexOf("values");
        if (indexCols ==-1){
            for (int i=1;i<query.size();i+=2){
                types.add(query.get(i));
                values.add(query.get(i+1));
            }
           return Constants.INSERT_VALUES;
        }else {
            for (int i=2;i<indexValues;i++){
                columns.add(query.get(i));
            }
            for (int i=indexValues+1;i<query.size();i+=2){
                types.add(query.get(i));
                values.add(query.get(i+1));
            }
            return Constants.INSERT_COLUMNS;
        }
    }
    private int formatDelete (ArrayList<String> query){
        tableName = new String( query.get(0));
        int indexWhere = query.indexOf("where");
        if (query.size()==1){
            return Constants.DELETE_ALL;
        }
        else {
            condition =new ArrayList<String>(query.subList(indexWhere+1,query.size()));
            return Constants.DELETE_ROWS;
        }
    }
}