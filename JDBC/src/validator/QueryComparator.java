package validator;




import java.util.ArrayList;



public class QueryComparator {
	
	/* this exist cols is for query comparator to modify in the one sent by comparator and send back to any validator*/
	private ArrayList<Boolean> existCol = new ArrayList<Boolean>();
	 private ArrayList<String> arrangedValue = new ArrayList<String>();
	 private String typeFile = new String();
	
	// check whether certain column in DTD colmns or not 
	// return : el index fl DTD
	 public ArrayList<Boolean> getexistCol(){
	    	return existCol;
	    }
	 
	 
     public ArrayList<String> getArrangedValues() {
         return arrangedValue;
     }
     
	 private int isColExist(String col, ArrayList<String> list){
         for (int i=0;i<list.size();i++){
             if (col.equalsIgnoreCase(list.get(i))){
                 return i;
             }
         }
         return -1;
     }
    
	 // marks the columns
     public boolean colsExist (ArrayList<String> col,ArrayList<String> colDTD){
         existCol.clear();
         for (int j=0;j<colDTD.size();j++){
             existCol.add(false);
         }
         for (int i=0;i<col.size();i++){
             int index=isColExist(col.get(i),colDTD);
             if (index==-1){
                     return false;
             }
             
             existCol.set(index,true);
         }
         return true;
     }
     
     /* used by insert by values */
     public boolean compareTypes (ArrayList<String> types, ArrayList<String> typesDTD,
    		 ArrayList<String> values){
    	 if (types.size()==typesDTD.size()){
    	   for (int i=0;i<types.size();i++){
    		  if (! compareType(types.get(i),typesDTD.get(i),values.get(i))){
    			 return false;
    		   }
    	   } 
    	   return true;
    	 }
    	 return false;
     }
     
     public boolean compareType(String type, String typeDTD, String value){
    	 
    	  if (type.equals(typeDTD)){
    		  
    		  typeFile = typeDTD;
    		  
    		  return true;
    	  }  
    	  else if (type.equals("varchar")){
    		  if (typeDTD.equals("int")){
    			  try {
    	                Integer.parseInt(value);
    	            } catch (Exception e) {
    	                return false;
    	            }       
    		  }
    		  else if (typeDTD.equals("float")){
    			  try {
  	                Float.parseFloat(value);
  	            } catch (Exception e) {
  	                return false;
  	            }   
    		  }
    		  else if (typeDTD.equals("date")){
    			  try {
    	                
    				  java.sql.Date  date = new java.sql.Date(0);
    				  date = date.valueOf(value);
    	            } catch (Exception e) {
    	                return false;
    	            }   
    		  }
    		  
    		  typeFile = typeDTD;
    		  return true;
    	  }
    	  else if (type.equals("int")){
    		  if (typeDTD.equals("float")){
    			  try {
    	                Float.parseFloat(value);
    	            } catch (Exception e) {
    	                return false;
    	            }       
    		  }
    		  typeFile = typeDTD;
    		  return true;
    	  }
 		return false;
      }
     
     /* used by insert/update colmn/val */
     public boolean compareAndArrangeColValuesTypes (ArrayList<String> cols, 
    		 ArrayList<String> colsDTD,ArrayList<String> types,ArrayList<String> typesDTD, ArrayList<String>values){
  
    	 existCol.clear();
    	 arrangedValue.clear();
         for (int j=0;j<colsDTD.size();j++){
             existCol.add(false);
             arrangedValue.add("null");
         }
         for (int i=0;i<cols.size();i++){
             int index=isColExist(cols.get(i),colsDTD);
             if (index==-1){
                     return false;
             }
             if (!existCol.get(index)){
             existCol.set(index,true);
             if (! compareType(types.get(i),typesDTD.get(index),values.get(i))){
            	 return false;
              }
               arrangedValue.set(index, values.get(i));
             }
         }
         int index= arrangedValue.indexOf("null");
         while(index!=-1){
            arrangedValue.remove(index);
            index=arrangedValue.indexOf("null");
         }
         return true;
     }
     
     /* it takes the array list condition from query formatter*/
     public boolean validateCondition (ArrayList<String>colDTD,ArrayList<String> conditionQuery,ArrayList<String> typesDTD){
     
        String col = conditionQuery.get(0);  
        
         int colIndex = isColExist(col,colDTD);
         if (colIndex != -1){
        	  
        	String typeDTD= typesDTD.get(colIndex);
        	 if (compareType(conditionQuery.get(2),typeDTD,conditionQuery.get(3))){
        		 
            	 return true;
             }
        	 return false;
         }    
       return false;     
    }
     protected String getTypeFile (){
    	 
    	 return typeFile;
     }
     public static void main(String args[]) throws Exception {
         
    	 ArrayList<String> condition = new ArrayList<String>();
    	 condition.add("age");
    	 condition.add("dd");
    	 condition.add("varchar");
    	 condition.add("20.3");
    	 
    	 ArrayList<String> colDTD = new ArrayList<String>();
    	 colDTD.add("name");
    	 colDTD.add("age");
    	 
    	 ArrayList<String> typeDTD = new ArrayList<String>();
    	 typeDTD.add("varchar");
    	 typeDTD.add("int");
    	 
    	 ArrayList<String> col = new ArrayList<String>();
    	 col.add("age");
    	 col.add("name");
    	 col.add("age");
    	 
    	 ArrayList<String> type = new ArrayList<String>();
    	 type.add("varchar");
    	 type.add("varchar");
    	 type.add("varchar");
    	 
    	 ArrayList<String> values = new ArrayList<String>();
    	 values.add("22");
    	 values.add("shosho");
    	 values.add("20");
    	 QueryComparator query = new QueryComparator();
    	 
    	/* System.out.println(query.colsExist(col,colDTD));
    	 System.out.println(query.getexistCol());
    	 System.out.print(query.compareType("varchar","int", "1.23"));
    	 System.out.print(query.validateCondition(colDTD,condition,typeDTD));*/
    	 System.out.println(query.compareAndArrangeColValuesTypes(col, colDTD, type, typeDTD, values));
    	 System.out.println(query.getArrangedValues());
     	
    	 
    	 
     }
}
