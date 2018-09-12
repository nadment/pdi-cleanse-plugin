package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class NonNumberRemovalRuleTest extends CleanseRuleTest<NonNumberRemovalRule> {

	@Test
	public void test() throws Exception {

		check("<csdfgsdg12345--6789..>", "123456789");
		check("0.1.2.3.4.5.6", "0123456");
		check("௩", "௩"); // Tamil Digit Three
	}

}
