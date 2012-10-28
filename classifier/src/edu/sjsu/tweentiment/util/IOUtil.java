package edu.sjsu.tweentiment.util;

import java.io.*;
import java.util.*;

public class IOUtil {

	/**
	 * @see http://stackoverflow.com/a/5445161/239151
	 * @param filename
	 * @return the concatenated string from the file
	 * @throws IOException
	 */
	public static String readToString(String filename) throws IOException {

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

	/**
	 * 
	 * @param filename
	 * @return a list of words in ArrayList
	 * @throws IOException
	 */
	public static ArrayList<String> readWordList(String filename) throws IOException {
		String text = readToString(filename);
		String[] lines = text.split("\n");
		ArrayList<String> wordList = new ArrayList<String>();

		for (String line : lines) {
			String trimedText = line.trim();

			if (trimedText.isEmpty() || trimedText.charAt(0) == '#') {
				continue;
			}

			wordList.add(trimedText);
		}

		return wordList;
	}
}
