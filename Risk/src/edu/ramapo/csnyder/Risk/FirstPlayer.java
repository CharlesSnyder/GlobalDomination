package edu.ramapo.csnyder.Risk;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ramapo.csnyder.gameLogic.*;
import edu.ramapo.csnyder.Risk.R;

public class FirstPlayer extends Activity {
	
	private static Game currentGame;
	public final static String GAME_FIRST_PLAYER = "edu.ramapo.csnyder.Risk.GAME_FIRST_PLAYER";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_player);
		currentGame = new Game();
		Intent intent = getIntent();
		currentGame = intent.getExtras().getParcelable(PlayerSelect.GAME_SELECT);
		
		int numPlayers = currentGame.getPlayersArray().size();
		try {
			displayPlayers(numPlayers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_player, menu);
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
	 * Function to start next activity.  Kills current instance of the first player screen.
	 * Each player rolls a die and the result is displayed, the winner is then determined and announced.
	 * After a three second pause the next activity is started.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void rollDice(View view) {
		ArrayList<Integer> numbers = currentGame.playersRollForTurn();
		currentGame.determineFirstPlayer(numbers);
		displayDice(numbers);
		displayWinner();
		findViewById(R.id.roll_die_button).setVisibility(View.GONE);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			public void run() {
				Intent intent = new Intent(FirstPlayer.this, PlayScreen.class);
				intent.putExtra(GAME_FIRST_PLAYER, currentGame);
				startActivity(intent);
				finish();
			}
		}, 3000);
		
		
	}
	
	
	/** 
	 * Displays the winner of who won the first player roll to a text view dialog.
	 * @author Charles Snyder
	 */
	public void displayWinner() {
		int winner = currentGame.getFirstFromTurnDeque();
		int winnerIndex = currentGame.findPlayerIndex(winner);
		
		TextView result = (TextView) findViewById(R.id.result_display);
		
		switch(winnerIndex) {
		case 0:
			result.setText("Player one goes first.");
			break;
		case 1:
			result.setText("Player two goes first.");
			break;
		case 2:
			result.setText("Player three goes first.");
			break;
		case 3:
			result.setText("Player four goes first.");
			break;
		case 4:
			result.setText("Player five goes first.");
			break;
		case 5:
			result.setText("Player six goes first.");
			break;
		}
	}
	
	
	/** 
	 * Displays the dice that were rolled to the screen.
	 * @param numbers - the numbers of the dice that were rolled.
	 * @author Charles Snyder
	 */
	public void displayDice(ArrayList<Integer> numbers) {
		ImageView playerOne = (ImageView) findViewById(R.id.player_one_die);
		ImageView playerTwo = (ImageView) findViewById(R.id.player_two_die);
		ImageView playerThree = (ImageView) findViewById(R.id.player_three_die);
		ImageView playerFour = (ImageView) findViewById(R.id.player_four_die);
		ImageView playerFive = (ImageView) findViewById(R.id.player_five_die);
		ImageView playerSix = (ImageView) findViewById(R.id.player_six_die);
		
		try {
			switch(currentGame.getPlayersArray().size()) {
			case 6:
				selectDieToDisplay(playerOne, numbers.get(0));
				selectDieToDisplay(playerTwo, numbers.get(1));
				selectDieToDisplay(playerThree, numbers.get(2));
				selectDieToDisplay(playerFour, numbers.get(3));
				selectDieToDisplay(playerFive, numbers.get(4));
				selectDieToDisplay(playerSix, numbers.get(5));
				break;
			case 5:
				selectDieToDisplay(playerOne, numbers.get(0));
				selectDieToDisplay(playerTwo, numbers.get(1));
				selectDieToDisplay(playerThree, numbers.get(2));
				selectDieToDisplay(playerFour, numbers.get(3));
				selectDieToDisplay(playerFive, numbers.get(4));
				break;
			case 4:
				selectDieToDisplay(playerOne, numbers.get(0));
				selectDieToDisplay(playerTwo, numbers.get(1));
				selectDieToDisplay(playerThree, numbers.get(2));
				selectDieToDisplay(playerFour, numbers.get(3));
				break;
			case 3:
				selectDieToDisplay(playerOne, numbers.get(0));
				selectDieToDisplay(playerTwo, numbers.get(1));
				selectDieToDisplay(playerThree, numbers.get(2));
				break;
			case 2:
				selectDieToDisplay(playerOne, numbers.get(0));
				selectDieToDisplay(playerTwo, numbers.get(1));
				break;
			}
		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	
	
	/** 
	 * Associates the appropriate die image with an integer value.
	 * @param image - the image view space where the picture is displayed.
	 * @param val - the number of the die rolled.
	 * @throw invalidDie - if a number less than 0 or greater than 5 is passed in.
	 * @author Charles Snyder
	 */
	public static void selectDieToDisplay(ImageView image, int val) throws Exception {
		switch(val) {
		case 5:
			image.setBackgroundResource(R.drawable.dice_six);
			break;
		case 4:
			image.setBackgroundResource(R.drawable.dice_five);
			break;
		case 3:
			image.setBackgroundResource(R.drawable.dice_four);
			break;
		case 2:
			image.setBackgroundResource(R.drawable.dice_three);
			break;
		case 1:
			image.setBackgroundResource(R.drawable.dice_two);
			break;
		case 0:
			image.setBackgroundResource(R.drawable.dice_one);
			break;
		default:
			Exception invalidDie = new Exception("Invalid die number");
			throw invalidDie;
		}
	}
	
	
	
	
	/** 
	 * Displays the appropriate number of player texts based on number of players in the game.
	 * @param numPlayers - the number of players in the game.
	 * @throw invalidNumPlayers - if an invalid number of players was specified.
	 * @author Charles Snyder
	 */
	public void displayPlayers(int numPlayers) throws Exception {
		switch(numPlayers) {
		case 6:
			findViewById(R.id.player_one).setVisibility(View.VISIBLE);
			findViewById(R.id.player_two).setVisibility(View.VISIBLE);
			findViewById(R.id.player_three).setVisibility(View.VISIBLE);
			findViewById(R.id.player_four).setVisibility(View.VISIBLE);
			findViewById(R.id.player_five).setVisibility(View.VISIBLE);
			findViewById(R.id.player_six).setVisibility(View.VISIBLE);
			break;
		case 5:
			findViewById(R.id.player_one).setVisibility(View.VISIBLE);
			findViewById(R.id.player_two).setVisibility(View.VISIBLE);
			findViewById(R.id.player_three).setVisibility(View.VISIBLE);
			findViewById(R.id.player_four).setVisibility(View.VISIBLE);
			findViewById(R.id.player_five).setVisibility(View.VISIBLE);
			break;
		case 4:
			findViewById(R.id.player_one).setVisibility(View.VISIBLE);
			findViewById(R.id.player_two).setVisibility(View.VISIBLE);
			findViewById(R.id.player_three).setVisibility(View.VISIBLE);
			findViewById(R.id.player_four).setVisibility(View.VISIBLE);
			break;
		case 3:
			findViewById(R.id.player_one).setVisibility(View.VISIBLE);
			findViewById(R.id.player_two).setVisibility(View.VISIBLE);
			findViewById(R.id.player_three).setVisibility(View.VISIBLE);
			break;
		case 2:
			findViewById(R.id.player_one).setVisibility(View.VISIBLE);
			findViewById(R.id.player_two).setVisibility(View.VISIBLE);
			break;
		default:
			Exception invalidNumPlayers = new Exception("Invalid number players");
			throw invalidNumPlayers;
		}
	}
}
