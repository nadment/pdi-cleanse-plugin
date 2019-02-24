package org.kettle.ui.trans.steps.cleanse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.kettle.trans.steps.cleanse.CleanseMeta;
import org.kettle.trans.steps.cleanse.CleanseRuleManager;
import org.kettle.trans.steps.cleanse.OperationDefinition;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.ui.core.ConstUI;
import org.pentaho.di.ui.core.FormDataBuilder;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.gui.GUIResource;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.ColumnsResizer;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * This dialogs allows you to edit operation definition.
 *
 * @author Nicolas ADMENT
 * @since 19-12-2018
 */
public class OperationEditorDialog extends Dialog {
	private static final Class<?> PKG = CleanseMeta.class;

	public static final int LARGE_MARGIN = 15;

	private PropsUI props;
	private Shell shell;
	private TableView tblRules;
	private Text txtName;
	private Text txtDescription;

	private ModifyListener lsMod;

	private int result;

	private OperationDefinition operation;

	public OperationEditorDialog(Shell parent, OperationDefinition operation) {
		super(parent, SWT.OPEN);

		this.props = PropsUI.getInstance();
		this.operation = operation;
	}

	public int open() {

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
		props.setLook(shell);
		shell.setImage(GUIResource.getInstance().getImageSpoon());
		shell.setText(BaseMessages.getString(PKG, "OperationEditorDialog.Shell.Title"));
		shell.setLayout(new FormLayout());

		// The ModifyListener used on all controls. It will update the meta object to
		// indicate that changes are being made.
		lsMod = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {

			}
		};

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

		Label lblName = new Label(content, SWT.NONE);
		lblName.setText(BaseMessages.getString(PKG, "OperationEditorDialog.Name.Label"));
		lblName.setLayoutData(new FormDataBuilder().top().left().result());
		props.setLook(lblName);

		txtName = new Text(content, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		txtName.setLayoutData(new FormDataBuilder().top(lblName).left().right().result());
		txtName.addModifyListener(lsMod);
		// txtName.addSelectionListener(lsDef);
		props.setLook(txtName);

		Label lblDescription = new Label(content, SWT.NONE);
		lblDescription.setText(BaseMessages.getString(PKG, "OperationEditorDialog.Description.Label"));
		lblDescription.setLayoutData(new FormDataBuilder().top(txtName).left().right().result());
		props.setLook(lblDescription);

		txtDescription = new Text(content, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		txtDescription.setLayoutData(new FormDataBuilder().top(lblDescription).left().right().result());
		txtDescription.addModifyListener(lsMod);
		// txtName.addSelectionListener(lsDef);
		props.setLook(txtDescription);

		Label lblListTarget = new Label(content, SWT.NONE);
		lblListTarget.setText(BaseMessages.getString(PKG, "OperationEditorDialog.Rules.Label"));
		lblListTarget.setLayoutData(new FormDataBuilder().top(txtDescription).left().result());
		props.setLook(lblListTarget);

		
		List<String> rules = operation.getRules();
		ColumnInfo[] columns = new ColumnInfo[1];

		columns[0] = new ColumnInfo(BaseMessages.getString(PKG, "OperationEditorDialog.ColumnInfo.Rule"), //$NON-NLS-1$
				ColumnInfo.COLUMN_TYPE_CCOMBO, CleanseRuleManager.getInstance().getRuleNames(), false);
		this.tblRules = new TableView(null, content, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, columns, rules.size(), lsMod,
				props);

		this.tblRules.setLayoutData(new FormDataBuilder().top(lblListTarget).left().right().bottom().result());
		this.tblRules.getTable().addListener(SWT.Resize, new ColumnsResizer(5, 94));

		this.txtName.setText(StringUtils.stripToEmpty(operation.getName()));
		this.txtDescription.setText(StringUtils.stripToEmpty(operation.getDescription()));

	
		for (int i = 0; i < rules.size(); i++) {

			String ruleId = rules.get(i);
			TableItem item = tblRules.getTable().getItem(i);

			PluginInterface rule = CleanseRuleManager.getInstance().getRuleById(ruleId);
			if (rule != null) {
				item.setText(1, Const.nullToEmpty(rule.getName()));
			} else {
				// Rule not found
				item.setText(1, "!" + ruleId + "!");
			}
		}

		tblRules.setRowNums();
		//tblRules.optWidth(true);

		// *******************************************************************
		// THE BOTTOM BUTTONS...
		// *******************************************************************

		Button btnCancel = new Button(bottom, SWT.PUSH);
		btnCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));
		btnCancel.setLayoutData(new FormDataBuilder().bottom().right().result());
		btnCancel.addListener(SWT.Selection, Event -> onCancelPressed());

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

		return result;
	}

	public void dispose() {
		WindowProperty winprop = new WindowProperty(shell);

		props.setScreen(winprop);
		shell.dispose();
	}

	protected void onOkPressed() {

		operation.setName(txtName.getText());
		operation.setDescription(txtDescription.getText());

		List<String> rules = new ArrayList<>();
		for (int i = 0; i < tblRules.nrNonEmpty(); i++) {
			TableItem item = tblRules.getNonEmpty(i);
			String ruleName = StringUtils.stripToNull(item.getText(1));
			if (ruleName != null) {
				String ruleId = CleanseRuleManager.getInstance().getRuleByName(ruleName).getIds()[0];
				rules.add(ruleId);
			}
		}
		operation.setRules(rules);

		result = SWT.OK;

		// Close the SWT dialog window
		dispose();
	}

	/**
	 * Called when the user cancels the dialog. Subclasses may override if desired.
	 */
	protected void onCancelPressed() {

		result = SWT.CANCEL;
		// Close the SWT dialog window
		dispose();
	}
}
