package com.itdoes.common.security;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.itdoes.common.util.Codecs;

/**
 * @author Jalen Zhong
 */
public class SecurityCli {
	private static final Options OPTIONS = new Options();
	static {
		OPTIONS.addOption("h", "help", false, "Show help");
		OPTIONS.addOption("ae", "aes-encrypt", true, "Encrypt using AES");
		OPTIONS.addOption("ad", "aes-decrypt", true, "Decrypt using AES");
		OPTIONS.addOption("ak", "aes-key", false, "Generate AES key");
	}

	public static void main(String[] args) throws ParseException {
		final CommandLineParser parser = new DefaultParser();
		final CommandLine cli = parser.parse(OPTIONS, args);

		if (cli.hasOption("h")) {
			showHelp();
		} else if (cli.hasOption("ak")) {
			System.out.println(Codecs.hexEncode(Cryptos.generateAesKey()));
		} else if (cli.hasOption("ae")) {
			final String result = Codecs
					.hexEncode(Cryptos.aesEncrypt(cli.getOptionValue("ae").getBytes(), Cryptos.DEFAULT_AES_KEY));
			System.out.println(result);
		} else if (cli.hasOption("ad")) {
			final String result = Cryptos.aesDecrypt(Codecs.hexDecode(cli.getOptionValue("ad")),
					Cryptos.DEFAULT_AES_KEY);
			System.out.println(result);
		} else {
			showHelp();
		}
	}

	private static void showHelp() {
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("security", OPTIONS);
	}
}
