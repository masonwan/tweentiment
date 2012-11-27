package edu.sjsu.tweentiment.classifier;

import java.io.IOException;

import org.junit.*;

import edu.sjsu.tweentiment.SentimentType;

public class ClassifierTest {
	DynamicClassifier classifier;

	@Before
	public void setUp() throws Exception {
		classifier = new DynamicClassifier("words.json", "stop_words.txt", "noise_words.txt");
	}

	@Test
	public void neutral() throws IOException {
		SentimentResult result = classifier.classify("The file is less than 8GB in size.");
		Assert.assertEquals(SentimentType.Neutral, result.type);
	}

	@Test
	public void positive() throws IOException {
		SentimentResult result = classifier.classify("I love the whole world.");
		Assert.assertEquals(SentimentType.Positive, result.type);
	}

	@Test
	public void negative1() throws IOException {
		SentimentResult result = classifier.classify("I messed up my midterm. I will be in the hell in the rest of the semester.");
		Assert.assertEquals(SentimentType.Negative, result.type);
	}

	@Test
	public void negative2() throws IOException {
		SentimentResult result = classifier.classify("Go hell, faggot!");
		Assert.assertEquals(SentimentType.Negative, result.type);
	}

	@Test
	public void negative3() throws IOException {
		SentimentResult result = classifier.classify("shit");
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
