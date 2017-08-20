package filesHandle;
 
import java.io.File;
import java.io.IOException;
 
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
 
public class XmlReader implements IFileReader {
    private ArrayList<LinkedHashMap<String, String>> listMap;
    private LinkedHashMap<String, String> tableMap;
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
 
    public XmlReader() throws SQLException {
    	listMap = new ArrayList<LinkedHashMap<String, String>>();
        docFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
        	throw new SQLException(e);
        	}
 
    }
    private String getTableName(Path path) {
        String Path = path.toString();
        int tableIndx = Path.lastIndexOf(File.separator);
        String tableName = Path.substring(tableIndx + 1, Path.length());
        return tableName;
    }
    @Override
    public ArrayList<LinkedHashMap<String, String>> read(Path path) throws SQLException {
        listMap.clear();
        String tableName = getTableName(path);
        Path pathXml = Paths.get(path.toString() + File.separator + tableName + ".xml");
        try {
            doc = docBuilder.parse(pathXml.toString());
        } catch (SAXException | IOException e) {
            // TODO Auto-generated catch block
        	throw new SQLException(e); 
        	}
        
        try {
        	NodeList rowList = doc.getElementsByTagName("row");
            for (int i = 0; i < rowList.getLength(); i++) {
            	tableMap = new LinkedHashMap<String, String>();
                NodeList cols = (NodeList) rowList.item(i);
                for (int j = 0; j < cols.getLength(); j++) {
                    Node field = cols.item(j);
                    if (field.getNodeType() == Node.ELEMENT_NODE) {
                        tableMap.put(field.getNodeName(), new String(field.getTextContent()));
                    }
                }
                listMap.add(tableMap);
            }
        } catch (Exception e) {
 
        }
        return listMap;
    }
    public ArrayList<LinkedHashMap<String, String>> readConfigurationUsers(Path configXML) throws SQLException {
    	
   	 listMap.clear();
        try {
            doc = docBuilder.parse(configXML.toString());
        } catch (SAXException | IOException e) {
            // TODO Auto-generated catch block
        	throw new SQLException(e); 
        	}
        
        try {
        	NodeList rowList = doc.getElementsByTagName("user");
        	 
            for (int i = 0; i < rowList.getLength(); i++) {
            	tableMap = new LinkedHashMap<String, String>();
            	NodeList info = (NodeList) rowList.item(i);
            	 for (int j = 0; j < info.getLength(); j++) {
                    Node field = info.item(j);
                    if (field.getNodeType() == Node.ELEMENT_NODE) {
                        tableMap.put(field.getNodeName(), new String(field.getTextContent()));
                    }
                }
                listMap.add(tableMap);
                
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return listMap;
   }
    public ArrayList<String> readConfigurationProtocols(Path configXML) throws SQLException {
    	
      	 ArrayList<String> protocols = new ArrayList<String> ();
           try {
               doc = docBuilder.parse(configXML.toString());
           } catch (SAXException | IOException e) {
               // TODO Auto-generated catch block
           	throw new SQLException(e); 
           	}
           
           try {
           	NodeList rowList = doc.getElementsByTagName("protocol");
           	 
               for (int i = 0; i < rowList.getLength(); i++) {
               	String protocol = new String();
               	Node info = (Node) rowList.item(i);        
                       if (info.getNodeType() == Node.ELEMENT_NODE) {
                           protocol=new String(info.getTextContent());
                              protocols.add(protocol);
                       }        
               }
           } catch (Exception e) {
        	   throw new SQLException(e.getMessage());
           }
           return protocols;
      }
   public static void main(String[] args) throws SQLException{
   	Path path = Paths.get(System.getProperty("user.home")+File.separator+"DataBases"+File.separator+"configurationFile.xml");
       XmlReader read =  new XmlReader();
       ArrayList<String> temp=read.readConfigurationProtocols(path);
      System.out.println(temp);
        
   }
   
 
}