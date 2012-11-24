package edu.sjsu.tweentiment.classifier;

import com.google.gson.annotations.*;

import edu.sjsu.tweentiment.SentimentType;

public class Word {
	@SerializedName("name")
	public String text;
	@SerializedName("weight")
	public double sentimentValue = 0d;
	@Expose
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
