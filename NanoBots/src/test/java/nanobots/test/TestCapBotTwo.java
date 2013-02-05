package nanobots.test;

import nanobot.core.NanoBot;

public class TestCapBotTwo extends NanoBot {
	
	public TestCapBotTwo() {
		super("TestCapBot2");
	}
	
	public String concatStrings(String s1, String s2) {
		return s1.concat(s2);
	}

}
