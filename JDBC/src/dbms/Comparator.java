package dbms;


import java.sql.Date;
import java.util.ArrayList;
public class Comparator {
 
    String operator, valType, val;
    String current; // value of current column
 
    Comparator() {
       
    }
   
    public boolean compare(ArrayList<String> query, String colVal) {
        setInfo(query, colVal);
        return compareType();
    }
 
    public void setInfo(ArrayList<String> query, String colVal) {
        operator = query.get(0);
        valType = query.get(1);
        val = query.get(2);
        val = val.replaceAll("~", " ");
        current = colVal;
        current=current.trim();
    }
 
    public boolean compareType() {
        if (valType.equals("int")) {
            try {
                return compare(Integer.parseInt(val), Integer.parseInt(current));
            } catch (Exception e) {
                System.out.println("Invalid Parsing To Compare Integers");
                System.out.println(e.toString());
            }          
        }
        else if (valType.equals("float")) {
            try {
                return compare(Float.parseFloat(val), Float.parseFloat(current));
            } catch (Exception e) {
                System.out.println("Invalid Parsing To Compare Floats");
                System.out.println(e.toString());
            }          
        }
        else if (valType.equals("date")) {
            Date date = new Date(0);
            try {
            	//java.sql.Date parsed=new java.sql.Date(date.getTime())
                return compare(date.valueOf(val), date.valueOf(current));
            } catch (Exception e) {
                System.out.println("Invalid Parsing To Compare Dates");
                System.out.println(e.toString());
            }          
        }
        else if (valType.equals("varchar")) {
            return compare(val, current);          
        }
        return false;
    }
 
    public boolean compare(int val,int current) {
        if(operator.equals(">")&&compareInt(current,val)>0)
            return true;
        else if(operator.equals("<")&&compareInt(current,val)<0)
            return true;
        else if(operator.equals("=")&&compareInt(current,val)==0)
            return true;
        else
            return false;
    }
    public boolean compare(String val,String current) {
        if(operator.equals(">")&&compareString(current,val)>0)
            return true;
        else if(operator.equals("<")&&compareString(current,val)<0)
            return true;
        else if(operator.equals("=")&&compareString(val,current)==0)
            return true;
        else
            return false;
    }
    public boolean compare(Float val,Float current) {
        if(operator.equals(">")&&compareFloat(current,val)>0)
            return true;
        else if(operator.equals("<")&&compareFloat(current,val)<0)
            return true;
        else if(operator.equals("=")&&compareFloat(val,current)==0)
            return true;
        else
            return false;
    }
    /* date1 value date 2 current*/
    public boolean compare(java.util.Date val,java.util.Date current) {
        if(operator.equals(">")&&compareDate(current,val)>0)
            return true;
        else if(operator.equals("<")&&compareDate(current,val)<0)
            return true;
        else if(operator.equals("=")&&compareDate(current,val)==0)
            return true;
        else
            return false;
    }
 ///General compare logic for different 4 types.
    public int compareInt(int val1,int val2) {
        if(val1>val2)
            return 1;
        else if(val1<val2)
            return -1;
        else
            return 0;
    }
    public int compareFloat(Float val1,Float val2) {
        return val1.compareTo(val2);
    }
    public int compareString(String val1,String val2) {
        return val1.compareTo(val2);
    }
    public int compareDate(java.util.Date date1,java.util.Date date2) {
        return date1.compareTo(date2);
    }
   
    public static void main(String[] args) {
        Comparator c = new Comparator();
        ArrayList<String> send = new ArrayList<String>();
        send.add("="); // ignore case?
        send.add("date");
        send.add("2016-12-30");
        System.out.println(c.compare(send, "2016-12-30"));
        // send.add("<"); // ignore case?
        // System.out.println(c.compare(send, "Shrouk"));
    }
}