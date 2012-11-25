package edu.sjsu.tweentiment.file;

import java.io.IOException;

import edu.sjsu.tweentiment.classifier.Word;

/**
 * Getting word sentiment
 */
public interface SentimentFile {

	public String getFileName();

	public void setFileName(String fileName);

	/**
	 * Write the word to the file. Format: word weight Example: happy 0.80654895
	 * 
	 * Main purpose is to upload initial data. After that this method is
	 * implicitly called by getWeight when needed.
	 * 
	 * @param word
	 *            is the Word object
	 */
	public void saveToFile(Word word) throws IOException;

	/**
	 * First Reads the File, which is loaded into memory, for the word. If the
	 * word is not found, get's the word weight, saves it to file and memory and
	 * returns the weight to the user.
	 * 
	 * @param wordName
	 * @return wordWeight
	 */
	public Double getWeight(String wordName) throws IOException;
}
