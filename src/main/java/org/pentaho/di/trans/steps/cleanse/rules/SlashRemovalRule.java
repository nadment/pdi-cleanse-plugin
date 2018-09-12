package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule removes forward and back slashes characters.
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "SlashRemoval", name = "Slash Removal", category = "Cleaning", description = "The rule removes forward and back slashes characters")

public class SlashRemovalRule implements CleanseProcessor {

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

			if (ch == '/' || ch == '\\') continue;
			

			result.append(ch);
		}

		return result.toString();
	}

}
