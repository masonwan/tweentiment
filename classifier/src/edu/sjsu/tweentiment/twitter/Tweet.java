package edu.sjsu.tweentiment.twitter;

import com.google.gson.annotations.*;

public class Tweet {
	@SerializedName("created_at")
	public String created_at;

	@SerializedName("from_user")
	public String from_user;

	@SerializedName("from_user_name")
	public String from_user_name;

	@SerializedName("profile_image_url")
	public String profile_image_url;

	@SerializedName("id_str")
	public String id;

	@SerializedName("text")
	public String text;

	private Boolean isRetweet = null;

	public boolean isRetweet() {
		if (isRetweet != null) {
			return isRetweet;
		}

		return isRetweet = text.startsWith("RT");
	}
}