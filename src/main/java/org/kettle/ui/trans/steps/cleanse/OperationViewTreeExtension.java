package org.kettle.ui.trans.steps.cleanse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.kettle.trans.steps.cleanse.CleanseMeta;
import org.kettle.trans.steps.cleanse.OperationDefinition;
import org.kettle.trans.steps.cleanse.OperationManager;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.extension.ExtensionPoint;
import org.pentaho.di.core.extension.ExtensionPointInterface;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.ui.core.gui.GUIResource;
import org.pentaho.di.ui.spoon.SelectionTreeExtension;
import org.pentaho.di.ui.spoon.Spoon;

@ExtensionPoint(id = "OperationViewTreeExtension", description = "Refreshes execution environment subtree", extensionPointId = "SpoonViewTreeExtension")
public class OperationViewTreeExtension implements ExtensionPointInterface {

	private static final Class<?> PKG = CleanseMeta.class;
	
	public static String TREE_LABEL = BaseMessages.getString( PKG, "OperationTree.Title" );

	@Override
	public void callExtensionPoint(LogChannelInterface log, Object object) throws KettleException {
		SelectionTreeExtension selectionTreeExtension = (SelectionTreeExtension) object;
		if (selectionTreeExtension.getAction().equals(Spoon.REFRESH_SELECTION_EXTENSION)) {
			refreshTree(selectionTreeExtension);
		}
		if (selectionTreeExtension.getAction().equals(Spoon.EDIT_SELECTION_EXTENSION)) {
			if (selectionTreeExtension.getSelection() instanceof OperationDefinition) {
				OperationDefinition operation = (OperationDefinition) selectionTreeExtension.getSelection();
				OperationEditorDialog dialog = new OperationEditorDialog(Spoon.getInstance().getShell(), operation);
				if (dialog.open() == SWT.OK) {
					OperationManager.getInstance().save(operation);
				}

			}
		}
	}

	private void refreshTree(SelectionTreeExtension selectionTreeExtension) {
		TreeItem tiRootName = selectionTreeExtension.getTiRootName();
		GUIResource guiResource = selectionTreeExtension.getGuiResource();

		TreeItem tiEETitle = createTreeItem(tiRootName, TREE_LABEL, guiResource.getImageFolder(), 0);

		for (OperationDefinition operation : OperationManager.getInstance().getOperations()) {
			createTreeItem(tiEETitle, operation.getName(), guiResource.getImageVariable(), -1);
		}

	}

	private TreeItem createTreeItem(TreeItem parent, String text, Image image, int index) {
		TreeItem item = index == -1 ? new TreeItem(parent, SWT.NONE) : new TreeItem(parent, SWT.NONE, index);
		item.setText(text);
		item.setImage(image);
		
		return item;
	}

}
