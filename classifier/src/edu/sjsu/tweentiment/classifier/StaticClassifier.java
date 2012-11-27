package edu.sjsu.tweentiment.classifier;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.google.gson.Gson;

public class StaticClassifier {

	Gson gson = new Gson();
	HashMap<String, Double> sentimentWordMap = new HashMap<String, Double>();
	HashSet<String> negationSet = new HashSet<String>();

	Pattern wordPattern = Pattern.compile("[@]?\\w+(\'\\w*)?|([^\\w\\s@])+");

	@SuppressWarnings("unchecked")
	public StaticClassifier(InputStream sentimentWordsStream, InputStream negationsStream) throws IOException {
		// Read sentiment words.
		sentimentWordMap = gson.fromJson(new InputStreamReader(sentimentWordsStream), (new HashMap<String, Integer>()).getClass());

		// Read negations.
		String[] negations = gson.fromJson(new InputStreamReader(negationsStream), (new String[0]).getClass());

		for (String negation : negations) {
			negationSet.add(negation);
		}
	}

	public SentimentResult classify(String text) {
		ArrayList<Word> positiveWordList = new ArrayList<Word>();
		ArrayList<Word> negativeWordList = new ArrayList<Word>();
		int totalSentimentValue = 0;

		Matcher matcher = wordPattern.matcher(text.toLowerCase());
		boolean isPreviousNegation = false;

		while (matcher.find()) {
			String matchedWord = matcher.group();

			// Reset the negation flag.
			if (matchedWord.equals(".") || matchedWord.equals("!") || matchedWord.equals("?")) {
				isPreviousNegation = false;
				continue;
			}

			char firstChar = matchedWord.charAt(0);

			// Filter out the mention and hashtag.
			if (firstChar == '@') {
				continue;
			}

			Double sentimentValue = sentimentWordMap.get(matchedWord);

			if (sentimentValue == null) {
				boolean doesContain = negationSet.contains(matchedWord);

				if (doesContain) {
					isPreviousNegation = true;
				}
			} else {
				if (isPreviousNegation) {
					sentimentValue = -sentimentValue;
				}

				totalSentimentValue += sentimentValue;

				if (sentimentValue > 0) {
					positiveWordList.add(new Word(matchedWord, sentimentValue));
				} else if (sentimentValue < 0) {
					negativeWordList.add(new Word(matchedWord, sentimentValue));
				}

				isPreviousNegation = false;
			}
		}

		return new SentimentResult(positiveWordList, negativeWordList, totalSentimentValue);
	}
}
