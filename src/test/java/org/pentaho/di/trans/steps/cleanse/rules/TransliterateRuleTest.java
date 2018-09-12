package org.pentaho.di.trans.steps.cleanse.rules;

import org.junit.Test;
import org.pentaho.di.trans.steps.cleanse.CleanseRuleTest;

public class TransliterateRuleTest extends CleanseRuleTest<TransliterateRule> {


	@Test
	public void test() throws Exception {

		// Latin-1 Supplement
		check("résumé", "resume");
		
		// Cyrillic
		check("Москвa", "Moskva");
		
		// Greek and Coptic
		check("Ελληνικά", "Ellenika");
		
		// CJK Unified Ideographs
		check("北亰", "Bei Jing ");
		
		// Braille Patterns
		check("⠏⠗⠑⠍⠊⠑⠗", "premier");
		
		// Currency Symbols
		check("₤ ₧ ₨", "L Pts Rs");
		
		// Number Forms
		check("⅔ Ⅷ", " 2/3  VIII");
	}

}
