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

import java.util.Arrays;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * The Cleanse Transformation step apply rules on fields value.
 * 
 * @author Nicolas ADMENT
 * @since 18-mai-2018
 */

public class CleanseStep extends BaseStep implements StepInterface {

	private static Class<?> PKG = CleanseMeta.class;

	public CleanseStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
			Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	@Override
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		// Casting to step-specific implementation classes is safe
		CleanseMeta meta = (CleanseMeta) smi;
		CleanseData data = (CleanseData) sdi;

	    if ( super.init( smi, sdi ) ) {
	    	first = true;
	    	
	        return true;
	    }
	   
		return false;
	}

	@Override
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		// safely cast the step settings (meta) and runtime info (data) to
		// specific implementations
		CleanseMeta meta = (CleanseMeta) smi;
		CleanseData data = (CleanseData) sdi;

		// get incoming row, getRow() potentially blocks waiting for more rows,
		// returns null if no more rows expected
		Object[] row = getRow();

		// if no more rows are expected, indicate step is finished and
		// processRow() should not be called again
		if (row == null) {
			setOutputDone();
			return false;
		}

		// the "first" flag is inherited from the base step implementation
		// it is used to guard some processing tasks, like figuring out field
		// indexes
		// in the row structure that only need to be done once
		if (first) {
			if (log.isDebug()) {
				logDebug(BaseMessages.getString(PKG, "Cleanse.Log.StartedProcessing"));
			}

			first = false;
			// clone the input row structure and place it in our data object
			data.outputRowMeta = getInputRowMeta().clone();

			// use meta.getFields() to change it, so it reflects the output row
			// structure
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this, null, null);

			// check rule processor
			for (Cleanse cleanse : meta.getCleanses()) {

				CleanseProcessor processor = CleanseRuleManager.getInstance().createProcessor(cleanse.getRule());
				if (processor == null) {
					throw new KettleStepException(BaseMessages.getString(PKG, "Cleanse.Log.UnknownRule",
							cleanse.getInputField(), cleanse.getRule()));
				}

				data.processors.put(cleanse, processor);
			}
		}

		// copies row into outputRowValues and pads extra null-default slots for
		// the output values
		Object[] outputRowValues = Arrays.copyOf(row, data.outputRowMeta.size());

		RowMetaInterface inputRowMeta = getInputRowMeta();

		// apply rules by order
		for (Cleanse cleanse : meta.getCleanses()) {

			int index = data.outputRowMeta.indexOfValue(cleanse.getInputField());
			ValueMetaInterface valueMeta = null;
			try {
				// Get value from output in case we apply multi rule on same
				// field
				Object value = outputRowValues[index];

				// Output field is different
				if (!Utils.isEmpty(cleanse.getName())) {
					index = data.outputRowMeta.indexOfValue(cleanse.getName());
				}
				valueMeta = data.outputRowMeta.getValueMeta(index);
				
				Object result = value;
				if (value != null) {
					result = data.processors.get(cleanse).processValue(valueMeta, value);
				}
				outputRowValues[index] = valueMeta.convertData(valueMeta, result);
			} catch (KettleValueException e) {
				logError(BaseMessages.getString(PKG, "Cleanse.Log.DataIncompatibleError", String.valueOf(row[index]),
						String.valueOf(inputRowMeta.getValueMeta(index)), valueMeta));
				throw e;
			}
		}

		// put the row to the output row stream
		putRow(data.outputRowMeta, outputRowValues);

		if (log.isRowLevel()) {
			logRowlevel(BaseMessages.getString(PKG, "Cleanse.Log.WroteRowToNextStep", outputRowValues));
		}

		// log progress if it is time to to so
		if (checkFeedback(getLinesRead())) {
			logBasic("Line nr " + getLinesRead()); // Some basic logging
		}

		// indicate that processRow() should be called again
		return true;
	}

	@Override
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {

		// Casting to step-specific implementation classes is safe
		CleanseMeta meta = (CleanseMeta) smi;
		CleanseData data = (CleanseData) sdi;

		data.outputRowMeta = null;
		data.processors = null;

		super.dispose(meta, data);
	}
}