package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule upper case all characters.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "UpperCase", name = "Upper case", category = "Case", description = "The rule upper case all characters")
public class UpperCaseRule implements CleanseProcessor {

	@Override
	public Object processValue(final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = null;

		if (object instanceof String) {
			value = (String) object;
			return value.toUpperCase();			
		} else
			throw new KettleValueException("Value is not a String");
	}

}
