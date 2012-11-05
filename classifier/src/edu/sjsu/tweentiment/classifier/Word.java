package edu.sjsu.tweentiment.classifier;

public class Word {
	public String text;
	public double sentimentValue = 0d;
	public SentimentType type = SentimentType.Neutral;

	public Word(String text, double sentimentValue) {
		this.text = text;
		this.sentimentValue = sentimentValue;

		if (sentimentValue == 0) {
			type = SentimentType.Neutral;
		} else if (sentimentValue > 0) {
			type = SentimentType.Positive;
		} else {
			type = SentimentType.Negative;
		}
	}

	public Word(Word word) {
		this(word.text, word.sentimentValue);
	}
}
