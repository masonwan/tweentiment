package edu.sjsu.tweentiment.classifier;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.google.gson.*;

import edu.sjsu.tweentiment.file.SentimentFile;
import edu.sjsu.tweentiment.file.SentimentFileImpl;
import edu.sjsu.tweentiment.util.*;

public class Classifier {

	Gson gson = new Gson();
	Set<String> stopWordSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	Set<String> noiseWordSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	Pattern wordPattern = Pattern.compile("\\w+'?\\w+");
	SentimentFile sentimentFile;

	public Classifier(String sentimentWordsFilename, String stopWordFilename, String noiseWordFilename) throws IOException {
		sentimentFile = new SentimentFileImpl(sentimentWordsFilename);

		stopWordSet.clear();
		noiseWordSet.clear();
		ArrayList<String> stopWordList = IOUtil.readWordList(stopWordFilename);
		ArrayList<String> noiseWordList = IOUtil.readWordList(noiseWordFilename);

		for (String stopWord : stopWordList) {
			stopWordSet.add(stopWord);
		}

		for (String noiseWord : noiseWordList) {
			if (!stopWordSet.contains(noiseWord)) { // don't want to add a stop
													// word to a noise word list
				noiseWordSet.add(noiseWord);
			}
		}
	}

	public SentimentResult classify(String text) throws IOException {
		ArrayList<Word> positiveWordList = new ArrayList<Word>();
		ArrayList<Word> negativeWordList = new ArrayList<Word>();
		Double totalSentimentValue = 0d;

		Matcher matcher = wordPattern.matcher(text);
		List<String> wordsFromInput = new ArrayList<String>(matcher.groupCount());

		while (matcher.find()) {
			String matchedWord = matcher.group();

			if (noiseWordSet.contains(matchedWord)) {
				continue;
			} else {
				wordsFromInput.add(matchedWord);
			}
		}

		int gameChanger = 1;
		for (String wordInput : wordsFromInput) {
			if (stopWordSet.contains(wordInput)) {
				gameChanger = -1;
			} else {
				Double weight = 0d;

				try {
					weight = sentimentFile.getWeight(wordInput);
				} catch (IOException e) {
					throw new IOException("Failed to get weight because could not read file -- " + sentimentFile.getFileName());
				}

				if (weight > 0) {
					positiveWordList.add(new Word(wordInput, weight));
				} else if (weight < 0) {
					negativeWordList.add(new Word(wordInput, weight));
				}

				totalSentimentValue += (gameChanger * weight);
				gameChanger = 1;

			}
		}

		return new SentimentResult(positiveWordList, negativeWordList, totalSentimentValue);
	}
}
