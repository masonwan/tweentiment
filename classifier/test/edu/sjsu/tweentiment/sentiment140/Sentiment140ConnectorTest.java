package edu.sjsu.tweentiment.sentiment140;

import org.junit.*;

import edu.sjsu.tweentiment.SentimentType;

public class Sentiment140ConnectorTest {
	Sentiment140Connector connector;

	@Before
	public void setUp() {
		connector = new Sentiment140Connector();
	}

	@Test
	public void sendThreeData() {
		Sentiment140Datum[] requestData = new Sentiment140Datum[] { new Sentiment140Datum("I have not idea."), new Sentiment140Datum("I hate you."), new Sentiment140Datum("I love you."), };

		Sentiment140Datum[] responseData = connector.send(requestData);

		Assert.assertEquals(3, responseData.length);
		Assert.assertEquals(SentimentType.Neutral, responseData[0].getType());
		Assert.assertEquals(SentimentType.Negative, responseData[1].getType());
		Assert.assertEquals(SentimentType.Positive, responseData[2].getType());
	}
}
