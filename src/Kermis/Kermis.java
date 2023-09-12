package Kermis;

import java.util.Scanner;

public class Kermis {
	public Botsautos botsAutos = new Attractie();
	
	
	public Kermis() {
		
	}
	
	public static void main(String[] args) {
		Kermis kermis = new Kermis();
		kermis.requestInput();
	}
	
	public void requestInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Voer attractie nummer in:");
		String numberStr = scanner.nextLine();
		
		try {
			int number = Integer.parseInt(numberStr);
		} catch (NumberFormatException e) {
			System.out.println("Je mag alleen nummers invoeren");
			requestInput();
		}
	}
	

	
}
