package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule remove leading and trailing whitespace characters.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "Trim", name = "Trim white space", category = "Cleaning", description = "The rule remove leading and trailing whitespace characters")
public class TrimRule implements CleanseProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {

		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		return value.trim();
	}

}
