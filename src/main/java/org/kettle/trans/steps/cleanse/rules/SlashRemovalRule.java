package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule removes forward and back slashes characters.
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "SlashRemoval", name = "Slash Removal", category = "Cleaning", description = "The rule removes forward and back slashes characters")

public class SlashRemovalRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		StringBuilder result = new StringBuilder(value.length());

		for (int index = 0; index < value.length(); index++) {
			char ch = value.charAt(index);

			if (ch == '/' || ch == '\\')
				continue;

			result.append(ch);
		}

		return result.toString();
	}

}
