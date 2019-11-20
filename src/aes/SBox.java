package aes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SBox {
	static final int GF = 0x11B;

//	public static void main(String[] args) {
//
//		SBox s = new SBox();
//
//		int box[][] = s.createSBox();
//		System.out.println("s-Box");
//		Common.printbox(box);
//
//		int inversBox[][] = s.createInversSBox();
//		System.out.println("\ninvers s-Box");
//		Common.printbox(inversBox);
//
//		s.saveSbox(box);
//		s.saveInversSbox(inversBox);
//	}

	public int[][] createSBox() {
		int box[][] = new int[16][16];
		for (int y = 0; y < box.length; y++) {
			for (int x = 0; x < box[y].length; x++) {
				String p = Integer.toHexString(y);
				p += Integer.toHexString(x);
				box[y][x] = getSubtitute(Common.getInverseNumber(Integer.parseInt(p, 16)));
			}
		}
		return box;
	}

	public int[][] createInversSBox() {
		int box[][] = new int[16][16];
		for (int y = 0; y < box.length; y++) {
			for (int x = 0; x < box[y].length; x++) {
				String p = Integer.toHexString(y);
				p += Integer.toHexString(x);
				box[y][x] = Common.getInverseNumber(getInversSubtitute(Integer.parseInt(p, 16)));
			}
		}
		return box;
	}

	public static int[][] getSBox() {
		int sbox[][] = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sbox.object"));
			sbox = (int[][]) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbox;
	}

	public static int[][] getInversSBox() {
		int sbox[][] = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sbox.object"));
			sbox = (int[][]) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbox;
	}

	public int getSubtitute(int input) {
		String b0[] = { "10001111", "11000111", "11100011", "11110001", "11111000", "01111100",
				"00111110", "00011111" };
		String c = "11000110";
		
		String b = Integer.toBinaryString(input);
		if (b.length() < 8) {
			int c1 = 8 - b.length();
			for (int i = 0; i < c1; i++) {
				b = "0" + b;
			}
		}
		String r = "";
		for (int i = 0; i < b.length(); i++) {
			String sb0 = b0[i];
			int a = Integer.parseInt(String.valueOf(sb0.charAt(0)))
					& Integer.parseInt(String.valueOf(b.charAt(b.length() - 1)));
			for (int j = 1; j < sb0.length(); j++) {
				int x1 = Integer.parseInt(String.valueOf(sb0.charAt(j)));
				int y1 = Integer.parseInt(String.valueOf(b.charAt((b.length() - 1) - j)));
				a ^= x1 & y1;
			}
			int c1 = Integer.parseInt(String.valueOf(c.charAt(i)));
			r += (a ^ c1);
		}
		String x = "";
		for (int i = r.length() - 1; i >= 0; i--) {
			x += String.valueOf(r.charAt(i));
		}
		return Integer.parseInt(x, 2);
	}

	public int getInversSubtitute(int input) {
		String b0[] = { "00100101", "10010010", "01001001", "10100100", "01010010", "00101001",
				"10010100", "01001010" };
		String c = "10100000";
		String b = Integer.toBinaryString(input);
		if (b.length() < 8) {
			int c1 = 8 - b.length();
			for (int i = 0; i < c1; i++) {
				b = "0" + b;
			}
		}
		String r = "";
		for (int i = 0; i < b.length(); i++) {
			String sb0 = b0[i];
			int a = Integer.parseInt(String.valueOf(sb0.charAt(0)))
					& Integer.parseInt(String.valueOf(b.charAt(b.length() - 1)));
			for (int j = 1; j < sb0.length(); j++) {
				int x1 = Integer.parseInt(String.valueOf(sb0.charAt(j)));
				int y1 = Integer.parseInt(String.valueOf(b.charAt((b.length() - 1) - j)));
				a ^= x1 & y1;
			}
			int c1 = Integer.parseInt(String.valueOf(c.charAt(i)));
			r += (a ^ c1);
		}
		String x = "";
		for (int i = r.length() - 1; i >= 0; i--) {
			x += String.valueOf(r.charAt(i));
		}
		return Integer.parseInt(x, 2);
	}

	@SuppressWarnings("unused")
	private void saveSbox(int box[][]) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sbox.object"));
			oos.writeObject(box);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void saveInversSbox(int box[][]) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("insbox.object"));
			oos.writeObject(box);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
