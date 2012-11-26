package edu.sjsu.tweentiment;

import java.io.*;
import java.net.URL;
import java.text.*;
import java.text.ParseException;
import java.util.*;

import android.app.*;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import edu.sjsu.tweentiment.classifier.StaticClassifier;
import edu.sjsu.tweentiment.twitter.*;

public class MainActivity extends Activity {
	private String searchQuery = "";
	private ListView listView;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private ArrayAdapter<Tweet> listAdapter;
	private String title;
	private LayoutInflater layoutInflater;

	private static final String TAG = "TwitterApp";
	private static final DateFormat INCOMING_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss zzz", Locale.getDefault());
	private static final DateFormat OUTGOING_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy KK:mm:ss a", Locale.getDefault());

	StaticClassifier classifier;
	SearchType searchType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle bundle = getIntent().getExtras();
		this.searchQuery = bundle.getString("TWEET_KEYWORD").trim();
		this.searchType = (SearchType) bundle.get("SEARCH_TYPE");

		Log.d(TAG, "Search Type = " + this.searchType);

		TextView welcomeTextView = (TextView) findViewById(R.id.welcomeLabel);
		welcomeTextView.setText("Sentiment keyword: " + this.searchQuery);

		if (this.searchQuery.equals("")) {
			Log.d(TAG, "KW (buttonClicked) = " + this.searchQuery.equals(""));
			Toast.makeText(MainActivity.this, "Please go back and enter a search string!", Toast.LENGTH_SHORT).show();
		} else {
			this.listView = (ListView) findViewById(R.id.tweetListView);
			this.listAdapter = new TweetAdapter(this, R.layout.tweet_unit, this.tweets);
			this.listView.setAdapter(listAdapter);
			this.layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		boolean isOkay = initializeClassifier();
		Log.i("onCreate", String.format("Classifier initialization: ", isOkay));
	}

	boolean initializeClassifier() {
		try {
			// File internalStorageDirecotry = getFilesDir();
			// File sentimentWordsFile = new File(internalStorageDirecotry,
			// "words.json");
			//
			// if (sentimentWordsFile.exists() == false) {
			// InputStream sentimentStream =
			// getResources().openRawResource(R.raw.words);
			// boolean isOkay = sentimentWordsFile.createNewFile();
			//
			// if (isOkay == false) {
			// Toast.makeText(this, "The file system doesn't allow writing",
			// Toast.LENGTH_SHORT);
			// return false;
			// }
			//
			// OutputStream outputStream = new
			// FileOutputStream(sentimentWordsFile);
			// IOUtil.copyStream(sentimentStream, outputStream);
			// outputStream.close();
			// }
			//
			// Resources resources = getResources();
			// InputStream stopWordsStream =
			// resources.openRawResource(R.raw.stop_words);
			// InputStream noiseWordsStream =
			// resources.openRawResource(R.raw.noise_words);

			// classifier = new Classifier(sentimentWordsFile.getAbsolutePath(),
			// stopWordsStream, noiseWordsStream);

			// stopWordsStream.close();
			// noiseWordsStream.close();

			Resources resources = getResources();
			InputStream sentimentWordsStream = resources.openRawResource(R.raw.afinn_111);
			InputStream negationsStream = resources.openRawResource(R.raw.negations);
			classifier = new StaticClassifier(sentimentWordsStream, negationsStream);
			sentimentWordsStream.close();
			negationsStream.close();

		} catch (IOException e) {
			Log.e("critical", e.getMessage());
			Toast.makeText(this, "Classifier failed to load", Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.loadTweet();
	}

	private void loadTweet() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			new LoadTask(this).execute(this.searchQuery);
		} else {
			Toast.makeText(this, "No Internet Connectivity !", Toast.LENGTH_LONG).show();
		}
	}

	public void buttonClicked(View view) {
		if (this.searchQuery.equals("")) {
			Log.d(TAG, "KW (buttonClicked) = " + this.searchQuery.equals(""));
			Toast.makeText(MainActivity.this, "Please go back and enter a search string!", Toast.LENGTH_SHORT).show();
		} else {
			this.loadTweet();
		}
	}

	static class TweetHolder {
		ImageView sentimentImageView;
		TextView tweet_bodyTextView;
		TextView created_atTextView;
	}

	private class TweetAdapter extends ArrayAdapter<Tweet> {
		private ArrayList<Tweet> tweet;

		public TweetAdapter(Context context, int viewResourceId, ArrayList<Tweet> objects) {
			super(context, viewResourceId, objects);
			this.tweet = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Tweet singleTweet = this.tweet.get(position);
			TweetHolder tweetHolder;

			if (convertView == null) {
				convertView = MainActivity.this.layoutInflater.inflate(R.layout.tweet_unit, parent, false);
				tweetHolder = new TweetHolder();
				tweetHolder.sentimentImageView = (ImageView) convertView.findViewById(R.id.sentimentImageView);
				tweetHolder.tweet_bodyTextView = (TextView) convertView.findViewById(R.id.tweet_bodyTextView);
				tweetHolder.created_atTextView = (TextView) convertView.findViewById(R.id.created_atTextView);
				convertView.setTag(tweetHolder);
			} else {
				tweetHolder = (TweetHolder) convertView.getTag();
			}

			// Get the text content of each Tweet.
			int resourceId = R.drawable.neutral;

			if (singleTweet.sentimentResult.type == SentimentType.Positive) {
				resourceId = R.drawable.happy;
			} else if (singleTweet.sentimentResult.type == SentimentType.Negative) {
				resourceId = R.drawable.sad;
			}

			tweetHolder.sentimentImageView.setImageResource(resourceId);
			tweetHolder.tweet_bodyTextView.setText(singleTweet.text);

			try {
				tweetHolder.created_atTextView.setText(OUTGOING_DATE_FORMAT.format(INCOMING_DATE_FORMAT.parse(singleTweet.createAt)));
			} catch (ParseException e) {
				tweetHolder.created_atTextView.setText("");
			}

			return convertView;
		}
	}

	private class LoadTask extends AsyncTask<String, Integer, ArrayList<Tweet>> {
		private ArrayList<Tweet> newTweets;
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
		protected ArrayList<Tweet> doInBackground(String... searchQuery) {
			try {
				TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder(MainActivity.this.searchQuery, 5, MainActivity.this.searchType);

				TwitterSearchWrapper searchWrapper = new TwitterSearchWrapper(builder);
				ArrayList<Tweet> tweetList = searchWrapper.getTweets();
				this.newTweets = tweetList;

				for (Tweet tweet : tweetList) {
					tweet.sentimentResult = classifier.classify(tweet.text);
				}

				if (this.newTweets.size() != 0) {
					Tweet firstTweet = this.newTweets.get(0);

					if (this.username == null) {
						this.username = firstTweet.profileImageUrlString;
					}

					if (this.userImage == null) {
						this.userImage = BitmapFactory.decodeStream(new URL(firstTweet.profileImageUrlString).openConnection().getInputStream());
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
					MainActivity.this.title = this.title;
					MainActivity.this.setTitle(this.title);
				}

				MainActivity.this.tweets.addAll(result);
				Log.d(TAG, "Showing : " + MainActivity.this.tweets.size());
				MainActivity.this.listAdapter.notifyDataSetChanged();
			} else {
				if (MainActivity.this.searchQuery.equals("")) {
					Log.d(TAG, "KW (onPostExecute) = " + MainActivity.this.searchQuery.equals(""));

					Toast.makeText(MainActivity.this, "Please go back and enter a search string!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "No more Tweets!", Toast.LENGTH_SHORT).show();
				}
			}

			if (this.progressDialog != null && this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}
		}
	}
}
