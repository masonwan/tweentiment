package edu.sjsu.tweentiment.util;

import java.io.*;
import java.util.*;

/**
 * Collections of IO utility methods.
 */
public class IOUtil {
	public static String readFileToString(String filename) throws IOException {
		String text;
		InputStream stream = null;
		Scanner scanner = null;

		try {
			stream = new FileInputStream(filename);
			scanner = new java.util.Scanner(stream);
			text = scanner.useDelimiter("\\A").next();
		} finally {
			if (scanner != null) {
				scanner.close();
			}

			if (stream != null) {
				stream.close();
			}
		}

		return text;
	}

	public static String readStreamToString(InputStream inputStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(inputStream);
		char[] buffer = new char[8192];
		int count = 0;

		while ((count = reader.read(buffer)) >= 0) {
			builder.append(buffer, 0, count);
		}

		return builder.toString();
	}

	public static ArrayList<String> readWordList(String filename) throws IOException {
		String text = readFileToString(filename);
		String[] lines = text.split("\n");
		ArrayList<String> wordList = new ArrayList<String>();

		for (String line : lines) {
			String trimedText = line.trim();

			if (trimedText.equals("") || trimedText.charAt(0) == '#') {
				continue;
			}

			wordList.add(trimedText);
		}

		return wordList;
	}

	public static ArrayList<String> readWordListFromStream(InputStream inputStream) throws IOException {
		String text = readStreamToString(inputStream);
		String[] lines = text.split("\n");
		ArrayList<String> wordList = new ArrayList<String>();

		for (String line : lines) {
			String trimedText = line.trim();

			if (trimedText.equals("") || trimedText.charAt(0) == '#') {
				continue;
			}

			wordList.add(trimedText);
		}

		return wordList;
	}

	public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		char[] buffer = new char[8192];
		int count = 0;

		while ((count = reader.read(buffer)) >= 0) {
			writer.write(buffer, 0, count);
		}
	}
}
