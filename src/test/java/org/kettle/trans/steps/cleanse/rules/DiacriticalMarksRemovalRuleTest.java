package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.RemoveDiacriticalMarksRule;

public class DiacriticalMarksRemovalRuleTest extends CleanseRuleTest<RemoveDiacriticalMarksRule> {

	@Test
	public void test() throws Exception {

		check("éèâçïîüô", "eeaciiuo");

	}

}
