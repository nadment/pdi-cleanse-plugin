package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.RemoveNonPrintableRule;

public class NonPrintableRemovalRuleTest extends CleanseRuleTest<RemoveNonPrintableRule> {

	
	@Test
	public void test() throws Exception {
		check("\tTEST", "TEST");
		check("TEST\n", "TEST");
	}

}
