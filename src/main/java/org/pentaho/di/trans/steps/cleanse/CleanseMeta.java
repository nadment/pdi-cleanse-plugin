/******************************************************************************
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
import java.util.List;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.injection.InjectionDeep;
import org.pentaho.di.core.injection.InjectionSupported;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

/**
 * Lets you to cleanse fields.
 * 
 * @author Nicolas ADMENT
 *
 */
@SuppressWarnings("unused")
@Step(id = "Cleanse", image = "cleanse.svg", i18nPackageName = "org.pentaho.di.trans.steps.cleanse", name = "Cleanse.Name", description = "Cleanse.Description", categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.DataQuality", documentationUrl = "https://github.com/nadment/pdi-cleanse-plugin/wiki")
@InjectionSupported(localizationPrefix = "CleanseMeta.Injection.", groups = { "FIELDS" })
public class CleanseMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = CleanseMeta.class; // for i18n purposes

	/**
	 * Constants:
	 */

	private static final String TAG_INPUT_FIELD = "input_field"; //$NON-NLS-1$

	private static final String TAG_NAME = "name"; //$NON-NLS-1$

	private static final String TAG_DESCRIPTION = "description"; //$NON-NLS-1$

	private static final String TAG_RULE = "rule"; //$NON-NLS-1$

	/** The fields to cleanse */
	@InjectionDeep
	private List<Cleanse> cleanses;

	/** The operation to use */
	@InjectionDeep
	private CleanseOperation[] operations;

	public CleanseMeta() {
		super();
	}

	/**
	 * Called by PDI to get a new instance of the step implementation. A
	 * standard implementation passing the arguments to the constructor of the
	 * step class is recommended.
	 *
	 * @param stepMeta
	 *            description of the step
	 * @param stepDataInterface
	 *            instance of a step data class
	 * @param cnr
	 *            copy number
	 * @param transMeta
	 *            description of the transformation
	 * @param disp
	 *            runtime implementation of the transformation
	 * @return the new instance of a step implementation
	 */
	@Override
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new CleanseStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	/**
	 * Called by PDI to get a new instance of the step data class.
	 */
	@Override
	public StepDataInterface getStepData() {
		return new CleanseData();
	}

	/**
	 * This method is called every time a new step is created and should
	 * allocate/set the step configuration to sensible defaults. The values set
	 * here will be used by Spoon when a new step is created.
	 */
	@Override
	public void setDefault() {
		this.cleanses = new ArrayList<>();

		CleanseOperation o = new CleanseOperation();
		o.setName("Cleane name");
		o.setDescription("test desc");
		// o.add(new TrimRule());

	}

	@Override
	public Object clone() {
		CleanseMeta clone = (CleanseMeta) super.clone();

		// clone.cleanses = Arrays.copyOf(cleanses, cleanses.length);

		return clone;
	}

	@Override
	public String getXML() throws KettleValueException {

		StringBuilder xml = new StringBuilder(500);

		// xml.append(" <operations>").append(Const.CR);
		// for (CleanseOperation operation : this.getOperations()) {
		// xml.append(" <operation>").append(Const.CR);
		// xml.append(" ").append(XMLHandler.addTagValue(TAG_NAME,
		// operation.getName()));
		// xml.append(" ").append(XMLHandler.addTagValue(TAG_DESCRIPTION,
		// operation.getDescription()));
		// xml.append(" <rules>").append(Const.CR);
		// for (CleanseRuleInterface rule : operation ) {
		// xml.append(" ").append(XMLHandler.addTagValue(TAG_RULE,
		// rule.getId()));
		// }
		// xml.append(" </rules>").append(Const.CR);
		// xml.append(" </operation>").append(Const.CR);
		// }
		// xml.append(" </operations>").append(Const.CR);

		xml.append("<fields>").append(Const.CR);
		for (Cleanse cleanse : this.getCleanses()) {
			xml.append("<field>").append(Const.CR);
			xml.append(XMLHandler.addTagValue(TAG_INPUT_FIELD, cleanse.getInputField()));
			xml.append(XMLHandler.addTagValue(TAG_NAME, cleanse.getName()));
			xml.append(XMLHandler.addTagValue(TAG_RULE, cleanse.getRule()));
			xml.append("</field>").append(Const.CR);
		}
		xml.append("</fields>").append(Const.CR);

		return xml.toString();
	}

	@Override
	public void loadXML(Node stepNode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {

		try {
			Node fields = XMLHandler.getSubNode(stepNode, "fields");
			int count = XMLHandler.countNodes(fields, "field");

			cleanses = new ArrayList<>(count);
			for (int i = 0; i < count; i++) {
				Node line = XMLHandler.getSubNodeByNr(fields, "field", i);

				Cleanse cleanse = new Cleanse();
				cleanse.setInputField(Const.NVL(XMLHandler.getTagValue(line, TAG_INPUT_FIELD), ""));
				cleanse.setName(Const.NVL(XMLHandler.getTagValue(line, TAG_NAME), ""));
				cleanse.setRule(XMLHandler.getTagValue(line, TAG_RULE));
				cleanses.add(cleanse);
			}
		} catch (Exception e) {
			throw new KettleXMLException(
					BaseMessages.getString(PKG, "CleanseMeta.Exception.UnableToReadStepInfoFromXML"), e);
		}
	}

	@Override
	public void saveRep(Repository repository, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {
		try {
			// for (int i = 0; i < this.getOperations().length; i++) {
			// CleanseOperation operation = operations[i];
			// repository.saveStepAttribute(id_transformation, id_step, i,
			// TAG_NAME, operation.getName());
			// repository.saveStepAttribute(id_transformation, id_step, i,
			// TAG_RULE, operation.getDescription());
			// }

			for (int i = 0; i < this.cleanses.size(); i++) {
				Cleanse cleanse = cleanses.get(i);
				repository.saveStepAttribute(id_transformation, id_step, i, TAG_INPUT_FIELD, cleanse.getInputField());
				repository.saveStepAttribute(id_transformation, id_step, i, TAG_NAME, cleanse.getName());
				repository.saveStepAttribute(id_transformation, id_step, i, TAG_RULE, cleanse.getRule());
			}
		} catch (Exception e) {

			throw new KettleException(
					BaseMessages.getString(PKG, "CleanseMeta.Exception.UnableToSaveRepository", id_step), e);
		}
	}

	@Override
	public void readRep(Repository repository, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases)
			throws KettleException {
		try {

			int count = repository.countNrStepAttributes(id_step, TAG_INPUT_FIELD);
			cleanses = new ArrayList<>(count);
			for (int i = 0; i < this.cleanses.size(); i++) {
				Cleanse cleanse = new Cleanse();
				cleanse.setInputField(repository.getStepAttributeString(id_step, i, TAG_INPUT_FIELD));
				cleanse.setName(repository.getStepAttributeString(id_step, i, TAG_NAME));
				cleanse.setRule(repository.getStepAttributeString(id_step, i, TAG_RULE));
				cleanses.add(cleanse);
			}
		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "CleanseMeta.Exception.UnableToReadRepository", id_step), e);
		}
	}

	/**
	 * This method is called to determine the changes the step is making to the
	 * row-stream.
	 *
	 * @param inputRowMeta
	 *            the row structure coming in to the step
	 * @param name
	 *            the name of the step making the changes
	 * @param info
	 *            row structures of any info steps coming in
	 * @param nextStep
	 *            the description of a step this step is passing rows to
	 * @param space
	 *            the variable space for resolving variables
	 * @param repository
	 *            the repository instance optionally read from
	 * @param metaStore
	 *            the metaStore to optionally read from
	 */
	@Override
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
			VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {
		try {

			// store the input stream meta
			RowMetaInterface unalteredInputRowMeta = inputRowMeta.clone();

			// add the output fields if specified
			for (Cleanse cleanse : this.getCleanses()) {
				if (!Utils.isEmpty(cleanse.getName())) {

					// extracts the ValueMeta type of an input field
					int type = ValueMetaInterface.TYPE_NONE;
					int index = unalteredInputRowMeta.indexOfValue(cleanse.getInputField());
					if (index > 0) {
						type = unalteredInputRowMeta.getValueMeta(index).getType();
					}

					// create ValueMeta
					ValueMetaInterface vm = ValueMetaFactory.createValueMeta(cleanse.getName(), type);
					vm.setOrigin(name);
					inputRowMeta.addValueMeta(vm);
				}
			}
		} catch (Exception e) {
			throw new KettleStepException(e);
		}
	}

	/**
	 * extracts the ValueMeta type of an input field, returns null if the field
	 * is not present in the input stream
	 */
	private Integer getInputFieldValueType(RowMetaInterface inputRowMeta, Cleanse cleanse, int inputIndex) {
		int index = inputRowMeta.indexOfValue(cleanse.getInputField());
		if (index > 0) {
			return inputRowMeta.getValueMeta(index).getType();
		}
		return null;
	}

	/**
	 * This method is called when the user selects the "Verify Transformation"
	 * option in Spoon.
	 *
	 * @param remarks
	 *            the list of remarks to append to
	 * @param transMeta
	 *            the description of the transformation
	 * @param stepMeta
	 *            the description of the step
	 * @param prev
	 *            the structure of the incoming row-stream
	 * @param input
	 *            names of steps sending input to the step
	 * @param output
	 *            names of steps this step is sending output to
	 * @param info
	 *            fields coming in from info steps
	 * @param metaStore
	 *            metaStore to optionally read from
	 */
	@Override
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev,
			String input[], String output[], RowMetaInterface info, VariableSpace space, Repository repository,
			IMetaStore metaStore) {

		// See if we have fields from previous steps
		if (prev == null || prev.size() == 0) {
			remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING,
					BaseMessages.getString(PKG, "CleanseMeta.CheckResult.NotReceivingFieldsFromPreviousSteps"),
					stepMeta));
		} else {
			remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(PKG,
					"CleanseMeta.CheckResult.ReceivingFieldsFromPreviousSteps", prev.size()), stepMeta));
		}

		// See if there are input streams leading to this step!
		if (input.length > 0) {
			remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					BaseMessages.getString(PKG, "CleanseMeta.CheckResult.ReceivingInfoFromOtherSteps"), stepMeta));
		} else {
			remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG, "CleanseMeta.CheckResult.NotReceivingInfoFromOtherSteps"), stepMeta));
		}

		List<String> missingFields = new ArrayList<String>();
		for (Cleanse cleanse : this.getCleanses()) {

			// See if there are missing input streams
			ValueMetaInterface vmi = prev.searchValueMeta(cleanse.getInputField());
			if (vmi == null) {
				missingFields.add(cleanse.getInputField());
				String message = BaseMessages.getString(PKG, "CleanseMeta.CheckResult.MissingInStreamFields",
						cleanse.getInputField());
				remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, message, stepMeta));
			}

			// See if there rule plugin exist
			PluginInterface plugin = CleanseRuleManager.getInstance().getRuleById(cleanse.getRule());
			if (plugin == null) {
				String message = BaseMessages.getString(PKG, "CleanseMeta.CheckResult.UnknownRule",
						cleanse.getInputField(), cleanse.getRule());
				remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, message, stepMeta));
			}
		}

		// See if there something to cleanse
		if (this.getCleanses().isEmpty()) {
			remarks.add(new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING,
					BaseMessages.getString(PKG, "CleanseMeta.CheckResult.EmptyInStreamFields"), stepMeta));
		}

		// TODO: See if the output file name doesn't exist in input field

	}

	public List<Cleanse> getCleanses() {
		return this.cleanses;
	}

	public void setCleanses(final List<Cleanse> cleanses) {
		this.cleanses = cleanses;
	}

	public CleanseOperation[] getOperations() {
		return this.operations;
	}

	public void setOperations(final CleanseOperation[] operations) {
		this.operations = operations;
	}
}