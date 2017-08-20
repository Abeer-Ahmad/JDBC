package validator;

import java.util.ArrayList;

public class InsertValidator {

	QueryComparator queryComparator = new QueryComparator();

	public ArrayList<Boolean> getexistCol() {
		return queryComparator.getexistCol();
	}

	public ArrayList<String> getArrangedValues() {
		return queryComparator.getArrangedValues();
	}

	public boolean validateInsert(ArrayList<String> cols, ArrayList<String> colsDTD, ArrayList<String> types,
			ArrayList<String> typesDTD, ArrayList<String> values) {
		if(queryComparator.compareAndArrangeColValuesTypes(cols, colsDTD, types, typesDTD, values)){
			return true;
		}
		return false;
	}

	public boolean validateInsert(ArrayList<String> types, ArrayList<String> values, ArrayList<String> typesDTD) {
		if (queryComparator.compareTypes(types, typesDTD, values)) {
			return true;
		}
		return false;
	}
}
