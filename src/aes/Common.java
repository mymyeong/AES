package aes;

import java.io.UnsupportedEncodingException;

public class Common {
	static final int GF = 0x11B;

	public static int getIntLength(int a) {
		return Integer.toBinaryString(a).length();
	}

	public static int getInverseNumber(int input) {
		if (input == 0x00) {
			return 00;
		}
		int a1 = 0x1, a2 = 0x0, a = GF;
		int b1 = 0x0, b2 = 0x1, b = input;

		while (b > 0x1) {
			int oldb1 = b1;
			int oldb2 = b2;
			int oldb = b;
			int q = getQuotient(a, b);

			b1 = a1 ^ multiply(q, b1);
			b2 = a2 ^ multiply(q, b2);

			a1 = oldb1;
			a2 = oldb2;

			b = getRemainder(a, b);
			a = oldb;
		}

		return b2;
	}

	public static int multiply(int multiplicand, int multiplier) {
		String d = Integer.toBinaryString(multiplicand);
		String r = Integer.toBinaryString(multiplier);

		int t = 0x0;
		for (int i = 0; i < d.length(); i++) {
			for (int j = 0; j < r.length(); j++) {
				if (d.charAt(i) == '1' && r.charAt(j) == '1') {
					int temp = 1 << d.length() - 1 - i;
					t ^= temp << r.length() - 1 - j;
				}
			}
		}
		if (Common.getIntLength(t) >= 9) {
			return getRemainder(t, GF);
		} else {
			return t;
		}
	}

	public static int getQuotient(int dividend, int divisor) {
		return euclidean(dividend, divisor)[1];
	}

	public static int getRemainder(int dividend, int divisor) {
		return euclidean(dividend, divisor)[0];
	}

	public static int[] euclidean(int dividend, int divisor) {
		int model[] = new int[2];
		String d = Integer.toBinaryString(dividend);
		String s = Integer.toBinaryString(divisor);
		int a = (d.length() - s.length());
		int b = divisor << a;
		int remainder = dividend ^ b;
		int quotient = 1 << a;
		while (getIntLength(remainder) >= getIntLength(divisor)) {
			d = Integer.toBinaryString(remainder);
			a = d.length() - s.length();
			b = divisor << a;
			remainder = remainder ^ b;
			if (a != 0) {
				quotient |= (1 << a);
			} else {
				quotient |= 1;
			}
		}
		model[0] = remainder;
		model[1] = quotient;

		return model;
	}

	public static int[][] multiplx(int a[][], int b[][]) {
		int[][] data = new int[4][4];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				data[j][i] = multiply(a[0][i], b[j][0]) ^ multiply(a[1][i], b[j][1])
						^ multiply(a[2][i], b[j][2]) ^ multiply(a[3][i], b[j][3]);
			}
		}
		return data;
	}

	public static void printGF(String binaryString) {
		boolean plus = false;
		for (int i = 0; i < binaryString.length(); i++) {
			if (binaryString.charAt(i) == '1') {
				if (plus) {
					System.out.print("+");
				}
				if (i != binaryString.length() - 1) {
					System.out.print("x^" + (binaryString.length() - i - 1));
				} else {
					System.out.print("1");
				}
				plus = true;
			}
		}
	}

	public static void printGF(int hex) {
		if (hex == 0) {
			System.out.print("0");
		}
		printGF(Integer.toBinaryString(hex));
	}

	public static void printbox(int box[][]) {
		for (int x = 0; x < box.length; x++) {
			System.out.printf("   %X", x);
		}
		System.out.println();
		for (int y = 0; y < box.length; y++) {
			System.out.printf("%X", y);
			for (int x = 0; x < box[y].length; x++) {
				System.out.printf("  %02X", box[y][x]);
			}
			System.out.println();
		}
	}

	public static void printKey(int key[][]) {
		for (int i = 0; i < key.length; i++) {
			for (int j = 0; j < key[i].length; j++) {
				System.out.printf("%02X\t", key[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	public static byte[] toByte(String str) {
		byte b[] = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static String hexByteToString(byte[] b) {
		String str = "";
		try {
			str = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString().toUpperCase();
	}

	public static String hexArrayToString(int data[][]) {
		String str = new String();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				String t = Integer.toHexString(data[i][j]);
				if (t.length() < 2) {
					t = "0" + t;
				}
				str += t;
			}
		}
		return str.toUpperCase();
	}

	public static int[][][] stringToHexArray(String str) {
		int index = str.length() / 32, k = 0;
		int arr[][][] = new int[index][4][4];
		for (int x = 0; x < index; x++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (k >= str.length()) {
						break;
					}
					String s = "";
					if (k + 2 > str.length()) {
						s = str.substring(k, k + 1);
					} else {
						s = str.substring(k, k + 2);
					}
					arr[x][i][j] = Integer.valueOf(s, 16);
					k += 2;
				}
			}
		}
		return arr;
	}
}
