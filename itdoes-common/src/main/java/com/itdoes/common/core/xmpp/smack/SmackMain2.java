package com.itdoes.common.core.xmpp.smack;

/**
 * @author Jalen Zhong
 */
public class SmackMain2 {
	public static void main(String[] args) throws Exception {
		String username = "dhxlsfn";
		String password = "dhxlsfn";
		String serviceName = "0nl1ne.cc";
		String userJid = "jalen@0nl1ne.cc/Spark";

		SmackMain main = new SmackMain(serviceName, username, password, userJid);
		main.demo();
	}
}
