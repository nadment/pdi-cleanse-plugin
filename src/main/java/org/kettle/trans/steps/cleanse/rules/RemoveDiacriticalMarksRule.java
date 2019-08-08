package org.kettle.trans.steps.cleanse.rules;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule removes all diacritics marks (~= accents).
 * 
 * <p>
 * Note that ligatures will be left as is.
 * </p>
 * 
 * @author Nicolas ADMENT *
 */
@CleanseRule(id = "RemoveDiacriticalMarks", name = "Remove diacritical marks", category = "Cleaning", description = "The rule removes all diacritics marks")
public class RemoveDiacriticalMarksRule implements ValueProcessor {

	/**
	 * The empty String {@code ""}.
	 * 
	 */
	public static final String EMPTY = "";

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);

		final Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");//$NON-NLS-1$
		final StringBuilder decomposed = new StringBuilder(Normalizer.normalize(value.toString(), Normalizer.Form.NFD));
		convertRemainingAccentCharacters(decomposed);
		// Note that this doesn't correctly remove ligatures...
		return pattern.matcher(decomposed).replaceAll(EMPTY);
	}

	private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
		for (int i = 0; i < decomposed.length(); i++) {
			if (decomposed.charAt(i) == '\u0141') {
				decomposed.deleteCharAt(i);
				decomposed.insert(i, 'L');
			} else if (decomposed.charAt(i) == '\u0142') {
				decomposed.deleteCharAt(i);
				decomposed.insert(i, 'l');
			}
		}
	}

}
