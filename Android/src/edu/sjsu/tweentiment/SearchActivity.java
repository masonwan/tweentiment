package edu.sjsu.tweentiment;

import java.util.regex.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class SearchActivity extends Activity {
	private Button searchBtn;
	private Button userBtn;
	private static final String TAG = "TwitterApp";
	
	String query = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		EditText editText = (EditText) findViewById(R.id.txtSearch);
		
		if (query == null) {
			editText.setText("#christmas");
		} else {
			editText.setText(query);
		}

		this.searchBtn = (Button) findViewById(R.id.btnGeneralSearch);
		this.searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText searchText = (EditText) findViewById(R.id.txtSearch);
				String keyword = searchText.getText().toString().trim();
				query = keyword;

				Intent intent = new Intent(SearchActivity.this, MainActivity.class);
				intent.putExtra("TWEET_KEYWORD", keyword);
				intent.putExtra("SEARCH_TYPE", "general");
				if (keyword.equals("")) {
					Log.d(TAG, "KW = " + keyword.equals(""));
					Toast.makeText(SearchActivity.this, "Please enter a tweet keyword!", Toast.LENGTH_SHORT).show();
				} else {
					startActivity(intent);
				}
			}
		});

		this.userBtn = (Button) findViewById(R.id.btnUserSearch);
		this.userBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText searchText = (EditText) findViewById(R.id.txtSearch);
				String keyword = searchText.getText().toString().trim();

				Intent intent = new Intent(SearchActivity.this, MainActivity.class);
				intent.putExtra("TWEET_KEYWORD", keyword);
				intent.putExtra("SEARCH_TYPE", "user");

				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(keyword);
				boolean wsFound = matcher.find();

				if (keyword.equals("") || wsFound) {
					Log.d(TAG, "KW = " + keyword.equals(""));
					Toast.makeText(SearchActivity.this, "Please enter a valid username!", Toast.LENGTH_SHORT).show();
				} else {
					startActivity(intent);
				}
			}
		});
	}
}
