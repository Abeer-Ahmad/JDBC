package filesHandle;
 
import java.io.File;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 


 
public class XmlWriter implements IFileWriter {
    private DocumentBuilderFactory docFactory;
    private TransformerFactory transformerFactory;
    private String INDENT_TRANSFORM;
    private DocumentBuilder docBuilder;
    private Transformer transformer;
    private Document doc;
    private DTD tableMaker;
 
    public XmlWriter() throws SQLException {
        docFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();
        INDENT_TRANSFORM = "{http://xml.apache.org/xslt}indent-amount";
        tableMaker = new DTD();
        try {
            docBuilder = docFactory.newDocumentBuilder();
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(INDENT_TRANSFORM, "2");
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
        	throw new SQLException(e);
        } catch (TransformerConfigurationException e) {
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
    public void write(ArrayList<LinkedHashMap<String, String>> tableMap, Path path) throws SQLException {
        // TODO Auto-generated method stub
        doc = docBuilder.newDocument();
        Element table = doc.createElement("table");
        doc.appendChild(table);
 
        for (int i = 0; i < tableMap.size(); i++) {
            Element row = doc.createElement("row");
            table.appendChild(row);
            //add fields to row
            LinkedHashMap<String, String> map = tableMap.get(i);
            Iterator itr = map.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry mp = (Map.Entry) itr.next();
                String key = (String) mp.getKey();
                String text = (String) mp.getValue();
                Element col = doc.createElement(key);
                col.appendChild(doc.createTextNode(text));
                row.appendChild(col);
            }
        }
        save(doc, path);
    }
 
    private void save(Document doc, Path path) throws SQLException {
        DOMSource source = new DOMSource(doc);
        String tableName = getTableName(path);
        path = Paths.get(path.toString() + File.separator + tableName + ".xml");
        StreamResult result = new StreamResult(new File(path.toString()));
        try {
            doc.setXmlVersion("1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "ROUTE-VALUE");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, tableName + ".dtd");
            transformer.transform(source, result);
        } catch (TransformerException e) {
        	throw new SQLException(e);
        	}
    }
 
    @Override
    public void creatTable(Path table, ArrayList<String> fields) throws SQLException {
        // TODO Auto-generated method stub
        ArrayList<LinkedHashMap<String, String>> tableMap = new ArrayList<LinkedHashMap<String, String>>();
        
        String tableName = getTableName(table);
        
        Path dtdPath = Paths.get(table.toString() + File.separator + tableName + ".dtd");
        tableMaker.createDTD(dtdPath, fields);
        write(tableMap, table);
    }
 
   /* public static void main(String args[]) {
        Path home  = Paths.get(System.getProperty("user.home") + File.separator + "DataBases");
        Path path = Paths.get(System.getProperty("user.home") + File.separator + "DataBases" + File.separator + "Teeest"
                + File.separator + "1");
    //  XmlWriter kk = new XmlWriter();
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
        System.out.println(tableMap.size());
 
        //kk.write(tableMap, path);
        DBMS db = new DBMS(Constants.XML_TYPE, home);
    }*/
}