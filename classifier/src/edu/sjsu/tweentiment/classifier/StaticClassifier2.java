package edu.sjsu.tweentiment.classifier;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;

import com.google.gson.Gson;

public class StaticClassifier2 {

	Gson gson = new Gson();
	HashMap<String, Double> sentimentWordMap = new HashMap<String, Double>();
	HashSet<String> negationSet = new HashSet<String>();

	Pattern wordPattern = Pattern.compile("\\w+('\\w*)?");

	SpellChecker spellChecker;

	@SuppressWarnings("unchecked")
	public StaticClassifier2(File dictionaryFile, InputStream sentimentWordsStream, InputStream negationsStream) throws IOException {

		Directory directory = FSDirectory.open(dictionaryFile);
		spellChecker = new SpellChecker(directory);
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		spellChecker.indexDictionary(new PlainTextDictionary(new File("dictionary.txt")), indexWriterConfig, true);

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

		Matcher matcher = wordPattern.matcher(text);
		boolean isPreviousNegation = false;

		while (matcher.find()) {
			String matchedWord = matcher.group();
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
