package dbms;
 
import java.util.Scanner;
 
public class Main {
    Scanner scan = new Scanner(System.in);
    public String queryScanner() {
        StringBuilder queryStatement = new StringBuilder();
        String line = new String();
        while (true) {          
            line = scan.nextLine();
            queryStatement.append(line + " ");
            char lastchar=line.charAt(line.length()-1);
            if (line.equals(";")||lastchar==';') {
                break;
            }
        }
        return queryStatement.toString();
    }
        public static void main(String args[]) throws Exception {
            
        }
 
}