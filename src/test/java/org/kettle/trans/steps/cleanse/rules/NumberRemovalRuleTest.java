package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.RemoveNumberRule;

public class NumberRemovalRuleTest extends CleanseRuleTest<RemoveNumberRule> {

	@Test
	public void test() throws Exception {

		check("<csdfgsdg12345--6789..>", "<csdfgsdg--..>");
		check("0.1.2.3.4.5.6", "......");
		check("௩", ""); // Tamil Digit Three
	}

}
