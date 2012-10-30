package edu.sjsu.tweentiment;

import org.junit.*;

public class ClassifierTest {
	Classifier classifier;

	@Before
	public void setUp() throws Exception {
		classifier = new Classifier("data.json", "stop words.txt");
	}

	@Test
	public void readWordMap() {
		Assert.assertEquals(2477, classifier.wordMap.size());
	}

	@Test
	public void readStopWords() {
		Assert.assertEquals(570, classifier.stopWordSet.size());
	}

	@Test
	public void neutral() {
		SentimentResult result = classifier.classify("Hello world");
		Assert.assertEquals(SentimentType.Neutral, result.type);
	}

	@Test
	public void positive() {
		SentimentResult result = classifier.classify("I love the whole world.");
		Assert.assertEquals(SentimentType.Positive, result.type);
	}

	@Test
	public void negative() {
		SentimentResult result = classifier.classify("I messed up my midterm. I will be in the hell in the rest of the semester.");
		Assert.assertEquals(SentimentType.Negative, result.type);
	}

	@Test
	public void doubleNegative() {
		SentimentResult result = classifier.classify("I will not be scared again.");
		Assert.assertEquals(SentimentType.Negative, result.type);
	}

	@Test
	public void relativeContext() {
		SentimentResult result = classifier.classify("The book is freaking good. I'd love to read it again.");
		Assert.assertEquals(SentimentType.Positive, result.type);
	}
}
