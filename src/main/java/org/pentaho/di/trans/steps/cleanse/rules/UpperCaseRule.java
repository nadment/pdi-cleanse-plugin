package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
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
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		return value.toUpperCase();
	}
}
