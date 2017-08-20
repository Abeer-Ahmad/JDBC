package utilities;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;
public class Logs {
	private Logger logger;
	public Logs(){
		logger = LogManager.getLogger(Logs.class);
	}
    public void info(String s){
    	logger.info(s);
    }
    public void error(String s){
    	logger.error(s);
    }

}
