package filesHandle;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

public class DTD {
	
	public DTD(){
		
	}
	public void createDTD(Path path, ArrayList<String> fields) throws SQLException {
        try {
        	
            FileWriter writer = new FileWriter(path.toFile(), false);
            int i;
            try {
            	writer.write("<!ELEMENT table (row+)>");
                writer.write(System.getProperty("line.separator"));
                writer.write("<!ELEMENT row (");
                for (i = 0; i < fields.size() - 2; i+=2) {
                    writer.write(fields.get(i) + ",");
                }
                writer.write(fields.get(i) + ")>");
                writer.write(System.getProperty("line.separator"));
                for (i = 0; i < fields.size(); i += 2) {
                    writer.write("<!ELEMENT " + fields.get(i) + " (#PCDATA)>");
                    writer.write(System.getProperty("line.separator"));
                    writer.write("<!ATTLIST " + fields.get(i) + " type  CDATA " + "\""+fields.get(i + 1)+"\""+ ">");
                    writer.write(System.getProperty("line.separator"));
                }
                writer.flush();
                writer.close();
            } catch (NumberFormatException e) {
            	throw new SQLException(e);
            }
        } catch (IOException ex) {
        	throw new SQLException(ex);
        }
    }
    public ArrayList<String> readDTD(Path path) throws SQLException{
    	
    	ArrayList<String> fields = new ArrayList <String>();
        String[] temp = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(path.toString())))
        {
            br.readLine();
            String sCurrentLine = br.readLine();
            String s = sCurrentLine.substring(sCurrentLine.indexOf('(') + 1, sCurrentLine.length()-2);
            temp = (s.split(","));
           for (int i=0; i<temp.length;i++){
        	   fields.add(temp[i]);
           }
        } catch (IOException e) {
        	throw new SQLException(e);
        	}
        return fields;
    }
    
    
    
    public ArrayList<String> readTypeDtd(Path path) throws SQLException {
        ArrayList<String> type = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
            br.readLine();
            br.readLine();
            while (br.readLine() != null) {
                String sCurrentLine = br.readLine();
                String s = sCurrentLine.substring(sCurrentLine.indexOf('"') + 1, sCurrentLine.length() - 2);
                type.add(s);
            }
 
        } catch (IOException e) {
        	throw new SQLException(e);
        	}
        return type;
    }
   
}
