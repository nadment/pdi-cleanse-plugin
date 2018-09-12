package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule lower case all characters.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "LowerCase", name = "Lower case", category = "Case", description = "The rule lower case all characters")
public class LowerCaseRule implements CleanseProcessor {

	@Override
	public Object processValue(final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = null;

		if (object instanceof String) {
			value = (String) object;
			return value.toLowerCase();
		} else
			throw new KettleValueException("Value is not a String");
	}

}
