package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.TrimRule;

public class TrimRuleTest extends CleanseRuleTest<TrimRule> {

	/** Long dash */
	public static final char LONG_DASH = '\u2014';

	/** Short dash */
	public static final char SHORT_DASH = '\u2010';

	/** Nonbreaking hyphen character */
	public static final char NONBREAKING_HYPHEN = '\u2011';

	@Test
	public void test() throws Exception {

		check(" TEST", "TEST");
		check("TEST  ", "TEST");
		check("\tTEST  ", "TEST");
		// check("TEST"+toUnicode(Character.SPACE_SEPARATOR),"TEST");
		// check("TEST"+String.valueOf(Character.LINE_SEPARATOR),"TEST");
		// check("TEST"+String.valueOf(Character.PARAGRAPH_SEPARATOR),"TEST");

	}

}
