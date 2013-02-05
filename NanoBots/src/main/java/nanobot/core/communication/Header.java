package nanobot.core.communication;

public class Header {
	
	private String version;
	private String sender;
	private String timestamp;
	// messageType = packageName + ClassName
	private String messageType;
	private String correlatiodId;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getCorrelatiodId() {
		return correlatiodId;
	}
	public void setCorrelatiodId(String correlatiodId) {
		this.correlatiodId = correlatiodId;
	}
	
	

}
