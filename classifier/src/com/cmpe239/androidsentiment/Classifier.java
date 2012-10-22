package com.cmpe239.androidsentiment;

import java.io.*;
import java.util.*;


import com.cmpe239.androidsentiment.util.*;
import com.google.gson.*;

public class Classifier {

	Gson gson = new Gson();
	HashMap<String, Word> wordMap = new HashMap<String, Word>();

	/**
	 * 
	 * @param filename
	 *            the name of file used as training set.
	 * @throws IOException
	 */
	public Classifier(String filename) throws IOException {
		String json = IOUtil.readToString(filename);
		parseDataSet(json);
	}

	public SentimentResult classify(String text) {
		double totalSentimentValue = 0d;

		String[] splitWords = text.split("");
		// TODO

		// return new SentimentResult(totalSentimentValue);
		return null;
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
