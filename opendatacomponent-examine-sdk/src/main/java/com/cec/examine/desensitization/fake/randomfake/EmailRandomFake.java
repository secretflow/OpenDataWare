package com.cec.examine.desensitization.fake.randomfake;
import com.cec.examine.desensitization.SensitivePatternRegex;

import java.util.Random;
import java.util.regex.Matcher;

public class EmailRandomFake extends SensitivePatternRegex {

	private char maskTag;
	private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final Random RANDOM = new Random();

	public EmailRandomFake() {
		this('*');

	}

	public EmailRandomFake(char maskTag) {

		this.maskTag = maskTag;
	}
	
	@Override
	public String pattern() {
		String pattern = "(([A-Za-z0-9]{1,20}[-_\\.]){0,5}[a-zA-Z0-9\\.-]{1,20})@(([a-zA-Z0-9_-]{1,20})(\\.[a-zA-Z0-9_-]{1,20}){1,3})";
		return pattern;
	}
//	@Override
//	public String target(Matcher m) {
//
//		String pre = m.group(1);
//
//		int len = pre.length();
//
//		String md5 = MD5Util.textToMD5L32(pre);
//
//		// TODO
//		String base = md5 + md5 + md5 + md5 + md5;
//
//		String newPre = base.substring(0, len);
//
//		String newResult = newPre + "@" + m.group(3);
//
//		return newResult;
//
//	}

	@Override
	public String target(String source) {
		String [] s = source.split("@",-1);
		String pre = s[0];
		String hashEmailPrefix = hashEmailPrefix(pre);
		return hashEmailPrefix+"@"+generateRandomEmail();
	}

	private static String generateRandomEmail() {
		Random random = new Random();
		String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com","qq.com","126.com","163.com","sina.com","foxmail.com","sohu.com","aliyun.com"};
//		String[] names = {"john", "jane", "smith", "emma", "david", "sarah"};

		String domain = domains[random.nextInt(domains.length)];
//		String name = names[random.nextInt(names.length)];
//		int number = random.nextInt(1000);

//		return name + number + "@" + domain;
		return domain;
	}



	public static String hashEmailPrefix(String email) {
		String prefix = extractEmailPrefix(email);
		String hashedPrefix = hashPrefix(prefix);
		return hashedPrefix;
	}

	private static String extractEmailPrefix(String email) {
		int atIndex = email.indexOf("@");
		if (atIndex != -1) {
			return email.substring(0, atIndex);
		}
		return email;
	}

	private static String hashPrefix(String prefix) {
		StringBuilder hashedPrefix = new StringBuilder();

		for (char c : prefix.toCharArray()) {
			if (Character.isLetter(c)) {
				char randomLetter = getRandomLetter();
				hashedPrefix.append(randomLetter);
			} else if (Character.isDigit(c)) {
				int randomDigit = getRandomDigit();
				hashedPrefix.append(randomDigit);
			} else {
				hashedPrefix.append(c);
			}
		}

		return hashedPrefix.toString();
	}

	private static char getRandomLetter() {
		return ALPHABET[RANDOM.nextInt(ALPHABET.length)];
	}

	private static int getRandomDigit() {
		return RANDOM.nextInt(10);
	}

}
