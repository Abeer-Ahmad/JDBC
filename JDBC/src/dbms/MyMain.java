package dbms;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MyMain {

    public static void main(String args[]) throws Exception {
		Path home  = Paths.get(System.getProperty("user.home") + File.separator + "DataBases");
        DBMS db = new DBMS(Constants.XML_TYPE, home);
        
        ArrayList<String> query = new ArrayList<String>();
        query.add("1");

        /*query.add("id"); 
        query.add("int");
        query.add("age");
        query.add("int");*/

        
        query.add("varchar");
        query.add("1"); 
        query.add("int");
        query.add("5");


        db.useDB("trials");
        //db.changeDBStructure(Constants.CREATE_TABLE, query);
        db.excuteUpdateQuery(Constants.INSERT_REC, query);
    //    db.changeTableStructure(Constants.ALTER_DROP, query);
        
    }
}
