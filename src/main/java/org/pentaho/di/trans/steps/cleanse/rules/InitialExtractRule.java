package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule extracts the initial characters from each word.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "Initial", name = "Initial", category = "Extract", description = "The rule extracts the initial characters from each word")
public class InitialExtractRule implements CleanseProcessor {

	@Override
	public Object processValue(final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = null;

		if (object instanceof String) {
			value = (String) object;

			if (value.length() == 0) {
				return value;
			}
		} else
			throw new KettleValueException("Value is not a String");

		StringBuilder result = new StringBuilder();

		boolean next = true;

		for (int i = 1; i < value.length(); i++) {
			char ch = value.charAt(i);

			if (Character.isWhitespace(ch)) {
				next = true;
			} else if (next)  {
				result.append(Character.toUpperCase(ch));
				next = false;
			}			
		}

		return result.toString();
	}

}
