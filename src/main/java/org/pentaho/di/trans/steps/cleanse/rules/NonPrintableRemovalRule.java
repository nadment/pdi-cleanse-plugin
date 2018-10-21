package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule removes all control characters and other non-printable characters.
 * 
 * @see <a href='https://www.compart.com/en/unicode/category'>Unicode Character
 *      Categories</a>
 * @author Nicolas ADMENT
 */

@CleanseRule(id = "NonPrintableRemoval", name = "Non Printable Removal", category = "Cleaning", description = "The rule removes all control characters and other non-printable characters")
public class NonPrintableRemovalRule implements CleanseProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		StringBuilder result = new StringBuilder(value.length());
		for (int offset = 0; offset < value.length();) {
			int codePoint = value.codePointAt(offset);
			offset += Character.charCount(codePoint);

			switch (Character.getType(codePoint)) {
			case Character.CONTROL: // {Cc}
			case Character.FORMAT: // {Cf}
			case Character.PRIVATE_USE: // {Co}
			case Character.SURROGATE: // {Cs}
			case Character.UNASSIGNED: // {Cn}
				break;

			default:
				result.append(Character.toChars(codePoint));
				break;
			}
		}
		return result.toString();
	}

}
