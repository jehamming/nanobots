package nanobots.core.utils;

import nanobot.core.utils.StringUtils;
import junit.framework.TestCase;

public class TestStringUtils extends TestCase {
	
	public void testStringCompare() {
		String s1 = "aap";
		String s2 = "noot";
		String s3 = "aabb";
		String s4 = "aa";
		
		assertEquals(100, StringUtils.compareStringsPercentage(s1, s1));
		assertEquals(0, StringUtils.compareStringsPercentage(s1, s2));
		assertEquals(50, StringUtils.compareStringsPercentage(s3, s4));
	}

}
