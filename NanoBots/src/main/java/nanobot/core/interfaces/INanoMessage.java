package nanobot.core.interfaces;

// NanoMessage Layout: 
//
//<NanoMessage>
// <header>
//  <originator>janneman</originator>
//  <receiver>piet</receiver>
//  <type>request/reply</type>
//  <capability>something</capability>
//  <path>
//   <nanobot>nanobot_id</nanobot>
//  </path>
// </header>
// <body>
//  <NanoDataObject>
//    <attribute>value</attribute>
//  </NanoDataObject>
// </body>
//</NanoMessage>
//
public interface INanoMessage {

	public final String MSG_ROOTNODE = "NanoMessage";
	public final String MSG_BODY = "body";
	public final String MSG_NDO_ROOTNODE = "NanoDataObject";
	public final String MSG_HEADER_ROOTNODE = "header";
	public final String MSG_HEADER_ORIGINATOR = "originator";
	public final String MSG_HEADER_RECEIVER = "receiver";
	public final String MSG_HEADER_TYPE = "type";
	public final String MSG_HEADER_CAPABILITY = "capability";
	public final String MSG_HEADER_PATH = "path";
	public final String MSG_HEADER_PATH_ITEM = "nanobot";
	
	public final String TYPE_REQUEST= "Request";
	public final String TYPE_REPLY= "Reply";
	public final String TYPE_MESSAGE= "Message";
	
}
