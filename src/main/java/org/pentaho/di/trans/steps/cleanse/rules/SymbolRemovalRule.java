package org.pentaho.di.trans.steps.cleanse.rules;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.steps.cleanse.CleanseProcessor;
import org.pentaho.di.trans.steps.cleanse.CleanseRule;

/**
 * The rule removes all symbol characters (Unicode Character Categories Currency
 * {Sc}, Math {Sm}, Modifier {Sk} and Other {So}).
 * 
 * @see <a href='https://www.compart.com/en/unicode/category'>Unicode Character
 *      Categories</a>
 * @author Nicolas ADMENT
 */

@CleanseRule(id = "SymbolRemoval", name = "Symbol Removal", category = "Cleaning", description = "The rule removes all symbol characters (Unicode Character Categories {Sc} {Sm} {Sk} {So})")
public class SymbolRemovalRule implements CleanseProcessor {

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = valueMeta.getString(object);
		StringBuilder result = new StringBuilder(value.length());

		for (int offset = 0; offset < value.length();) {
			int codePoint = value.codePointAt(offset);
			offset += Character.charCount(codePoint);

			switch (Character.getType(codePoint)) {
			case Character.CURRENCY_SYMBOL: // {Sc}
			case Character.MATH_SYMBOL: // {Sm}
			case Character.MODIFIER_SYMBOL: // {Sk}
			case Character.OTHER_SYMBOL: // {So}
				break;

			default:
				result.append(Character.toChars(codePoint));
				break;
			}
		}

		return result.toString();

	}

}
