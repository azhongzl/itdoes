package com.itdoes.common.test.spring;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author Jalen Zhong
 */
@ActiveProfiles(Profiles.UNIT_TEST)
public abstract class SpringTestCase extends AbstractJUnit4SpringContextTests {

}
