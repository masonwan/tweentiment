package edu.sjsu.tweentiment.twitter;

import java.io.UnsupportedEncodingException;
import java.net.*;

import android.util.Log;

public class TwitterSearchUrlBuilder {

	static String baseUrlString = "http://search.twitter.com/search.json";
	static final int defaultNumTweetsPerPage = 100;
	SearchType searchType;

	int numTweetsPerPage = defaultNumTweetsPerPage;
	String query = null;

	public TwitterSearchUrlBuilder(String query) {
		if (query == null || query.equals("")) {
			throw new IllegalArgumentException();
		}

		this.query = query;
	}

	public TwitterSearchUrlBuilder(String query, int numTweetsPerPage, SearchType searchType) {
		this(query);
		this.numTweetsPerPage = numTweetsPerPage;
		this.searchType = searchType;
	}

	@Override
	public String toString() {
		String encodedQuery = null;
		String url = null;

		try {
			encodedQuery = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		url = this.buildUrlString(encodedQuery);

		return url;
	}

	/**
	 * Builds a URL using a search keyword
	 * 
	 * @param String
	 *            encodedQuery - Search term
	 * @param String
	 *            searchType - general | user
	 * @return String url
	 */
	public final String buildUrlString(String encodedQuery) {
		String url = "";

		if (encodedQuery.equals("")) {
			return url;
		}

		if (searchType == SearchType.User) {
			encodedQuery = "from%3A" + encodedQuery;
		}

		url = String.format("%s?q=%s&lang=en&rpp=%d", baseUrlString, encodedQuery, numTweetsPerPage);
		Log.d("URL", url); 
		return url;
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
