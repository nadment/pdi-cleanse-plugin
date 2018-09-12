package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class NonPrintableRemovalRuleTest extends CleanseRuleTest<NonPrintableRemovalRule> {

	
	@Test
	public void test() throws Exception {
		check("\tTEST", "TEST");
		check("TEST\n", "TEST");
	}

}
