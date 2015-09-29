package com.rhhs.hannah.pokersolitaireandroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TableView extends View
{
	private static final int TOP_OFFSET = 100;
	private static final int LEFT_OFFSET = 13;
	private static int ROW_SPACING;
	private static int COL_SPACING;
	private static Point POS_OF_NEXT_CARD;

	private Deck myDeck;
	private PokerHand[] rowHands;
	private PokerHand[] colHands;
	private Card nextCard;
	private Card currentCard;
	private Point lastPoint;
	private int originalRow, originalCol;
	private boolean[][] spotsTaken;
	private int score;
	private boolean gameOver;

	private ArrayList<Player> topPlayers;

	private Paint paint;

	public TableView(Context context)
	{
		super(context);

		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		// Smooth the edges
		paint.setAntiAlias(true);

		try
		{
			// Opens the file for input – file is on the SD card
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath()
			+ "/ICS4UGames/PokerSHannah");
			File file = new File(directory, "highscores.dat");
			
			ObjectInputStream topScoresIn = new ObjectInputStream(
					new FileInputStream(file));
			topPlayers = (ArrayList<Player>) topScoresIn.readObject();
		    topScoresIn.close();
		}
		catch (Exception e) // This could include different types of
							// Exceptions
		{
			// If we had trouble reading the file (e.g. it doesn't exist) or
			// if our file has errors an Exception will be thrown and we can
			// create a new empty list
			topPlayers = new ArrayList<Player>();
		}

		// Set up the deck and hands
		myDeck = new Deck(context.getResources());

		ROW_SPACING = Card.HEIGHT + 10;
		COL_SPACING = Card.WIDTH + 10;
		POS_OF_NEXT_CARD = new Point(2 * COL_SPACING + LEFT_OFFSET, 5);

		// We need 10 hands in total
		rowHands = new PokerHand[5];
		colHands = new PokerHand[5];
		for (int hand = 0; hand < 5; hand++)
		{
			rowHands[hand] = new PokerHand();
			colHands[hand] = new PokerHand();
		}
		
		
		
		// Set up an array to keep track of the spots taken on the table
		spotsTaken = new boolean[5][5];
		newGame();
	}

	/**
	 * Starts a new game by shuffling the deck and re-initialising the hands and
	 * the spots taken on the table
	 */
	public void newGame()
	{
		myDeck.shuffle();

		// Clear the hands (the clear method just resets the size to 0)
		// Also reset the spotsTaken all to false
		for (int hand = 0; hand < 5; hand++)
		{
			rowHands[hand].clear();
			colHands[hand].clear();
			for (int column = 0; column < 5; column++)
				spotsTaken[hand][column] = false;
		}
		score = 0;
		gameOver = false;

		// Deal the first card
		nextCard = myDeck.deal();
		nextCard.setLocation(POS_OF_NEXT_CARD);
		currentCard = null;

		this.postInvalidate();
	}

	/**
	 * Updates the score based on the score in all of the hands
	 */
	public void updateScore()
	{
		score = 0;
		for (int hand = 0; hand < 5; hand++)
		{
			score += rowHands[hand].getScore();
			score += colHands[hand].getScore();
		}
	}

	/**
	 * All your drawing code for this View goes here
	 * 
	 * @param canvas the Canvas to draw in
	 */
	protected void onDraw(Canvas canvas)
	{

		// Draw the spots for the cards to go
		// You may want to make this nicer
		canvas.drawColor(Color.BLACK);

		paint.setColor(Color.YELLOW);

		for (int row = 0; row < 5; row++)
			for (int column = 0; column < 5; column++)
			{
				int x = LEFT_OFFSET + column * COL_SPACING;
				int y = TOP_OFFSET + row * ROW_SPACING;
				canvas.drawRect(x, y, x + Card.WIDTH, y + Card.HEIGHT, paint);
			}

		// Draw the hands and their current scores
		// Only the row hands need to be drawn
		paint.setColor(Color.WHITE);
		for (int hand = 0; hand < 5; hand++)
		{
			rowHands[hand].draw(canvas);
			int score = rowHands[hand].getScore();
			if (score > 0)
				canvas.drawText(String.valueOf(score), 5 * COL_SPACING
						+ LEFT_OFFSET + 10, hand * ROW_SPACING + Card.HEIGHT
						/ 2 + TOP_OFFSET + 5, paint);
			score = colHands[hand].getScore();
			if (score > 0)
				canvas.drawText(String.valueOf(score), hand * COL_SPACING
						+ Card.WIDTH / 2 - 5 + LEFT_OFFSET, 5 * ROW_SPACING
						+ TOP_OFFSET + 10, paint);
		}

		// Draw the total score
		canvas.drawText("Score: " + score, 65, 40, paint);

		// Draw the next card if not game over
		if (!gameOver)
			nextCard.draw(canvas);
		else
		{
			canvas.drawText("Game", POS_OF_NEXT_CARD.x - 7,
					POS_OF_NEXT_CARD.y + 25, paint);
			canvas.drawText("Over", POS_OF_NEXT_CARD.x,
					POS_OF_NEXT_CARD.y + 55, paint);
		}

		// Draw the moving card
		if (currentCard != null)
			currentCard.draw(canvas);

	}

	/**
	 * Responds to touch events
	 * 
	 * @param event the motion event that generated this touch event
	 * @return true if the event was handle, false otherwise
	 */
	public boolean onTouchEvent(MotionEvent event)
	{
		Point clickPoint = new Point((int) event.getX(), (int) event.getY());

		// If action down, try to place a Card
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
		{
			// If the game is over, we disable any mouse presses
			if (gameOver)
			{
				Toast.makeText(this.getContext(), "Game Over",
						Toast.LENGTH_LONG).show();

				return true;
			}

			// If we are dragging a Card disregard any mouse pressed
			// May not be needed
			if (currentCard != null)
				return true;

			// Figure out the selected row and column on the board
			int row = (clickPoint.y - TOP_OFFSET) / ROW_SPACING;
			int column = (clickPoint.x - LEFT_OFFSET) / COL_SPACING;

			// Ignore any clicks off the board area
			if (row < 0 || row > 4 || column < 0 || column > 4)
				return true;

			// Pick up card if there is a Card here
			if (spotsTaken[row][column])
			{
				lastPoint = new Point(clickPoint);

				// Find out which Card was selected
				currentCard = rowHands[row].getCardAt(lastPoint);

				// Ignore clicks between Cards
				if (currentCard == null)
					return true;

				// ... and remove it from both hands and the board
				rowHands[row].removeCard(currentCard);
				colHands[column].removeCard(currentCard);
				spotsTaken[row][column] = false;
				updateScore();

				// Keep track of original position if we need to return the
				// card
				originalRow = row;
				originalCol = column;
				// We can quit early
				return true;
			}

			// Clicking on an empty spot
			// Place the next card in this spot
			placeACard(nextCard, row, column);

			// ... and add it to the corresponding row and column hand
			rowHands[row].addCard(nextCard);
			colHands[column].addCard(nextCard);
			updateScore();
			nextCard = null;

			// Deal the next card if not done
			if (myDeck.noOfCardsLeft() > 27)
			{
				nextCard = myDeck.deal();

				nextCard.setLocation(POS_OF_NEXT_CARD);
				postInvalidate();
			}
			// Game is over - check for top scores
			else
			{
				gameOver = true;
				this.postInvalidate();

				// Update top scores
				// If the current score is greater than or equal to
				// the original score, then change it
				 if (topPlayers.size() < 5||
						 score >= topPlayers.get(topPlayers.size()-1).getScore())
				 {
				 String name = "hannah";
				 Player currentPlayer = new Player(name, score);
				 topPlayers.add(currentPlayer);
				 Collections.sort(topPlayers);
				 
				 try
					{
					// Opens the file for output – file is on the SD card
					File sdCard = Environment.getExternalStorageDirectory();
					File directory = new File(sdCard.getAbsolutePath()
							+ "/ICS4UGames/PokerSHannah");
					if (!directory.isDirectory())
						directory.mkdirs();
					
					File file = new File(directory, "highscores.dat");
					ObjectOutputStream topScoresOut = new ObjectOutputStream(
					new FileOutputStream(file));
					topScoresOut.writeObject(topPlayers);
					topScoresOut.close();
					}
					
					catch (Exception e)
	                {
	                        e.printStackTrace();
	                }

				 }
				
			}
			postInvalidate();
			return true;

		}
		else if (event.getActionMasked() == MotionEvent.ACTION_UP)
		{
			// Only can release a Card we have
			if (currentCard != null)
			{
				// Figure out the selected row and column on the board
				int row = (clickPoint.y - TOP_OFFSET) / ROW_SPACING;
				int column = (clickPoint.x - LEFT_OFFSET) / COL_SPACING;

				// If off the grid or in a taken spot return to original
				// spot
				if (row < 0 || row > 4 || column < 0 || column > 4
						|| spotsTaken[row][column])
				{
					rowHands[originalRow].addCard(currentCard);
					colHands[originalCol].addCard(currentCard);
					placeACard(currentCard, originalRow, originalCol);
				}
				// else add to new spot
				else
				{
					rowHands[row].addCard(currentCard);
					colHands[column].addCard(currentCard);
					placeACard(currentCard, row, column);
				}

				currentCard = null;
				updateScore();
				postInvalidate();
			}
		}
		else if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
		{
			Point currentPoint = new Point(clickPoint);

	
			if (currentCard != null)
			{
	
				// We use the difference between the lastPoint and the
				// currentPoint to move the card so that the position of
				// the mouse on the card doesn't matter.
				// i.e. we can drag the card from any point on the card
				// image
				currentCard.translate(currentPoint.x - lastPoint.x,
						currentPoint.y - lastPoint.y);
				lastPoint = currentPoint;
				postInvalidate();
			}
			return true;
		}
		return false; // event not handled

	}

	/**
	 * Places a card in the given row and column on the board and marks this
	 * spot as taken
	 * 
	 * @param card the Card to place
	 * @param row the row to place the card
	 * @param column the column to place the card Precondition row and column
	 *            are on the board
	 */
	private void placeACard(Card card, int row, int column)
	{
		spotsTaken[row][column] = true;
		Point newCardPos = new Point(column * COL_SPACING + LEFT_OFFSET, row
				* ROW_SPACING + TOP_OFFSET);
		card.setLocation(newCardPos);
	}

	/** 
	 * Exits
	 */
	public void exit()
	{
		System.exit(0);
	}
	
	/** Shows a simple About Dialog
	*/
	public void showTopScore(Context context)
	{
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle("Top Scores")
	.setMessage(topPlayers.toString())
	.setCancelable(false)
	.setNeutralButton("OK", new DialogInterface.OnClickListener()
	{
	public void onClick(DialogInterface dialog, int id)
	{
	dialog.cancel();
	}
	});
	builder.show();
	}

	/** Shows a simple About Dialog
	*/
	public void showAbout(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("About")
		.setMessage("Hannah Kim")
		.setCancelable(false)
		.setNeutralButton("OK", new DialogInterface.OnClickListener()
	{
			public void onClick(DialogInterface dialog, int id)
	{
				dialog.cancel();
	}
	});
		builder.show();
	}
}
