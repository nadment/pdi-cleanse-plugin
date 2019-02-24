package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.NonPrintableRemovalRule;

public class NonPrintableRemovalRuleTest extends CleanseRuleTest<NonPrintableRemovalRule> {

	
	@Test
	public void test() throws Exception {
		check("\tTEST", "TEST");
		check("TEST\n", "TEST");
	}

}
