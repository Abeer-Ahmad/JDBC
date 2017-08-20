package validator;

import java.util.ArrayList;

public class DeleteValidator {
	private QueryComparator queryComparator = new QueryComparator();

	public boolean validateCondition(ArrayList<String> colDTD, ArrayList<String> conditionQuery,
			ArrayList<String> typesDTD) {
		if (queryComparator.validateCondition(colDTD, conditionQuery, typesDTD)) {
			return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public String getTypeFile() {
		// TODO Auto-generated method stub
		return queryComparator.getTypeFile();
	}
}

