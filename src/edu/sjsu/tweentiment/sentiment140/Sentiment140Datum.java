package edu.sjsu.tweentiment.sentiment140;

import edu.sjsu.tweentiment.SentimentType;

public class Sentiment140Datum {
	public String text = null;
	public int polarity = 0;

	private SentimentType type = null;

	public SentimentType getType() {
		if (type != null) {
			return type;
		}

		if (polarity == 2) {
			return this.type = SentimentType.Neutral;
		} else if (polarity == 4) {
			return this.type = SentimentType.Positive;
		}

		return this.type = SentimentType.Negative;
	}

	public Sentiment140Datum(String text) {
		this.text = text;
	}
}
