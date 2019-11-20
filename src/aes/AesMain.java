package aes;

import java.util.Scanner;

public class AesMain {
	public static void main(String[] args) {
		int cipherKey[][] = { { 0x2b, 0x28, 0xab, 0x09 }, { 0x7e, 0xae, 0xf7, 0xcf },
				{ 0x15, 0xd2, 0x15, 0x4f }, { 0x16, 0xa6, 0x88, 0x3c } };

		System.out.println("암호화 하고자 하는 Text를 입력하세요");

		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		System.out.println("Input Text : " + str);

		System.out.println("\nDecoding");
		AesEncoding ae2 = new AesEncoding(str, cipherKey);
		String cipherText = ae2.getCipherText();
		System.out.println("Chiper Text : " + cipherText);

		System.out.println("\nEncoding");
		AesDecoding ad2 = new AesDecoding(cipherText, cipherKey);
		System.out.println("plaint Text : " + ad2.getPlaintText());

		sc.close();
	}
}
