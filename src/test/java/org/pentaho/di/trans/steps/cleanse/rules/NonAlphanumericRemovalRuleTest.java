package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class NonAlphanumericRemovalRuleTest extends CleanseRuleTest<NonAlphanumericRemovalRule> {

	@Test
	public void test() throws Exception {

		check(" <csdf  gsdg12345--6789..> ", "csdfgsdg123456789");
		check("0.1.2.3.4.5.6", "0123456");
	}

}
