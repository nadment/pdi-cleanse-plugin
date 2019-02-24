package org.kettle.trans.steps.cleanse;

import org.junit.Before;
import org.junit.Test;
import org.kettle.trans.steps.cleanse.CleanseMeta;
import org.pentaho.di.core.injection.BaseMetadataInjectionTest;

public class CleanseMetaInjectionTest extends BaseMetadataInjectionTest<CleanseMeta> {

	@Before
	public void setup() {
		setup(new CleanseMeta());
	}

	@Test
	public void test() throws Exception {

		check("NAME", new StringGetter() {
			@Override
			public String get() {
				return meta.getCleanses().get(0).getInputField();
			}
		});
	}
}
