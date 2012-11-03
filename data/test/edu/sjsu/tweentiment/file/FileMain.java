package edu.sjsu.tweentiment.file;

import java.io.*;

import edu.sjsu.tweentiment.obj.*;

/**
 * For Demo purposes
 * 
 * @author Lakshmi Mallampati
 * 
 */
public class FileMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		SentimentFile sf = new SentimentFileImpl();

		// to save word and weight manually to file
		sf.saveToFile(new Word("happy", 0.80654895));
		sf.saveToFile(new Word("happy", 0.80654895)); // duplicates will not be added

		// for our system, directly call getWeight
		// if the word doesn't exist in the file (memory), the system gets
		// weight and
		// save to file and memory.
		// memory is basically a List of Word objects
		System.out.println("happy weight: " + sf.getWeight("happy"));
		System.out.println("sad weight: " + sf.getWeight("sad"));
	}
}