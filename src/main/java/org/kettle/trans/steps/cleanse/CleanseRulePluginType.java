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

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.plugins.BasePluginType;
import org.pentaho.di.core.plugins.PluginAnnotationType;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.PluginMainClassType;
import org.pentaho.di.core.plugins.PluginTypeInterface;

/**
 * This class represents the cleanse rule plugin type.
 * 
 * @author Nicolas ADMENT
 *
 */
@PluginMainClassType(ValueProcessor.class)
@PluginAnnotationType(CleanseRule.class)
public class CleanseRulePluginType extends BasePluginType implements PluginTypeInterface {

	private static CleanseRulePluginType INSTANCE;

	public static CleanseRulePluginType getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CleanseRulePluginType();
		}
		return INSTANCE;
	}

	private CleanseRulePluginType() {
		super(CleanseRule.class, "CLEANSE_RULE", "Cleanse rule");
		pluginFolders.add(new PluginFolder("plugins", false, true));
	}

	@Override
	protected String extractCategory(Annotation annotation) {
		return ((CleanseRule) annotation).category();
	}

	@Override
	protected String extractDesc(Annotation annotation) {
		return ((CleanseRule) annotation).description();
	}

	@Override
	protected String extractID(Annotation annotation) {
		return ((CleanseRule) annotation).id();
	}

	@Override
	protected String extractName(Annotation annotation) {
		return ((CleanseRule) annotation).name();
	}

	@Override
	protected String extractImageFile(Annotation annotation) {
		return null;
	}

	@Override
	protected boolean extractSeparateClassLoader(Annotation annotation) {
		return false;
	}

	@Override
	protected String extractClassLoaderGroup(Annotation annotation) {
		return ((CleanseRule) annotation).classLoaderGroup();
	}

	@Override
	protected String extractI18nPackageName(Annotation annotation) {
		return ((CleanseRule) annotation).i18nPackageName();
	}

	@Override
	protected void addExtraClasses(Map<Class<?>, String> classMap, Class<?> clazz, Annotation annotation) {
	}

	@Override
	protected String extractDocumentationUrl(Annotation annotation) {
		return null;
	}

	@Override
	protected String extractCasesUrl(Annotation annotation) {
		return null;
	}

	@Override
	protected String extractForumUrl(Annotation annotation) {
		return null;
	}

	// @Override API change, should exists since 8.2
	protected String extractSuggestion(Annotation annotation) {
		return null;
	}

	@Override
	public void handlePluginAnnotation(Class<?> clazz, Annotation annotation, List<String> libraries,
			boolean nativePluginType, URL pluginFolder) throws KettlePluginException {

		// FIXME: annotation is null with method getAnnotation(), seem to not be
		// in the same class loader
		if (annotation == null)
			try {
				annotation = Class.forName(clazz.getName()).getAnnotation(CleanseRule.class);
			} catch (ClassNotFoundException e) {
				throw new KettlePluginException("Annotation is null for plugin with class: " + clazz.getName());
			}

		super.handlePluginAnnotation(clazz, annotation, libraries, nativePluginType, pluginFolder);
	}

	@Override
	protected void registerXmlPlugins() throws KettlePluginException {
	}

	@Override
	// To avoid error, should be implemented
	protected void registerNatives() throws KettlePluginException {
	}
}
