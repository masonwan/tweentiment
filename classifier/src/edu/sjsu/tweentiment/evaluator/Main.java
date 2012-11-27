package edu.sjsu.tweentiment.evaluator;

import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

import edu.sjsu.tweentiment.SentimentType;
import edu.sjsu.tweentiment.twitter.*;
import edu.sjsu.tweentiment.util.IOUtil;

public class Main {
	static String[] texts;
	static ArrayList<ArrayList<SentimentType>> resultList;
	static ArrayList<ICommand> commandList;

	public static void main(String[] args) {
		try {
			commandList = new ArrayList<>();
			// commandList.add(new NormalClassifierCommand());
			commandList.add(new StaticClassifierCommand());
			commandList.add(new Sentiement140Command());
			// commandList.add(new ViralheatCommand());

			// ITextProvider textProvider = new CustomizedTextProvider();
			ITextProvider textProvider = new TwitterTextProvider("@obama");

			final int maxTextCount = 100;

			texts = textProvider.getTexts(maxTextCount);
			resultList = new ArrayList<>(commandList.size());

			for (int i = 0; i < commandList.size(); i++) {
				ICommand command = commandList.get(i);
				SentimentType[] sentimentTypes = command.getSentimentValue(texts);
				resultList.add(new ArrayList<>(Arrays.asList(sentimentTypes)));

				int[] statistics = new int[] {
						0, 0, 0
				};

				for (int j = 0; j < sentimentTypes.length; j++) {
					SentimentType type = sentimentTypes[j];

					if (type == SentimentType.Positive) {
						statistics[0]++;
					} else if (type == SentimentType.Negative) {
						statistics[2]++;
					} else {
						statistics[1]++;
					}
				}

				System.out.format("%s: %s\n", command.getName(), statistics.toString());
			}

			ResultJson resultJson = generateResultJson();
			printResult(resultJson);
			saveResult(resultJson);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static ResultJson generateResultJson() {
		ResultJson result = new ResultJson();

		// Generate the JSON object.
		ClassifierResult[][] resultsOfTexts = new ClassifierResult[texts.length][];
		boolean[] isSentimentDiffOfTexts = new boolean[texts.length];

		for (int i = 0; i < texts.length; i++) {
			resultsOfTexts[i] = new ClassifierResult[commandList.size()];
			boolean isDiff = false;
			SentimentType currentType = null;

			for (int j = 0; j < commandList.size(); j++) {
				ICommand command = commandList.get(j);
				String name = command.getName();
				SentimentType type = resultList.get(j).get(i);

				if (isDiff == false) {
					if (currentType == null) {
						currentType = type;
					} else {
						if (currentType != type) {
							isDiff = true;
							isSentimentDiffOfTexts[i] = true;
						}
					}
				}

				ClassifierResult classifierResult = new ClassifierResult();
				classifierResult.name = name;
				classifierResult.sentimentType = type;

				resultsOfTexts[i][j] = classifierResult;
			}
		}

		TextUnit[] allResults = new TextUnit[texts.length];
		ArrayList<TextUnit> differentResults = new ArrayList<>();

		for (int i = 0; i < allResults.length; i++) {
			TextUnit textUnit = new TextUnit();
			textUnit.id = i;
			textUnit.text = texts[i];
			textUnit.results = resultsOfTexts[i];

			allResults[i] = textUnit;

			if (isSentimentDiffOfTexts[i]) {
				differentResults.add(textUnit);
			}
		}

		result.allResults = allResults;
		result.differentResults = (TextUnit[]) differentResults.toArray(new TextUnit[differentResults.size()]);

		return result;
	}

	public static void printResult(ResultJson resultJson) {
		System.out.println("===================================");
		System.out.println("Different result");
		System.out.println("===================================");

		printTextUnits(resultJson.differentResults);

		System.out.println("===================================");
		System.out.println("All result");
		System.out.println("===================================");

		printTextUnits(resultJson.allResults);
	}

	public static void printTextUnits(TextUnit[] textUnits) {
		for (int i = 0; i < textUnits.length; i++) {
			TextUnit textUnit = textUnits[i];
			System.out.format("Text %d:\n%s\n", textUnit.id, textUnit.text);
			System.out.println();

			ClassifierResult[] results = textUnit.results;

			for (ClassifierResult classifierResult : results) {
				System.out.format("%15s: %8s\n", classifierResult.name, classifierResult.sentimentType);
			}

			System.out.println();
		}
	}

	public static void saveResult(ResultJson resultJson) {
		Random rand = new Random();
		Gson gson = new Gson();

		// Translate to string and save it.
		String text = gson.toJson(resultJson);
		String filename = String.format("Results %d.json", rand.nextInt());

		try {
			IOUtil.saveStringToFile(text, filename);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}

class ResultJson {
	public TextUnit[] allResults;
	public TextUnit[] differentResults;
}

class TextUnit {
	public int id;
	public String text;
	public ClassifierResult[] results;
}

class ClassifierResult {
	String name;
	SentimentType sentimentType;
}

interface ITextProvider {
	String[] getTexts(int count);
}

class CustomizedTextProvider implements ITextProvider {
	LinkedList<String> textQueue = new LinkedList<>();

	public CustomizedTextProvider() {
		textQueue.add("@LionelMedia b4 hand I'm not an #obama supporter, but would it had made a difference if McCain had won in '08?");
		textQueue.add("@TheCeliacDiva Coupon Book for #CyberMonday is only $19....crazy Gluten Free Savings inside! http://t.co/QvPciQmR");
		textQueue.add("Cyber Monday did not go well for me! #CyberMonday");
		textQueue.add("There's no such thing as owning to many pairs of shoes, is there? #heels #cybermonday #sales");
		textQueue.add("RT @JABBAWOCKEEZ: Hours left of #cybermonday sales! Don't miss out! Up to 50% off new merch incl #SchoolOfDance DVD http://t.co/jtwYJcwU http://t.co/nBU1CwY8");
		textQueue.add("#cybermonday this made me laugh haha!! aye lets do is...Lauren Bett has a wee ring tae it http://t.co/olAZltr6");
		textQueue.add("Do you like my iPad?");
		textQueue.add("If you have an Android Phone, Android Tablet, iPad, iPhone, iPod touch or Kindle Fire you will want to sign up... http://t.co/u8uwCSQ4");
		textQueue.add("#ipad this made me laugh so fucking much.");
		textQueue.add("RT @nbsalert: Win an iPad 2! Click here to join: http://t.co/Ga9nIHFH");
		textQueue.add("RT @ElbowRoomApps: \"My 3 year old will not put this down. One of the best learning apps on the market!\" #ios #iphone #ipad #freeapp #IDRTG http://t.co/stuXVyHL");
		textQueue.add("Check out my ride in #CSRRacing for iPad! Try it for FREE!");
		textQueue.add("Dat fukkin mini iPad commercial makes me want to learn how to play the piano");
		textQueue.add("my mom took the ipad w. her :(");
		textQueue.add("#ipad good one Pete, last time I heard that I laughed so hard I fell off my dinosaur....???? http://t.co/Ngl0NYNz");
		textQueue.add("@gayle_lynne @sandytucker1 @MarisaVitaljich  just remembered I had the external battery 4 my iPad yeah no recharging. iPad");
		textQueue.add("RT @bjoern: In more detail: The iPad Owned Black Friday, So Let’s Start Seeing Better Shopping Experiences Designed For It http://t.co/EMkn3X5j");
		textQueue.add("Dropcam adds splitscreen support to iPad app http://t.co/IHOaMW8z");
		textQueue.add("I just guessed ipad mini as the next deal in @RadioShack’s #24dealsin24 sale! You can play too at http://t.co/NCPThaZz");
		textQueue.add("RT @WAYN: 'Go with Oh' &amp; you could win fantastic prizes including the iPhone 5, an iPad &amp; €750 towards any ‘Go with Oh’ apartment:http://t.co/5kHDMA8w");
		textQueue.add("@charmosdell just heard the news your one of us now? WELCOME TO THE GOOOOOD LIFE");
	}

	@Override
	public String[] getTexts(int count) {

		count = Math.min(count, textQueue.size());
		String[] texts = new String[count];

		for (int i = 0; i < count; i++) {
			texts[i] = textQueue.poll();
		}

		return texts;
	}

}

class TwitterTextProvider implements ITextProvider {

	TwitterSearchWrapper searchWrapper;
	LinkedList<Tweet> tweetQueue = new LinkedList<>();
	int page = 1;

	public TwitterTextProvider(String query) {
		TwitterSearchUrlBuilder builder = new TwitterSearchUrlBuilder(query, 20, SearchType.General);
		searchWrapper = new TwitterSearchWrapper(builder);
	}

	@Override
	public String[] getTexts(int count) {
		while (tweetQueue.size() < count) {
			List<Tweet> tweetList = searchWrapper.getTweets();
			tweetQueue.addAll(tweetList);
		}

		String[] texts = new String[count];

		for (int i = 0; i < count; i++) {
			Tweet tweet = tweetQueue.poll();
			texts[i] = tweet.text;
		}

		return texts;
	}
}