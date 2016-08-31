package com.itdoes.common.core.test.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * @author Jalen Zhong
 */
@FixMethodOrder
public class OrderedRunner extends BlockJUnit4ClassRunner {
	// Only compute once
	private static List<FrameworkMethod> testMethodList;

	public OrderedRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		if (testMethodList != null) {
			return testMethodList;
		}

		testMethodList = new ArrayList<FrameworkMethod>(super.computeTestMethods());
		Collections.sort(testMethodList, new Comparator<FrameworkMethod>() {
			@Override
			public int compare(FrameworkMethod f1, FrameworkMethod f2) {
				final TestOrder o1 = f1.getAnnotation(TestOrder.class);
				final TestOrder o2 = f2.getAnnotation(TestOrder.class);

				if (o1 == null || o2 == null)
					return 0;

				return o1.value() - o2.value();
			}
		});
		return testMethodList;
	}
}
