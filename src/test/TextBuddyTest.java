/*
 * National University of Singapore
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Kenneth Ham Gao Jie (A0111875E)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package test;

import com.CS2103.TextBuddy_v2.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TextBuddyTest {
	private static final String APP_NAME = "TextBuddy";
	private static final String FILE_NAME = "in.txt";
	private static final String[] ARGS = {APP_NAME, FILE_NAME};

	@BeforeClass
	public static void initialize() throws IOException {
		TextBuddyPlusPlus textbuddypp = new TextBuddyPlusPlus();
		textbuddypp.TextBuddy(ARGS);
	}

	// Test Search Case: Search Empty
	@Test
	public void testSearchEmpty() throws IOException {
		assertEquals("Search Invalid!", TextBuddyPlusPlus.search(""));
	}

	// Test Search Case 1: Search invalid keyword
	@Test
	public void testSearchInvalid() throws IOException {
		assertEquals("\'aaa\': keyword could not be found.\n", TextBuddyPlusPlus.search("aaa"));
	}

	// Test Search Case 2: Search valid keywords
	@Test
	public void testSearchValid() throws IOException {
		assertEquals("1. Meeting with Microsoft CEO at Headquarters, 2PM\n" +
								 "2. Meeting with Facebook CEO at Alto Palo, 6PM\n", TextBuddyPlusPlus.search("CEO"));
	}

	// Test Search Case 3: Search random keywords
	@Test
	public void testSearchRandom() throws IOException {
		assertEquals("1. Telepresence with Paypal Developer Team to discuss on policy management system, Thursday 5AM\n", TextBuddyPlusPlus.search("me"));
	}

	// Test Search Case 4: Search case-sensitive keywords
	@Test
	public void testSearchCaseSensitive() throws IOException {
		assertEquals("\'ceo\': keyword could not be found.\n", TextBuddyPlusPlus.search("ceo"));
	}

	// Test Search Case 5: Search case-sensitive keywords
	@Test
	public void testSearchCaseSensitive2() throws IOException {
		assertEquals("\'meeting\': keyword could not be found.\n", TextBuddyPlusPlus.search("meeting"));
	}

	// Test Sort Case: Sort Empty
	@Test
	public void testSortEmpty() throws IOException {
		assertEquals("\'in.txt\' is empty!\n", TextBuddyPlusPlus.sort());
	}
}