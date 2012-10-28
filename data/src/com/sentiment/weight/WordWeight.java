package com.sentiment.weight;

import java.io.*;

import com.google.gson.Gson;
import com.sentiment.obj.WordVH;
import com.sentiment.util.*;

/**
 * viralheat https://www.viralheat.com/
 * 
 * Let's us make 5000 calls per day
 */
public class WordWeight {

	private final String VIRALHEAT_KEY = "zWYw1AnRhv7oftC7X75E"; // 5000 Calls per day

	public Double getWordWeight(String word) throws IOException {
		String url = "https://www.viralheat.com/api/sentiment/review.json?api_key=" + VIRALHEAT_KEY + "&text=" + word;
		String jsonTxt = Util.httpGet(url);
		// String jsonTxt =
		// "{\"prob\":0.806548944920931,\"mood\":\"positive\",\"text\":\"happy\"}";
		System.out.println("jsonTxt:  " + jsonTxt);
		
		Gson gson = new Gson();
		WordVH wordVh = gson.fromJson(jsonTxt, WordVH.class);

		
		Double prob = wordVh.getProb();
		if (wordVh.getMood().equals("negative")) {
			return prob * -1;
		}

		return prob;
	}

}
