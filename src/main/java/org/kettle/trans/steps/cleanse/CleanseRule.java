/*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.kettle.trans.steps.cleanse;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation signals to the plugin system that the class is a cleanse
 * rule.
 * 
 * The class needs to implement the {@link ValueProcessor} interface
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
