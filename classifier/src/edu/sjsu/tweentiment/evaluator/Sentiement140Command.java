package edu.sjsu.tweentiment.evaluator;

import edu.sjsu.tweentiment.SentimentType;
import edu.sjsu.tweentiment.sentiment140.*;

class Sentiement140Command extends AbstractCommand {
	Sentiment140Connector connector;

	public Sentiement140Command() {
		connector = new Sentiment140Connector();
	}

	@Override
	public String getName() {
		return "Sentiement140";
	}

	@Override
	public SentimentType[] getSentimentValue(String[] texts) {
		Sentiment140Datum[] data = new Sentiment140Datum[texts.length];

		for (int i = 0; i < texts.length; i++) {
			data[i] = new Sentiment140Datum(texts[i]);
		}

		Sentiment140Datum[] response = connector.send(data);
		SentimentType[] sentimentTypes = new SentimentType[response.length];

		for (int i = 0; i < response.length; i++) {
			sentimentTypes[i] = response[i].getType();
		}

		return sentimentTypes;
	}
}