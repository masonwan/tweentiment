package edu.sjsu.tweentiment.twitter;

import java.awt.*;
import java.io.*;
import java.net.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

/**
 * The connector with Twitter via OAuth.
 */
public class TwitterConnector {
	final String consumerKey = "D1lSpio5YX0GchoysDqg";
	final String consumerSecret = "UOezdu7QxraGBiRUb5lo0r0d1DT72lSmWo7Tm3BKNY";
	OAuthService service;
	Token requestToken;
	String authUrl;
	Token accessToken;

	public TwitterConnector() {
		ServiceBuilder builder = new ServiceBuilder();
		builder.provider(TwitterApi.class);
		builder.apiKey(this.consumerKey);
		builder.apiSecret(this.consumerSecret);
		this.service = builder.build();

		this.requestToken = service.getRequestToken();
		this.authUrl = service.getAuthorizationUrl(requestToken);
	}

	public void authenticateThroughBrowser() {
		if (authUrl == null) {
			throw new NullPointerException();
		}

		openUrl(authUrl);
	}

	public String readVerifyCodeFromStandardInput() {
		final byte[] buffer = new byte[16];
		int count = 0;

		try {
			count = System.in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (new String(buffer, 0, count)).trim();
	}

	public void verifyToken(String verifyCode) {
		Verifier verifier = new Verifier(verifyCode);
		this.accessToken = service.getAccessToken(requestToken, verifier);
	}

	/**
	 * TODO: this needs more work. A separate thread is needed to deal with the blocking call due to streaming call.
	 * 
	 * @return
	 */
	public String[] getTweets() {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://stream.twitter.com/1.1/statuses/sample.json");
		service.signRequest(this.accessToken, request);
		Response response = request.send();

		InputStream stream = response.getStream();
		byte[] buffer = new byte[8192];
		int readCount = 0;
		int totalCount = 0;

		try {
			while ((readCount = stream.read(buffer)) >= 0) {
				System.out.print(new String(buffer, 0, readCount));

				totalCount += readCount;

				if (totalCount > 10000) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();

		return null;
	}

	/**
	 * Open an URL in desktop browser.
	 * 
	 * @param url
	 */
	static void openUrl(String url) {
		if (Desktop.isDesktopSupported() == false) {
			return;
		}

		Desktop desktop = Desktop.getDesktop();

		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI(url));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}
