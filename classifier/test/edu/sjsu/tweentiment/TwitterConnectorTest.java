package edu.sjsu.tweentiment;

import org.junit.*;

public class TwitterConnectorTest {
	TwitterConnector connector;

	@Before
	public void setUp() throws Exception {
		connector = new TwitterConnector();
	}

	// @Test
	// public void throughBrowser() {
	// connector.authenticateThroughBrowser();
	// String verifyCode = connector.readVerifyCodeFromStandardInput();
	// connector.verifyToken(verifyCode);
	// connector.getTweets();
	// }
}
