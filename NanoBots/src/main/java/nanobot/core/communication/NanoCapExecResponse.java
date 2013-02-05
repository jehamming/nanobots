package nanobot.core.communication;

public class NanoCapExecResponse extends NanoMessage {
	private Object response;

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

}
