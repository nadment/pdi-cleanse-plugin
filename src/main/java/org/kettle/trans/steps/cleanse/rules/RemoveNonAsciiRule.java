package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule removes all non ascii characters.
 * 
 * @see <a href='https://www.compart.com/en/unicode/category'>Unicode Character
 *      Categories</a>
 * @author Nicolas ADMENT
 */

@CleanseRule(id = "RemoveNonAscii", name = "Remove non Ascii characters", category = "Cleaning", description = "The rule removes all non asci characters")
public class RemoveNonAsciiRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		String result = value.replaceAll("[^\\x00-\\x7F]", "");
		
		return result.toString();
	}

}
