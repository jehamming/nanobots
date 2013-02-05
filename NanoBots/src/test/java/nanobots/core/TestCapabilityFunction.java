package nanobots.core;

import java.util.ArrayList;
import java.util.List;

import nanobots.test.TestCapBotOne;
import nanobots.test.TestCapBotTwo;
import junit.framework.TestCase;

public class TestCapabilityFunction extends TestCase {
	
	public void testConcatCapability() {
		TestCapBotTwo tc2 = new TestCapBotTwo();		
		List<Object> params = new ArrayList<Object>(); 
		params.add("a");
		params.add("b");
		// tc2 can directly execute this capability
		Object result = tc2.executeCapabilityWithParams("concatStrings", params);
		assertNotNull(result);
		assertEquals("ab", result);
		
		
		TestCapBotOne tc1 = new TestCapBotOne();
		// tc1 does not have the capability and needs tc2 to execute it.
		result = tc1.executeCapabilityWithParams("concatStrings", params);
		assertNotNull(result);
		assertEquals("ab", result);
		
	}

}
