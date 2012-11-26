package edu.sjsu.tweentiment.util;

import java.io.*;
import java.util.*;

/**
 * Collections of IO utility methods.
 */
public class IOUtil {
	/**
	 * @see "Read/convert an InputStream to a String"
	 *      http://stackoverflow.com/a/5445161/239151
	 * @param filename
	 * @return the concatenated string from the file
	 * @throws IOException
	 */
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

	/**
	 * Read an input stream to a string.
	 * 
	 * @param inputStream
	 *            The input stream
	 * @return The read string
	 * @throws IOException
	 */
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

	/**
	 * Read a text file into an array list. Each item corresponds to one line.
	 * 
	 * @param filename
	 *            The path of the file to read
	 * @return a list of words in ArrayList
	 * @throws IOException
	 */
	public static ArrayList<String> readWordList(String filename) throws IOException {
		String text = readFileToString(filename);
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

	public static void saveStringToStream(String text, OutputStream outputStream) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		writer.write(text);
		writer.flush();
	}

	public static void saveStringToFile(String text, String filename) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(filename);
		saveStringToStream(text, fileOutputStream);
		fileOutputStream.close();
	}
}
