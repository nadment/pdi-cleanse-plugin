package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule lower case all characters.
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "LowerCase", name = "Lower case", category = "Case", description = "The rule lower case all characters")
public class LowerCaseRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		return value.toLowerCase();
	}
}
