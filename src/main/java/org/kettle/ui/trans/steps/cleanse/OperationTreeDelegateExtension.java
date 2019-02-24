package org.kettle.ui.trans.steps.cleanse;

import java.util.List;

import org.kettle.trans.steps.cleanse.OperationDefinition;
import org.kettle.trans.steps.cleanse.OperationManager;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.extension.ExtensionPoint;
import org.pentaho.di.core.extension.ExtensionPointInterface;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.ui.spoon.TreeSelection;
import org.pentaho.di.ui.spoon.delegates.SpoonTreeDelegateExtension;

@ExtensionPoint(id = "OperationTreeDelegateExtension", description = "", extensionPointId = "SpoonTreeDelegateExtension")
public class OperationTreeDelegateExtension implements ExtensionPointInterface {

	// private static final Class<?> PKG = CleanseMeta.class;

	@Override
	public void callExtensionPoint(LogChannelInterface log, Object extension) throws KettleException {

		SpoonTreeDelegateExtension treeDelExt = (SpoonTreeDelegateExtension) extension;
		int caseNumber = treeDelExt.getCaseNumber();
		AbstractMeta meta = treeDelExt.getTransMeta();
		String[] path = treeDelExt.getPath();
		List<TreeSelection> objects = treeDelExt.getObjects();

		TreeSelection object = null;

		if (path[2].equals(OperationViewTreeExtension.TREE_LABEL)) {
			switch (caseNumber) {
			case 3:
				object = new TreeSelection(path[2], OperationDefinition.class, meta);
				break;
			case 4:
				try {
					OperationDefinition operation = OperationManager.getInstance().load(path[3]);
					object = new TreeSelection(path[3], operation, meta);
				} catch (Exception e) {
					// Do Nothing
				}
				break;
			}
		}

		if (object != null) {
			objects.add(object);
		}
	}

}
