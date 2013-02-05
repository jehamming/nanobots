package nanobots.test;

import nanobot.core.NanoBot;
import nanobot.core.communication.NanoMessage;

public class TestBot extends NanoBot {
	
	NanoMessage message;

	public TestBot( String name ) {
		super(name);
	}


	@Override
	public void receiveMessage(NanoMessage message) {
		super.receiveMessage(message);
		this.message = message;
	}



	public Object getMessage() {
		return message;
	}
	
	

}
