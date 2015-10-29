package edu.ramapo.csnyder.Risk;

import java.util.ArrayList;

import edu.ramapo.csnyder.gameLogic.*;
import edu.ramapo.csnyder.Risk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

public class PlayerSelect extends Activity {
	
	public final static String GAME_SELECT = "edu.ramapo.csnyder.Risk.GAME_SELECT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_select);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_select, menu);
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
	 * Function to start next activity.  Kills current instance of the player select screen.
	 * Takes the player to the determine first player screen.
	 * Also gives the players their initial armies and shuffles the card deck for the game.
	 * Will not start a new game unless all players are determined to be valid.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void startGame(View view) {
		// The cpu selection spinners.
		Spinner cpuOne = (Spinner) findViewById(R.id.player_spin2);
		Spinner cpuTwo = (Spinner) findViewById(R.id.player_spin3);
		Spinner cpuThree = (Spinner) findViewById(R.id.player_spin4);
		Spinner cpuFour = (Spinner) findViewById(R.id.player_spin5);
		Spinner cpuFive = (Spinner) findViewById(R.id.player_spin6);
		
		// The cpu difficulty spinners.
		Spinner cpuDiffOne = (Spinner) findViewById(R.id.difficulty_spin2);
		Spinner cpuDiffTwo = (Spinner) findViewById(R.id.difficulty_spin3);
		Spinner cpuDiffThree = (Spinner) findViewById(R.id.difficulty_spin4);
		Spinner cpuDiffFour = (Spinner) findViewById(R.id.difficulty_spin5);
		Spinner cpuDiffFive = (Spinner) findViewById(R.id.difficulty_spin6);
		
		// The color spinners for all players.
		Spinner playerColor = (Spinner) findViewById(R.id.color_spin1);
		Spinner cpuColorOne = (Spinner) findViewById(R.id.color_spin2);
		Spinner cpuColorTwo = (Spinner) findViewById(R.id.color_spin3);
		Spinner cpuColorThree = (Spinner) findViewById(R.id.color_spin4);
		Spinner cpuColorFour = (Spinner) findViewById(R.id.color_spin5);
		Spinner cpuColorFive = (Spinner) findViewById(R.id.color_spin6);
		
		// Array list to ensure all cpu players are valid.
		ArrayList<Boolean> cpuValid = new ArrayList<Boolean>();

		// Check each cpu player has valid difficulty and color.
		cpuValid.add(validCpuPlayer(cpuOne, cpuDiffOne, cpuColorOne));
		cpuValid.add(validCpuPlayer(cpuTwo, cpuDiffTwo, cpuColorTwo));
		cpuValid.add(validCpuPlayer(cpuThree, cpuDiffThree, cpuColorThree));
		cpuValid.add(validCpuPlayer(cpuFour, cpuDiffFour, cpuColorFour));
		cpuValid.add(validCpuPlayer(cpuFive, cpuDiffFive, cpuColorFive));
		
		if(!cpuPlayerCheck(cpuValid)) {
			return;
		}
		
		// Hold the colors of all players.
		ArrayList<String> colorStrings = new ArrayList<String>();
		
		// Ensure two players do not have the same color.
		colorStrings.add((String) playerColor.getSelectedItem());
		colorStrings.add((String) cpuColorOne.getSelectedItem());
		colorStrings.add((String) cpuColorTwo.getSelectedItem());
		colorStrings.add((String) cpuColorThree.getSelectedItem());
		colorStrings.add((String) cpuColorFour.getSelectedItem());
		colorStrings.add((String) cpuColorFive.getSelectedItem());
		
		if(!colorCheck(colorStrings)) {
			return;
		}
		
		// Add all the players to the game.
		ArrayList<Player> players = new ArrayList<Player>();
		String humanColor = (String) playerColor.getSelectedItem();
		Player human = new Player(convertColorStringToInt(humanColor));
		players.add(human);
		
		addCpuPlayerToGame(cpuOne, cpuDiffOne, cpuColorOne, players);
		addCpuPlayerToGame(cpuTwo, cpuDiffTwo, cpuColorTwo, players);
		addCpuPlayerToGame(cpuThree, cpuDiffThree, cpuColorThree, players);
		addCpuPlayerToGame(cpuFour, cpuDiffFour, cpuColorFour, players);
		addCpuPlayerToGame(cpuFive, cpuDiffFive, cpuColorFive, players);
		
		
		// Need at least three players.
		if(players.size() < 3) {
			displayInvalidNumPlayers();
			return;
		}
		
		// Create new game.
		Game newGame = new Game(players);
		
		// Set the starting armies and shuffle the cards.
		int total = 0;
		try {
			total = newGame.determineStartArmyTotal(players.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		newGame.addInitialArmiesToPlayers(total);
		newGame.shuffleCards();
		
		// Send to determine first player screen.
		Intent intent = new Intent(PlayerSelect.this, FirstPlayer.class);
		intent.putExtra(GAME_SELECT, newGame);
		startActivity(intent);
		finish();
	}
	
	
	
		

	
	/** 
	 * Adds a computer player to the players array list.
	 * @param cpu - the spinner that ensures a cpu player has been selected.
	 * @param cpuDiff - the spinner that holds what difficulty was selected for the computer player.
	 * @param cpuColor - the spinner that holds the color selected for the computer player.
	 * @param players - the array list that holds all the players for the game.
	 * @author Charles Snyder
	 */
	private void addCpuPlayerToGame(Spinner cpu, Spinner cpuDiff, Spinner cpuColor, ArrayList<Player> players) {
		String cpuString = (String) cpu.getSelectedItem();
		if(cpuString.equals("CPU")) {
			String cpuColorString = (String) cpuColor.getSelectedItem();
			String cpuDiffString = (String) cpuDiff.getSelectedItem();
			switch(cpuDiffString) {
			case "Easy":
				Player cpuPlayer = new EasyAI(convertColorStringToInt(cpuColorString));
				players.add(cpuPlayer);
				break;
			case "Medium":
				Player medPlayer = new MediumAI(convertColorStringToInt(cpuColorString));
				players.add(medPlayer);
				break;
			case "Hard":
				Player hardPlayer = new HardAI(convertColorStringToInt(cpuColorString));
				players.add(hardPlayer);
				break;
			}
		}
	}
	
	
	
	/** 
	 * Converts a color that is in string form, to a specific integer meant to represent that color.
	 * @param color - the color selected.
	 * @return the integer that represents the color parameter.
	 * @author Charles Snyder
	 */
	private int convertColorStringToInt(String color) {
		switch(color) {
		case "Red":
			return Player.RED;
		case "Blue":
			return Player.BLUE;
		case "Green":
			return Player.GREEN;
		case "Yellow":
			return Player.YELLOW;
		case "Orange":
			return Player.ORANGE;
		case "Purple":
			return Player.PURPLE;
		default:
			return -1;
		}
	}
	
	
	
	/** 
	 * Ensures that two players do not have the same color.
	 * @param colors - all the colors that have been chosen by the players.
	 * @return True if all colors are unique, false otherwise.
	 * @author Charles Snyder
	 */
	private boolean colorCheck(ArrayList<String> colors) {
		for(int index = 0; index < colors.size() - 1; index++) {
			for(int subIndex = index + 1; subIndex < colors.size(); subIndex++) {
				if(!colors.get(index).equals("Empty") && colors.get(index).equals(colors.get(subIndex))) {
					displayInvalidColor(index, subIndex);
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	/** 
	 * Looks to see if any of the cpu players are invalid, if so it will display a dialog.
	 * @param cpuPlayers - an array list of booleans, if one of the indices is false then an invalid player exists.
	 * @return True if all players are valid, false if at least one is not.
	 * @author Charles Snyder
	 */
	private boolean cpuPlayerCheck(ArrayList<Boolean> cpuPlayers) {
		for(int index = 0; index < cpuPlayers.size(); index++) {
			if(cpuPlayers.get(index) == false) {
				displayInvalidPlayer(index);
				return false;
			}
		}
		return true;
	}
	
	
	
	/** 
	 * A dialog display that prompts the user that one of the players is invalid.
	 * @param index - the index that indicates which player was invalid.
	 * @author Charles Snyder
	 */
	private void displayInvalidPlayer(int index) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_player);
		String player = "";
		switch(index) {
		case 0:
			player = "CPU One";
			break;
		case 1:
			player = "CPU Two";
			break;
		case 2:
			player = "CPU Three";
			break;
		case 3:
			player = "CPU Four";
			break;
		case 4:
			player = "CPU Five";
			break;
		}
		builder.setMessage(player + " " + "is an invalid player.");

		builder.setNegativeButton("OK",
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
	 * Takes an index value and returns a string of the player associated with that index.
	 * @param index - the index of the player.
	 * @return - a string of a certain player, null if the index provided was invalid.
	 * @author Charles Snyder
	 */
	private String colorIndexToPlayer(int index) {
		switch(index) {
		case 0:
			return "Human";
		case 1:
			return "CPU One";
		case 2:
			return "CPU Two";
		case 3:
			return "CPU Three";
		case 4:
			return "CPU Four";
		case 5:
			return "CPU Five";
		default:
			return "null";
		}
	}
	
	
	
	/** 
	 * Displays a prompt to the user that two of the players have the same color.
	 * @param playerOne - the index of the first player with the same color.
	 * @param playerTwo - the index of the second player with the same color.
	 * @author Charles Snyder
	 */
	private void displayInvalidColor(int playerOne, int playerTwo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_color);		
		String playerOneString = colorIndexToPlayer(playerOne);
		String playerTwoString = colorIndexToPlayer(playerTwo);
		
		builder.setMessage(playerOneString + " " + "has the same color as " + playerTwoString);

		builder.setNegativeButton("OK",
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
	 * Displays a prompt to the user that there must be at least three players in the game.
	 * @author Charles Snyder
	 */
	private void displayInvalidNumPlayers() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_num_players);		
		
		builder.setMessage(R.string.invalid_num_players_message);

		builder.setNegativeButton("OK",
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
	 * Determines if a cpu player was selected, that it has a difficulty and a color selected as well.
	 * @param player - the spinner that holds whether a cpu player was selected.
	 * @param difficulty - the spinner that holds whether a difficulty was selected.
	 * @param color - the spinner that holds whether a color was selected.
	 * @return True if a valid combination of spinner selections exists, false otherwise.
	 * @author Charles Snyder
	 */
	private boolean validCpuPlayer(Spinner player, Spinner difficulty, Spinner color) {
		String playerString = (String) player.getSelectedItem();
		String diffString = (String) difficulty.getSelectedItem();
		String colorString = (String) color.getSelectedItem();
		
		if(playerString.equals("CPU")) {
			// A cpu player was selected make sure it has a difficulty and color.
			if(!diffString.equals("N/A")) {
				if(!colorString.equals("Empty")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			// A cpu player was not selected make sure it does not have a difficulty or color.
			if(diffString.equals("N/A")) {
				if(colorString.equals("Empty")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
}
