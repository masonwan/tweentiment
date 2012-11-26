package edu.sjsu.tweentiment.evaluator;

import edu.sjsu.tweentiment.SentimentType;

/**
 * Command pattern
 * 
 * @see http://en.wikipedia.org/wiki/Command_pattern
 */
interface ICommand {
	String getName();

	SentimentType[] getSentimentValue(String[] texts);
}