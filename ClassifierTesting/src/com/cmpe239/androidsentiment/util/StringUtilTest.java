package com.cmpe239.androidsentiment.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class StringUtilTest {

	@Test
	public void test() {
		ArrayList<String> list = StringUtil.splitWords("I swear, I can never stay happy.. I always find a way to get in my feelings.");
		Assert.assertEquals(7, list.size());
	}

}
