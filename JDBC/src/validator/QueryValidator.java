package validator;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import dbms.Constants;
import utilities.Logs;

public class QueryValidator {

	private InsertValidator insertValidator = new InsertValidator();
	private UpdateValidator updateValidator = new UpdateValidator();
	private DeleteValidator deleteValidator = new DeleteValidator();
	private SelectValidator selectValidator = new SelectValidator();
	
	private ArrayList<Boolean> existCols;
    
	private ArrayList<String> arrangedValue;
    private Logs logger = new Logs();
	private String typeFile;
	public ArrayList<Boolean> getExistFields() {

		return existCols;
	}

	public ArrayList<String> getArrangedValues() {
		return arrangedValue;
	}
    
	public String getTypeFile(){
		return typeFile;
	}
	// 2 methods of validateInsert
	public boolean validateInsert(ArrayList<String> type, ArrayList<String> values, ArrayList<String> typesFields,
			Path path) {
		if (insertValidator.validateInsert(type, values, typesFields)) {
			return true;
		}
		// print not valid parameter.

		return false;
	}

	public boolean validateInsert(ArrayList<String> type, ArrayList<String> cols, ArrayList<String> values,
			ArrayList<String> fields, ArrayList<String> typesFields, Path path) {
		if (insertValidator.validateInsert(cols, fields, type, typesFields, values)) {
			existCols = new ArrayList<Boolean>(insertValidator.getexistCol());
			arrangedValue = new ArrayList<String>(insertValidator.getArrangedValues());
			return true;
		}
		// print not valid parameter.

		return false;
	}

	public boolean validateUpate(ArrayList<String> type, ArrayList<String> cols, ArrayList<String> values,
			ArrayList<String> fields, ArrayList<String> typesFields, Path path) {
		// TODO Auto-generated method stub
		if (updateValidator.validateUpdate(cols, fields, type, typesFields, values)) {
			existCols = new ArrayList<Boolean>(updateValidator.getexistCol());
			arrangedValue = new ArrayList<String>(updateValidator.getArrangedValues());
			return true;
		}
		// print not valid parameter.

		return false;
	}

	public boolean validateUpate(ArrayList<String> type, ArrayList<String> cols, ArrayList<String> values,
			ArrayList<String> condtion, ArrayList<String> fields, ArrayList<String> typesFields, Path path) {
		// TODO Auto-generated method stub
		if (updateValidator.validateUpdate(cols, fields, type, typesFields, values)) {
			if (updateValidator.validateCondition(fields, condtion, typesFields)) {
				existCols = new ArrayList<Boolean>(updateValidator.getexistCol());
				arrangedValue = new ArrayList<String>(updateValidator.getArrangedValues());
				typeFile= new String(updateValidator.getTypeFile());
				return true;
			}
		}
		// print not valid parameter.

		return false;
	}

	public boolean validateDelete(ArrayList<String> condtion, ArrayList<String> fields, ArrayList<String> typesFields,
			Path path) {
		if (deleteValidator.validateCondition(fields, condtion, typesFields)) {
			typeFile = deleteValidator.getTypeFile();
			return true;
		}
		return false;
	}

	private ArrayList<String> formatQuery(ArrayList<String> colsDTD, ArrayList<String> typesDTD) {
		ArrayList<String> queryFormatted = new ArrayList<String>();
		for (int i = 0; i < colsDTD.size(); i++) {
			queryFormatted.add(colsDTD.get(i));
			queryFormatted.add(typesDTD.get(i));
		}
		return queryFormatted;
	}

	public ArrayList<String> changeTableFields(int mode, ArrayList<String> query, ArrayList<String> fields,
			ArrayList<String> typesFields, Path path) throws SQLException {
		// TODO Auto-generated method stub
		if (mode == Constants.ALTER_ADD) {
			fields.add(query.get(1));
			typesFields.add(query.get(2));
		} else {
			int indxRemove = fields.indexOf(query.get(1));
			if (indxRemove == -1){
				 logger.info("Invalid Alter(Delete) paramters !");
				throw new SQLException("ERROR: Invalid parameter.");
			}
			fields.remove(indxRemove);
			typesFields.remove(indxRemove);
		}
		return formatQuery(fields, typesFields);
	}

	public boolean validateSelect(ArrayList<String> cols, ArrayList<String> fields, Path path) {
		if (selectValidator.validateSelect(cols, fields)) {
			existCols = new ArrayList<Boolean>(selectValidator.getexistCol());
			return true;
		}
		return false;
	}

	public boolean validateSelect(ArrayList<String> cols, ArrayList<String> condition, ArrayList<String> fields,
			ArrayList<String> typesFields, Path path) {
		if (selectValidator.validateSelect(cols, fields, typesFields, condition)) {
			existCols = new ArrayList<Boolean>(selectValidator.getexistCol());
			typeFile= new String(selectValidator.getTypeFile());
			return true;
		}
		return false;
	}

	public boolean validateSelectRows(ArrayList<String> condition, ArrayList<String> fields,
			ArrayList<String> typesFields, Path path) {
		if (selectValidator.validateSelectRows(condition, fields, typesFields)) {
			typeFile = new String(selectValidator.getTypeFile());
			return true;
		}
		return false;
	}

}
