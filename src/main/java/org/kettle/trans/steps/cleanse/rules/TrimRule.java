package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule remove leading and trailing whitespace characters.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "Trim", name = "Trim white space", category = "Cleaning", description = "The rule remove leading and trailing whitespace characters")
public class TrimRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface vm, final Object object) throws KettleValueException {

		if (object == null)
			return null;

		String value = vm.getString(object);
		return value.trim();
	}

}
