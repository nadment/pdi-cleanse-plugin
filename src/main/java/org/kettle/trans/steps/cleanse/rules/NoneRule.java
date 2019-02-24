package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule do nothing.
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "None", name = "None", category = "Cleaning", description = "The rule do nothing")
public class NoneRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {

		return object;
	}

}
