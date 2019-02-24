package org.kettle.trans.steps.cleanse.rules;

import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseRuleTest;
import org.kettle.trans.steps.cleanse.rules.TitleCaseRule;

public class TitleRuleTest extends CleanseRuleTest<TitleCaseRule> {

	

	@Test
	public void test() throws Exception {

		check(" TEST", " Test");
		check("TEST ,pour chaque mot. merci", "Test ,pour chaque mot. Merci");
		check("\tTEST  ", "\tTest");
		// check("TEST"+toUnicode(Character.SPACE_SEPARATOR),"TEST");
		// check("TEST"+String.valueOf(Character.LINE_SEPARATOR),"TEST");
		// check("TEST"+String.valueOf(Character.PARAGRAPH_SEPARATOR),"TEST");

	}

}
