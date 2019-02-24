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
package org.kettle.trans.steps.cleanse;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.metastore.api.exceptions.MetaStoreException;
import org.pentaho.metastore.persist.MetaStoreFactory;
import org.pentaho.metastore.util.PentahoDefaults;

/**
 * Singleton controlling cleanse rule plugin.
 * 
 * @author Nicolas ADMENT
 *
 */
public class OperationManager {

	private static final OperationManager INSTANCE = new OperationManager();
	
	private OperationManager() {		
	}

	public static OperationManager getInstance() {
		return INSTANCE;
	}

	
	protected MetaStoreFactory<OperationDefinition> getFactory() {
		return  new MetaStoreFactory<>(OperationDefinition.class, Spoon.getInstance().getMetaStore(),PentahoDefaults.NAMESPACE);
	}
	
	public String[] getOperationNames() throws KettleException {

		try {

			List<String> names = getFactory().getElementNames();
			
//			CleanseOperationDefinition operation;
//			if ( names.isEmpty() ) {
//				operation = new CleanseOperationDefinition("test","test description",Arrays.asList("LowerCase","SpaceRemoval"));
//				//operation.setName("test");
//				//operation.setDescription("test description");
//				factory.saveElement(operation);
//			} else {			
//				//factory.deleteElement(names.get(0));
//				operation = factory.loadElement(names.get(0));
//			}

			
			
			return names.stream().sorted().toArray(String[]::new);
		} catch (Exception e) {
			
			
			
			throw new KettleException("Unable to load operations from the metastore", e);
		}

	}

	
	public List<OperationDefinition> getOperations() {
		List<OperationDefinition> definitions = new  ArrayList<>();


		try {
			for ( String name:  getFactory().getElementNames() ) {
				OperationDefinition definition = load(name);
				
				definitions.add(definition);
			}
		} catch (MetaStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return definitions;
	}

	
	/**
	 * Find operation definition by name
	 * 
	 * @param name
	 *            The operation name
	 * @return the operation definition or null if not found
	 */
	public OperationDefinition load(final String name) {
		if (!Utils.isEmpty(name)) {

			try {
				return getFactory().loadElement(name);

			} catch (MetaStoreException e) {
				// throw new KettleException("Unable to load operation
				// '"+name+"' from the metastore", e);
			}

		}

		return null;
	}
	

	/**
	 * Delete operation definition by name
	 * 
	 * @param name
	 *            The operation name
	 * @return the operation definition or null if not found
	 */
	public void delete(final String name) {
		try {
			getFactory().deleteElement(name);
		} catch (MetaStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void save(final OperationDefinition operation) {	
		try {
			getFactory().saveElement(operation);
		} catch (MetaStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
