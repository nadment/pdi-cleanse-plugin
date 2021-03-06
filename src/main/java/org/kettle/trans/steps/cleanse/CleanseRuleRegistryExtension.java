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

import org.pentaho.di.core.KettleClientEnvironment;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.PluginRegistryExtension;
import org.pentaho.di.core.plugins.PluginTypeInterface;
import org.pentaho.di.core.plugins.RegistryPlugin;

@RegistryPlugin(id = "CleanseRuleRegistryExtension", name = "CleanseRuleRegistryExtension", description = "Registers cleanse rule plugin type")
public class CleanseRuleRegistryExtension implements PluginRegistryExtension {

	@Override
	public String getPluginId(Class<? extends PluginTypeInterface> arg0, Object arg1) {
		return null;
	}

	@Override
	public void init(final PluginRegistry pluginRegistry) {
		if (KettleClientEnvironment.isInitialized()) {
			PluginRegistry.addPluginType(CleanseRulePluginType.getInstance());
		}
	}

	@Override
	public void searchForType(PluginTypeInterface pluginTypeInterface) {
	}

}
