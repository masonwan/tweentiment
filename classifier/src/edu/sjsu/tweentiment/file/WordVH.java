package edu.sjsu.tweentiment.file;

/**
 * For converting the json returned by viralheat to a java object.
 */
public class WordVH {
	private Double prob;
	private String mood;
	private String text;

	public Double getProb() {
		return prob;
	}

	public void setProb(Double prob) {
		this.prob = prob;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
