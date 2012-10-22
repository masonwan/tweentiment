package com.cmpe239.androidsentiment;

import org.junit.*;

public class ClassifierTest {

	Classifier classifier;

	@Before
	public void setUp() throws Exception {
		classifier = new Classifier("test\\data.json");
	}

	@Test
	public void neutral() {
		SentimentResult result = classifier.classify("Hello world");
		Assert.assertEquals(ResultType.Neutral, result.type);
	}

	@Test
	public void positive() {
		SentimentResult result = classifier.classify("I love the whole world.");
		Assert.assertEquals(ResultType.Positive, result.type);
	}

	@Test
	public void negative() {
		SentimentResult result = classifier.classify("I messed up my midterm. I will be in the hell in the rest of the semester.");
		Assert.assertEquals(ResultType.Negative, result.type);
	}

	@Test
	public void doubleNegative() {
		SentimentResult result = classifier.classify("I will not be scared again.");
		Assert.assertEquals(ResultType.Negative, result.type);
	}

	@Test
	public void relativeContext() {
		SentimentResult result = classifier.classify("The book is freaking good. I'd love to read it again.");
		Assert.assertEquals(ResultType.Negative, result.type);
	}
}
