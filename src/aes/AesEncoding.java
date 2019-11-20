package aes;

import java.io.UnsupportedEncodingException;

public class AesEncoding {
	private int plainText[][] = new int[4][4];
	private int cipherKey[][];
	private int cipherArray[][];
	private String cipherText = "";
	private int round = 0;
	private KeyScheduling ks;

	public AesEncoding(int plainText[][], int cipherKey[][]) {
		this.plainText = plainText;
		this.cipherKey = cipherKey;
		endcoding();
		cipherText = Common.hexArrayToString(cipherArray);
	}

	public AesEncoding(String plaintText, int cipherKey[][]) {
		this.cipherKey = cipherKey;
		try {
			byte b[] = plaintText.getBytes("utf-8");
			int i = 0;
			String str = Common.byteArrayToHex(b);
			for (int x = 0; x < (str.length() / 2) / 16 + 1; x++) {
				int j = 0;
				int pText[][] = new int[4][4];
				for (; i < str.length() && i < 32 * (x + 1); i += 2) {
					String c = str.substring(i, i + 2);
					int a = Integer.valueOf(c, 16);
					pText[j / 4][j % 4] = a;
					j++;
				}
				endcoding(pText);
				cipherText += Common.hexArrayToString(cipherArray);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void endcoding(int plaintText[][]) {
		this.plainText = plaintText;
		round = 0;
		endcoding();
	}

	private void endcoding() {
		ks = new KeyScheduling(cipherKey);
		int data[][] = new int[4][4];

		// 초기화, 값 복사
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = plainText[i][j];
			}
		}

		// 기본 암호키와 XOR
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = data[i][j] ^ cipherKey[i][j];
			}
		}

		// 암호화 과정 수행
		while (round < 9) {
			data = addRoundKey(mixColumns(shiftRows(subBytes(data))));

			round++;
		}
		data = addRoundKey(shiftRows(subBytes(data)));

		cipherArray = data;
	}

	private int[][] addRoundKey(int data[][]) {
		int roundData[][] = new int[4][4];

		int roundKey[][] = ks.getKey(round);

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				roundData[i][j] = data[i][j] ^ roundKey[i][j];
			}
		}
		return roundData;
	}

	private int[][] subBytes(int data[][]) {
		int subData[][] = new int[4][4];
		SBox sb = new SBox();

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				subData[i][j] = sb.getSubtitute(Common.getInverseNumber(data[i][j]));
			}
		}
		return subData;
	}

	private int[][] shiftRows(int data[][]) {
		int shiftData[][] = new int[4][4];

		shiftData[0] = data[0];

		for (int i = 1; i < shiftData.length; i++) {
			for (int j = 0; j < shiftData[i].length; j++) {
				shiftData[i][j] = data[i][(j + i) % 4];
			}
		}

		return shiftData;
	}

	private int[][] mixColumns(int data[][]) {
		int mixData[][] = new int[4][4];
		int matrix[][] = { { 0x02, 0x03, 0x01, 0x01 }, { 0x01, 0x02, 0x03, 0x01 },
				{ 0x01, 0x01, 0x02, 0x03 }, { 0x03, 0x01, 0x01, 0x02 } };
		mixData = Common.multiplx(data, matrix);

		return mixData;
	}

	public String getCipherText() {
		return cipherText;
	}

	public int[][] getCipherArray() {
		return cipherArray;
	}
}

class TestClass {
	void testMethod() {
		int i;
		String ssss = "11";
	}
}
