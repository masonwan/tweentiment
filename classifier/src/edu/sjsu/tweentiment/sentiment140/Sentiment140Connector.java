package edu.sjsu.tweentiment.sentiment140;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import edu.sjsu.tweentiment.util.IOUtil;

public class Sentiment140Connector {
	static final String baseUriString = "http://www.sentiment140.com/api/bulkClassifyJson?appid=%s";
	static ArrayList<URI> uris;
	static String[] applicationIds;
	static Random rand = new Random();
	static Gson gson = new Gson();

	static {
		applicationIds = new String[] { "masonaxcte@gmail.com", };

		uris = new ArrayList<URI>(applicationIds.length);
		for (int i = 0; i < applicationIds.length; i++) {
			URI url;
			try {
				url = new URI(String.format(baseUriString, applicationIds[i]));
				uris.add(url);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public Sentiment140Connector() {
	}

	static URI getNextUri() {
		int index = rand.nextInt(uris.size());
		return uris.get(index);
	}

	public Sentiment140Datum[] send(Sentiment140Datum[] requests) {
		Sentiment140Request request = new Sentiment140Request();
		request.data = requests;
		String requestJson = gson.toJson(request);

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getNextUri());

		StringEntity entity;
		try {
			entity = new StringEntity(requestJson);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		entity.setContentType("application/json");
		httpPost.setEntity(entity);

		HttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (httpResponse.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
			System.out.println("Server responds " + httpResponse.getStatusLine().getStatusCode());
			return null;
		}

		String jsonText = null;

		try {
			jsonText = IOUtil.readStreamToString(httpResponse.getEntity().getContent());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Sentiment140Response responseJson = gson.fromJson(jsonText, Sentiment140Response.class);

		return responseJson.data;
	}
}
