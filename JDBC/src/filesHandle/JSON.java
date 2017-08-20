package filesHandle;
 
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
 
 
 
public class JSON {
 
   
    public JSON(){
       
    }
    public void createJSON(Path path, ArrayList<String> fields) throws SQLException {
        try {
           
            FileWriter writer = new FileWriter(path.toFile(), false);
                JSONArray list = new JSONArray();
                for (int i=0;i<fields.size();i+=2){
                    JSONObject obj = new JSONObject();
                    ArrayList<String> temp= new ArrayList<String>();
                    temp.add(fields.get(i));
                    temp.add(fields.get(i+1));
                    int index= (i/2)+1;
                    String key = new String("col"+index);
                    obj.put(key,temp );
                    list.add(obj);
                }
                JSONObject header = new JSONObject();
                header.put("Header", list);
                header.writeJSONString(writer);
                //writer.flush();
        
               writer.close();
            
        }catch (Exception e) {
               throw new SQLException(e.getMessage());
        }
    }
   
    public ArrayList<String> readJSON(Path path) throws SQLException{
        ArrayList<String> fields= new ArrayList<String>();
       
        try {
            FileReader fileReader = new FileReader(path.toFile());
            JSONParser parser = new JSONParser();
            JSONArray list= (JSONArray)((JSONObject) parser.parse(fileReader)).get("Header");
            for (int i=0;i<list.size();i++){
                JSONObject obj = (JSONObject) list.get(i);
                ArrayList<String> temp = new ArrayList<String> ();
                String key = new String("col"+(i+1));
                temp = (ArrayList<String>) obj.get(key);
                fields.add(temp.get(0));
            }
            fileReader.close();
            return fields;
           
        } catch (Exception e) {
       
            throw new SQLException(e.getMessage());
        }
       
    }
     public ArrayList<String> readTypesJSON(Path path) throws SQLException {
         try {
                FileReader fileReader = new FileReader(path.toFile());
                JSONParser parser = new JSONParser();
                JSONArray list= (JSONArray)((JSONObject) parser.parse(fileReader)).get("Header");
                ArrayList<String> types = new ArrayList<String>();
                for (int i=0;i<list.size();i++){
                    JSONObject obj = (JSONObject) list.get(i);
                    ArrayList<String> temp = new ArrayList<String> ();
                    String key = new String("col"+(i+1));
                    temp = (ArrayList<String>) obj.get(key);
                    types.add(temp.get(1));
                }
                return types;
               
            } catch (Exception e) {
                // TODO Auto-generated catch block
                throw new SQLException(e.getMessage());
            }
         
     }
    public static void main(String[] args) throws ParseException, SQLException{
       /*
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("toto");
        fields.add("tete");
        fields.add("soso");
        fields.add("hamo");
        
        JSON json = new JSON();
        Path path = Paths.get("C:\\Users"+File.separator+"user"+File.separator+"Desktop"+File.separator+"zew.txt");
        json.createJSON(path, fields);
        try {
            System.out.print(json.readJSON(path));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
}