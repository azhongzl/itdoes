package com.itdoes.common.test.spring;

/**
 * @author Jalen Zhong
 */
public class Profiles {
	public static final String ACTIVE_PROFILE_KEY = "spring.profiles.active";
	public static final String DEFAULT_PROFILE_KEY = "spring.profiles.default";

	public static final String PRODUCTION = "production";
	public static final String DEVELOPMENT = "development";
	public static final String UNIT_TEST = "unit-test";
	public static final String FUNCTIONAL_TEST = "functional-test";

	public static void activeProfile(String profile) {
		System.setProperty(ACTIVE_PROFILE_KEY, profile);
	}

	private Profiles() {
	}
}
