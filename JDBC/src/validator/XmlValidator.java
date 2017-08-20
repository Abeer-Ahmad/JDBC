package validator;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import filesHandle.DTD;

public class XmlValidator extends FileValidator {
	private ArrayList<String> colsDTD;
	private ArrayList<String> typesDTD;
	private DTD dtdFile = new DTD();

	private boolean validPath(Path p) {
		if (Files.exists(p))
			return true;
		return false;
	}

	@Override
	public boolean validatePath(Path path) throws SQLException {
		String name = getTableName(path);
		Path xmlPath = Paths.get(path.toString() + File.separator + name + ".xml");
		Path dtdPath = Paths.get(path.toString() + File.separator + name + ".dtd");
		if (validPath(xmlPath) && validPath(dtdPath)) {
			colsDTD = dtdFile.readDTD(dtdPath);
			typesDTD = dtdFile.readTypeDtd(dtdPath);
			return true;
		}
		// print message
		return false;
	}

	private String getTableName(Path path) {
		String Path = path.toString();
		int tableIndx = Path.lastIndexOf(File.separator);
		String tableName = Path.substring(tableIndx + 1, Path.length());
		return tableName;
	}

	@Override
	public ArrayList<String> getFields() {
		// TODO Auto-generated method stub
		return colsDTD;
	}

	@Override
	public ArrayList<String> getTypesFields() {
		// TODO Auto-generated method stub
		return typesDTD;
	}

}
