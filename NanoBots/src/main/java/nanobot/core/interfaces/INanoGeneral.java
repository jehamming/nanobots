/*
 * Created on Jun 22, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package nanobot.core.interfaces;

/**
 * @author dg363
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface INanoGeneral {
	

    
	// To kill all (little backdoor to stop aggressive spreading
	// Note: This kills ALL nanobots that are connected!
	public final String KILL_ALL_NANOBOTS = "KILL_ALL_NANOBOTS";
	    
    // Inter JVM Server using Multicast
    public static final int JVM_PORT = 1200;
    public final String     JVM_MULTICAST = "228.255.255.255";
    
    // Direct Connection using TCP/IP
    public static final int TCP_SERVER_PORT = 4321;
    
    // Capabilities
    
    // Publish a capability
    public static final String CAP_MSG = "CAP:";
    // Probe all the other bots for their capabilities
    public static final String CAP_PROBE = "CAP_PROBE:";
    // Execute a capability
    public static final String CAP_EXEC = "CAP_EXEC:";
    // Result of execution of capability
    public static final String CAP_RESULT = "CAP_RESULT:";
    // Timeout to wait for a result
    public static final int CAP_RESULT_TIMEOUT = 10000;
    
    public static final String NANOBOT_PKG_PREFIX= "nanobots.";
    
    
}
