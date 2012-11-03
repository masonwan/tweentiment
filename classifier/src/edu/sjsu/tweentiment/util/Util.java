package edu.sjsu.tweentiment.util;

import java.io.*;
import java.net.*;

/**
 * Util Class for utility methods
 */
public class Util {

	/**
	 * Call URL and get Response as String
	 * 
	 * @param urlStr
	 *            URL to call
	 * @return response as String
	 * @throws IOException
	 */
	public static String httpGet(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setInstanceFollowRedirects(true);

		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		return sb.toString();
	}
}
