package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule remove leading and trailing whitespace and collapses multiple white
 * space characters by a single space character.
 * 
 * <p>
 * Whitespace is defined by {@link Character#isWhitespace(char)}.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "NormalizeSpaceRule", name = "Normalize space", category = "Cleaning", description = "The rule remove leading and trailing whitespace and collapses multiple white space characters by a single space character")
public class NormalizeSpaceRule implements CleanseProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		char last = value.charAt(0);
		StringBuilder result = new StringBuilder(value.length());

		for (int index = 0; index < value.length(); index++) {
			char ch = value.charAt(index);

			if (!Character.isWhitespace(ch) || !Character.isWhitespace(last)) {

				// Replace tab with space
				if (Character.isWhitespace(ch))
					ch = ' ';

				result.append(ch);
				last = ch;
			}
		}

		return result.toString();
	}

}
