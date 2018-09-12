package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

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
