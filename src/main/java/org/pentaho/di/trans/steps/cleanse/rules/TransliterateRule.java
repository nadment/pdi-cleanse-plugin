package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

import gcardone.junidecode.Junidecode;

/**
 * The rule converts strings from many languages of the world to a standard set of characters Universal Coded Character Set (UCS).
 * 
 * @author Nicolas ADMENT
 *
 */
@CleanseRule(id = "Transliterate", name = "Transliterate UCS", category = "Transliterate", description = "The rule converts strings from many languages of the world to a standard set of characters Universal Coded Character Set (UCS)")
public class TransliterateRule implements CleanseProcessor {

	@Override
	public Object processValue(final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = null;

		if (object instanceof String) {
			value = (String) object;
			return Junidecode.unidecode(value);			
		} else
			throw new KettleValueException("Value is not a String");
	}

}
