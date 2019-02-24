package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.DiacriticalMarksRemovalRule;

public class DiacriticalMarksRemovalRuleTest extends CleanseRuleTest<DiacriticalMarksRemovalRule> {

	@Test
	public void test() throws Exception {

		check("éèâçïîüô", "eeaciiuo");

	}

}
