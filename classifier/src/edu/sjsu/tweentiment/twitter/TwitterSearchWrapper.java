package edu.sjsu.tweentiment.twitter;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

import edu.sjsu.tweentiment.util.*;

/**
 * The Twitter deprecated API (API 1.0) wrapper.
 * 
 * @see GET search | Twitter Developers https://dev.twitter.com/docs/api/1/get/search
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

	public ArrayList<Tweet> getTweets() {
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
			System.out.format("HTTP response is %s\n", httpResponse.getStatusLine());
			return null;
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
			System.out.format("Response contains error: %s\n", response.errorMessage);
			return null;
		}

		ArrayList<Tweet> tweetList = response.tweets;

		for (int i = 0, length = tweetList.size(); i < length;) {
			Tweet tweet = tweetList.get(i);

			if (tweet == null) {
				tweetList.remove(i);
				continue;
			}

			i++;
		}

		return tweetList;
	}

}

class SearchResponse {
	@SerializedName("error")
	public String errorMessage;

	@SerializedName("results_per_page")
	public int numResultsPerPage;

	@SerializedName("results")
	public ArrayList<Tweet> tweets;

	@SerializedName("next_page")
	public String nextPageUrlString;
}
