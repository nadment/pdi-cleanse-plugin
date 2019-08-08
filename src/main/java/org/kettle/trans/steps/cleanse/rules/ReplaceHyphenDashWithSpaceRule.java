package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule replaces hyphen and dash characters with a space character.
 * 
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "ReplaceHyphenDashWithSpace", name = "Replace hyphen and dash with space", category = "Cleaning", description = "The rule replaces hyphen and dash characters with a space character")
public class ReplaceHyphenDashWithSpaceRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		StringBuilder result = new StringBuilder(value.length());

		for (int index = 0; index < value.length(); index++) {
			char ch = value.charAt(index);

			if (Character.getType(ch) == Character.DASH_PUNCTUATION) {
				ch = ' ';
			}

			result.append(ch);

		}

		return result.toString();
	}

}
