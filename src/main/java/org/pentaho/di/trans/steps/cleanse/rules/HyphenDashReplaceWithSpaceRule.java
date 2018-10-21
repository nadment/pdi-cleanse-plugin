package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule replaces hyphen and dash characters with a space character.
 * 
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "HyphenDashReplaceWithSpace", name = "Hyphen Dash Replace With Space", category = "Cleaning", description = "The rule replaces hyphen and dash characters with a space character")
public class HyphenDashReplaceWithSpaceRule implements CleanseProcessor {

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
