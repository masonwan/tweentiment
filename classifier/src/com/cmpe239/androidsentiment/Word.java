package com.cmpe239.androidsentiment;

public class Word {
	public String text;
	public double sentimentValue = 0d;

	public Word(String text, double sentimentValue) {
		this.text = text;
		this.sentimentValue = sentimentValue;
	}
}
