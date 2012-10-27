package com.sentiment.obj;

/**
 * For converting the json returned by viralheat to a java object.
 * 
 * @author Lakshmi Mallampati
 *
 */
public class WordVH {

	//"{\"prob\":0.806548944920931,\"mood\":\"positive\",\"text\":\"happy\"}"
	
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
