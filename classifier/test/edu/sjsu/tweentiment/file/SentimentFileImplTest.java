package edu.sjsu.tweentiment.file;

import java.io.IOException;

import org.junit.*;

import edu.sjsu.tweentiment.classifier.Word;

/**
 * For Demo purposes
 * 
 * @author Lakshmi Mallampati
 * 
 */
public class SentimentFileImplTest {
	@Test
	public void basic() throws IOException {
		SentimentFile sf = new SentimentFileImpl("words.json");

		// to save word and weight manually to file
		sf.saveToFile(new Word("happy", 0.80654895));
		sf.saveToFile(new Word("happy", 0.80654895)); // duplicates will not be
														// added

		// for our system, directly call getWeight
		// if the word doesn't exist in the file (memory), the system gets
		// weight and
		// save to file and memory.
		// memory is basically a List of Word objects
		Assert.assertTrue(sf.getWeight("happy") > 0);
		Assert.assertTrue(sf.getWeight("sad") < 0);
	}
}