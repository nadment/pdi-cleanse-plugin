package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule removes hyphen and dash characters.
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "RemoveHyphenDash", name = "Remove hyphen and dash characters", category = "Cleaning", description = "The rule removes hyphen and dash characters")
public class RemoveHyphenDashRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

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
