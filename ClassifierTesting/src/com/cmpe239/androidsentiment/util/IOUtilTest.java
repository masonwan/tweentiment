package com.cmpe239.androidsentiment.util;

import java.io.*;
import java.util.*;

import org.junit.*;

public class IOUtilTest {

	@Test
	public void readToString() throws IOException {
		String text = IOUtil.readToString("test/data.json");
		Assert.assertNotNull(text);
		Assert.assertFalse(text.isEmpty());
	}

	@Test
	public void readWordList() throws IOException {
		ArrayList<String> list = IOUtil.readWordList("test/stop words.txt");
		Assert.assertEquals(571, list.size());
	}

}
