package edu.sjsu.tweentiment.evaluator;

import java.io.*;

import edu.sjsu.tweentiment.SentimentType;
import edu.sjsu.tweentiment.classifier.*;

class NormalClassifierCommand extends AbstractCommand {
	Classifier classifier;

	public NormalClassifierCommand() {
		try {
			File file = new File("words.json");
			file.delete();
			file.createNewFile();
			classifier = new Classifier("words.json", "stop_words.txt", "noise_words.txt");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public String getName() {
		return "Viralheat-based";
	}

	@Override
	public SentimentType[] getSentimentValue(String[] texts) {
		if (classifier == null) {
			return null;
		}

		SentimentType[] sentimentTypes = new SentimentType[texts.length];

		for (int i = 0; i < texts.length; i++) {
			SentimentResult result;

			try {
				result = classifier.classify(texts[i]);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			sentimentTypes[i] = result.type;
		}

		return sentimentTypes;
	}
}