package filesHandle;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class JsonReader implements IFileReader{
 
     private JSON header = new JSON(); 
     
     private String getTableName(Path path) {
            String Path = path.toString();
            int tableIndx = Path.lastIndexOf(File.separator);
            String tableName = Path.substring(tableIndx + 1, Path.length());
            return tableName;
        }
 
    @Override
    public ArrayList<LinkedHashMap<String, String>> read(Path path) throws SQLException {
        
            String tableName = getTableName(path);
            
            Path path1 = Paths.get(path.toString() + File.separator + tableName + ".json");
            Path path2 = Paths.get(path.toString() + File.separator + tableName + ".schema"); 
            ArrayList<String> fields = header.readJSON(path2);
            ArrayList<LinkedHashMap<String, String>> tableMap = new ArrayList<LinkedHashMap<String, String>>();
            FileReader fileReader = null;
			try {
				fileReader = new FileReader(path1.toFile());
			} catch (FileNotFoundException e) {
				
			  throw new SQLException(e.getMessage());
			}
            JSONParser parser = new JSONParser();
            JSONArray list = null;
			try {
				list = (JSONArray)((JSONObject) parser.parse(fileReader)).get("Table");
			} catch (IOException | ParseException e) {
				
				throw new SQLException(e.getMessage());
				
			}
			 
            for (int i=0;i<list.size();i++){
                JSONObject row = (JSONObject) list.get(i);
                Map<String,String> temp=(Map<String, String>) row.get("row"+(i+1));
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                
                for(int j = 0; j < fields.size(); j++){
                    String key = fields.get(j);
                    String value = temp.get(key);
                    map.put(key, value);
                }
                                
                tableMap.add(map);
            }
           
            return tableMap;
        
    }
}