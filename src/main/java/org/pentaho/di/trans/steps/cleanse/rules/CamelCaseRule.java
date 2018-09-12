package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule capitalise the first letter of each word.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "CamelCase", name = "Camel case", category = "Case", description = "The rule capitalise the first letter of each word")
public class CamelCaseRule implements CleanseProcessor {

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
		char last = value.charAt(0);
		result.append(Character.toUpperCase(last));
		for (int i = 1; i < value.length(); i++) {
			char ch = value.charAt(i);

			if (Character.isWhitespace(last)) {
				result.append(Character.toTitleCase(ch));
			} else {

				result.append(Character.toLowerCase(ch));
			}
			last = ch;
		}

		return result.toString();
	}

}
