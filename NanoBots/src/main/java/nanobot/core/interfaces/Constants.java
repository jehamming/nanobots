package nanobot.core.interfaces;

public interface Constants {

	// Messages
	public final String XML_MESSAGE = "Message";
	public final String XML_HEADER = "Header";
	
	 // Inter JVM Server using Multicast
    public static final int JVM_PORT = 1200;
    public final String     JVM_MULTICAST = "228.255.255.255";
    
    // Direct Connection using TCP/IP
    public static final int TCP_SERVER_PORT = 4321;
}
