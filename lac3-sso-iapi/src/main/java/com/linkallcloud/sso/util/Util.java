package com.linkallcloud.sso.util;

import java.security.SecureRandom;

import com.linkallcloud.core.lang.Lang;

/**
 * Some static utility methods.
 */
public class Util {

	public static final String INNER_TICKET_ID_FREFIX = "INTID-";

	private static int TRANSACTION_ID_LENGTH = 32;

	/** Returns a printable String corresponding to a byte array. */
	public static synchronized String toPrintable(byte[] b) {
		final char[] alphabet = ("abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890")
				.toCharArray();
		char[] out = new char[b.length];
		for (int i = 0; i < b.length; i++) {
			int index = b[i] % alphabet.length;
			if (index < 0)
				index += alphabet.length;
			out[i] = alphabet[index];
		}
		return new String(out);
	}

	public static String getTransactionId() {
		// produce the random transaction ID
		byte[] b = new byte[TRANSACTION_ID_LENGTH];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(b);
		return Util.toPrintable(b);
	}

	public static String getInnerTicketId(String ticketId) {
		if (!ticketId.startsWith(Util.INNER_TICKET_ID_FREFIX)) {
			ticketId = Util.INNER_TICKET_ID_FREFIX + Lang.md5(ticketId);
		}
		return ticketId;
	}
}
