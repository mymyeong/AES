package aes;

public class KeyScheduling {
	int key[][] = null;
	int rcon[][] = { { 0x1, 0, 0, 0 },//
			{ 0x2, 0, 0, 0 },//
			{ 0x4, 0, 0, 0 },//
			{ 0x8, 0, 0, 0 },//
			{ 0x10, 0, 0, 0 },//
			{ 0x20, 0, 0, 0 },//
			{ 0x40, 0, 0, 0 },//
			{ 0x80, 0, 0, 0 },//
			{ 0x1b, 0, 0, 0 },//
			{ 0x36, 0, 0, 0 } };

	private int roundKey[][][] = new int[10][4][4];

	public KeyScheduling(int key[][]) {
		this.key = key;
		schduling();
	}

//	public static void main(String[] args) {
//		int key[][] = { { 0x2b, 0x28, 0xab, 0x09 }, { 0x7e, 0xae, 0xf7, 0xcf },
//				{ 0x15, 0xd2, 0x15, 0x4f }, { 0x16, 0xa6, 0x88, 0x3c } };
//		new KeyScheduling(key);
//	}

	public int[][] getKey(int round) {
		return roundKey[round];
	};

	public int[][][] getKey() {
		return roundKey;
	};

	private void schduling() {
		int round = 0;
		int key[][] = this.key;
		while (round < 10) {

			if (round != 0) {
				key = roundKey[round - 1];
			}

			int word[] = new int[4];

			for (int i = 0; i < 4; i++) {
				if (round == 0) {
					word[i] = key[i][3];
				} else {
					word[i] = roundKey[round - 1][i][3];
				}
			}

			word = rotWord(word);

			SBox s = new SBox();
			for (int i = 0; i < word.length; i++) {
				word[i] = s.getSubtitute(Common.getInverseNumber(word[i]));
			}

			int rkey[][] = new int[4][4];

			for (int i = 0; i < key.length; i++) {
				for (int j = 0; j < key[i].length; j++) {
					if (j == 0) {
						rkey[i][j] = key[i][j] ^ word[i] ^ rcon[round][i];
					} else {
						rkey[i][j] = key[i][j] ^ rkey[i][j - 1];
					}
				}
			}
			roundKey[round] = rkey;
			round++;
			// printKey(rkey);
		}
	}

	public int[] rotWord(int word[]) {
		int temp = word[0];
		for (int i = 0; i < word.length - 1; i++) {
			word[i] = word[i + 1];
		}
		word[3] = temp;
		return word;
	}

	public void printKey(int key[][]) {

		for (int i = 0; i < key.length; i++) {
			for (int j = 0; j < key[i].length; j++) {
				System.out.printf("%H\t", key[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printKey(int key[]) {
		for (int i = 0; i < key.length; i++) {
			System.out.printf("%H\t", key[i]);
		}
		System.out.println();
	}
}
