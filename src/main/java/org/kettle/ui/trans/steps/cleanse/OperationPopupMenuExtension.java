package org.kettle.ui.trans.steps.cleanse;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.kettle.trans.steps.cleanse.CleanseMeta;
import org.kettle.trans.steps.cleanse.OperationDefinition;
import org.kettle.trans.steps.cleanse.OperationManager;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.extension.ExtensionPoint;
import org.pentaho.di.core.extension.ExtensionPointInterface;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.ui.core.ConstUI;
import org.pentaho.di.ui.core.dialog.ShowMessageDialog;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.spoon.TreeSelection;

@ExtensionPoint(id = "OperationPopupMenuExtension", description = "Creates popup menus for execution environments", extensionPointId = "SpoonPopupMenuExtension")
public class OperationPopupMenuExtension implements ExtensionPointInterface {

	private static final Class<?> PKG = CleanseMeta.class;

	@Override
	public void callExtensionPoint(LogChannelInterface log, Object extension) throws KettleException {
		Menu popupMenu = null;

		Tree tree = (Tree) extension;
		TreeSelection[] objects = this.getSpoon().getTreeObjects(tree);
		Object selection = objects[0].getSelection();

		if (selection == OperationDefinition.class) {
			popupMenu = createRootPopupMenu(tree);
		} else if (selection instanceof OperationDefinition) {
			popupMenu = createItemPopupMenu(tree, (OperationDefinition) selection);
		}

		if (popupMenu != null) {
			ConstUI.displayMenu(popupMenu, tree);
		} else {
			tree.setMenu(null);
		}
	}

	private Menu createRootPopupMenu(Tree tree) {

		Menu menu = new Menu(tree);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(BaseMessages.getString(PKG, "OperationPopupMenuExtension.MenuItem.New"));
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				OperationDefinition operation = new OperationDefinition();
				OperationEditorDialog dialog = new OperationEditorDialog(getShell(), operation);
				if (dialog.open() == SWT.OK) {
					OperationManager.getInstance().save(operation);
					getSpoon().refreshTree();
				}
			}
		});

		return menu;
	}

	private Menu createItemPopupMenu(Tree tree, OperationDefinition operation) {

		Menu menu = new Menu(tree);
		MenuItem editMenuItem = new MenuItem(menu, SWT.NONE);
		editMenuItem.setText(BaseMessages.getString(PKG, "OperationPopupMenuExtension.MenuItem.Edit"));
		editMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				OperationEditorDialog dialog = new OperationEditorDialog(getShell(), operation);
				if (dialog.open() == SWT.OK) {
					OperationManager.getInstance().save(operation);
					getSpoon().refreshTree();
				}
			}
		});

		MenuItem deleteMenuItem = new MenuItem(menu, SWT.NONE);
		deleteMenuItem.setText(BaseMessages.getString(PKG, "OperationPopupMenuExtension.MenuItem.Delete"));
		deleteMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				
				ShowMessageDialog dialog = new ShowMessageDialog(getShell(),
						SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION,
						BaseMessages.getString(PKG, "OperationPopupMenuExtension.DeleteConfirmation.Title"),
						BaseMessages.getString(PKG, "OperationPopupMenuExtension.DeleteConfirmation.Message",operation.getName()));
				if (dialog.open() == SWT.OK) {
					OperationManager.getInstance().delete(operation.getName());
					getSpoon().refreshTree();
				}
			}
		});

		return menu;
	}

	public Spoon getSpoon() {
		return Spoon.getInstance();
	}

	public Shell getShell() {
		return getSpoon().getShell();
	}
}
