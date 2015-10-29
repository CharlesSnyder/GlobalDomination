package edu.ramapo.csnyder.Risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.ramapo.csnyder.gameLogic.Game;
import edu.ramapo.csnyder.Risk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	public final static String EXTRA_GAME = "edu.ramapo.csnyder.Risk.GAME";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/** 
	 * Function to start next activity.  Kills current instance of the main screen.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void newGame(View view) {
		Intent intent = new Intent(MainActivity.this, PlayerSelect.class);
		startActivity(intent);
		finish();
	}
	

	/** 
	 * Launches a browser to view the rules of the game.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void rules(View view) {
		String url = "http://www.hasbro.com/common/instruct/risk.pdf";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	
	/**
	 * Loads a game from internal location on device specific to application.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder					   
	 */
	public void loadGame(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.load);
		//Get files located in internal app folder.
		final String[] myFiles = getApplicationContext().fileList();
		builder.setItems(myFiles, new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				try {
					//BufferedReader which will be used to read game data from.
					BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(myFiles[which])));
				    Game loadedGame = new Game();
				    loadedGame.loadGame(inputReader);
				    //Send to main play screen.
					Intent intent = new Intent(MainActivity.this, PlayScreen.class);
					intent.putExtra(EXTRA_GAME, loadedGame);
					startActivity(intent);
				}
				catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						dialog.cancel();
					}
				});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	* Delete a saved game from the internal save location of app.
	* @param view - view of the screen, base class for widgets.
	* @author Charles Snyder					   
	*/
	public void deleteSavedGames(View view) {
		
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] myFiles = getApplicationContext().fileList();
		// Chain together various setter methods to set the dialog characteristics.
		builder.setTitle(R.string.dialog_title);
		builder.setItems(myFiles,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position of the selected item.
						File dir = getFilesDir();
						File file = new File(dir, myFiles[which]);
						file.delete();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						dialog.cancel();
					}
				});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
}
