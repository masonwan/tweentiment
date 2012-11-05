package edu.sjsu.tweentiment.twitter;

import java.util.*;

import org.junit.*;

public class TwitterSearchWrapperTest {
	TwitterSearchWrapper wrapper;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void emptyQuery() {
		wrapper = new TwitterSearchWrapper("          ");

		ArrayList<Tweet> tweetList = wrapper.getTweets();

		Assert.assertNull(tweetList);
	}

	@Test
	public void getTweets() {
		int numTweets = 100;
		TwitterSearchUrlBuilder urlBuilder = new TwitterSearchUrlBuilder("#apple stock", numTweets);
		wrapper = new TwitterSearchWrapper(urlBuilder);

		ArrayList<Tweet> tweetList = wrapper.getTweets();

		Assert.assertNotNull(tweetList);
		Assert.assertTrue(tweetList.size() <= numTweets);
	}

	@SuppressWarnings("unused")
	private void printTweets(final List<Tweet> tweetList) {
		for (int i = 0; i < tweetList.size(); i++) {
			Tweet tweet = tweetList.get(i);
			System.out.format("Id: %s\n", tweet.id);
			System.out.format("Is a retweet? %b\n", tweet.isRetweet());
			System.out.format("Text: %s\n", tweet.text);
			System.out.println();
		}

		System.out.format("Total %d number of tweets.\n", tweetList.size());
	}
}