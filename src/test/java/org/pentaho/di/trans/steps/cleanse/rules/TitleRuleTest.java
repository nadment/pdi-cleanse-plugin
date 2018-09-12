package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

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
