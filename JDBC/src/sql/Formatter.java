package sql;
 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Formatter {
	
	public Formatter() {
		
	}
   
    // edits the statement format
    public String format(String statement) {
    	statement = statement.trim();
    	if (statement.charAt(statement.length() - 1) != ';') {
        	statement = statement + ";";
        }
        statement = statement.replaceAll("\\;", " ;");
        statement = statement.replaceAll("\\(", " ( ");
        statement = statement.replaceAll("\\)", " ) ");
        statement = statement.replaceAll("\\*", " * ");
        statement = statement.replaceAll("\\,", " , ");
        statement = statement.replaceAll("\\>", " > ");
        statement = statement.replaceAll("\\<", " < ");
        statement = statement.replaceAll("\\=", " = ");
        statement = statement.trim().replaceAll("\\s{2,}", " ");
        return formatQuoted(statement);
    }
 
    // edits values format
    public String formatQuoted(String statement) {
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(statement);
        String quoted, modified;
        while (matcher.find()) {
            quoted = matcher.group(1); // without quotes
            modified = quoted.trim();
            modified = modified.replaceAll("\\s{1,}", "~");
            modified = "'" + modified + "'";
            quoted = "'" + quoted + "'";
            statement = statement.replace(quoted, modified);
        }
        return statement;
    }
}