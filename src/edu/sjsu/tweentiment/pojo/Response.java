package edu.sjsu.tweentiment.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Response {
	@SerializedName("results_per_page")
	private int results_per_page;
	@SerializedName("results")
	private ArrayList<Tweet> tweets;

	public int getResults_per_page() {
		return results_per_page;
	}

	public void setResults_per_page(int results_per_page) {
		this.results_per_page = results_per_page;
	}

	public ArrayList<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}
}
