package com.rhhs.hannah.pokersolitaireandroid;

import android.graphics.Point;

/** Rectangle class
* @author ICS4U Hannah Kim
* @version November 2013
*/

public class Rectangle 
{
	protected int x, y;
	protected int width, height;
	
	/** Constructor 
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     */
	public Rectangle(int x, int y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/** Sets the size 
     * @param width the width
     * @param height the height
     */
	public void setSize (int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	/** Sets the location 
     * @param x the x
     * @param y the y
     */
	public void setLocation (int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/** Sets the location 
     * @param point the point
     */
	public void setLocation (Point point)
	{
		setLocation (point.x, point.y);
	}
	
	/** Translates 
     * @param dx the x
     * @param dy the y
     */
	public void translate (int dx, int dy)
	{
		this.x += dx;
		this.y += dy;
	}
	
	/** Checks if the point is contained in the given area 
     * @param point the given point
     */
	public boolean contains (Point point)
	{
		return point.x > this.x &&
				point.x < this.x + this.width &&
				point.y > this.y &&
				point.y < this.y + this.height;
	}
}
