package com.itdoes.common.extension.security;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.itdoes.common.core.security.Cryptos;
import com.itdoes.common.core.util.Codecs;

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
	private static final HelpFormatter FORMATTER = new HelpFormatter();
	private static final CommandLineParser PARSER = new DefaultParser();

	public static void main(String[] args) throws ParseException {
		final CommandLine cli = PARSER.parse(OPTIONS, args);

		if (cli.hasOption("h")) {
			showHelp();
		} else if (cli.hasOption("ak")) {
			System.out.println(Codecs.hexEncode(Cryptos.generateAesKey()));
		} else if (cli.hasOption("ae")) {
			System.out.println(Cryptos.aesEncryptDefault(cli.getOptionValue("ae")));
		} else if (cli.hasOption("ad")) {
			System.out.println(Cryptos.aesDecryptDefault(cli.getOptionValue("ad")));
		} else {
			showHelp();
		}
	}

	private static void showHelp() {
		FORMATTER.printHelp(SecurityCli.class.getSimpleName(), OPTIONS);
	}
}
