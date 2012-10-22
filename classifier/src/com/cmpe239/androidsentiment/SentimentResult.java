package com.cmpe239.androidsentiment;

import java.util.*;

public class SentimentResult {
	public ResultType type;
	public double sentimentValue;

	private List<Word> positiveWords;

	private List<Word> negativeWords;

	public List<Word> getNegativeWords() {
		return negativeWords;
	}

	public List<Word> getPositiveWords() {
		return positiveWords;
	}

	public SentimentResult(List<Word> positiveWords, List<Word> negativeWords) {

		this.positiveWords = positiveWords;
		this.negativeWords = negativeWords;

		sentimentValue = 0d;

		for (Word word : positiveWords) {
			sentimentValue += word.sentimentValue;
		}

		for (Word word : negativeWords) {
			sentimentValue += word.sentimentValue;
		}
	}
}
