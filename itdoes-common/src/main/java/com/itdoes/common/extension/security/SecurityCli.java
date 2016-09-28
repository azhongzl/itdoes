package com.itdoes.common.extension.security;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.itdoes.common.core.Constants;
import com.itdoes.common.core.security.Cryptos;
import com.itdoes.common.core.security.Digests;
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
		OPTIONS.addOption("sha1", "digest-sha1", true, "Digest SHA1");
		OPTIONS.addOption("sha256", "digest-sha256", true, "Digest SHA256");
		OPTIONS.addOption("md5", "digest-md5", true, "Digest MD5");
		OPTIONS.addOption("sa_gen", "salt-generate", false, "Digest with auto-generating salt");
		OPTIONS.addOption("sa_txt", "salt-text", true, "Digest with given text salt");
		OPTIONS.addOption("sa_hex", "salt-hex", true, "Digest with given hex salt");
		OPTIONS.addOption("it", "iterations", true, "Digest iterations");
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
		} else if (cli.hasOption("sha1")) {
			digest(cli, Digests.SHA1, cli.getOptionValue("sha1"));
		} else if (cli.hasOption("sha256")) {
			digest(cli, Digests.SHA256, cli.getOptionValue("sha256"));
		} else if (cli.hasOption("md5")) {
			digest(cli, Digests.MD5, cli.getOptionValue("md5"));
		} else {
			showHelp();
		}
	}

	private static void digest(CommandLine cli, String algorithm, String data) {
		final byte[] salt;
		if (cli.hasOption("sa_gen")) {
			salt = Digests.generateSalt();
		} else if (cli.hasOption("sa_txt")) {
			salt = cli.getOptionValue("sa_txt").getBytes(Constants.UTF8_CHARSET);
		} else if (cli.hasOption("sa_hex")) {
			salt = Codecs.hexDecode(cli.getOptionValue("sa_hex"));
		} else {
			salt = null;
		}

		final int iterations;
		if (cli.hasOption("it")) {
			iterations = Integer.parseInt(cli.getOptionValue("it"));
		} else {
			iterations = 1;
		}

		final String hexDigest = Codecs.hexEncode(Digests.digest(algorithm, data, salt, iterations));
		final String hexSalt = salt != null ? Codecs.hexEncode(salt) : "";
		System.out.println("Hex Digest: [" + hexDigest + "], Hex Salt: [" + hexSalt + "]");
	}

	private static void showHelp() {
		FORMATTER.printHelp(SecurityCli.class.getSimpleName(), OPTIONS);
	}
}
