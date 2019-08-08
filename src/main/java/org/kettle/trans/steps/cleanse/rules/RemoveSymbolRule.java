package org.kettle.trans.steps.cleanse.rules;

import org.kettle.trans.steps.cleanse.CleanseRule;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * The rule removes all symbol characters (Unicode Character Categories Currency
 * {Sc}, Math {Sm}, Modifier {Sk} and Other {So}).
 * 
 * @see <a href='https://www.compart.com/en/unicode/category'>Unicode Character
 *      Categories</a>
 * @author Nicolas ADMENT
 */

@CleanseRule(id = "RemoveSymbol", name = "Remove symbol characters", category = "Cleaning", description = "The rule removes all symbol characters (Unicode Character Categories {Sc} {Sm} {Sk} {So})")
public class RemoveSymbolRule implements ValueProcessor {

	@Override
	public Object processValue(final ValueMetaInterface vm, final Object object) throws KettleValueException {
		if (object == null)
			return null;

		String value = vm.getString(object);
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
