package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.NormalizeSpaceRule;

public class NormalizeSpaceRuleTest extends CleanseRuleTest<NormalizeSpaceRule> {

	@Test
	public void test() throws Exception {

		check(" TEST", "TEST");
		check("  TEST", "TEST");
		check("    TEST", "TEST");
		check(" TE  ST", "TE ST");

		check(" TE  ST  ", "TE ST ");
		check(" TE     ST    ", "TE ST ");

		check(" TE \tST", "TE ST");
		check(" TE\t\tST", "TE ST");
	}
}
