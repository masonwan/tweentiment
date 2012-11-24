package edu.sjsu.tweentiment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import edu.sjsu.tweentiment.pojo.Response;
import edu.sjsu.tweentiment.pojo.Tweet;


public class MainActivity extends Activity {
	private String jsonUrl;
	private String searchKeyword = "";
	private int count = 0;
	private ListView listView;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private ArrayAdapter<Tweet> listAdapter;
	private String title; 
//	private String username;
//	private Bitmap userImage;
	private LayoutInflater layoutInflater;

	private static final String TAG = "TwitterApp";
	private static final DateFormat INCOMING_DATE_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy kk:mm:ss zzz", Locale.getDefault());
	private static final DateFormat OUTGOING_DATE_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy KK:mm:ss a", Locale.getDefault());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bundle bundle = getIntent().getExtras();
		this.searchKeyword = bundle.getString("TWEET_KEYWORD").trim();
		String searchType = bundle.getString("SEARCH_TYPE").trim();
		Log.d(TAG, "Search Type = " + searchType);

		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeLabel);
		welcomeTextView.setText("Sentiment keyword: " + this.searchKeyword);

		if(this.searchKeyword.equals("")){
			Log.d(TAG, "KW (buttonClicked) = " + this.searchKeyword.equals(""));
			Toast.makeText(MainActivity.this, "Please go back and enter a search string!",
					Toast.LENGTH_SHORT).show();			
		}else{
			this.jsonUrl = this.urlBuilder(this.searchKeyword, searchType);
			this.listView = (ListView) findViewById(R.id.tweetListView);
			this.listAdapter = new TweetAdapter(this, R.layout.tweet_unit, this.tweets);
			this.listView.setAdapter(listAdapter);
			this.layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}
	
	/**
	 * Builds a URL using a search keyword
	 * @param String keyword - Search term
	 * @param String searchType - general | user
	 * @return String url
	 */
	public final String urlBuilder(String keyword, String searchType){
		String replacementString = "";
		String url = "";
		
		if(keyword.equals("")){
			return url;
		}

		if(searchType.equals("general")){
			replacementString = "%20";
			keyword = this.replace(keyword, " ", replacementString);
			Log.d(TAG, "Keyword = " + keyword);
		}
		
		if(searchType.equals("user")){
			keyword = "from%3A" + keyword;
			Log.d(TAG, "Keyword = " + keyword);
		}

		url = "http://search.twitter.com/search.json?q="
				+ keyword +"&result_type=recent&rpp=2&page=";
		Log.d(TAG, "URL = " + url);
		
		return url;
	}

	/**
	 * Replaces white spaces with the desired string
	 * @param String text - hay stack
	 * @param String searchString - niddle
	 * @param String replacementString
	 * @return String
	 */
	public final String replace(String text, String searchString, String replacementString)
	{
	    StringBuffer sBuffer = new StringBuffer();
	    int pos = 0;

	    while ((pos = text.indexOf(searchString)) != -1)
	    {
	        sBuffer.append(text.substring(0, pos) + replacementString);
	        text = text.substring(pos + searchString.length());
	    }

	    sBuffer.append(text);
	    return sBuffer.toString();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.loadTweet();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void loadTweet() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new LoadTask(this).execute(this.jsonUrl + ++count);
		} else {
			Toast.makeText(this, "No Internet Connectivity !",
					Toast.LENGTH_LONG).show();
		}
	}

	public void buttonClicked(View view) {
		if(this.searchKeyword.equals("")){
			Log.d(TAG, "KW (buttonClicked) = " + this.searchKeyword.equals(""));
			Toast.makeText(MainActivity.this, "Please go back and enter a search string!",
					Toast.LENGTH_SHORT).show();			
		}else{
			this.loadTweet();
		}
	}

	static class TweetHolder {
//		ImageView userImageView;
		ImageView sentimentImageView;
//		TextView usernameTextView;
		TextView tweet_bodyTextView;
		TextView created_atTextView;
	}

	private class TweetAdapter extends ArrayAdapter<Tweet> {
		private ArrayList<Tweet> tweet;

		public TweetAdapter(Context context, int viewResourceId,
				ArrayList<Tweet> objects) {
			super(context, viewResourceId, objects);
			this.tweet = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Tweet singleTweet = this.tweet.get(position);
			TweetHolder tweetHolder;
			if (convertView == null) {
				convertView = MainActivity.this.layoutInflater.inflate(
						R.layout.tweet_unit, parent, false);
				tweetHolder = new TweetHolder();
//				tweetHolder.userImageView = (ImageView) convertView
//						.findViewById(R.id.userImageView);
				tweetHolder.sentimentImageView = (ImageView) convertView
						.findViewById(R.id.sentimentImageView);
//				tweetHolder.usernameTextView = (TextView) convertView
//						.findViewById(R.id.usernameTextView);
				tweetHolder.tweet_bodyTextView = (TextView) convertView
						.findViewById(R.id.tweet_bodyTextView);
				tweetHolder.created_atTextView = (TextView) convertView
						.findViewById(R.id.created_atTextView);
				convertView.setTag(tweetHolder);
			} else {
				tweetHolder = (TweetHolder) convertView.getTag();
			}

			// get the text content of each tweet			
			String tweetText = singleTweet.getText();
			/**
 			// integrate our classifier here
 			sentiment = Classifier(tweetText);
 			sentiment->classify();
 			
 			if (sentiment > 0){
 				tweetHolder.sentimentImageView.setImageResource(R.drawable.happy);
 			}else if(sentiment < 0){
 				tweetHolder.sentimentImageView.setImageResource(R.drawable.sad);
 			}else{
 				tweetHolder.sentimentImageView.setImageResource(R.drawable.neutral);
 			}
 			
			 */
			//TODO: add sentiment generated by classifier for this tweet here
			tweetHolder.sentimentImageView.setImageResource(R.drawable.happy);

//			tweetHolder.usernameTextView.setText(MainActivity.this.username);
			tweetHolder.tweet_bodyTextView.setText(tweetText);
			try {
				tweetHolder.created_atTextView.setText(OUTGOING_DATE_FORMAT
						.format(INCOMING_DATE_FORMAT.parse(singleTweet
								.getCreated_at())));
			} catch (ParseException e) {
				tweetHolder.created_atTextView.setText("");
			}
//			tweetHolder.userImageView.setImageBitmap(MainActivity.this.userImage);

			return convertView;
		}
	}

	private class LoadTask extends AsyncTask<String, Integer, ArrayList<Tweet>> {
		private final HttpClient httpClient = new DefaultHttpClient();
		private HttpResponse httpResponse;
		private BufferedReader bufferedReader;
		private Gson gson;
		private ArrayList<Tweet> newTweets;
		private Response response;
		private String username;
		private Bitmap userImage;
		private String title;
		private ProgressDialog progressDialog;
		private Context context;

		public LoadTask(Context context) {
			this.context = context;
			this.progressDialog = new ProgressDialog(this.context);
			this.progressDialog.setCancelable(true);
			this.progressDialog.setMessage("Loading ...");
			this.progressDialog.setIndeterminate(true);
		}

		@Override
		protected void onPreExecute() {
			this.progressDialog.show();
		}

		@Override
		protected ArrayList<Tweet> doInBackground(String... url) {
			try {
				this.httpResponse = this.httpClient
						.execute(new HttpGet(url[0]));
				this.bufferedReader = new BufferedReader(new InputStreamReader(
						this.httpResponse.getEntity().getContent()));
				this.gson = new Gson();
				this.response = gson.fromJson(bufferedReader, Response.class);
				this.newTweets = this.response.getTweets();
				if (this.newTweets.size() != 0) {
					if (this.username == null) {
						this.username = this.newTweets.get(0)
								.getFrom_user_name();
					}
					if (this.userImage == null) {
						this.userImage = BitmapFactory.decodeStream(new URL(
								this.newTweets.get(0).getProfile_image_url())
								.openConnection().getInputStream());
					}
					if (this.title == null) {
						this.title = "Tweentiment";
					}
					return this.newTweets;
				}
			} catch (Exception e) {
				Log.e(TAG, "exception thrown ", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Tweet> result) {
			super.onPostExecute(result);
			if (result != null) {
				if (MainActivity.this.title == null) {
//					MainActivity.this.username = this.username;
//					MainActivity.this.userImage = this.userImage;
					MainActivity.this.title = this.title;
					MainActivity.this.setTitle(this.title);
				}
				MainActivity.this.tweets.addAll(result);
				Log.d(TAG, "Showing : " + MainActivity.this.tweets.size());
				MainActivity.this.listAdapter.notifyDataSetChanged();
			} else {
				if(MainActivity.this.searchKeyword.equals("")){
					Log.d(TAG, "KW (onPostExecute) = " + MainActivity.this.searchKeyword.equals(""));

					Toast.makeText(MainActivity.this, "Please go back and enter a search string!",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, "No more Tweets!",
							Toast.LENGTH_SHORT).show();
				}
			}
			if (this.progressDialog != null && this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}
		}
	}
}
