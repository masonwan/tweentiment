package edu.sjsu.tweentiment.twitter;

import com.google.gson.annotations.*;

public class Tweet {
	@SerializedName("created_at")
	public String createAt;

	@SerializedName("from_user")
	public String fromUser;

	@SerializedName("from_user_name")
	public String fromUserName;

	@SerializedName("profile_image_url")
	public String profileImageUrlString;

	@SerializedName("id_str")
	public String id;

	@SerializedName("text")
	public String text;

	@Expose
	private Boolean isRetweet = null;

	public boolean isRetweet() {
		if (isRetweet != null) {
			return isRetweet;
		}

		return isRetweet = text.startsWith("RT");
	}
}