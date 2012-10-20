/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Utils {
	private final static char[] digits = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};

	private final static String[] flags = { "0", "1" };

	public static String toBinaryFlag(int value) {
		return flags[value];
	}

	public static int getBinaryWidth(int width) {
		return width + ((width - 1) >> 2);
	}

	public static String toBinary(int value, int width) {
		int chars = getBinaryWidth(width);
		char[] buf = new char[chars];
		int pos = chars;

		for (int i = 0; i < width; i++) {
			if ((i != 0) && (i & 3) == 0)
				buf[--pos] = ' ';
            buf[--pos] = digits[value & 1];
            value >>= 1;
        }

		return new String(buf);
	}

	public static int getBitNo(int pos, int width) {
		pos = width - pos;

		if (pos % 5 == 0)
			return -1;

		return pos - (pos / 5) - 1;
	}

	public static int getHexWidth(int width) {
		return (width + 3) >> 2;
	}

	public static String toHex(int value, int width) {
		int chars = getHexWidth(width);
		char[] buf = new char[chars];

		while (chars > 0) {
			buf[--chars] = digits[value & 0xf];
			value >>= 4;
		}

		return new String(buf);
	}

}