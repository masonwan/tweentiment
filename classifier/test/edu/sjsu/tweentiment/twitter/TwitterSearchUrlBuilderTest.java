package edu.sjsu.tweentiment.twitter;

import java.net.URI;

import org.junit.*;
import org.junit.rules.ExpectedException;

public class TwitterSearchUrlBuilderTest {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void nullQuery() {
		expectedException.expect(IllegalArgumentException.class);
		new TwitterSearchUrlBuilder(null);
	}

	@Test
	public void emptyQuery() {
		expectedException.expect(IllegalArgumentException.class);
		new TwitterSearchUrlBuilder("");
	}

	@Test
	public void textQuery() {
		TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder("icecream");
		URI uri = builder.toUri();
		Assert.assertEquals("http://search.twitter.com/search.json?q=icecream&lang=en&rpp=100", uri.toString());
	}

	@Test
	public void userQuery() {
		TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder("@BarackObama");
		URI uri = builder.toUri();
		Assert.assertEquals("http://search.twitter.com/search.json?q=%40BarackObama&lang=en&rpp=100", uri.toString());
	}

	@Test
	public void hashTagQuery() {
		TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder("#thanksgiving");
		URI uri = builder.toUri();
		Assert.assertEquals("http://search.twitter.com/search.json?q=%23thanksgiving&lang=en&rpp=100", uri.toString());
	}

	@Test
	public void mixedQuery() {
		TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder("christmas #idea");
		URI uri = builder.toUri();
		Assert.assertEquals("http://search.twitter.com/search.json?q=christmas+%23idea&lang=en&rpp=100", uri.toString());
	}

	@Test
	public void fiftyTweetQuery() {
		TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder("christmas #idea", 50, SearchType.General);
		URI uri = builder.toUri();
		Assert.assertEquals("http://search.twitter.com/search.json?q=christmas+%23idea&lang=en&rpp=50", uri.toString());
	}
}