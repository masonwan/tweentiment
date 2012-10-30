package edu.sjsu.tweentiment.util;

import java.io.*;
import java.util.*;

import org.junit.*;

public class IOUtilTest {
	@Test
	public void readToString() throws IOException {
		String text = IOUtil.readToString("data.json");
		Assert.assertNotNull(text);
		Assert.assertFalse(text.isEmpty());
	}

	@Test
	public void readWordList() throws IOException {
		ArrayList<String> list = IOUtil.readWordList("stop words.txt");
		Assert.assertEquals(570, list.size());
	}
}
