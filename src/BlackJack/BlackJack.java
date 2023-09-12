package BlackJack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class BlackJack {
	private int stage = 0;
	
	private String playerName = "";
	
	private ArrayList<Kaart> playerCards = new ArrayList<>();
	private ArrayList<Kaart> dealerCards = new ArrayList<>();
	
	
	private String dealerName;
	private String[] dealerNames = new String[] { "Joe", "Harry", "Mandy", "Zoe" };
	
	private Kaart[] deck = new Kaart[52];
	
	private String[] deckValues = new String[] {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	private String[] deckTypes = new String[] { "\u2660", "\u2764", "\u2662", "\u2663" };
	
	
	private Random rng = new Random();
	
	public BlackJack() {
		this.dealerName = dealerNames[rng.nextInt(4)];
	}
	
	public void process() {
		Scanner sc = new Scanner(System.in);
		switch(stage) {
			case 0:
				System.out.println("Wat is jouw naam?");
				String input = sc.nextLine();
				this.playerName = input;
				System.out.println("Welkom, " + this.playerName + "!");
				stage++;
				break;
			case 1:
				System.out.println("Wil je een potje Blackjack spelen? 'j' voor ja, 'n' voor nee");
				input = sc.nextLine();
				if (input.equals("j")) {
					System.out.println("Veel succes!");
					stage++;
				} else if (input.equals("n")) {
					System.out.println("Misschien later!");
				} else {
					System.out.println("Ik snap niet precies wat je bedoelt..");
				}
				break;
			case 2:
				initializeDeck();		
				initializeGameCards();
				stage++;
				break;
			case 3:
				handleInput();
				break;
			case 4:
				handleDealer();
				break;
			case 5: 
				handleWinner();
				break;
			default:
				handleInput();
				break;
		}
		visualize();
		process();
	}
	
	
	public void initializeGameCards() {
		playerCards.add(nieuweKaart(true));
		dealerCards.add(nieuweKaart(true));
		playerCards.add(nieuweKaart(true));
		dealerCards.add(nieuweKaart(false));
	}
	
	public void initializeDeck() {
		deck = new Kaart[52];
		for (int i = 0; i < deckTypes.length; i++) {
			for (int j = 0; j < deckValues.length; j++) {
				deck[i * 13 + j] = new Kaart(deckTypes[i], deckValues[j]);
			}
		}
		shuffle();
	}
			
	
	public void handleInput() {
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		switch(input) {
			case "k":
				playerCards.add(nieuweKaart(true));
				break;
			case "p":
				System.out.println("Ik pass.");
				stage = 4;
				break;
			case "q":
				System.out.println("Jammer dat je weer gaat! Tot de volgende keer!");
				System.exit(0);
				break;	
			default:
				System.out.println("Ik begrijp niet wat je bedoelt..");
				break;
		}		
	}
	
	public void handleBust() {
		int[] value = getHandValueAces(playerCards);
		if (value[0] > 21) {
			handleWinner();
		}
	}
	
	public void handleDealer() {
		for (int i = 0; i < dealerCards.size(); i++) {
			Kaart kaart = dealerCards.get(i);
			if (!dealerCards.get(i).isVisible()) {
				dealerCards.get(i).setVisible(true);
			}
		}		
		int[] dealerCardsValues = getHandValueAces(dealerCards);
		int dealerCardsValue = 0;
		if (dealerCardsValues[1] > dealerCardsValues[0] && dealerCardsValues[1] <= 21) {
			dealerCardsValue = dealerCardsValues[1];
		} else {
			dealerCardsValue = dealerCardsValues[0];
		}
		
		if (dealerCardsValue >= 17)  {
			stage++;
		} else {
			dealerCards.add(nieuweKaart(true));
		}
	}

	
	public void handleWinner() {
		int[] playerValues = getHandValueAces(playerCards);
		int[] dealerValues = getHandValueAces(dealerCards);
		
		int playerValue = 0;
		if (playerValues[1] > playerValues[0] && playerValues[1] <= 21) {
			playerValue = playerValues[1];
		} else {
			playerValue = playerValues[0];
		}
		
		int dealerValue = 0;
		if (dealerValues[1] > dealerValues[0] && dealerValues[1] <= 21) {
			dealerValue = dealerValues[1];
		} else {
			dealerValue = dealerValues[0];
		}
		
		if (dealerValue > 21) {
			System.out.println("You have won!");
		} else if (dealerValue > playerValue) {
			System.out.println("You have lost..");
		} else if (dealerValue == playerValue) {
			System.out.println("Game is a draw!");
		} else if (playerValue > 21) {
			System.out.println("You have lost..");
		} else {
			System.out.println("You have won!");
		}
		stage = 1;
		resetCards();
		initializeDeck();		
	}
	
	public Kaart nieuweKaart(boolean isVisible) {
		Kaart[] deck = getDeck();
		Kaart nieuweKaart = deck[0];
		nieuweKaart.setVisible(isVisible);
		Kaart[] newDeck = Arrays.copyOfRange(deck, 1, deck.length);
		setDeck(newDeck);
		return nieuweKaart;
		
	}
	
	public void shuffle() {
		Kaart[] deck = getDeck();
		Collections.shuffle(Arrays.asList(deck));
		setDeck(deck);
	}
	
	
	public int getHandValue(ArrayList<Kaart> cards) {
		int result = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).isVisible()) {
				result += cards.get(i).getPlayValue();
			}
		}
		return result;
	}
	
	public int[] getHandValueAces(ArrayList<Kaart> cards) {
		int[] result = new int[] { 0, 0 };
		int azen = 0;
		int initialValue = 0;
		
		for (Kaart k : cards) {
			if (k.isVisible()) {
				if (k.isAce()) 
					azen++;
				else 
					initialValue += k.getPlayValue();
			}
		}
		
		if (azen == 0) {
			result = new int[] { initialValue, 0 }; 
		} else {
			result = new int[] { initialValue + azen * 1, (initialValue + (azen - 1) * 1) + 11};
		}
		
		
		
		
		return result;
	}
	
	
	public void setDeck(Kaart[] deck) {
		this.deck = deck;
	}
	
	public Kaart[] getDeck() {
		return this.deck;
	}
	
	public void resetCards() {
		this.dealerCards = new ArrayList<>();
		this.playerCards = new ArrayList<>();
	}
	
	public static void main(String[] args) {
		BlackJack bj = new BlackJack();
		bj.process();
	}
	
	public void visualize() {
		if (dealerCards.isEmpty() || playerCards.isEmpty()) return;
		String dealerCardsString = "";
		int[] dealerCardsValue = getHandValueAces(dealerCards);
		for (int i = 0;  i < dealerCards.size(); i++) {
			Kaart kaart = dealerCards.get(i);
			if (kaart.isVisible()) {
				dealerCardsString += " " + kaart.toString() + "  ";
			} else {
				dealerCardsString += " ? ? ";
			}
		}
		String playerCardsString = "";
		int[] playerCardsValue = getHandValueAces(playerCards);
		for (int i = 0;  i < playerCards.size(); i++) {
			Kaart kaart = playerCards.get(i);
			if (kaart.isVisible()) {
				playerCardsString += " " + kaart.toString() + "  ";
			} else {
				playerCardsString += " ? ? ";
			}
		}
		System.out.println("----- " + this.dealerName + " -----");
		System.out.println();
		System.out.println(dealerCardsString);
		System.out.println("     " + dealerCardsValue[0] + (dealerCardsValue[1] > 0 && dealerCardsValue[1] < 21 ? "/" + dealerCardsValue[1] : ""));
		System.out.println();
		System.out.println(playerCardsString);
		System.out.println("     " + playerCardsValue[0] + (playerCardsValue[1] > 0 && playerCardsValue[1] < 21 ? "/" + playerCardsValue[1] : ""));
		System.out.println();
		System.out.println("----- " + this.playerName + " -----");
	}
}
