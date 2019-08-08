package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.RemoveNonAlphanumericRule;

public class NonAlphanumericRemovalRuleTest extends CleanseRuleTest<RemoveNonAlphanumericRule> {

	@Test
	public void test() throws Exception {

		check(" <csdf  gsdg12345--6789..> ", "csdfgsdg123456789");
		check("0.1.2.3.4.5.6", "0123456");
	}

}
