package nanobot.core.consumers;

import nanobot.core.NanoBot;
import nanobot.core.communication.NanoCapExecResponse;
import nanobot.core.utils.Log;

import com.sdds.Consumer;

public class NanoCapExecResponseConsumer extends Consumer<NanoCapExecResponse> {

	private NanoBot parent;
	private boolean waitingForResponse = false;
	private String correlationId;
	private Object response;
	
	public NanoCapExecResponseConsumer(NanoBot bot) {
		this.parent = bot;
	}

	@Override
	public void consume(NanoCapExecResponse resp) {
		Log.info(parent.getName() + ": Consuming NanoCapExecResponse");
		if (waitingForResponse) {
			if ( resp.getHeader().getCorrelatiodId().equals(correlationId)) {
				// Is a response !
				
				// DO WORK
				this.response = resp.getResponse();
				
				synchronized (parent) {
					parent.notify();					
				}
				
				waitingForResponse = false;
				correlationId = "";
			}
		}
	}

	public void startWaitingForResponse(String correlationId) {
		this.response = null;
		this.waitingForResponse = true;
		this.correlationId = correlationId;
	}


	public Object getResponse() {
		return response;
	}
	
	
	
	
}
