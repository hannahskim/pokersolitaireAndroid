package com.rhhs.hannah.pokersolitaireandroid;

import java.util.Comparator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;



/** Keeps track of Card object
* @author ICS4U Hannah Kim
* @version October 2013
*/

public class Card extends Rectangle implements Comparable<Card>
{
	// D-1, C-2, H-3, S-4
	private int suit;
	// 2-10, J-11, Q-12, K-13, A-14
	private int rank;
	private boolean isFaceUp;
	
	private static final String RANKS = " A23456789TJQK";
	private static final String SUITS = " DCHS";
	
    // Constant Comparator object for comparing cards by suits
    public static final Comparator < Card >
	RANK_ORDER = new RankOrder ();
    
    // Code to keep track of an Bitmap for each Card
    private static Bitmap background;
    public static int WIDTH ;
    public static int HEIGHT ;
    private Bitmap image;
    
 // 2D Constant Array to Match up Card Resource IDs to the suit and rank
    private final static int[][] CARD_RES = {
    { 0 },
    { 0, R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4,
    R.drawable.d5, R.drawable.d6, R.drawable.d7, R.drawable.d8,
    R.drawable.d9, R.drawable.d10, R.drawable.d11,
    R.drawable.d12, R.drawable.d13 },
    { 0, R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4,
    R.drawable.c5, R.drawable.c6, R.drawable.c7, R.drawable.c8,
    R.drawable.c9, R.drawable.c10, R.drawable.c11,
    R.drawable.c12, R.drawable.c13 },
    { 0, R.drawable.h1, R.drawable.h2, R.drawable.h3, R.drawable.h4,
    R.drawable.h5, R.drawable.h6, R.drawable.h7, R.drawable.h8,
    R.drawable.h9, R.drawable.h10, R.drawable.h11,
    R.drawable.h12, R.drawable.h13 },
    { 0, R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4,
    R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8,
    R.drawable.s9, R.drawable.s10, R.drawable.s11,
    R.drawable.s12, R.drawable.s13 } };
	
    /** Constructor to make a card
     * @param rank the given rank
     * @param suit the given suit
     */
    public Card(int rank, int suit, Resources res)
	{
		super(0, 0, 0, 0);
		this.rank = rank;
		this.suit = suit;
		isFaceUp = true;
	
//		// Load up the appropriate Image file for this card
//		String ImageFileName = “” + “ dchs”.charAt(suit) + rank + “.png”;
//		ImageFileName = “Bitmaps\\” + ImageFileName;
//		Image = new ImageIcon(ImageFileName).getImage();
		
//		// Set the size of the card based on the Bitmap size
//		setSize(Bitmap.getWidth(null), Bitmap.getHeight(null));
		
		// If this is the first Card being created, load up the background image
		// and set Card Height and Width constants from the background Card
		if (background == null)
		{
		background= BitmapFactory.decodeResource(res, R.drawable.blackback);
		HEIGHT = background.getHeight();
		WIDTH = background.getWidth ();
		}
		
		// Load up the appropriate image file for this card
		image = BitmapFactory.decodeResource(res, CARD_RES[suit][rank]);
		// Set the size of the card based on the image size
		setSize(image.getWidth(), image.getHeight());
	}
	
	/** Draws a card in a Canvas context
	* @param canvas Canvas to draw the card in
	*/ public void draw(Canvas canvas)
	{
	if (isFaceUp)
	canvas.drawBitmap(image, x, y, null);
	else
	canvas.drawBitmap(background, x, y, null);
	}
	
	/** Calls the translate method from the Rectangle class
     */
	public void move (Point initialPos, Point finalPos)
	{
	translate (finalPos.x - initialPos.x, finalPos.y - initialPos.y);
	}
	
	// "JS" "5C" "TD"
    /** Changes card as a String ("JS" "5C" "TD")
     * @return the card as a String
     */
	public String toString()
	{
	    return String.format("%c%c", RANKS.charAt(rank), SUITS.charAt(suit));
	}
	
    /** Gets the rank of the card
     * @return the rank
     */
	public int getRank()
	{
		return rank;
	}
	
    /** Gets the suit of the card
     * @return the suit
     */
	public int getSuit()
	{
		return suit;
	}
	
    /** Checks if the card is ace
     * @return true if it's ace or false if it isn't
     */
	public boolean isAce()
	{
		if (rank == 1)
			return true;
		return false;
	}

    /** Returns BlackJack value 
     * 2-10: Face value. J,Q,K: 10. A: 11
     * @return the value of the card
     */
	public int getValue()
	{
	if (rank > 10)
	    return 10;
	else if (this.isAce())
		return 11;
	return rank;
	}
	
    /** Compares the suits of each cards 
     * @param other the card to compare
     * @return a value < 0 if the suit of the card is smaller than other, 
     *         a value > 0, if the suit of the card is bigger than other and
     *         0, if the suit of the card are the same
     */
	public int compareTo(Card other)
	{
		// If rank is the same, compare suits
	if (this.suit - other.suit == 0)
	    return this.rank - other.rank;
	return this.suit - other.suit;
	}
	
    /** An inner Comparator class that compares ranks
    */
    private static class RankOrder implements Comparator < Card >
    {
	/** Compares the ranks of two Card objects
	* @param first the first card to compare
	* @param second the second card to compare
	* @return a value < 0 if the first rank has a lower suit, a
	*                       a value < 0 if first rank has a higher value and
	*                       0 if the rank of the Cards are the same
	*/
	public int compare (Card first, Card second)
	{
	    if (first.rank < second.rank)
		return -1;
	    else if (first.rank > second.rank)
		return 1;
	    else
		return (first.compareTo(second));
	    }
    }
    
    /** Checks to see if the given object is a card and if it has the
     * same suit and rank as this card
     * @param other the object of compare to this card
     * @return true if the given object is a card
     *                  with the same suit and rank as the card
     */
    public boolean equals (Object other)
    {
	if (this.getClass()!= other.getClass())
		return false;
	Card otherCard = (Card)other;
	return this.suit == otherCard.suit &&
			this.rank == otherCard.rank;
    }

}
