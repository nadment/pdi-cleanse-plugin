package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class NumberRemovalRuleTest extends CleanseRuleTest<NumberRemovalRule> {

	@Test
	public void test() throws Exception {

		check("<csdfgsdg12345--6789..>", "<csdfgsdg--..>");
		check("0.1.2.3.4.5.6", "......");
		check("௩", ""); // Tamil Digit Three
	}

}
