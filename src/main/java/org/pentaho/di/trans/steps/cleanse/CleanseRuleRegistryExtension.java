package org.pentaho.di.trans.steps.cleanse;

import org.pentaho.di.core.KettleClientEnvironment;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.PluginRegistryExtension;
import org.pentaho.di.core.plugins.PluginTypeInterface;
import org.pentaho.di.core.plugins.RegistryPlugin;

@RegistryPlugin(
    id = "CleanseRuleRegistryExtension",
    name = "CleanseRuleRegistryExtension",
    description = "Registers cleanse rule plugin type")
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

    //      try {
    //        pluginTypeInterface.searchPlugins();
    //      } catch (KettlePluginException e) {
    //        // TODO Auto-generated catch block
    //        e.printStackTrace();
    //      }

  }

}
