package edu.sjsu.tweentiment.pojo;

import com.google.gson.annotations.*;

public class Tweet {
	@SerializedName("created_at")
	private String created_at;
	@SerializedName("from_user")
	private String from_user;
	@SerializedName("from_user_name")
	private String from_user_name;
	@SerializedName("profile_image_url")
	private String profile_image_url;
	@SerializedName("id_str")
	private String id_str;
	@SerializedName("text")
	private String text;

	public Tweet() {

	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getFrom_user() {
		return from_user;
	}

	public void setFrom_user(String from_user) {
		this.from_user = from_user;
	}

	public String getFrom_user_name() {
		return from_user_name;
	}

	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url_https(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
