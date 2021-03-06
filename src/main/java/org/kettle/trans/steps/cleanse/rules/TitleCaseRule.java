package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule capitalize the first letter of each phrase.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "TitleCase", name = "Title case", category = "Case", description = "The rule capitalize the first letter of each phrase")
public class TitleCaseRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		if (value.length() == 0) {
			return value;
		}

		StringBuilder result = new StringBuilder(value.length());
		char last = '.';
		result.append(Character.toUpperCase(last));
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);

			if (Character.getType(last) == Character.END_PUNCTUATION) {
				result.append(Character.toTitleCase(ch));
			} else {

				result.append(Character.toLowerCase(ch));
			}
			last = ch;
		}

		return result.toString();
	}

}
