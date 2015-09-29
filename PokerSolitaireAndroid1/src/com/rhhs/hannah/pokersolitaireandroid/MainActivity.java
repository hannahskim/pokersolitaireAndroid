package com.rhhs.hannah.pokersolitaireandroid;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private TableView table;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		table = new TableView (this);
		setContentView (table);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Code to handle a menu selection
	 * @param item the menu item selected
	 */
	public boolean onOptionsItemSelected(MenuItem item)
	{
        // Respond to users menu selection
	   switch (item.getItemId())
        {
		case R.id.new_game:
			table.newGame();
			return true;
			
		case R.id.exit:
			table.exit();
			return true;
			
		case R.id.top_score:
			table.showTopScore(this);
			return true;
			
		case R.id.about:
			table.showAbout(this);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
	    }
	   

	 }

}
