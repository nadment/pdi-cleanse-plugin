package org.pentaho.di.trans.steps.cleanse;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation signals to the plugin system that the class is a cleanse rule.
 * 
 * The class needs to implement the {@link CleanseProcessor} interface
 *
 */


@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface CleanseRule {
  /**
   * The unique identifier for the rule.
   * 
   * @return
   */
  String id();

  /**
   * The display name of the rule.
   *  
   * @return
   */
  String name();

  String description() default "";

  String category() default "";

  String i18nPackageName() default "";

  String classLoaderGroup() default "";
}
