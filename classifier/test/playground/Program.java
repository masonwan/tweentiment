package playground;

import com.softcorporation.suggester.BasicSuggester;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;

public class Program {

	public static void main(String[] args) {
		try {

			// File dir = new File("./");
			// Directory directory = FSDirectory.open(dir);
			// SpellChecker spellChecker = new SpellChecker(directory);
			//
			// StandardAnalyzer analyzer = new
			// StandardAnalyzer(Version.LUCENE_36);
			// IndexWriterConfig indexWriterConfig = new
			// IndexWriterConfig(Version.LUCENE_36, analyzer);
			// spellChecker.indexDictionary(new PlainTextDictionary(new
			// File("dictionary.txt")), indexWriterConfig, true);
			//
			// spellChecker.setAccuracy(0.2f);
			// String word = "gooood";
			// boolean doesExist = spellChecker.exist(word);
			// String[] suggestions = spellChecker.suggestSimilar(word, 5);
			// System.out.println(suggestions);
			//
			// spellChecker.close();

			BasicSuggesterConfiguration configuration = new BasicSuggesterConfiguration("test.config");
			BasicSuggester suggester = new BasicSuggester(configuration);
			com.softcorporation.suggester.d.d dictionary = new com.softcorporation.suggester.d.d("english.jar", null, null);

			// suggester.attach(dictionary);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
