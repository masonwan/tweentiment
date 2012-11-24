package edu.sjsu.tweentiment.classifier;

import java.io.IOException;

import org.junit.*;

public class ClassifierTest {
	Classifier classifier;
	
	@Before
	public void setUp() throws Exception {
		classifier = new Classifier("words.json", "stop words.txt", "noise words.txt");
	}

	@Test
	public void readStopWords() {
		Assert.assertEquals(570, classifier.stopWordSet.size());
	}

	@Test
	public void neutral() throws IOException {
		SentimentResult result = classifier.classify("Hello world");
		Assert.assertEquals(SentimentType.Neutral, result.type);
	}

	@Test
	public void positive() throws IOException {
		SentimentResult result = classifier.classify("I love the whole world.");
		Assert.assertEquals(SentimentType.Positive, result.type);
	}

	@Test
	public void negative() throws IOException {
		SentimentResult result = classifier.classify("I messed up my midterm. I will be in the hell in the rest of the semester.");
		Assert.assertEquals(SentimentType.Negative, result.type);
	}

	@Test
	public void doubleNegative() throws IOException {
		SentimentResult result = classifier.classify("I will not be scared again.");
		Assert.assertEquals(SentimentType.Positive, result.type);
	}

	@Test
	public void relativeContext() throws IOException {
		SentimentResult result = classifier.classify("The book is freaking good. I'd love to read it again.");
		Assert.assertEquals(SentimentType.Positive, result.type);
	}
}
