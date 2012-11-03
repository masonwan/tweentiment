package edu.sjsu.tweentiment;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.google.gson.*;

import edu.sjsu.tweentiment.util.*;

public class Classifier {

	Gson gson = new Gson();
	HashSet<String> stopWordSet = new HashSet<String>();
	HashMap<String, Word> wordMap = new HashMap<String, Word>();
	Pattern wordPattern = Pattern.compile("\\w+");

	/**
	 * 
	 * @param sentimentWordsFilename
	 *            the name of file used as training set.
	 * @throws IOException
	 */
	public Classifier(String sentimentWordsFilename, String stopWordFilename) throws IOException {
		String sentimentWordsJson = IOUtil.readFileToString(sentimentWordsFilename);
		parseDataSet(sentimentWordsJson);

		stopWordSet.clear();
		ArrayList<String> stopWordList = IOUtil.readWordList(stopWordFilename);

		for (String stopWord : stopWordList) {
			boolean isOkay = stopWordSet.add(stopWord);

			if (!isOkay) {
				System.out.printf("'%s' has already been added.\n", stopWord);
			}
		}
	}

	public SentimentResult classify(String text) {
		ArrayList<Word> positiveWordList = new ArrayList<Word>();
		ArrayList<Word> negativeWordList = new ArrayList<Word>();
		double totalSentimentValue = 0d;

		Matcher matcher = wordPattern.matcher(text);

		while (matcher.find()) {
			String matchedWord = matcher.group();

			if (stopWordSet.contains(matchedWord)) {
				continue;
			}

			Word word = wordMap.get(matchedWord);

			if (word != null) {
				if (word.type == SentimentType.Positive) {
					positiveWordList.add(new Word(word));
				} else {
					negativeWordList.add(new Word(word));
				}

				totalSentimentValue += word.sentimentValue;
				continue;
			}
		}

		return new SentimentResult(positiveWordList, negativeWordList, totalSentimentValue);
	}

	@SuppressWarnings("unchecked")
	void parseDataSet(String json) {
		wordMap.clear();
		HashMap<String, Double> map = gson.fromJson(json, (new HashMap<String, Double>()).getClass());

		Set<String> set = map.keySet();

		for (String key : set) {
			wordMap.put(key, new Word(key, map.get(key)));
		}
	}

}
