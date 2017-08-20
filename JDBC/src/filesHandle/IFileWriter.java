package filesHandle;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IFileWriter {
	 /**
     * take a hashMap list and write it to any File by it's order
     * @param tableMap
     * @param colArrange
     * @throws SQLException 
     */
    public void write(ArrayList<LinkedHashMap<String, String>>tableMap, Path path) throws SQLException;
    public void creatTable(Path p, ArrayList<String> fields) throws SQLException;
}
