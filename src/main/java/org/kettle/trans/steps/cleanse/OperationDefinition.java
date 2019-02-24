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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pentaho.di.core.changed.ChangedFlag;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.shared.SharedObjectInterface;
import org.pentaho.metastore.persist.MetaStoreAttribute;
import org.pentaho.metastore.persist.MetaStoreElementType;

@MetaStoreElementType(name = "Cleanse Operation", description = "Describes a operation rules to process")
public class OperationDefinition extends ChangedFlag implements Serializable, SharedObjectInterface {

	private static final long serialVersionUID = -6879930389319046230L;

	public static final String XML_TAG = "operation";

	// public static final RepositoryObjectType REPOSITORY_ELEMENT_TYPE =
	// RepositoryObjectType.CLUSTER_SCHEMA;

	@MetaStoreAttribute
	private String name;

	@MetaStoreAttribute
	private String description;

	private boolean shared;
	private Date changedDate;
	@MetaStoreAttribute
	private List<String> rules;

	public OperationDefinition() {
		this.rules = new ArrayList<>();
		
		this.changedDate = new Date();
	}

	public OperationDefinition(String name, String description, List<String> rules) {
		this.name = name;
		this.description = description;
		this.rules = rules;

		this.changedDate = new Date();
	}

	/**
	 * Gets name
	 *
	 * @return value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets description
	 *
	 * @return value of description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	@Override
	public void setShared(boolean shared) {
		this.shared = shared;
	}

	@Override
	public boolean isShared() {
		return shared;
	}

	@Override
	public String getXML() throws KettleException {
		StringBuilder xml = new StringBuilder( 500 );

	    xml.append( XMLHandler.openTag( XML_TAG ) );
	    xml.append( XMLHandler.addTagValue( "name", this.name ) );
	    xml.append( XMLHandler.addTagValue( "description", this.description ) );
	    xml.append( "        " ).append( XMLHandler.openTag( "rules" ) );
	    for (String rule: rules ) {
	      xml.append( XMLHandler.addTagValue( "rule", rule ) );
	    }
	    xml.append( XMLHandler.closeTag( "rules" ) );
	    xml.append( XMLHandler.closeTag( XML_TAG ) );
	    return xml.toString();
	}

	@Override
	public Date getChangedDate() {
		return this.changedDate;
	}

	/**
	 * @param changedDate the changedDate to set
	 */
	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return name.equals(((OperationDefinition) obj).name);
	}

	public int hashCode() {
		return name.hashCode();
	}
}
