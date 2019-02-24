package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

import gcardone.junidecode.Junidecode;

/**
 * The rule converts strings from many languages of the world to a standard set
 * of characters Universal Coded Character Set (UCS).
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "Transliterate", name = "Transliterate UCS", category = "Transliterate", description = "The rule converts strings from many languages of the world to a standard set of characters Universal Coded Character Set (UCS)")
public class TransliterateRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		return Junidecode.unidecode(value);
	}

}
