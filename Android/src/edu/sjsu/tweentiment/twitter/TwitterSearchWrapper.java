package edu.sjsu.tweentiment.twitter;

import java.io.IOException;
import java.net.*;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import edu.sjsu.tweentiment.util.IOUtil;

/**
 * The Twitter deprecated API (API 1.0) wrapper.
 * 
 * @see GET search | Twitter Developers
 *      https://dev.twitter.com/docs/api/1/get/search
 */
public class TwitterSearchWrapper {

	URI uri;
	SearchResponse originalRespond;
	String originalRespondContent;

	public TwitterSearchWrapper(String query) {
		this(new TwitterSearchUrlBuilder(query));
	}

	public TwitterSearchWrapper(URI uri) {
		this.uri = uri;
	}

	public TwitterSearchWrapper(TwitterSearchUrlBuilder urlBuilder) {
		this(urlBuilder.toUri());
	}

	public List<Tweet> getTweets() {
		HttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(this.uri);
		HttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (httpResponse.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
			return new ArrayList<Tweet>();
		}

		String jsonText = null;

		try {
			this.originalRespondContent = jsonText = IOUtil.readStreamToString(httpResponse.getEntity().getContent());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Gson gson = new Gson();
		SearchResponse response = this.originalRespond = gson.fromJson(jsonText, SearchResponse.class);

		if (response.errorMessage != null) {
			return null;
		}

		Tweet[] tweetList = response.tweets;

		return Arrays.asList(tweetList);
	}

	class SearchResponse {
		@SerializedName("error")
		public String errorMessage;

		@SerializedName("results_per_page")
		public int numResultsPerPage;

		@SerializedName("results")
		public Tweet[] tweets;

		@SerializedName("next_page")
		public String nextPageUrlString;
	}
}
