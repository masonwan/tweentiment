package edu.sjsu.tweentiment.util;

import java.io.*;
import java.util.*;

import org.junit.*;

import edu.sjsu.tweentiment.*;

public class IOUtilTest {
	@Test
	public void readToString() throws IOException {
		String text = IOUtil.readFileToString("data.json");
		Assert.assertNotNull(text);
		Assert.assertFalse(text.isEmpty());
	}

	@Test
	public void readWordList() throws IOException {
		ArrayList<String> list = IOUtil.readWordList("stop words.txt");
		Assert.assertEquals(570, list.size());
	}

	@Test
	public void readStreamToString() {
		FileInputStream fileInputStream = null;

		try {
			fileInputStream = new FileInputStream("data.json");
			String result = IOUtil.readStreamToString(fileInputStream);
			fileInputStream.close();
			Assert.assertEquals(50390, result.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
