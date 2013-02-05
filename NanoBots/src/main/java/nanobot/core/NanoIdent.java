/*
 * Created on Jun 25, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package nanobot.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author dg363
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NanoIdent {
	UUID uuid;
	String host;
	
	public NanoIdent() {
		//HOST
		host = "host_unknown";
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		uuid = new UUID();
	}

	/**
	 * @return
	 */
	public String getHost() {
		return host;
	}


	/**
	 * @return
	 */
	public UUID getUuid() {
		return uuid;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "NanoBot_" + uuid.toString() + "@" + host;
	}

}
