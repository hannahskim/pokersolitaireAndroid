package com.rhhs.hannah.pokersolitaireandroid;

import java.io.Serializable;

/** Keeps track of Player object
 * @author Hannah Kim
 * @version October 2013
 */

public class Player implements Comparable <Player>, Serializable
{
	private String name;
	private int score;
	
    /** Constructor to a new Player object
     * @param name the given name of the player
     * @param score the given score of the player
     */
	public Player (String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
    /** Compares the scores of each player
     * @param other the score to compare
     * @return a value < 0 if the score of the player is smaller than other, 
     *         a value > 0, if the score of the player is bigger than other and
     *         0, if the scores of the players are the same
     */
	public int compareTo(Player other)
	{
		return this.score - other.score;
	}
	
    /** Displays player information
     * @return player information
     */
	public String toString()
	{
		return String.format("%s %d", name, score);
	}

	public int getScore() {
		return score;
	}
	 

}
