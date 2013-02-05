package nanobot.core.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {
	
	private static Logger logger = Logger.getRootLogger();
	
	public static void info( String msg) {
		logger.log(Level.INFO, msg);
	}
	public static void info( String msg, Throwable e ) {
		logger.log(Level.INFO, msg, e);
	}
	public static void debug( String msg) {
		logger.log(Level.DEBUG, msg);
	}
	public static void debug( String msg, Throwable e ) {
		logger.log(Level.DEBUG, msg, e);
	}
	public static void error( String msg) {
		logger.log(Level.ERROR, msg);
	}
	public static void error( Throwable e ) {
		logger.log(Level.ERROR, e.getMessage(), e);
	}
	public static void error( String msg, Throwable e ) {
		logger.log(Level.ERROR, msg, e);
	}

}
