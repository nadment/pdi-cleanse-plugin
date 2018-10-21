package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule removes all white space characters.
 * 
 * <p>
 * Whitespace is defined by {@link Character#isWhitespace(char)}.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "SpaceRemoval", name = "Space Removal", category = "Cleaning", description = "The rule removes all white space characters")
public class SpaceRemovalRule implements CleanseProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		StringBuilder result = new StringBuilder(value.length());

		for (int offset = 0; offset < value.length();) {
			int codePoint = value.codePointAt(offset);
			offset += Character.charCount(codePoint);

			if (!Character.isWhitespace(codePoint)) {
				result.append(Character.toChars(codePoint));
			}
		}

		return result.toString();
	}

}
