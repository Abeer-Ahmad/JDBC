package validator;
 
import java.util.ArrayList;


 
public class SelectValidator {
 
	QueryComparator queryComparator = new QueryComparator();
	
	public ArrayList<Boolean> getexistCol(){
    	return queryComparator.getexistCol();
    }
     
	public boolean validateSelect(ArrayList<String> cols,ArrayList<String> colsDTD) {
		if (queryComparator.colsExist(cols, colsDTD)){
			return true;
		}
		return false;
	}


	public boolean validateSelect(ArrayList<String> cols, ArrayList<String> colsDTD,
			ArrayList<String> typesDTD,ArrayList<String> condition) {
		if (queryComparator.colsExist(cols, colsDTD)&& queryComparator.validateCondition(colsDTD, condition, typesDTD)){
			return true;
		}
		return false;
	}
	
	public boolean validateSelectRows(ArrayList<String> condition, ArrayList<String> colsDTD,
			ArrayList<String> typesDTD) {		
		if (queryComparator.validateCondition(colsDTD, condition, typesDTD)){
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