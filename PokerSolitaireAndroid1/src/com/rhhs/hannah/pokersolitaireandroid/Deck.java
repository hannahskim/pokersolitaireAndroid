package com.rhhs.hannah.pokersolitaireandroid;

import android.content.res.Resources;

/** Keeps track of Deck object
 * @author Hannah Kim
 * @version October 2013
 */

public class Deck
{
	protected Card [] deck;
	private int topCard;

    /** Constructs a standard 52 card deck
     */
	public Deck(Resources res)
	{
		deck = new Card[52];
        topCard = 0;
        for (int suit = 1; suit <= 4; suit++)
        {
        	for (int rank = 1; rank <= 13; rank++)
                        deck[topCard++] = new Card(rank, suit, res);
        }
	}
	
    /** Shuffles the deck, resets topCard
     */
	public void shuffle()
	{
		topCard = deck.length;
		// Shuffle the deck by switching cards inside
		for (int ind = 0; ind < topCard; ind ++)
		{
			int randomPos = (int) (Math.random() * topCard);
			Card card = deck[randomPos];
			deck[randomPos] = deck [ind];
			deck[ind] = card;
					
		}
	}
	
    /** Deals the top card
     * @return the top card
     */
	public Card deal()
	{
	if (topCard == 0)
		return deck[0];
	
	return deck[--topCard];
	}
	
    /** Checks the number of cards left
     * @return the number of cards left
     */
	public int noOfCardsLeft()
	{
		return topCard;
	}

}
