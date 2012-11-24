package edu.sjsu.tweentiment.file;

import java.util.List;

import edu.sjsu.tweentiment.classifier.Word;

/**
 * Object to convert JSON file to memory.
 */
public class WordsFile {

	private List<Word> words;

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

}
