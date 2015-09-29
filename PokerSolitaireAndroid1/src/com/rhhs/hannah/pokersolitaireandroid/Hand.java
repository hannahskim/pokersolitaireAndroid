package com.rhhs.hannah.pokersolitaireandroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import android.graphics.Canvas;
import android.graphics.Point;


/** Keeps track of Hand object
 * @author Hannah Kim
 * @version October 2013
 */

public class Hand
{
	protected ArrayList<Card> hand;
	
    /** Constructs a new Hand
     */
	public Hand()
	{
		hand = new ArrayList<Card>();
	}
	
	/** Displays the Cards in this Hand
	*@param g Graphics context to display the deck
	*/
	public void draw(Canvas canvas)
	{
	for (Card next : hand)
	next.draw(canvas);
	}
	
	/** Removes Cards from the Hand
	*@param card the given card to remove
	*/
	public void removeCard(Card card)
	{
	hand.remove(card);
	}
	
	/** Finds a selected Card at a given point
	*@param point the selected point
	*/
	public Card getCardAt(Point point)
	{
	for (Card next : hand)
	if (next.contains(point))
		return next;
	return null;
	}

	
    /** Adds a card into the ArrayList
     * @param cardToAdd the card to add
     */
	public void addCard (Card cardToAdd)
	{
		hand.add(cardToAdd);
	}
	
    /** Returns the cards in the hand as a String
     * @return the cards in the hand as a String
     */
	public String toString()
	{
		StringBuilder cardStr = new StringBuilder(hand.size()*3);
        for (int i = 0; i < hand.size(); i ++)
        {
                cardStr.append(hand.get(i));
                cardStr.append(" ");
        }
        return cardStr.toString();
	}
	
    /** Clears the hand
     */
	public void clear()
	{
		hand.clear();
	}
	
	// Sort by suit and then rank within each suit
	public void sortBySuit()
	{
		Collections.sort(hand);
	}
	
	// Sort by rank and then suit within each rank
	public void sortByRank()
	{
		Collections.sort(hand, Card.RANK_ORDER);
	}
	
    /** Returns the value of card in BlackJack game
     *  2-10: Face value. J,Q,K: 10. A: 1 or 11
     * @return the total value of the cards
     */
    public int getValue()
    {
            int totalValue = 0;

            // Add all the card value and count number of aces
            for (int i = 0; i < hand.size(); i++)
            {
                    Card card = hand.get(i);
                    totalValue += card.getValue();
            }
            return totalValue;
    }
}
