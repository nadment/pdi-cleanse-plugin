package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule capitalize the first letter of each word.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "CamelCase", name = "Camel case", category = "Case", description = "The rule capitalise the first letter of each word")
public class CamelCaseRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		if (value.length() == 0) {
			return value;
		}

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
