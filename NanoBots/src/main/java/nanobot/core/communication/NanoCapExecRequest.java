package nanobot.core.communication;

import java.util.List;

public class NanoCapExecRequest extends NanoMessage {
	private String capability;
	private List<Object> params;
	
	public String getCapability() {
		return capability;
	}
	public void setCapability(String capability) {
		this.capability = capability;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}

	

}
