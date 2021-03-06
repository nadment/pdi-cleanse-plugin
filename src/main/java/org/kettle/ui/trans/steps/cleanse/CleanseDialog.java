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

package org.kettle.ui.trans.steps.cleanse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.kettle.trans.steps.cleanse.Cleanse;
import org.kettle.trans.steps.cleanse.CleanseMeta;
import org.kettle.trans.steps.cleanse.CleanseRuleManager;
import org.kettle.trans.steps.cleanse.OperationDefinition;
import org.kettle.trans.steps.cleanse.OperationManager;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.PluginDialog;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.core.FormDataBuilder;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.ColumnsResizer;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.ui.trans.step.TableItemInsertListener;

@PluginDialog(id = "Cleanse", image = "cleanse.svg", pluginType = PluginDialog.PluginType.STEP, documentationUrl = "https://github.com/nadment/pdi-cleanse-plugin/wiki")
public class CleanseDialog extends AbstractStepDialog<CleanseMeta> {

	private static final Class<?> PKG = CleanseMeta.class; // for i18n purposes

	private TableView tblFields;

	/**
	 * Constructor that saves incoming meta object to a local variable, so it can
	 * conveniently read and write settings from/to it.
	 *
	 * @param parent    the SWT shell to open the dialog in
	 * @param in        the meta object holding the step's settings
	 * @param transMeta transformation description
	 * @param sName     the step name
	 */
	public CleanseDialog(Shell parent, Object in, TransMeta transMeta, String sName) {
		super(parent, in, transMeta, sName);

		setText(BaseMessages.getString(PKG, "CleanseDialog.Shell.Title"));
	}

	@Override
	protected void loadMeta(final CleanseMeta meta) {

		List<Cleanse> cleanses = meta.getCleanses();
		for (int i = 0; i < cleanses.size(); i++) {

			Cleanse cleanse = cleanses.get(i);
			TableItem item = tblFields.getTable().getItem(i);
			item.setText(1, StringUtils.stripToEmpty(cleanse.getInputField()));
			item.setText(2, StringUtils.stripToEmpty(cleanse.getName()));

			String id = cleanse.getRule();
			if (id.charAt(0) == '@') {
				item.setText(3, Const.nullToEmpty(id));
			} else {
				PluginInterface rule = CleanseRuleManager.getInstance().getRuleById(id);
				if (rule != null) {
					item.setText(3, Const.nullToEmpty(rule.getName()));
				} else {
					// Rule not found
					item.setText(3, "!" + cleanse.getRule() + "!");
				}
			}
		}

		tblFields.setRowNums();
		tblFields.optWidth(true);
	}

	@Override
	public Point getMinimumSize() {
		return new Point(260, 500);
	}

	@Override
	protected void saveMeta(final CleanseMeta meta) {

		// save step name
		stepname = wStepname.getText();

		List<Cleanse> cleanses = new ArrayList<>();
		for (int i = 0; i < tblFields.nrNonEmpty(); i++) {
			TableItem item = tblFields.getNonEmpty(i);

			Cleanse cleanse = new Cleanse();
			cleanse.setInputField(StringUtils.stripToNull(item.getText(1)));
			cleanse.setName(StringUtils.stripToNull(item.getText(2)));

			String name = StringUtils.stripToNull(item.getText(3));
			if (name != null) {
				if (name.charAt(0) == '@') {
					// Ignore operation
				} else
					cleanse.setRule(CleanseRuleManager.getInstance().getRuleByName(name).getIds()[0]);
			}

			cleanses.add(cleanse);
		}
		meta.setCleanses(cleanses);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		Label wlFields = new Label(parent, SWT.NONE);
		wlFields.setText(BaseMessages.getString(PKG, "CleanseDialog.Fields.Label"));
		wlFields.setLayoutData(new FormDataBuilder().top().fullWidth().result());
		props.setLook(wlFields);

		// Widget Get fields
		wGet = new Button(parent, SWT.PUSH);
		wGet.setText(BaseMessages.getString(PKG, "System.Button.GetFields"));
		wGet.setLayoutData(new FormDataBuilder().top(wlFields, Const.MARGIN).right().result());
		wGet.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				onGetField();
			}
		});

//		Button wEditOperation = new Button(parent, SWT.PUSH);
//		wEditOperation.setText(BaseMessages.getString(PKG, "Edit operations"));
//		wEditOperation.setLayoutData(new FormDataBuilder().top(wGet, Const.MARGIN).right().result());
//		wEditOperation.addListener(SWT.Selection, new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				onEditOperation();
//			}
//		});

		ColumnInfo[] columns = new ColumnInfo[] {
				new ColumnInfo(BaseMessages.getString(PKG, "CleanseDialog.ColumnInfo.FieldInName"),
						ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" }, false),
				new ColumnInfo(BaseMessages.getString(PKG, "CleanseDialog.ColumnInfo.FieldOutName"),
						ColumnInfo.COLUMN_TYPE_TEXT, new String[] { "" }, false),
				new ColumnInfo(BaseMessages.getString(PKG, "CleanseDialog.ColumnInfo.Rule"),

//						ColumnInfo.COLUMN_TYPE_CCOMBO, CleanseRuleManager.getInstance().getRuleNames()) };
						ColumnInfo.COLUMN_TYPE_CCOMBO, this.getOperation()) };

		columns[1].setToolTip(BaseMessages.getString(PKG, "CleanseDialog.ColumnInfo.OutputField.Tooltip"));
		columns[1].setUsingVariables(true);

		tblFields = new TableView(transMeta, parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, columns,
				this.getStepMeta().getCleanses().size(), lsMod, props);

		tblFields.setLayoutData(
				new FormDataBuilder().top(wlFields, Const.MARGIN).bottom().left().right(wGet, -Const.MARGIN).result());

		tblFields.getTable().addListener(SWT.Resize, new ColumnsResizer(4, 30, 30, 56));

		// Search the fields in the background
		new Thread(() -> {
			try {
				StepMeta stepMeta = transMeta.findStep(stepname);
				if (stepMeta != null) {
					RowMetaInterface row = transMeta.getPrevStepFields(stepMeta);
					String[] fieldNames = new String[row.size()];
					for (int i = 0; i < row.size(); i++) {
						fieldNames[i] = row.getValueMeta(i).getName();
					}
					columns[0].setComboValues(fieldNames);
				}
			} catch (KettleException e) {
				logError(BaseMessages.getString(PKG, "CleanseDialog.Log.UnableToFindInput"));
			}
		}).start();

		return parent;
	}

	protected String[] getOperation() {

		List<String> names = new ArrayList<String>();

		for (PluginInterface plugin : CleanseRuleManager.getInstance().getRules()) {
			names.add(plugin.getName());
		}

		for (OperationDefinition definition : OperationManager.getInstance().getOperations()) {
			names.add('@' + definition.getName());
		}

		return names.stream().sorted().toArray(String[]::new);
	}

	protected void onGetField() {
		try {
			RowMetaInterface row = transMeta.getPrevStepFields(stepname);
			if (row != null) {
				TableItemInsertListener listener = new TableItemInsertListener() {
					public boolean tableItemInserted(TableItem tableItem, ValueMetaInterface v) {
						if (v.getType() == ValueMetaInterface.TYPE_STRING) {
							// Only process strings
							return true;
						} else {
							return false;
						}
					}
				};

				BaseStepDialog.getFieldsFromPrevious(row, tblFields, 1, new int[] { 1 }, new int[] {}, -1, -1,
						listener);
			}
		} catch (KettleException ke) {
			new ErrorDialog(shell, BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Title"),
					BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Message"), ke);
		}

	}

	protected void onEditOperation() {
		OperationSelectionDialog dialog = new OperationSelectionDialog(shell, SWT.OPEN);
		dialog.open();
	}
}