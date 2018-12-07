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
package org.pentaho.di.trans.steps.cleanse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.util.Utils;

/**
 * Singleton controlling cleanse rule plugin.
 * 
 * @author Nicolas ADMENT
 *
 */
public class CleanseRuleManager {

	private static final CleanseRuleManager INSTANCE = new CleanseRuleManager();

	private CleanseRuleManager() {
	}

	public static CleanseRuleManager getInstance() {
		return INSTANCE;
	}

	public List<PluginInterface> getRules() {
		List<PluginInterface> plugins = PluginRegistry.getInstance().getPlugins(CleanseRulePluginType.class);
		if (plugins == null)
			return Collections.emptyList();

		return plugins;
	}

	/**
	 * Find rule plugin by name
	 * 
	 * @param i
	 *            The rule identifier
	 * @return the rule name or null if not found
	 */
	public PluginInterface getRuleByName(final String name) {
		if (!Utils.isEmpty(name)) {
			for (PluginInterface plugin : getRules()) {
				if (name.equals(plugin.getName())) {
					return plugin;
				}
			}
		}

		return null;
	}

	/**
	 * Find rule name by one of is plugin identifier
	 * 
	 * @param id
	 *            The rule identifier
	 * @return the rule plugin or null if not found
	 */
	public PluginInterface getRuleById(final String id) {
		if (!Utils.isEmpty(id)) {
			for (PluginInterface plugin : getRules()) {
				for (String pluginId : plugin.getIds()) {
					if (pluginId.equals(id)) {
						return plugin;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Create processor instance by rule identifer
	 * 
	 * @param id
	 *            The rule identifier
	 * @return the processor instance or null if not found
	 */
	public CleanseProcessor createProcessor(final String id) throws KettlePluginException {

		if (Utils.isEmpty(id)) {
			return null;
		}

		return PluginRegistry.getInstance().loadClass(CleanseRulePluginType.class, id, CleanseProcessor.class);
	}

	public String[] getRuleNames() {
						
		List<String> names = new ArrayList<String>();
		for (PluginInterface plugin : getRules()) {
			names.add(plugin.getName());
		}
		
		return names.stream().sorted().toArray(String[]::new);
	}

}
