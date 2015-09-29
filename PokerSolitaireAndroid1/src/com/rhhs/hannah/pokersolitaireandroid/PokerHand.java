package com.rhhs.hannah.pokersolitaireandroid;


/**
 * Keeps track of a Poker Hand.  Hand size could be 1 to 7 cards.
 * Includes a getType() method that finds the type (e.g. two pair,
 * flush, straight) of this hand.  Note: In determining a hand's type
 * you should consider up to the best 5 cards in the hand.
 * 
 * @author Hannah Kim
 * @version October 2013
 */

public class PokerHand extends Hand
{
	// Poker Hand types/categories
	// Use these constants in your getType method
	// e.g. return FULL_HOUSE;
	public final static int ROYAL_FLUSH = 9;
	public final static int STRAIGHT_FLUSH = 8;
	public final static int FOUR_OF_A_KIND = 7;
	public final static int FULL_HOUSE = 6;
	public final static int FLUSH = 5;
	public final static int STRAIGHT = 4;
	public final static int THREE_OF_A_KIND = 3;
	public final static int TWO_PAIR = 2;
	public final static int PAIR = 1;
	public final static int NOTHING = 0;

	public final static String[] TYPES = { "Nothing", "Pair", "Two Pair",
			"Three of a Kind ", "Straight", "Flush", "Full House",
			"Four of a Kind", "Straight Flush", "Royal Flush" };
	
	static final int[] SCORES = { 0, 1, 3, 6, 12, 5, 10, 16, 30, 50 };

	/** Constructs an empty PokerHand
	  */        
	public PokerHand()
	{
		super();
	}

	/** Checks the type of the hand
	  * @return the poker hand type
	  */
	public int getType()
	{
		// Make two arrays to help analyze the cards in this hand
				int[] ranks = new int[15]; 
				int[] suits = new int[5]; 
				
				// Fill the arrays
				for (Card nextCard : hand)
				{
					ranks[nextCard.getRank()]++;
					suits[nextCard.getSuit()]++;
				}
				ranks[14] = ranks[1];

				// Check the type
				boolean isFlush = false;
				int[] flushCards = new int[15];
				int consecutive = 0;

				// Check for flush
				for (int count = 1; count < suits.length; count++)
				{
					if (suits[count] >= 5)
					{
						isFlush = true;
						// Make a new array and check for royal flush and straight flush
						for (Card nextCard : hand)
							if (nextCard.getSuit() == count)
								flushCards[nextCard.getRank()]++;
						flushCards[14] = flushCards[1];
						for (int check = flushCards.length - 1; check > 0; check--)
						{
							if (flushCards[check] >= 1)
							{
								consecutive++;
								if (consecutive >= 5)
								{
									if (check == 10)
										return ROYAL_FLUSH;
									return STRAIGHT_FLUSH;
								}
							}
							else
								consecutive = 0;
						}
					}
				}
				consecutive = 0; // Reset

				// Checking for straight
				boolean isStraight = false;
				for (int count = 1; count < ranks.length; count++)
				{
					if (ranks[count] >= 1)
					{
						consecutive++;
						if (consecutive >= 5)
							isStraight = true;
					}
					else
						consecutive = 0;
				}

				boolean threeKind = false;
				int noOfPairs = 0;
				for (int rank = 1; rank < ranks.length - 1; rank++)
				{
					if (ranks[rank] >= 4)
						return FOUR_OF_A_KIND;
					if (!threeKind && ranks[rank] >= 3)
						threeKind = true;
					else if (ranks[rank] >= 2)
						noOfPairs++;
				}
				// Other scores
				if (threeKind && noOfPairs >= 1)
					return FULL_HOUSE;
				if (isFlush)
					return FLUSH;
				if (isStraight)
					return STRAIGHT;
				if (threeKind)
					return THREE_OF_A_KIND;
				if (noOfPairs >= 2 || (noOfPairs == 1 && threeKind))
					return TWO_PAIR;
				if (noOfPairs == 1)
					return PAIR;
				return NOTHING;
	}

	/** gets the score
	 * @return the score
	 **/
	public int getScore()
	{
		return SCORES[getType()];
	}
}
