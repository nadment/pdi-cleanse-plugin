package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule removes hyphen and dash characters.
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "HyphenDashRemoval", name = "Hyphen Dash Removal", category = "Cleaning", description = "The rule removes hyphen and dash characters")
public class HyphenDashRemovalRule implements CleanseProcessor {


	@Override
	public Object processValue(final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = null;

		if (object instanceof String)
			value = (String) object;
		else
			throw new KettleValueException("Value is not a String");

		StringBuilder result = new StringBuilder(value.length());

		for (int index = 0; index < value.length(); index++) {
			char ch = value.charAt(index);

			if (Character.getType(ch) != Character.DASH_PUNCTUATION) {
				result.append(ch);
			}
		}

		return result.toString();
	}

}
