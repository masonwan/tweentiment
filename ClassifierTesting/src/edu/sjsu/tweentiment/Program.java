package edu.sjsu.tweentiment;

import java.util.regex.*;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String text = "I swear, I can never stay happy.. I always find a way to get in my feelings.";

		Pattern pattern = Pattern.compile("\\w+");
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(matcher.group(0));
		}

		String[] x = text.replaceAll("\\w+", "").split(" ");

		System.out.println(x);

		// Classifier classifier = null;
		//
		// try {
		// classifier = new Classifier("test\\data.json");
		// } catch (IOException e) {
		// e.printStackTrace();
		// return;
		// }
		//
		// SentimentResult result = classifier.classify("Hello world");
	}
}
