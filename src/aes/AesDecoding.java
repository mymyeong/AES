package aes;

public class AesDecoding {
	private int cipherArray[][] = new int[4][4];
	private String plaintText = "";
	private int cipherKey[][] = {
			{ 0x2b, 0x28, 0xab, 0x09 },
			{ 0x7e, 0xae, 0xf7, 0xcf },
			{ 0x15, 0xd2, 0x15, 0x4f },
			{ 0x16, 0xa6, 0x88, 0x3c } };

	private KeyScheduling ks = new KeyScheduling(cipherKey);
	private int roundKey[][][] = null;
	private int plaintArray[][] = null;
	private int round = 9;

	public AesDecoding(int cipherArray[][], int cipherKey[][]) {
		this.cipherArray = cipherArray;
		this.cipherKey = cipherKey;
		roundKey = ks.getKey();
		decoding();
		plaintText = Common.hexByteToString(Common.hexToByteArray(Common
				.hexArrayToString(plaintArray)));
	}

	public AesDecoding(String cipherText, int cipherKey[][]) {
		int tempCipherArray[][][] = Common.stringToHexArray(cipherText);
		roundKey = ks.getKey();
		String temp = "";
		for (int j = 0; j < tempCipherArray.length; j++) {
			cipherArray = tempCipherArray[j];
			decoding();
			temp += Common.hexArrayToString(plaintArray);
			round = 9;
		}
		plaintText = Common.hexByteToString(Common.hexToByteArray(temp)).trim();
	}

	private void decoding() {
		int state[][] = new int[4][4];

		for (int i = 0; i < cipherArray.length; i++) {
			for (int j = 0; j < cipherArray[i].length; j++) {
				state[i][j] = cipherArray[i][j];
			}
		}
		state = addRoundKey(cipherArray);
		while (round >= 0) {
			state = mixColumns(addRoundKey(inversSubBytes(inversShiftRows(state))));
		}
		state = addRoundKey(inversSubBytes(inversShiftRows(state)), cipherKey);
		plaintArray = state;
	}

	private int[][] mixColumns(int data[][]) {
		int mixData[][] = new int[4][4];
		int matrix[][] = {
				{ 0x0E, 0x0B, 0x0D, 0x09 },
				{ 0x09, 0x0E, 0x0B, 0x0D },
				{ 0x0D, 0x09, 0x0E, 0x0B },
				{ 0x0B, 0x0D, 0x09, 0x0E } };

		mixData = Common.multiplx(data, matrix);

		return mixData;
	}

	private int[][] addRoundKey(int data[][]) {
		int roundData[][] = new int[4][4];

		int roundKey[][] = this.roundKey[round];

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				roundData[i][j] = data[i][j] ^ roundKey[i][j];
			}
		}
		round--;
		return roundData;
	}

	private int[][] addRoundKey(int data[][], int cipherKey[][]) {
		int roundData[][] = new int[4][4];

		int roundKey[][] = cipherKey;

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				roundData[i][j] = data[i][j] ^ roundKey[i][j];
			}
		}
		return roundData;
	}

	private int[][] inversSubBytes(int data[][]) {
		int subData[][] = new int[4][4];
		SBox sb = new SBox();

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				subData[i][j] = Common.getInverseNumber(sb.getInversSubtitute(data[i][j]));
			}
		}
		return subData;
	}

	private int[][] inversShiftRows(int data[][]) {
		int shiftData[][] = new int[4][4];

		shiftData[0] = data[0];

		for (int i = 1; i < shiftData.length; i++) {
			for (int j = 0; j < shiftData[i].length; j++) {
				shiftData[i][j] = data[i][(4 - i + j) % 4];
			}
		}

		return shiftData;
	}

	public String getPlaintText() {
		return plaintText;
	}

	public int[][] getPlaintArray() {
		return plaintArray;
	}

	public void print() {
		System.out.println("plaintText : " + plaintText);
	}
}
