package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule do nothing.
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "None", name = "None", category = "Cleaning", description = "The rule do nothing")
public class NoneRule implements CleanseProcessor {

	@Override
	public Object processValue(final Object object) throws KettleValueException {

		return object;
	}

}
