package validator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;


import filesHandle.JSON;

public class JsonValidator extends FileValidator{

	private ArrayList<String> colsJSON;
	private ArrayList<String> typesJSON;
	private JSON json = new JSON();
	private boolean validPath(Path p) {
		if (Files.exists(p))
			return true;
		return false;
	}

	public boolean validatePath(Path path) throws SQLException {
		String name = getTableName(path);
		Path jsonPath = Paths.get(path.toString() + File.separator + name + ".json");
		Path schemaPath = Paths.get(path.toString() + File.separator + name + ".schema");
		if (validPath(jsonPath) && validPath(schemaPath)) {
			colsJSON = json.readJSON(schemaPath);
			typesJSON = json.readTypesJSON(schemaPath);
			return true;
		}
		// print message
		return false;
	}

	@Override
	public ArrayList<String> getFields() {
		return colsJSON;
	}

	@Override
	public ArrayList<String> getTypesFields() {
		// TODO Auto-generated method stub
		return typesJSON;
	}

	private String getTableName(Path path) {
		String Path = path.toString();
		int tableIndx = Path.lastIndexOf(File.separator);
		String tableName = Path.substring(tableIndx + 1, Path.length());
		return tableName;
	}
}
