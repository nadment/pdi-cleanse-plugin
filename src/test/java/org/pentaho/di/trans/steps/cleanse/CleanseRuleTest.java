package org.pentaho.di.trans.steps.cleanse;

import java.lang.reflect.ParameterizedType;

import org.junit.Assert;
import org.pentaho.di.core.exception.KettleValueException;

public class CleanseRuleTest<R extends CleanseProcessor> {

  public void check(String input, String expected) {

    try {
      @SuppressWarnings("unchecked")
      R rule = ((R) ((Class<R>) ((ParameterizedType) this.getClass().getGenericSuperclass())
          .getActualTypeArguments()[0]).newInstance());

      Object actual = rule.processValue(input);
      Assert.assertEquals(expected, actual);
    } catch (KettleValueException | InstantiationException | IllegalAccessException e) {
      Assert.fail(e.getMessage());
    }

  }

  public static String toUnicode(byte ch) {
    return String.format("\\u%04x", (int) ch);
  }

}
