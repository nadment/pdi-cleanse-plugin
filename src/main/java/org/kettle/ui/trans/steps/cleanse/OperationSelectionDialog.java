package org.kettle.ui.trans.steps.cleanse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.kettle.trans.steps.cleanse.CleanseMeta;
import org.kettle.trans.steps.cleanse.OperationDefinition;
import org.kettle.trans.steps.cleanse.OperationManager;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.ui.core.ConstUI;
import org.pentaho.di.ui.core.FormDataBuilder;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.dialog.ShowMessageDialog;
import org.pentaho.di.ui.core.gui.GUIResource;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * This dialogs allows you to select a ordered number of items from a list of
 * strings.
 *
 * @author Nicolas ADMENT
 * @since 29-09-2018
 */
public class OperationSelectionDialog extends Dialog {
	private static final Class<?> PKG = CleanseMeta.class;

	public static final int LARGE_MARGIN = 15;

	private PropsUI props;
	private Shell shell;
	private List lstOperation;

	public OperationSelectionDialog(Shell parent, int style) {
		super(parent, style);

		this.props = PropsUI.getInstance();
	}

	public void open() {

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
		props.setLook(shell);
		shell.setImage(GUIResource.getInstance().getImageSpoon());
		shell.setText(BaseMessages.getString(PKG, "OperationSelectionDialog.Shell.Title"));
		shell.setLayout(new FormLayout());

		// *******************************************************************
		// Top & Bottom regions.
		// *******************************************************************
		Composite content = new Composite(shell, SWT.NONE);
		FormLayout topLayout = new FormLayout();
		topLayout.marginHeight = LARGE_MARGIN;
		topLayout.marginWidth = LARGE_MARGIN;
		content.setLayout(topLayout);
		content.setLayoutData(new FormDataBuilder().top().bottom(100, -50).left().right(100, 0).result());
		props.setLook(content);

		Composite bottom = new Composite(shell, SWT.NONE);
		FormLayout bottomLayout = new FormLayout();
		bottomLayout.marginHeight = LARGE_MARGIN;
		bottomLayout.marginWidth = LARGE_MARGIN;
		bottom.setLayout(bottomLayout);
		bottom.setLayoutData(new FormDataBuilder().top(content, 0).bottom().right().result());
		props.setLook(bottom);

		Label lblOperation = new Label(content, SWT.NONE);
		lblOperation.setText(BaseMessages.getString(PKG, "OperationSelectionDialog.AvailableOperations.Label"));
		lblOperation.setLayoutData(new FormDataBuilder().top().left().result());
		props.setLook(lblOperation);

		ToolBar toolbar = new ToolBar(content, SWT.NONE);
		toolbar.setLayoutData(new FormDataBuilder().top().right().result());
		this.props.setLook(toolbar);

		ToolItem addItem = new ToolItem(toolbar, SWT.NONE);
		addItem.setImage(GUIResource.getInstance().getImageAdd());
		addItem.setToolTipText(BaseMessages.getString(PKG, "OperationSelectionDialog.AddButton.Label"));
		addItem.addListener(SWT.Selection, Event -> onCreate());

		ToolItem removeItem = new ToolItem(toolbar, SWT.NONE);
		removeItem.setImage(GUIResource.getInstance().getImageDelete());
		removeItem.setToolTipText(BaseMessages.getString(PKG, "OperationSelectionDialog.DeleteButton.Label"));
		removeItem.addListener(SWT.Selection, Event -> onDelete());

		lstOperation = new List(content, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		lstOperation.setLayoutData(
				new FormDataBuilder().top(toolbar, ConstUI.SMALL_MARGIN).left().bottom(100, 0).right(100, 0).result());

		try {
			for (String name : OperationManager.getInstance().getOperationNames()) {

				lstOperation.add(name);
			}
		} catch (KettleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		props.setLook(lstOperation);

		lstOperation.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.character == SWT.CR) {
					onEdit();
				}
			}
		});

		// Double click to edit operation.
		lstOperation.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// onEdit(lstOperation.getSelection()[0]);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				onEdit();
			}
		});

		// *******************************************************************
		// THE BUTTON BAR
		// *******************************************************************

		Button btnCancel = new Button(bottom, SWT.PUSH);
		btnCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));
		btnCancel.setLayoutData(new FormDataBuilder().bottom().right().result());
		btnCancel.addListener(SWT.Selection, event -> onCancelPressed());

		Button btnOK = new Button(bottom, SWT.PUSH);
		btnOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
		btnOK.setLayoutData(new FormDataBuilder().bottom().right(btnCancel, -ConstUI.SMALL_MARGIN).result());
		btnOK.addListener(SWT.Selection, Event -> onOkPressed());

		BaseStepDialog.setSize(shell);

		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	protected void onEdit() {
		try {
			String name = lstOperation.getSelection()[0];
			OperationDefinition operation = OperationManager.getInstance().load(name);
			OperationEditorDialog dialog = new OperationEditorDialog(shell, operation);
			if (dialog.open() == SWT.OK) {
				OperationManager.getInstance().save(operation);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onCreate() {
		OperationDefinition operation = new OperationDefinition();

		OperationEditorDialog dialog = new OperationEditorDialog(shell, operation);
		if (dialog.open() == SWT.OK) {

			OperationManager.getInstance().save(operation);
			this.lstOperation.add(operation.getName());
		}
	}

	protected void onDelete() {

		String name = this.lstOperation.getSelection()[0];

		ShowMessageDialog dialog = new ShowMessageDialog(shell, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION,
				BaseMessages.getString(PKG, "PreviewRowsDialog.NoRows.Text"),
				BaseMessages.getString(PKG, "PreviewRowsDialog.NoRows.Message"));
		if (dialog.open() == SWT.OK) {
			OperationManager.getInstance().delete(name);
			this.lstOperation.remove(name);
		}
	}

	public void dispose() {
		WindowProperty winprop = new WindowProperty(shell);

		props.setScreen(winprop);
		shell.dispose();
	}

	protected void onOkPressed() {
		dispose();
	}

	/**
	 * Called when the user cancels the dialog. Subclasses may override if desired.
	 */
	protected void onCancelPressed() {
		// Close the SWT dialog window
		dispose();
	}

}
