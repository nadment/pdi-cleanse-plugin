package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class HyphenDashRemovalRuleTest extends CleanseRuleTest<HyphenDashRemovalRule> {

	/** Long dash */
	public static final char LONG_DASH = '\u2014';

	/** Short dash */
	public static final char SHORT_DASH = '\u2010';

	/** Nonbreaking hyphen character */
	public static final char NONBREAKING_HYPHEN = '\u2011';

	@Test
	public void test() throws Exception {

		check("-TEST", "TEST");
		check("T---EST", "TEST");
		check("--TEST--", "TEST");
		check("TEST" + SHORT_DASH, "TEST");
		check("TEST" + LONG_DASH, "TEST");
		check("TEST" + NONBREAKING_HYPHEN, "TEST");
	}

}
