package org.pentaho.di.trans.steps.cleanse;

import org.pentaho.di.core.exception.KettleValueException;

public interface CleanseProcessor {
	public Object processValue(final Object object) throws KettleValueException;
}
