package edu.sjsu.tweentiment.file;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;

import edu.sjsu.tweentiment.classifier.Word;

/**
 * Implements getting word sentiment
 */
public class SentimentFileImpl implements SentimentFile {

	// This is the memory
	private static TreeMap<String, Word> words = new TreeMap<String, Word>(String.CASE_INSENSITIVE_ORDER);
	private static Gson gson = new Gson();
	private String fileName;

	public SentimentFileImpl(String fileName) {
		// set fileName
		this.fileName = fileName;
		// read file into memory here
		getAllWords();
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void saveToFile(Word word) throws IOException {

		Word w = wordExistInFile(word.text);

		if (w == null) {
			Gson gson = new Gson();
			String json = gson.toJson(word);
			// System.out.println(json);
			writeToFile(json);
			w = new Word(word);
			words.put(w.text, w);
		}
	}

	@Override
	public Double getWeight(String wordName) throws IOException {

		Word word = wordExistInFile(wordName);

		if (word != null) {
			return word.sentimentValue;
		}

		// word does not exit
		// (1) get weight
		// (2) write Word to file
		WordWeight ww = new WordWeight();
		Double weight = ww.getWordWeight(wordName);
		Word newWord = new Word(wordName, weight);
		saveToFile(newWord);
		return weight;
	}

	/**
	 * Returns the Word if the word exits in the file or returns null
	 * 
	 * @param wordName
	 *            text of the word
	 * @return word from file or null
	 */
	private Word wordExistInFile(String wordName) {
		return words.get(wordName);
	}

	private void writeToFile(String text) throws IOException {
		Writer output = null;
		File file = new File(fileName);
		if (file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			if (reader.readLine() != null) {
				text = ",\n" + text;
			}

			reader.close();
		}
		output = new BufferedWriter(new FileWriter(file, true)); // true adds on
		output.write(text);
		output.close();
//		System.out.println("New word has been written to file");
	}

	/**
	 * Adding additional string to complete the JSON string in .json file
	 * 
	 * @return complete JSON string
	 */
	private String readFileToJsonString() {
		try {
			String json = "{'words':[";

			File file = new File(fileName);

			if (!file.exists()) { // first time
				return null;
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));

			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				json += currentLine;
			}
			reader.close();

			json += "]}";

			// System.out.println(json);

			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read file to List of Word
	 * 
	 * @return List<Word> all words in list to file
	 */
	private void getAllWords() {
		String json = readFileToJsonString();

		if (json == null) {
			return;
		}

		WordsFile data = gson.fromJson(json, WordsFile.class);
		List<Word> wordList = data.getWords();

		for (Word word : wordList) {
			if (word != null && word.text != null) {
				try {
					words.put(word.text, word);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
