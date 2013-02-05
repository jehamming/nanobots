package nanobot.core.utils;

import nanobot.core.NanoBot;

public class NanoUtilities {
	
	
	
	public static String capitalize( String s ) {
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}

	public static String deCapitalize( String s ) {
		return s.substring(0,1).toLowerCase() + s.substring(1);
	}

}
