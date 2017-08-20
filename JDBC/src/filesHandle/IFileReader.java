package filesHandle;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IFileReader {

	/**
	 * to pars a file and can read it
	 * @return a list of hashMap 
	 * @throws SQLException 
	 */
     public ArrayList<LinkedHashMap<String, String>> read(Path path) throws SQLException;
}
