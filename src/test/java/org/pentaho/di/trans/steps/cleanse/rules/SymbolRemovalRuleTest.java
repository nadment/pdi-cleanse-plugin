package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class SymbolRemovalRuleTest extends CleanseRuleTest<SymbolRemovalRule> {

	@Test
	public void testCurrencySymbol() throws Exception {

		check("€T¥E£S¥T$", "TEST");
	}

	@Test
	public void testMathSymbol() throws Exception {

		check("<=T+E×S±T÷⅀>", "TEST");
	}

	@Test
	public void testModifierSymbol() throws Exception {

		check("T^EST", "TEST");
	}

	@Test
	public void testOtherSymbol() throws Exception {

		check("©T¦E௳S᧞T", "TEST");
	}

}
