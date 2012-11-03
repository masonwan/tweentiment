package edu.sjsu.tweentiment.file;

import java.io.*;
import java.util.*;

import com.google.gson.*;

import edu.sjsu.tweentiment.obj.*;
import edu.sjsu.tweentiment.weight.*;

/**
 * Implements getting word sentiment
 */
public class SentimentFileImpl implements SentimentFile {

	// This is the memory
	private static List<Word> words;

	public SentimentFileImpl() {
		// read file into memory here
		words = getAllWords();
	}

	private final String FILE_NAME = "words.json";

	@Override
	public void saveToFile(Word word) throws IOException {

		Word w = wordExistInFile(word.getName());

		if (w == null) {
			Gson gson = new Gson();
			String json = gson.toJson(word);
			// System.out.println(json);
			writeToFile(json);
			words.add(word);
		}
	}

	@Override
	public Double getWeight(String wordName) throws IOException {

		Word word = wordExistInFile(wordName);

		if (word != null) {
			return word.getWeight();
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
		// read from memory
		for (Word word : words) {
			if (word.getName().equals(wordName)) {
				return word;
			}
		}
		return null;
	}

	/**
	 * Write text,
	 * 
	 * @param text
	 * @throws IOException
	 */
	private void writeToFile(String text) throws IOException {
		Writer output = null;
		File file = new File(FILE_NAME);
		if (file.exists()) {
			text = ",\n" + text;
		}
		output = new BufferedWriter(new FileWriter(file, true)); // true adds on
		output.write(text);
		output.close();
		System.out.println("New word has been written to file");
	}

	/**
	 * Adding additional string to complete the Json string in .json file
	 * 
	 * @return complete Json string
	 */
	private String readFileToJsonString() {
		try {
			String json = "{'words':[";

			File file = new File(FILE_NAME);

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
	private List<Word> getAllWords() {
		String json = readFileToJsonString();
		if (json == null) {
			return new ArrayList<Word>();
		}
		Gson gson = new Gson();
		WordsFile data = gson.fromJson(json, WordsFile.class);
		return data.getWords();
	}

}
