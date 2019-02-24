package org.kettle.trans.steps.cleanse;

import java.lang.reflect.ParameterizedType;

import org.junit.Assert;
import org.kettle.trans.steps.cleanse.ValueProcessor;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;

public class CleanseRuleTest<R extends ValueProcessor> {

  public void check(String input, String expected) {

    try {
      @SuppressWarnings("unchecked")
      R rule = ((R) ((Class<R>) ((ParameterizedType) this.getClass().getGenericSuperclass())
          .getActualTypeArguments()[0]).newInstance());

      
  	// create ValueMeta
		ValueMetaInterface vm = ValueMetaFactory.createValueMeta("Test", ValueMetaInterface.TYPE_STRING);

      
      Object actual = rule.processValue(vm, input);
      Assert.assertEquals(expected, actual);
    } catch (KettleValueException | KettlePluginException | InstantiationException | IllegalAccessException e) {
      Assert.fail(e.getMessage());
    }

  }

  public static String toUnicode(byte ch) {
    return String.format("\\u%04x", (int) ch);
  }

}
