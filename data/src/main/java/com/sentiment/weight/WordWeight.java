package com.sentiment.weight;

import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.sentiment.util.Util;

/**
 * viralheat https://www.viralheat.com/
 * 
 * Let's us make 5000 calls per day
 */
public class WordWeight {

	private final String VIRALHEAT_KEY = "zWYw1AnRhv7oftC7X75E"; // 5000 Calls
																	// per day

	public Double getWordWeight(String word) throws IOException {
		String url = "https://www.viralheat.com/api/sentiment/review.json?api_key="
				+ VIRALHEAT_KEY + "&text=" + word;
		String jsonTxt = Util.httpGet(url);
		// String jsonTxt =
		// "{\"prob\":0.806548944920931,\"mood\":\"positive\",\"text\":\"happy\"}";
		System.out.println("jsonTxt:  " + jsonTxt);

		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
		Double prob = json.getDouble("prob");
		String mood = json.getString("mood");
		// String text = json.getString("text");

		// System.out.println("text: " + text);
		// System.out.println("prob: " + prob);
		// System.out.println("mood: " + mood);

		if (mood.equals("negative")) {
			return prob * -1;
		}

		return prob;
	}

}
