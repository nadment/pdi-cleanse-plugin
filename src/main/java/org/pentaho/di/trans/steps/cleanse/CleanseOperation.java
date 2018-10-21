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

import java.util.ArrayList;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

public class CleanseOperation extends ArrayList<CleanseProcessor> implements CleanseProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public Object processValue(final ValueMetaInterface valueMeta, Object object) throws KettleValueException {

		for (CleanseProcessor processor : this) {
			object = processor.processValue(valueMeta, object);
		}

		return object;
	}

}
