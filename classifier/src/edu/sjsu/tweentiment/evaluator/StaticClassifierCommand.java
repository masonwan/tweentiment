package edu.sjsu.tweentiment.evaluator;

import java.io.*;

import edu.sjsu.tweentiment.SentimentType;
import edu.sjsu.tweentiment.classifier.*;

class StaticClassifierCommand extends AbstractCommand {
	StaticClassifier classifier;

	public StaticClassifierCommand() {
		try {
			FileInputStream negationsStream = new FileInputStream("negations.json");
			FileInputStream sentimentWordsStream = new FileInputStream("afinn_111.json");
			classifier = new StaticClassifier(sentimentWordsStream, negationsStream);
			negationsStream.close();
			sentimentWordsStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public String getName() {
		return "AFINN-based";
	}

	@Override
	public SentimentType[] getSentimentValue(String[] texts) {
		SentimentType[] sentimentTypes = new SentimentType[texts.length];

		for (int i = 0; i < texts.length; i++) {
			String text = texts[i];
			SentimentResult result = classifier.classify(text);
			sentimentTypes[i] = result.type;
		}

		return sentimentTypes;
	}
}
