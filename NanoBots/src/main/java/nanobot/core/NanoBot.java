package nanobot.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nanobot.core.communication.Header;
import nanobot.core.communication.NanoCapExecRequest;
import nanobot.core.communication.NanoMessage;
import nanobot.core.consumers.NanoCapExecRequestConsumer;
import nanobot.core.consumers.NanoCapExecResponseConsumer;
import nanobot.core.interfaces.INanoGeneral;
import nanobot.core.utils.Log;
import nanobot.core.utils.NanoIntrospectionUtilities;
import nanobot.core.utils.NanoUtilities;

import com.sdds.SDDSManager;

/**
 * @author J.E Hamming
 * 
 * This is the base class
 * 
 */

/**
 * @author A121916
 *
 */
public abstract class NanoBot implements INanoGeneral {
	private Logger log = Logger.getLogger(this.getClass().getName());

	private String name = null;

	private NanoIdent nanoid;
	private Map<String, List<String>> allCapabilities = new HashMap<String, List<String>>();

	private ArrayList<String> myCapabilities;

	// Publish subscribe mechanism : Consumers
	private List<NanoMessage> consumedMessages = new ArrayList<NanoMessage>();
	
	private NanoCapExecRequestConsumer capRequestConsumer;
	private NanoCapExecResponseConsumer capResponseConsumer;
	
	// SDDS Manager
	private static String DATA_REALM = "NANOBOTS";
	SDDSManager sddsManager = SDDSManager.getInstance(DATA_REALM);

	
	public NanoBot() {
		nanoid = new NanoIdent();
		// Initialize the capabilities
		initializeCapabilities();
		initializeBot();

	}
	
	public NanoBot( String name ) {
		this();
		this.name = name;
	}

	// ****************************************************************************
	// Initialize
	// ****************************************************************************
	public void initializeBot() {

		// Initialize capabilities
		for (String cap : getMyCapabilities()) {
			List<String> l = new ArrayList<String>();
			l.add(getNanoid().toString());
			allCapabilities.put(cap, l);
		}
		

		capRequestConsumer = new NanoCapExecRequestConsumer( this );
		capResponseConsumer = new NanoCapExecResponseConsumer(this);
		
		// Register as a listener
		sddsManager.getConsumerManager().addConsumer(capRequestConsumer);
		sddsManager.getConsumerManager().addConsumer(capResponseConsumer);

	}

	// ****************************************************************************
	// Capabilities
	// ****************************************************************************

	/**
	 * This method returns an Array of Strings, each string representing a
	 * capability (in effect: a methodname)
	 * 
	 * @return
	 */
	public ArrayList<String> getMyCapabilities() {
		if (myCapabilities == null) {
			myCapabilities = new ArrayList<String>();
			List<String> l = NanoIntrospectionUtilities.getPublicMethods(this.getClass());
			// Remove the PREFIX off the method name
			for (String cap : l) {
				cap = NanoUtilities.deCapitalize(cap);
				myCapabilities.add(cap);
			}
		}
		return myCapabilities;

	}

	private void initializeCapabilities() {
		// First, populate the list with my own capabilities
		for (String cap : getMyCapabilities()) {
			List<String> l = new ArrayList<String>();
			l.add(getNanoid().toString());
			allCapabilities.put(cap, l);
		}

	}

	public Map<String, List<String>> getCapabilities() {
		return allCapabilities;
	}


	// ****************************************************************************
	// Capabilities
	// ****************************************************************************
	public boolean hasCapability(String capability) {
		String method = NanoUtilities.deCapitalize(capability);
		return NanoIntrospectionUtilities.containsMethod(method, this.getClass());
	}

	
	public Object executeCapabilityWithParams(String capability, List<Object> params) {
		return _executeCapabilityWithParams(capability, params, true);
	}
	
	public Object _executeCapabilityWithParams(String capability, List<Object> params, boolean goExternal) {
		Object returnValue = null;
		if ( hasCapability(capability) ) { 
			returnValue = executeCapabilityWithParamsInternal(capability, params);
		} else if (goExternal) {
			// See if another bot is present with this capability
			try {
				returnValue = executeCapabilityWithParamsExternal(capability, params);
			} catch (InterruptedException e) {
				Log.info("Not able to send execute capability externally", e);
			}
		}
		return returnValue;		
	}
	
	private Object executeCapabilityWithParamsInternal(String capability, List<Object> params) {
		Object retVal = null;

		Object[] paramArr = params.toArray();
		Class[] classArr = new Class[paramArr.length];
		for (int i = 0; i < paramArr.length; i++) {
			Object object = paramArr[i];
			classArr[i] = object.getClass();
		}
		

		String method = NanoUtilities.deCapitalize(capability);
		try {
			retVal = NanoIntrospectionUtilities.invokeMethodByName(method, this, classArr, paramArr);
		} catch (NoSuchMethodException e) {
			log.log(Level.SEVERE, "Cannot execute capability " + capability + "! :" + e);
			e.printStackTrace();
		}

		return retVal;
	}
	
	private Object executeCapabilityWithParamsExternal(String capability, List<Object> params) throws InterruptedException {
		Object retVal = null;
		NanoCapExecRequest request = new NanoCapExecRequest();
		request.setCapability(capability);
		request.setParams(params);
		String correlationId = sendMessage(request);
		capResponseConsumer.startWaitingForResponse(correlationId);
		synchronized (this) {
			this.wait(CAP_RESULT_TIMEOUT);
		}
//		Thread.sleep(5000);
		retVal = capResponseConsumer.getResponse();
		return retVal;
	}

	public String findCapability(String capability) {
		if (hasCapability(capability)) {
			return this.getNanoid().toString();
		}

		List<String> l = getCapabilities().get(capability);
		if (l != null && l.size() > 0) {
			String id = l.get(0);
			return id;
		}

		return null;
	}

	// ****************************************************************************
	// Messaging
	// ****************************************************************************

	/**
	 * This method gets called when a message is sent from another NanoBot
	 * 
	 * @param header
	 * @param msg
	 */
	public void receiveMessage(NanoMessage message) {
		consumedMessages.add(message);
	}
	


	/**
	 * TODO Remove ?
	 * @param msg
	 * @return
	 */
	public String sendMessage(NanoMessage message) {
		String correlationId = generateCorrelationId();
		return sendMessage(message, correlationId);
	}
	
	public String sendMessage( NanoMessage message, String correlationId) {
		Header header = new Header();
		header.setSender(this.getNanoid().toString());
		header.setTimestamp(Calendar.getInstance().getTime().toString());
		header.setCorrelatiodId(correlationId);
		header.setVersion("1.0");
		String type = message.getClass().getName();
		header.setMessageType(type);
		
		message.setHeader(header);
		
		sddsManager.produce(message);

		return correlationId;
	}

	private String generateCorrelationId() {
		UUID uuid = new UUID();
		return uuid.toString();
	}

	public String getName() {
		if (name != null)
			return name + "_" + nanoid.toString();
		return nanoid.toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public NanoIdent getNanoid() {
		return nanoid;
	}

	public List<NanoMessage> getConsumedMessages() {
		return consumedMessages;
	}

}
