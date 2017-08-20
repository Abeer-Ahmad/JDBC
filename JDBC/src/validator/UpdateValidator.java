package validator;




import java.util.ArrayList;



public class UpdateValidator {
	private QueryComparator queryComparator = new QueryComparator();

	public ArrayList<Boolean> getexistCol() {
		return queryComparator.getexistCol();
	}

	public ArrayList<String> getArrangedValues() {
		return queryComparator.getArrangedValues();
	}

	public boolean validateUpdate(ArrayList<String> cols, ArrayList<String> colsDTD, ArrayList<String> types,
			ArrayList<String> typesDTD, ArrayList<String> values) {
		if(queryComparator.compareAndArrangeColValuesTypes(cols, colsDTD, types, typesDTD, values)){
			return true;
		}
		return false;
	}

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
