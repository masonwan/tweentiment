package edu.sjsu.tweentiment.obj;

/**
 * Word object
 * 
 * name = word text like "happy" weight = weight of the word
 */
public class Word {

	private String name;
	private Double weight;

	public Word() {
	}

	public Word(String name, Double weight) {
		this.name = name;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}
