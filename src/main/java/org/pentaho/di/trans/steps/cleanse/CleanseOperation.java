package org.pentaho.di.trans.steps.cleanse;

import java.util.ArrayList;

import org.pentaho.di.core.exception.KettleValueException;

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
	public Object processValue(Object object) throws KettleValueException {
		
		for( CleanseProcessor processor: this) {
			object = processor.processValue(object);		
		}
		
		return object;
	}
		
}
