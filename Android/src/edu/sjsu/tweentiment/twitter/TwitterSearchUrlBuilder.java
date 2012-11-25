package edu.sjsu.tweentiment.twitter;

import java.io.UnsupportedEncodingException;
import java.net.*;

public class TwitterSearchUrlBuilder {

	static String baseUrlString = "http://search.twitter.com/search.json";
	static final int defaultNumTweetsPerPage = 100;

	int numTweetsPerPage = defaultNumTweetsPerPage;
	String query = null;

	public TwitterSearchUrlBuilder(String query) {
		if (query == null || query.equals("")) {
			throw new IllegalArgumentException();
		}

		this.query = query;
	}

	public TwitterSearchUrlBuilder(String query, int numTweetsPerPage) {
		this(query);
		this.numTweetsPerPage = numTweetsPerPage;
	}

	@Override
	public String toString() {
		String encodedQuery = null;

		try {
			encodedQuery = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return String.format("%s?q=%s&lang=en&rpp=%d", baseUrlString, encodedQuery, numTweetsPerPage);
	}

	public URI toUri() {
		try {
			return new URI(toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
	}
}
