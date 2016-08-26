package com.itdoes.common.core.util;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class IdsTest {
	@Test
	public void ids() {
		Ids.uuid();
		Ids.uuidWithoutHyphen();
		Ids.randomLongAll();
		Ids.randomLongAbs();
		Ids.randomBase62(8);
	}
}
