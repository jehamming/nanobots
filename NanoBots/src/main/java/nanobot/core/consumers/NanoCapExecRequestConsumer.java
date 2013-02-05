package nanobot.core.consumers;

import java.util.List;

import nanobot.core.NanoBot;
import nanobot.core.communication.NanoCapExecRequest;
import nanobot.core.communication.NanoCapExecResponse;
import nanobot.core.utils.Log;

import com.sdds.Consumer;

public class NanoCapExecRequestConsumer extends Consumer<NanoCapExecRequest> {
	private NanoBot parent;
	
	public NanoCapExecRequestConsumer(NanoBot bot) {
		this.parent = bot;
	}

	@Override
	public void consume(NanoCapExecRequest capRequest) {
		Log.info(parent.getName() + ": Consuming NanoCapExecRequest");
		String cap = capRequest.getCapability();
		List<Object> params = capRequest.getParams();
		Object retval = parent._executeCapabilityWithParams(cap, params, false);
		if ( retval != null ) {
			Log.info(parent.getName() + ": NanoCapExecRequest: - result of cap exec : " + retval);
			NanoCapExecResponse response = new NanoCapExecResponse();
			response.setResponse(retval);
			parent.sendMessage(response, capRequest.getHeader().getCorrelatiodId());
		}		
	}

}
