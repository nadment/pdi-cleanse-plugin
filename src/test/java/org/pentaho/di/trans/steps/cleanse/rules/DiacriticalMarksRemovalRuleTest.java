package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class DiacriticalMarksRemovalRuleTest extends CleanseRuleTest<DiacriticalMarksRemovalRule> {

	@Test
	public void test() throws Exception {

		check("éèâçïîüô", "eeaciiuo");

	}

}
