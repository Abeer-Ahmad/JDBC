package filesHandle;
 
import java.io.File;

 
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
 
import java.io.FileWriter;
 
import java.io.Writer;
 
 
 
 
 
 
public class JsonWriter implements IFileWriter{
 
    private JSON jsonMaker;
    public JsonWriter(){
        jsonMaker = new JSON();
    }
    @Override
    public void write(ArrayList<LinkedHashMap<String, String>> tableMap, Path path) throws SQLException {
        try{
        JSONArray table = new JSONArray();
        String tableName = getTableName(path);
        path = Paths.get(path.toString() + File.separator + tableName + ".json");
         
            FileWriter writer = new FileWriter(path.toFile());
            for (int i=0; i < tableMap.size(); i++){
                JSONObject row = new JSONObject();
                row.put("row"+(i+1), tableMap.get(i));
                table.add(row);
            }
            JSONObject all = new JSONObject();
            all.put("Table", table);
            all.writeJSONString(writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
           
            throw new SQLException(e.getMessage());
        }
       
    }
    @Override
    public void creatTable(Path path, ArrayList<String> fields) throws SQLException {
       
         ArrayList<LinkedHashMap<String, String>> tableMap = new ArrayList<LinkedHashMap<String, String>>();
         
         String tableName = getTableName(path);
         Path schemaPath = Paths.get(path.toString() + File.separator + tableName + ".schema");
         jsonMaker.createJSON(schemaPath, fields);
         write(tableMap,path);
    }
   
    private String getTableName(Path path) {
        String Path = path.toString();
        int tableIndx = Path.lastIndexOf(File.separator);
        String tableName = Path.substring(tableIndx + 1, Path.length());
        return tableName;
    }
 
   
     public static void main(String args[]) throws SQLException {
            LinkedHashMap<String,String> row = new LinkedHashMap<String,String>();
            ArrayList<String> fieldss = new ArrayList<String>();
            fieldss.add("age");
            fieldss.add("int");
            fieldss.add("name");
            fieldss.add("varchar");
            fieldss.add("num");
            fieldss.add("int");
           
            JSON json = new JSON();
            Path home  = Paths.get(System.getProperty("user.home") + File.separator + "DataBases" + File.separator + "zew"
                    + File.separator + "1"+File.separator+"1.schema");
            json.createJSON(home, fieldss);
            
            
            Path path2 = Paths.get(System.getProperty("user.home") + File.separator + "DataBases" + File.separator + "zew"
                    + File.separator + "1");
          JsonWriter kk = new JsonWriter();
          JsonReader yy = new JsonReader();
            ArrayList<LinkedHashMap<String, String>> tableMap = new ArrayList<LinkedHashMap<String, String>>();
            LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
            fields.put("age","22");
            fields.put("name", "yass");
            fields.put("num", "5");
            tableMap.add(fields);
            fields = new LinkedHashMap<String, String>();
            fields.put("age","89");
            fields.put("name", "yara");
            fields.put("num", "9");
            tableMap.add(fields);
            kk.write(tableMap, path2);
            System.out.println("jsonwriter"+yy.read(path2));
            //kk.write(tableMap, path);
           
        }
       
}