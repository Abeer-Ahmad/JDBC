package validator;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class FileValidator {
	public abstract boolean validatePath(Path path) throws SQLException;//exist
	public abstract ArrayList<String> getFields();//exist
	public abstract ArrayList<String> getTypesFields();//exist
}
