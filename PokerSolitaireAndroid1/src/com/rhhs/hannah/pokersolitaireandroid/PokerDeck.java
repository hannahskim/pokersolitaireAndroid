package com.rhhs.hannah.pokersolitaireandroid;

/** Keeps track of a Poker Deck.  This is a subclass of a regular deck
 * Includes a constructor to indicate the size of your Poker Hands (e.g. 5 or7)
 * and a method getNextHand that can be used to return all possible PokerHands
 * Each call to getNextHand returns the next hand in a sequence.  This method will
 * return null after all hands have been returned
 * 
 * @author Ridout
 * @version October 2013
 *
 */

public class PokerDeck extends Deck
{
	// Keeps track of the deck indexes of the Cards in the next hand
	private int[] cardsInHand;

	/**
	 * Creates a PokerDeck that can be used to generate all of the possible
	 * hands of the given handSize
	 * @param handSize the number of cards in each generated PokerHand
	 */
	public PokerDeck(int handSize)
	{
		super(null);

		// Set up the starting hand with all different cards
		// Uses card numbers from 0 to 51 to keep track of each card
		cardsInHand = new int[handSize];
		for (int card = 0; card < handSize; card++)
			cardsInHand[card] = card;

		// Since we will increase the last card when we get the next hand
		// (see below), we need this to get the proper first hand
		cardsInHand[handSize - 1]--;
	}

	/**
	 * Generates the next unique PokerHand from this PokerDeck The number of
	 * cards in each hand is set when this PokerDeck was created
	 * @return the next unique PokerHand from this PokerDeck, null if all
	 * hands have already been returned
	 */
	public PokerHand getNextHand()
	{
		// Increase the card number of the last card in the hand
		int cardToChange = cardsInHand.length - 1;
		cardsInHand[cardToChange]++;

		// If the card number is too big, try increasing the previous card
		// This continues until we find a card that can be increased. If no
		// card can be increased, we have generated all hands - return null
		int endOfDeck = deck.length - cardsInHand.length;
		while (cardsInHand[cardToChange] > endOfDeck + cardToChange)
		{
			cardToChange--;
			if (cardToChange < 0)
				return null;
			cardsInHand[cardToChange]++;
		}

		// Reset all of the cards after the changed card to be 1 greater than
		// the previous card. This will ensure we get unique hands
		for (int card = cardToChange + 1; card < cardsInHand.length; card++)
			cardsInHand[card] = cardsInHand[card - 1] + 1;

		// Generate a new hand to return, based on the cardsInHand
		PokerHand nextHand = new PokerHand();
		for (int card = 0; card < cardsInHand.length; card++)
			nextHand.addCard(deck[cardsInHand[card]]);
		return nextHand;
	}
}
