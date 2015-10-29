package edu.ramapo.csnyder.Risk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.ramapo.csnyder.gameLogic.*;
import edu.ramapo.csnyder.Risk.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View.OnClickListener;

public class PlayScreen extends Activity {
	public final static String GAME_PLAY_SCREEN = "edu.ramapo.csnyder.Risk.GAME_PLAY_SCREEN";
	private final static float selected = (float) 0.5;
	private final static float unselected = (float) 1.0;
	private static Game currentMainGame;
	private static Player currentPlayer;
	private static Cpu cpuPlayer;
	private boolean userDeployPhase = false;
	private boolean userAttackPhase = false;
	private boolean isFromCards = false;
	private int globalOriginCountry = -1;
	private int globalDestCountry = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_screen);

		// Create all on touch listeners.
		initializeOnTouchListeners();

		// Get the the game from either the card screen, the first player screen, or the main screen from a load.
		currentMainGame = new Game();
		Intent intent = getIntent();
		currentMainGame = intent.getExtras().getParcelable(
				FirstPlayer.GAME_FIRST_PLAYER);

		if (currentMainGame == null) {
			currentMainGame = intent.getExtras().getParcelable(
					MainActivity.EXTRA_GAME);
		}
		if (currentMainGame == null) {
			currentMainGame = intent.getExtras().getParcelable(
					CardActivity.GAME_CARDS);
			isFromCards = true;
			userDeployPhase = true;
		}
		
		updateCardGameInfo();
		updateUserInfoDisplay();
		refreshBoard();

		// Phase one is the country selection phase of the game.
		if (currentMainGame.getIsPhaseOne() == true) {
			boolean cpuTurn = true;
			displayPhaseOne();
			// If the cpu is the next player, have it execute it's turn.
			while (cpuTurn) {
				int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
				if (currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
					cpuFirstPhaseTurn(playerIndex);
					if (currentMainGame.getBoard().isBoardFull() == true) {
						currentMainGame.setIsPhaseOne(false);
						currentMainGame.setIsPhaseTwo(true);
						initializePhaseTwo();
						break;
					}
				}
				// Not the cpu's turn.
				else {
					currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
					TextView instruct = (TextView) findViewById(R.id.instructions);
					instruct.setText(R.string.phase_one_instructions);
					instruct.setVisibility(View.VISIBLE);
					cpuTurn = false;
				}
			}
		} 
		// Phase two is where the the players are placing their starting armies after all countries have been selected.
		else if(currentMainGame.getIsPhaseTwo() == true) {
			initializePhaseTwo();
		}
		// Phase three is the players turn, either allocating their armies or attacking.
		else if(currentMainGame.getIsPhaseThree() == true) {
			initializePhaseThree();
			int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
			if(currentMainGame.getPlayersArray().get(playerIndex).getArmyTotal() != 0 && currentMainGame.getPlayersArray().get(playerIndex).getDidCaptureTerritory() == false) {
				userDeployPhase = true;
			}
			else if(isFromCards != true) {
				userAttackPhase = true;
				userDeployPhase = false;
			}
			else if(isFromCards == true && currentMainGame.getPlayersArray().get(playerIndex).getDidCaptureTerritory() == true) {
				userAttackPhase = true;
				userDeployPhase = false;
				findViewById(R.id.view_cards_button).setVisibility(View.GONE);
			}
		}
		// Phase four is the fortify country portion of the player's turn.
		else if(currentMainGame.getIsPhaseFour() == true) {
			int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
			currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
			
			TextView instruct = (TextView) findViewById(R.id.instructions);
			instruct.setText(R.string.phase_four_instructions);
			instruct.setVisibility(View.VISIBLE);
			Button endTurnButton = (Button) findViewById(R.id.end_turn_button);
			endTurnButton.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.save_game_action) {
			saveGame();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	* Saves game internally to app specific location.  All relevant game data is written
	* to a string which is written to a fileoutput stream.
	* @author Charles Snyder				   
	*/
	public void saveGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.save_game_name);
		//Used to get file name.
		final EditText input = new EditText(this);
		builder.setView(input);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//User confirmed to save the file.
				String fileName = input.getText().toString();
				String savedGame = currentMainGame.saveGame();

				try {
					//Writes a save file internally, will overwrite any existing file with same name.
					FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
					fos.write(savedGame.getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
	* This is the player's phase one turn, it has to account for the possibility of the next player
	* being a computer player and if so execute their turn as well.  It places one unit in the country the
	* player chose and sets the countries owner status to the color of the player.
	* @param country - the country the player has chosen to occupy.
	* @return True if the move executed, false if the country selection was invalid.
	* @author Charles Snyder				   
	*/
	public boolean phaseOneAction(int country) {
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		try {
			currentPlayer.placeUnitInitial(currentMainGame.getBoard(), country);	
		} catch (Exception e) {
			if(e.getMessage().equals("Country Occupied")) {
				displayPhaseCountryTaken();
				return false;
			}
		}
		updateUserInfoDisplay();
		currentMainGame.endPlayerTurn(currentMainGame.getFirstFromTurnDeque());

		if (currentMainGame.getBoard().isBoardFull() == true) {
			currentMainGame.setIsPhaseOne(false);
			currentMainGame.setIsPhaseTwo(true);
			initializePhaseTwo();
		} else {
			// See if next player is computer.
			boolean cpuTurn = true;
			while (cpuTurn) {
				playerIndex = currentMainGame.findPlayerIndex(currentMainGame
						.getFirstFromTurnDeque());
				if (currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
					cpuFirstPhaseTurn(playerIndex);
					if (currentMainGame.getBoard().isBoardFull() == true) {
						currentMainGame.setIsPhaseOne(false);
						currentMainGame.setIsPhaseTwo(true);
						initializePhaseTwo();
						break;
					}
				} 
				else {
					cpuTurn = false;
				}
			}
		}
		return true;
	}
	
	
	
	/**
	* This is to set the screen up for phase two, that is make sure the proper dialogs and buttons are visible
	* to instruct the player what to do.  Phase two is placing your remaining initial armies to the countries you own.
	* When this gets called if the first player is a computer player, it's turn will also be executed.
	* @author Charles Snyder				   
	*/
	public void initializePhaseTwo() {
		displayPhaseTwo();
		
		TextView instruct = (TextView) findViewById(R.id.instructions);
		instruct.setText(R.string.phase_two_instructions);
		instruct.setVisibility(View.VISIBLE);
		
		// Get next player
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		
		// See if player is computer.
		if(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
			boolean cpuTurn = true;
			
			// Execute all cpu player's turns as long as the next player is a computer player.
			while(cpuTurn) {
				playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
				if(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
					cpuSecondPhaseTurn(playerIndex);
					
					// Check for end of phase two.
					if (currentMainGame.doPlayersHaveArmies() == false) {
						currentMainGame.setIsPhaseTwo(false);
						currentMainGame.setIsPhaseThree(true);
						
						// Human specific instructions.
						if(isNextPlayerHuman()) {
							int playerCurrentIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
							humanBeginPhaseThree(playerCurrentIndex);
							updateUserInfoDisplay();
						}
						userDeployPhase = true;
						initializePhaseThree();
						displayPhaseThree();
						break;
					}
				}
				// Next player was human, break out of cpu loop.
				else {
					cpuTurn = false;
				}
			}
		}
	}
	
	
	/**
	* Determine if the next player in the turn list is a human.
	* @return True if the next player is a human, false otherwise.
	* @author Charles Snyder				   
	*/
	public boolean isNextPlayerHuman() {
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		if(!(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	* Display the proper instructions and buttons for phase three section of the game.  Phase three can either 
	* be army allocation or attacking, so it has to account for both scenarios.
	* @author Charles Snyder				   
	*/
	public void initializePhaseThree() {
		TextView instruct = (TextView) findViewById(R.id.instructions);
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		
		// See if current player is human.
		if(!(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu)) {
			if((currentMainGame.getPlayersArray().get(playerIndex).getArmyTotal() != 0 && currentMainGame.getPlayersArray().get(playerIndex).getDidCaptureTerritory() == false) || (isFromCards == true  && currentMainGame.getPlayersArray().get(playerIndex).getDidCaptureTerritory() == false)) {
				instruct.setText(R.string.phase_three_instructions_placement);
			}
			else {
				instruct.setText(R.string.phase_three_instructions_capture);
			}
		}
		else {
			instruct.setText(R.string.phase_three_instructions_placement);
		}
		
		instruct.setVisibility(View.VISIBLE);
		Button viewCards = (Button) findViewById(R.id.view_cards_button);
		viewCards.setVisibility(View.VISIBLE);
		Button endTurnButton = (Button) findViewById(R.id.end_turn_button);
		endTurnButton.setVisibility(View.VISIBLE);
	}
	
	
	/**
	* The computer players' turn function.  It takes care of army allocation, trading in any cards, attacking moves, and fortifying positions.
	* @param playerIndex - the index in the players array of the current player.
	* @param beginOfTurn - indicates whether this is the beginning of the cpu's turn, which would mean army allocation and card trade-ins.
	* Otherwise it is the attack and fortify portion of its turn.
	* @author Charles Snyder				   
	*/
	private void cpuPhaseThreeTurn(int playerIndex, boolean beginOfTurn) {
		// Make sure player is a computer player.
		if(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
			cpuPlayer = (Cpu) currentMainGame.getPlayersArray().get(playerIndex);
			
			// Check if beginning of turn and if so perform beginning of turn actions.
			if(beginOfTurn == true) {
				currentMainGame.addBaseArmyStartTurn(cpuPlayer);
				currentMainGame.addArmyContinentBonus(cpuPlayer);
				currentMainGame.addArmyFromCardsCPU(cpuPlayer);
				updateCardGameInfo();
				
				// Placement phase.
				while(cpuPlayer.getArmyTotal() > 0) {
					try {
						cpuPlayer.placeUnits(currentMainGame.getBoard(), -1, -1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			ArrayList<Integer> attackLocations = new ArrayList<Integer>();
			
			// Attack phase.
			boolean didCapture = false;
			boolean cpuTurn = true;
			while(cpuTurn) {
				attackLocations = cpuPlayer.attackCoordinates(currentMainGame.getBoard());
				if(attackLocations.isEmpty() == false) {
					
					// Execute move.
					ArrayList<Integer> attackDice = new ArrayList<Integer>(cpuPlayer.cpuDiceRollAttack(currentMainGame.getBoard(), attackLocations.get(0)));
					int defenderColor = currentMainGame.getDefendingPlayerColor(attackLocations.get(1));
					int defenderIndex = currentMainGame.findPlayerIndex(defenderColor);
					
					//Check to see if defender is a human or computer player.
					if(currentMainGame.getPlayersArray().get(defenderIndex) instanceof Cpu) {
						Cpu defender = (Cpu) currentMainGame.getPlayersArray().get(currentMainGame.findPlayerIndex(defenderColor));
						ArrayList<Integer> defendDice = new ArrayList<Integer>(defender.cpuDiceRollDefend(currentMainGame.getBoard(), attackLocations.get(1)));
						currentMainGame.attackResult(attackLocations.get(0), attackLocations.get(1), attackDice, defendDice);
						
						// Check to see if the current player captured the territory it attacked.
						if(currentMainGame.canCaptureTerritory(attackLocations.get(1)) == true) {
							try {
								currentMainGame.getBoard().setBoardSpaceOwner(attackLocations.get(1), cpuPlayer.getColor());
								cpuPlayer.addCountryControl(attackLocations.get(1));
								defender.removeCountryControl(attackLocations.get(1));
								didCapture = true;
								cpuPlayer.moveArmiesToCaptureCountry(currentMainGame.getBoard(), attackLocations.get(0), attackLocations.get(1), attackDice.size());					
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						refreshBoard();
					}
					// Human defender.
					else {
						cpuTurn = false;
						// Notify player it is being attacked.
						displayDefendDialog(attackDice, attackLocations, defenderColor, cpuPlayer.getColor());
						try {
							if(currentMainGame.getBoard().getBoardSpaceOwner(attackLocations.get(1)) == cpuPlayer.getColor()) {
								didCapture = true;
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						refreshBoard();
					}
				}
				// Attack phase is over.
				else {
					cpuTurn = false;
					refreshBoard();
					// Check if any players got eliminated during this turn.
					ArrayList<Player> eliminated = currentMainGame.eliminatePlayersFromGame();
					
					// Make sure game is not over.
					if(currentMainGame.getPlayersArray().size() > 1) {
						
						// If this list is populated that means at least one player got eliminated.
						if(eliminated.isEmpty() == false) {
							
							// Collect cards from eliminated players.
							for(int index = 0; index < eliminated.size(); index++) {
								displayPlayerEliminated(eliminated.get(index).getColor());
								ArrayList<Card> cards = new ArrayList<Card>(eliminated.get(index).getCardHand());
								for(int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
									cpuPlayer.addCardHand(cards.get(cardIndex));
								}
							}
							// Must trade in cards if now have six or more.
							if(cpuPlayer.getCardHand().size() >= 6) {
								currentMainGame.addArmyFromCardsCPU(cpuPlayer);
							}
						}
						// Take a card for capturing a territory if they did.
						if(didCapture == true) {
							if(currentMainGame.getCardDeck().getDeckSize() != 0) {
								cpuPlayer.takeCard(currentMainGame.getCardDeck());
							}
							else {
								currentMainGame.resetCardDeck();
								cpuPlayer.takeCard(currentMainGame.getCardDeck());
							}
						}
						
						// Fortify position phase.
						cpuPlayer.fortifyPosition(currentMainGame.getBoard());
						refreshBoard();
					
						currentMainGame.endPlayerTurn(currentMainGame.getFirstFromTurnDeque());
						
						if(isNextPlayerHuman()) {
							int playerCurrentIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
							humanBeginPhaseThree(playerCurrentIndex);
							updateUserInfoDisplay();
						}
						userDeployPhase = true;
						initializePhaseThree();
						displayPhaseThree();	
					}
				}
			}
		}
	}
	
	
	
	/**
	* The human player's phase two turn, it places an additional army into the selected country.
	* @param country - the country to place the army into.
	* @return True if the placement is successful, false if an invalid country was chosen.
	* @author Charles Snyder				   
	*/
	public boolean phaseTwoAction(int country) {
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		
		// Place unit.
		try {
			currentPlayer.placeUnitAllOccupied(currentMainGame.getBoard(), country);
		} catch (Exception e) {
			if(e.getMessage().equals("Invalid Country")) {
				displayNotOwnedCountry();
				return false;
			}
		}
		updateUserInfoDisplay();
		currentMainGame.endPlayerTurn(currentMainGame.getFirstFromTurnDeque());
		
		// Check to see if phase two is over.
		if(currentMainGame.doPlayersHaveArmies() == false) {
			currentMainGame.setIsPhaseTwo(false);
			currentMainGame.setIsPhaseThree(true);
			if(isNextPlayerHuman()) {
				int playerCurrentIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
				humanBeginPhaseThree(playerCurrentIndex);
				updateUserInfoDisplay();
			}
			userDeployPhase = true;
			initializePhaseThree();
			displayPhaseThree();
		}
		// Phase two not over.
		else {
			boolean cpuTurn = true;
			while(cpuTurn) {
				playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
				if(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
					
					// Perform cpu phase two turn.
					cpuSecondPhaseTurn(playerIndex);
					
					// Check to see if phase two is over.
					if(currentMainGame.doPlayersHaveArmies() == false) {
						currentMainGame.setIsPhaseTwo(false);
						currentMainGame.setIsPhaseThree(true);

						if(isNextPlayerHuman()) {
							int playerCurrentIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
							humanBeginPhaseThree(playerCurrentIndex);
							updateUserInfoDisplay();
						}
						userDeployPhase = true;
						initializePhaseThree();
						displayPhaseThree();
						break;
					}
				}
				else {
					cpuTurn = false;
				}
			}
		}
		return true;
	}
	
	
	
	/**
	* The computer player's second phase turn execution. It places a unit into a computer player's occupied territory.
	* @param playerIndex - the index of the current player in the players array.
	* @author Charles Snyder				   
	*/
	private void cpuSecondPhaseTurn(int playerIndex) {
		try {
			currentMainGame.getPlayersArray().get(playerIndex).placeUnitAllOccupied(currentMainGame.getBoard(), -1);
			refreshBoard();
		} catch (Exception e) {
			e.printStackTrace();
		}
		currentMainGame.endPlayerTurn(currentMainGame.getFirstFromTurnDeque());
	}

	
	/**
	* The computer player's first phase turn execution. It places a unit into an empty territory.
	* @param playerIndex - the index of the current player in the players array.
	* @author Charles Snyder				   
	*/
	private void cpuFirstPhaseTurn(int playerIndex) {
		try {
			currentMainGame.getPlayersArray().get(playerIndex).placeUnitInitial(currentMainGame.getBoard(), -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int size = currentMainGame.getPlayersArray().get(playerIndex).getCountriesControlled().size();
		int country = currentMainGame.getPlayersArray().get(playerIndex).getCountriesControlled().get(size - 1);
		updateCountryText(country, 1);
		determineCountryColor(country, currentMainGame.getFirstFromTurnDeque());
		currentMainGame.endPlayerTurn(currentMainGame.getFirstFromTurnDeque());
	}

	
	/**
	* Displays a dialog to prompt the user at the start of the country selection phase.
	* @author Charles Snyder				   
	*/
	private void displayPhaseOne() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.phase_one);

		builder.setMessage(R.string.phase_one_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Displays a dialog to prompt the user at the start of the phase where players place their initial armies.
	* @author Charles Snyder				   
	*/
	private void displayPhaseTwo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.phase_two);

		builder.setMessage(R.string.phase_two_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	/**
	* Displays a dialog to prompt the user that the country they selected is already taken.
	* @author Charles Snyder				   
	*/
	private void displayPhaseCountryTaken() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_selection);

		builder.setMessage(R.string.country_taken);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Displays a dialog to prompt the user that phase three is beginning and whoever's turn it is
	* will begin their turn.
	* @author Charles Snyder				   
	*/
	private void displayPhaseThree() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.phase_three);
		String message = "";
		refreshBoard();
		message = convertColorToString(currentMainGame.getFirstFromTurnDeque());
		
		builder.setMessage(message + " player will start their turn");

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
				int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
				if(currentMainGame.getPlayersArray().get(playerIndex) instanceof Cpu) {
					cpuPhaseThreeTurn(playerIndex, true);
					refreshBoard();
				}
				//Human Turn
				else {
					refreshBoard();					
				}
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Performs the beginning of turn actions for the human player.  It adds their armies and displays
	* a breakdown of how they earned that number.
	* @author Charles Snyder				   
	*/
	public void humanBeginPhaseThree(int playerIndex) {
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		int baseArmy = currentMainGame.addBaseArmyStartTurn(currentPlayer);
		int contBonus = currentMainGame.addArmyContinentBonus(currentPlayer);
		displayArmyBreakdown(baseArmy, contBonus);
	}
	
	
	
	/**
	* Displays a dialog that the country the player selected is not one they own.
	* @author Charles Snyder				   
	*/
	private void displayNotOwnedCountry() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_selection);

		builder.setMessage(R.string.not_owned_country);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Displays a dialog that the player has no more armies to place.
	* @author Charles Snyder				   
	*/
	private void displayNoArmiesRemaining() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.no_armies_remaining);

		builder.setMessage(R.string.no_armies_remaining_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Displays a dialog that the country the player has chosen to attack is an invalid move.
	* @author Charles Snyder				   
	*/
	private void displayInvalidAttackMove() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_attack_move);

		builder.setMessage(R.string.invalid_attack_move_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Displays a dialog that the country the player has chosen to fortify is an invalid move.
	* @author Charles Snyder				   
	*/
	private void displayInvalidFortifyMove() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_attack_move);

		builder.setMessage(R.string.invalid_fortify_move_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Updates the information key with the player's current game information.  That is army count, countries
	* controlled, and continents controlled.
	* @author Charles Snyder				   
	*/
	private void updateUserInfoDisplay() {
		TextView armyValue = (TextView) findViewById(R.id.user_army_value);
		TextView countryValue = (TextView) findViewById(R.id.country_controlled_value);
		TextView contValue = (TextView) findViewById(R.id.cont_controlled_value);
		
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		
		armyValue.setText(Integer.toString(currentMainGame.getPlayersArray().get(playerIndex).getArmyTotal()));
		countryValue.setText(Integer.toString(currentMainGame.getPlayersArray().get(playerIndex).getCountriesControlled().size()));
		contValue.setText(Integer.toString(currentMainGame.getPlayersArray().get(playerIndex).getContinentsControlled()));
	}
	
	
	
	/**
	* Updates the information key with the player's current game information.  That is army count, countries
	* controlled, and continents controlled.
	* @author Charles Snyder				   
	*/
	private void updateCardGameInfo() {
		TextView numCards = (TextView) findViewById(R.id.num_cards_value);
		TextView cardVal = (TextView) findViewById(R.id.value_cards_value);
		
		numCards.setText(Integer.toString(currentMainGame.getTradedInCardTotal()));
		cardVal.setText(Integer.toString(currentMainGame.getTradeInCardValue()));
	}
	
	
	
	/**
	* During the player's army deployment phase of their turn, this dialog appears when a country is
	* selected to place their armies.
	* @param country - the country that was selected.
	* @return True if the selection was valid, false otherwise.
	* @author Charles Snyder				   
	*/
	public boolean phaseThreeDeploy(int country) {
		// Get current player.
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		
		// Make sure the player controls the country.
		if(currentPlayer.isCountryControlled(country) == false) {
			displayNotOwnedCountry();
			return false;
		}
		// Make sure the player has armies to place.
		if(currentPlayer.getArmyTotal() == 0) {
			displayNoArmiesRemaining();
			return false;
		}
		
		displayArmyPlacementDialog(currentPlayer.getArmyTotal(), country);

		return true;
	}
	
	
	/**
	* Determines how many armies to let the player choose from to place in the country they selected
	* for the army placement phase.
	* @param armyCount - the number of armies of the current player.
	* @param armySelect - the spinner that holds the number of armies to choose from for the player to place.
	* @author Charles Snyder				   
	*/
	public void addItemsOnUnitPlacementSpinner(int armyCount, Spinner armySelect) {
		ArrayList<String> list = new ArrayList<String>();
		for(int index = 0; index <= armyCount; index++) {
			list.add(Integer.toString(index));
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			armySelect.setAdapter(dataAdapter);
	}
	
	
	/**
	* Determines how many armies to let the player choose from to place in the country they selected
	* during the fortify position phase.
	* @param armyCount - the number of armies of the current player.
	* @param armySelect - the spinner that holds the number of armies to choose from for the player to place.
	* @author Charles Snyder				   
	*/
	public void addItemsOnFortifyMovementSpinner(int armyCount, Spinner armySelect) {
		ArrayList<String> list = new ArrayList<String>();
		for(int index = 0; index < armyCount; index++) {
			list.add(Integer.toString(index));
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			armySelect.setAdapter(dataAdapter);
	}
	
	
	
	/**
	* Sets the attack from country as well as executing an attack move if the attack from country was selected,
	* and another country was then selected to attack.
	* @param country - the country the player selected.
	* @author Charles Snyder				   
	*/
	public void selectAttackFromCountry(int country) {
		// Get the current player.
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		
		// Check to see if attack from country was selected yet.
		if(globalOriginCountry == -1) {
			
			// Make sure the player owns the attack from country.
			if(currentPlayer.isCountryControlled(country) == true) {
				globalOriginCountry = country;
				selectCountry(country);
			}
			else {
				displayNotOwnedCountry();
				return;
			}
		}
		
		// Deselect the attack from country.
		else if(globalOriginCountry == country) {
			globalOriginCountry = -1;
			selectCountry(country);
		}
		else {
			// This means a destination country was selected.
			// So the country passed in must be the attack destination country.
			globalDestCountry = country;
			if(currentPlayer.canAttackCountry(globalOriginCountry, globalDestCountry, currentMainGame.getBoard()) == false) {
				displayInvalidAttackMove();
				resetSelectedButtons();
				return;
			}
			else {
				int defenderColor;
				try {
					defenderColor = currentMainGame.getBoard().getBoardSpaceOwner(globalDestCountry);
					displayAttackDialog(globalDestCountry, globalOriginCountry, defenderColor, currentPlayer.getColor());
				} catch (Exception e) {
					e.printStackTrace();
				}
					
			}
		}
	}
	
	
	
	/**
	* Sets the fortify from country as well as executing a fortify move if the fortify from country was selected,
	* and another country was then selected to fortify.
	* @param country - the country the player selected.
	* @author Charles Snyder				   
	*/
	public void selectFortifyCountry(int country ) {
		
		// Get the current player.
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		
		// See if a fortify from country was selected.
		if(globalOriginCountry == -1) {
			
			// Make sure the player owns the country.
			if(currentPlayer.isCountryControlled(country) == true) {
				try {
					if(currentMainGame.getBoard().getBoardSpaceArmy(country) < 2) {
						displayInvalidFortifyMove();
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				globalOriginCountry = country;
				selectCountry(country);
			}
			else {
				displayNotOwnedCountry();
				return;
			}
		}
		else if(globalOriginCountry == country) {
			globalOriginCountry = -1;
			selectCountry(country);
		}
		else {
			// Destination selected.
			if(currentPlayer.isCountryControlled(country) == true) {
				globalDestCountry = country;
				if(currentPlayer.isInFortifyTerritory(currentMainGame.getBoard(), globalOriginCountry, globalDestCountry, new ArrayList<Integer>()) == true) {
					try {
						displayFortifyArmyMovementDialog(currentMainGame.getBoard().getBoardSpaceArmy(globalOriginCountry), globalOriginCountry, globalDestCountry);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					displayInvalidFortifyMove();
					return;
				}
			}
			else {
				displayNotOwnedCountry();
				return;
			}
		}
	}
	
	
	
	
	/**
	* Determines the action that should be taken when a country is selected during any phase of the game.
	* @param country - the country the player selected.
	* @author Charles Snyder				   
	*/
	private void buttonSelectionAction(int country) {
		if (currentMainGame.getIsPhaseOne() == true) {
			if(phaseOneAction(country) == true) {
				updateCountryText(country, 1);
				determineCountryColor(country, currentPlayer.getColor());
			}	
		}
		else if(currentMainGame.getIsPhaseTwo() == true) {		
			if(phaseTwoAction(country) == true) {
				refreshBoard();
			}
		}
		else if(currentMainGame.getIsPhaseThree() == true && userDeployPhase == true) {
			phaseThreeDeploy(country);
		}
		else if(currentMainGame.getIsPhaseThree() == true && userAttackPhase == true) {
			selectAttackFromCountry(country);
		}
		else if(currentMainGame.getIsPhaseFour() == true) {
			selectFortifyCountry(country);
		}
	}

	
	/**
	* Perform a button action on alaska button.
	* @author Charles Snyder				   
	*/
	public void alaskaSelection(View view) {
		buttonSelectionAction(Board.ALASKA);
	}

	
	/**
	* Perform a button action on northwest territory button.
	* @author Charles Snyder				   
	*/
	public void nwSelection(View view) {
		buttonSelectionAction(Board.NORTHWEST_TERR);
	}

	
	/**
	* Perform a button action on alberta button.
	* @author Charles Snyder				   
	*/
	public void albSelection(View view) {
		buttonSelectionAction(Board.ALBERTA);
	}

	
	/**
	* Perform a button action on greenland button.
	* @author Charles Snyder				   
	*/
	public void greenSelection(View view) {
		buttonSelectionAction(Board.GREENLAND);
	}

	
	/**
	* Perform a button action on ontario button.
	* @author Charles Snyder				   
	*/
	public void ontSelection(View view) {
		buttonSelectionAction(Board.ONTARIO);
	}

	
	/**
	* Perform a button action on quebec button.
	* @author Charles Snyder				   
	*/
	public void queSelection(View view) {
		buttonSelectionAction(Board.QUEBEC);
	}

	
	/**
	* Perform a button action on western us button.
	* @author Charles Snyder				   
	*/
	public void wusSelection(View view) {
		buttonSelectionAction(Board.WESTERN_US);
	}

	
	/**
	* Perform a button action on eastern us button.
	* @author Charles Snyder				   
	*/
	public void eusSelection(View view) {
		buttonSelectionAction(Board.EASTERN_US);
	}

	
	/**
	* Perform a button action on central america button.
	* @author Charles Snyder				   
	*/
	public void camSelection(View view) {
		buttonSelectionAction(Board.CENTRAL_AMERICA);
	}

	
	/**
	* Perform a button action on venezuela button.
	* @author Charles Snyder				   
	*/
	public void venSelection(View view) {
		buttonSelectionAction(Board.VENEZUELA);
	}

	
	/**
	* Perform a button action on peru button.
	* @author Charles Snyder				   
	*/
	public void peruSelection(View view) {
		buttonSelectionAction(Board.PERU);
	}

	
	/**
	* Perform a button action on argentina button.
	* @author Charles Snyder				   
	*/
	public void argSelection(View view) {
		buttonSelectionAction(Board.ARGENTINA);
	}

	
	/**
	* Perform a button action on iceland button.
	* @author Charles Snyder				   
	*/
	public void iceSelection(View view) {
		buttonSelectionAction(Board.ICELAND);
	}

	
	/**
	* Perform a button action on great britain button.
	* @author Charles Snyder				   
	*/
	public void gbSelection(View view) {
		buttonSelectionAction(Board.GREAT_BRITAIN);
	}

	
	/**
	* Perform a button action on scandinavia button.
	* @author Charles Snyder				   
	*/
	public void scanSelection(View view) {
		buttonSelectionAction(Board.SCANDINAVIA);
	}

	
	/**
	* Perform a button action on northern europe button.
	* @author Charles Snyder				   
	*/
	public void neSelection(View view) {
		buttonSelectionAction(Board.NORTHERN_EUROPE);
	}

	
	/**
	* Perform a button action on western europe button.
	* @author Charles Snyder				   
	*/
	public void weSelection(View view) {
		buttonSelectionAction(Board.WESTERN_EUROPE);
	}

	
	/**
	* Perform a button action on southern europe button.
	* @author Charles Snyder				   
	*/
	public void seSelection(View view) {
		buttonSelectionAction(Board.SOUTHERN_EUROPE);
	}

	
	/**
	* Perform a button action on west africa button.
	* @author Charles Snyder				   
	*/
	public void wafSelection(View view) {
		buttonSelectionAction(Board.NORTH_AFRICA);
	}

	
	/**
	* Perform a button action on egypt button.
	* @author Charles Snyder				   
	*/
	public void egSelection(View view) {
		buttonSelectionAction(Board.EGYPT);
	}

	
	/**
	* Perform a button action on congo button.
	* @author Charles Snyder				   
	*/
	public void congSelection(View view) {
		buttonSelectionAction(Board.CONGO);
	}

	
	/**
	* Perform a button action on madagascar button.
	* @author Charles Snyder				   
	*/
	public void madSelection(View view) {
		buttonSelectionAction(Board.MADAGASCAR);
	}

	
	/**
	* Perform a button action on middle east button.
	* @author Charles Snyder				   
	*/
	public void midEastSelection(View view) {
		buttonSelectionAction(Board.MIDDLE_EAST);
	}

	
	/**
	* Perform a button action on siam button.
	* @author Charles Snyder				   
	*/
	public void siamSelection(View view) {
		buttonSelectionAction(Board.SOUTHEAST_ASIA);
	}

	
	/**
	* Perform a button action on indonesia button.
	* @author Charles Snyder				   
	*/
	public void indoSelection(View view) {
		buttonSelectionAction(Board.INDONESIA);
	}

	
	/**
	* Perform a button action on new guinea button.
	* @author Charles Snyder				   
	*/
	public void guineaSelection(View view) {
		buttonSelectionAction(Board.NEW_GUINEA);
	}

	
	/**
	* Initializes any buttons that require specific coordinates to restrict their touch actions.
	* @author Charles Snyder				   
	*/
	private void initializeOnTouchListeners() {
		findViewById(R.id.brazil_button).setOnTouchListener(
				new View.OnTouchListener() {
					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							if (x > 105) {
								view.performClick();
								buttonSelectionAction(Board.BRAZIL);
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.east_africa_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							float x = event.getX();
							if (y < 100 && x > 43) {
								view.performClick();
								buttonSelectionAction(Board.EAST_AFRICA);
							
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.south_africa_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							if (y > 60) {
								view.performClick();
								buttonSelectionAction(Board.SOUTH_AFRICA);
								
							}
							return true;
						}
						return false;
						
					}
				});

		findViewById(R.id.ural_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							float y = event.getY();
							if (x < 70 && y < 150) {
								view.performClick();
								buttonSelectionAction(Board.URAL);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.afghanistan_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							if (y > 57) {
								view.performClick();
								buttonSelectionAction(Board.AFGHANISTAN);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.india_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							if (y > 60) {
								view.performClick();
								buttonSelectionAction(Board.INDIA);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.siberia_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							float x = event.getX();
							if (x < 35) {
								view.performClick();
								buttonSelectionAction(Board.URAL);
							}
							if (y < 138 && x > 58) {
								view.performClick();
								buttonSelectionAction(Board.SIBERIA);
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.yakutsk_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							float y = event.getY();
							if (x > 44 && x < 95 && y < 52) {
								view.performClick();
								buttonSelectionAction(Board.YAKUTSK);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.kamchatka_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							float y = event.getY();
							if (x < 13 && y < 63) {
								view.performClick();
								buttonSelectionAction(Board.YAKUTSK);
								
							}
							if (x > 60 && y < 67) {
								view.performClick();
								buttonSelectionAction(Board.KAMCHATKA);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.irkutsk_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							if (x > 40 && x < 105) {
								view.performClick();
								buttonSelectionAction(Board.IRKUTSK);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.mongolia_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							if (y > 47) {
								view.performClick();
								buttonSelectionAction(Board.MONGOLIA);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.japan_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							if (x > 41) {
								view.performClick();
								buttonSelectionAction(Board.JAPAN);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.china_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float y = event.getY();
							float x = event.getX();
							if (y < 40 && x > 105) {
								view.performClick();
								buttonSelectionAction(Board.MONGOLIA);
								
							}
							if (y > 76) {
								view.performClick();
								buttonSelectionAction(Board.CHINA);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.ukraine_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							if (x > 40 && x < 100) {
								view.performClick();
								buttonSelectionAction(Board.UKRAINE);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.west_aust_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							if (x < 71) {
								view.performClick();
								buttonSelectionAction(Board.WESTERN_AUST);
								
							}
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.east_aust_button).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP) {
							float x = event.getX();
							if (x > 76) {
								view.performClick();
								buttonSelectionAction(Board.EASTERN_AUST);
								
							}
							return true;
						}
						return false;
					}
				});
	}

	
	/**
	* Makes all countries the correct color and the army count in each country current.
	* @author Charles Snyder				   
	*/
	private void refreshBoard() {
		determineCountryColor(Board.AFGHANISTAN, currentMainGame.getBoard()
				.getAfghanistanOwner());
		determineCountryColor(Board.ALASKA, currentMainGame.getBoard()
				.getAlaskaOwner());
		determineCountryColor(Board.ALBERTA, currentMainGame.getBoard()
				.getAlbertaOwner());
		determineCountryColor(Board.ARGENTINA, currentMainGame.getBoard()
				.getArgentinaOwner());
		determineCountryColor(Board.BRAZIL, currentMainGame.getBoard()
				.getBrazilOwner());
		determineCountryColor(Board.CENTRAL_AMERICA, currentMainGame.getBoard()
				.getCentralAmericaOwner());
		determineCountryColor(Board.CHINA, currentMainGame.getBoard()
				.getChinaOwner());
		determineCountryColor(Board.CONGO, currentMainGame.getBoard()
				.getCongoOwner());
		determineCountryColor(Board.EAST_AFRICA, currentMainGame.getBoard()
				.getEastAfricaOwner());
		determineCountryColor(Board.EASTERN_AUST, currentMainGame.getBoard()
				.getEasternAustOwner());
		determineCountryColor(Board.EASTERN_US, currentMainGame.getBoard()
				.getEasternUSOwner());
		determineCountryColor(Board.EGYPT, currentMainGame.getBoard()
				.getEgyptOwner());
		determineCountryColor(Board.GREAT_BRITAIN, currentMainGame.getBoard()
				.getGreatBritainOwner());
		determineCountryColor(Board.GREENLAND, currentMainGame.getBoard()
				.getGreenlandOwner());
		determineCountryColor(Board.ICELAND, currentMainGame.getBoard()
				.getIcelandOwner());
		determineCountryColor(Board.INDIA, currentMainGame.getBoard()
				.getIndiaOwner());
		determineCountryColor(Board.INDONESIA, currentMainGame.getBoard()
				.getIndonesiaOwner());
		determineCountryColor(Board.IRKUTSK, currentMainGame.getBoard()
				.getIrkutskOwner());
		determineCountryColor(Board.JAPAN, currentMainGame.getBoard()
				.getJapanOwner());
		determineCountryColor(Board.KAMCHATKA, currentMainGame.getBoard()
				.getKamchatkaOwner());
		determineCountryColor(Board.MADAGASCAR, currentMainGame.getBoard()
				.getMadagascarOwner());
		determineCountryColor(Board.MIDDLE_EAST, currentMainGame.getBoard()
				.getMiddleEastOwner());
		determineCountryColor(Board.MONGOLIA, currentMainGame.getBoard()
				.getMongoliaOwner());
		determineCountryColor(Board.NEW_GUINEA, currentMainGame.getBoard()
				.getNewGuineaOwner());
		determineCountryColor(Board.NORTH_AFRICA, currentMainGame.getBoard()
				.getNorthAfricaOwner());
		determineCountryColor(Board.NORTHERN_EUROPE, currentMainGame.getBoard()
				.getNorthernEuropeOwner());
		determineCountryColor(Board.NORTHWEST_TERR, currentMainGame.getBoard()
				.getNorthwestTerrOwner());
		determineCountryColor(Board.ONTARIO, currentMainGame.getBoard()
				.getOntarioOwner());
		determineCountryColor(Board.PERU, currentMainGame.getBoard()
				.getPeruOwner());
		determineCountryColor(Board.QUEBEC, currentMainGame.getBoard()
				.getQuebecOwner());
		determineCountryColor(Board.SCANDINAVIA, currentMainGame.getBoard()
				.getScandinaviaOwner());
		determineCountryColor(Board.SIBERIA, currentMainGame.getBoard()
				.getSiberiaOwner());
		determineCountryColor(Board.SOUTH_AFRICA, currentMainGame.getBoard()
				.getSouthAfricaOwner());
		determineCountryColor(Board.SOUTHEAST_ASIA, currentMainGame.getBoard()
				.getSoutheastAsiaOwner());
		determineCountryColor(Board.SOUTHERN_EUROPE, currentMainGame.getBoard()
				.getSouthernEuropeOwner());
		determineCountryColor(Board.UKRAINE, currentMainGame.getBoard()
				.getUkraineOwner());
		determineCountryColor(Board.URAL, currentMainGame.getBoard()
				.getUralOwner());
		determineCountryColor(Board.VENEZUELA, currentMainGame.getBoard()
				.getVenezuelaOwner());
		determineCountryColor(Board.WESTERN_AUST, currentMainGame.getBoard()
				.getWesternAustOwner());
		determineCountryColor(Board.WESTERN_EUROPE, currentMainGame.getBoard()
				.getWesternEuropeOwner());
		determineCountryColor(Board.WESTERN_US, currentMainGame.getBoard()
				.getWesternUSOwner());
		determineCountryColor(Board.YAKUTSK, currentMainGame.getBoard()
				.getYakutskOwner());
		
		determineCountryText(Board.AFGHANISTAN, currentMainGame.getBoard().getAfghanistanArmy());
		determineCountryText(Board.ALASKA, currentMainGame.getBoard().getAlaskaArmy());
		determineCountryText(Board.ALBERTA, currentMainGame.getBoard().getAlbertaArmy());
		determineCountryText(Board.ARGENTINA, currentMainGame.getBoard().getArgentinaArmy());
		determineCountryText(Board.BRAZIL, currentMainGame.getBoard().getBrazilArmy());
		determineCountryText(Board.CENTRAL_AMERICA, currentMainGame.getBoard().getCentralAmericaArmy());
		determineCountryText(Board.CHINA, currentMainGame.getBoard().getChinaArmy());
		determineCountryText(Board.CONGO, currentMainGame.getBoard().getCongoArmy());
		determineCountryText(Board.EAST_AFRICA, currentMainGame.getBoard().getEastAfricaArmy());
		determineCountryText(Board.EASTERN_AUST, currentMainGame.getBoard().getEasternAustArmy());
		determineCountryText(Board.EASTERN_US, currentMainGame.getBoard().getEasternUSArmy());
		determineCountryText(Board.EGYPT, currentMainGame.getBoard().getEgyptArmy());
		determineCountryText(Board.GREAT_BRITAIN, currentMainGame.getBoard().getGreatBritainArmy());
		determineCountryText(Board.GREENLAND, currentMainGame.getBoard().getGreenlandArmy());
		determineCountryText(Board.ICELAND, currentMainGame.getBoard().getIcelandArmy());
		determineCountryText(Board.INDIA, currentMainGame.getBoard().getIndiaArmy());
		determineCountryText(Board.INDONESIA, currentMainGame.getBoard().getIndonesiaArmy());
		determineCountryText(Board.IRKUTSK, currentMainGame.getBoard().getIrkutskArmy());
		determineCountryText(Board.JAPAN, currentMainGame.getBoard().getJapanArmy());
		determineCountryText(Board.KAMCHATKA, currentMainGame.getBoard().getKamchatkaArmy());
		determineCountryText(Board.MADAGASCAR, currentMainGame.getBoard().getMadagascarArmy());
		determineCountryText(Board.MIDDLE_EAST, currentMainGame.getBoard().getMiddleEastArmy());
		determineCountryText(Board.MONGOLIA, currentMainGame.getBoard().getMongoliaArmy());
		determineCountryText(Board.NEW_GUINEA, currentMainGame.getBoard().getNewGuineaArmy());
		determineCountryText(Board.NORTH_AFRICA, currentMainGame.getBoard().getNorthAfricaArmy());
		determineCountryText(Board.NORTHERN_EUROPE, currentMainGame.getBoard().getNorthernEuropeArmy());
		determineCountryText(Board.NORTHWEST_TERR, currentMainGame.getBoard().getNorthwestTerrArmy());
		determineCountryText(Board.ONTARIO, currentMainGame.getBoard().getOntarioArmy());
		determineCountryText(Board.PERU, currentMainGame.getBoard().getPeruArmy());
		determineCountryText(Board.QUEBEC, currentMainGame.getBoard().getQuebecArmy());
		determineCountryText(Board.SCANDINAVIA, currentMainGame.getBoard().getScandinaviaArmy());
		determineCountryText(Board.SIBERIA, currentMainGame.getBoard().getSiberiaArmy());
		determineCountryText(Board.SOUTH_AFRICA, currentMainGame.getBoard().getSouthAfricaArmy());
		determineCountryText(Board.SOUTHEAST_ASIA, currentMainGame.getBoard().getSoutheastAsiaArmy());
		determineCountryText(Board.SOUTHERN_EUROPE, currentMainGame.getBoard().getSouthernEuropeArmy());
		determineCountryText(Board.UKRAINE, currentMainGame.getBoard().getUkraineArmy());
		determineCountryText(Board.URAL, currentMainGame.getBoard().getUralArmy());
		determineCountryText(Board.VENEZUELA, currentMainGame.getBoard().getVenezuelaArmy());
		determineCountryText(Board.WESTERN_AUST, currentMainGame.getBoard().getWesternAustArmy());
		determineCountryText(Board.WESTERN_EUROPE, currentMainGame.getBoard().getWesternEuropeArmy());
		determineCountryText(Board.WESTERN_US, currentMainGame.getBoard().getWesternUSArmy());
		determineCountryText(Board.YAKUTSK, currentMainGame.getBoard().getYakutskArmy());
	}

	
	/**
	* Associates a country with the proper color image of the owner.
	* @param country - the country the determine.
	* @param ownerColor - the color of the player who owns the country.
	* @author Charles Snyder				   
	*/
	private void determineCountryColor(int country, int ownerColor) {
		switch (country) {
		case Board.ALASKA:
			Button alaskaButton = (Button) findViewById(R.id.alaska_button);
			switch (ownerColor) {
			case Player.RED:
				alaskaButton
						.setBackgroundResource(R.drawable.alaska_country_red);
				break;
			case Player.BLUE:
				alaskaButton
						.setBackgroundResource(R.drawable.alaska_country_blue);
				break;
			case Player.GREEN:
				alaskaButton
						.setBackgroundResource(R.drawable.alaska_country_green);
				break;
			case Player.YELLOW:
				alaskaButton
						.setBackgroundResource(R.drawable.alaska_country_yellow);
				break;
			case Player.PURPLE:
				alaskaButton
						.setBackgroundResource(R.drawable.alaska_country_purple);
				break;
			case Player.ORANGE:
				alaskaButton
						.setBackgroundResource(R.drawable.alaska_country_orange);
				break;
			}
			break;
		case Board.AFGHANISTAN:
			Button afghanButton = (Button) findViewById(R.id.afghanistan_button);
			switch (ownerColor) {
			case Player.RED:
				afghanButton
						.setBackgroundResource(R.drawable.afghanistan_country_red);
				break;
			case Player.BLUE:
				afghanButton
						.setBackgroundResource(R.drawable.afghanistan_country_blue);
				break;
			case Player.GREEN:
				afghanButton
						.setBackgroundResource(R.drawable.afghanistan_country_green);
				break;
			case Player.YELLOW:
				afghanButton
						.setBackgroundResource(R.drawable.afghanistan_country_yellow);
				break;
			case Player.PURPLE:
				afghanButton
						.setBackgroundResource(R.drawable.afghanistan_country_purple);
				break;
			case Player.ORANGE:
				afghanButton
						.setBackgroundResource(R.drawable.afghanistan_country_orange);
				break;
			}
			break;
		case Board.ALBERTA:
			Button albertaButton = (Button) findViewById(R.id.alberta_button);
			switch (ownerColor) {
			case Player.RED:
				albertaButton
						.setBackgroundResource(R.drawable.alberta_country_red);
				break;
			case Player.BLUE:
				albertaButton
						.setBackgroundResource(R.drawable.alberta_country_blue);
				break;
			case Player.GREEN:
				albertaButton
						.setBackgroundResource(R.drawable.alberta_country_green);
				break;
			case Player.YELLOW:
				albertaButton
						.setBackgroundResource(R.drawable.alberta_country_yellow);
				break;
			case Player.PURPLE:
				albertaButton
						.setBackgroundResource(R.drawable.alberta_country_purple);
				break;
			case Player.ORANGE:
				albertaButton
						.setBackgroundResource(R.drawable.alberta_country_orange);
				break;
			}
			break;
		case Board.ARGENTINA:
			Button argentinaButton = (Button) findViewById(R.id.argentina_button);
			switch (ownerColor) {
			case Player.RED:
				argentinaButton
						.setBackgroundResource(R.drawable.argentina_country_red);
				break;
			case Player.BLUE:
				argentinaButton
						.setBackgroundResource(R.drawable.argentina_country_blue);
				break;
			case Player.GREEN:
				argentinaButton
						.setBackgroundResource(R.drawable.argentina_country_green);
				break;
			case Player.YELLOW:
				argentinaButton
						.setBackgroundResource(R.drawable.argentina_country_yellow);
				break;
			case Player.PURPLE:
				argentinaButton
						.setBackgroundResource(R.drawable.argentina_country_purple);
				break;
			case Player.ORANGE:
				argentinaButton
						.setBackgroundResource(R.drawable.argentina_country_orange);
				break;
			}
			break;
		case Board.BRAZIL:
			Button brazilButton = (Button) findViewById(R.id.brazil_button);
			switch (ownerColor) {
			case Player.RED:
				brazilButton
						.setBackgroundResource(R.drawable.brazil_country_red);
				break;
			case Player.BLUE:
				brazilButton
						.setBackgroundResource(R.drawable.brazil_country_blue);
				break;
			case Player.GREEN:
				brazilButton
						.setBackgroundResource(R.drawable.brazil_country_green);
				break;
			case Player.YELLOW:
				brazilButton
						.setBackgroundResource(R.drawable.brazil_country_yellow);
				break;
			case Player.PURPLE:
				brazilButton
						.setBackgroundResource(R.drawable.brazil_country_purple);
				break;
			case Player.ORANGE:
				brazilButton
						.setBackgroundResource(R.drawable.brazil_country_orange);
				break;
			}
			break;
		case Board.CENTRAL_AMERICA:
			Button central_americaButton = (Button) findViewById(R.id.central_america_button);
			switch (ownerColor) {
			case Player.RED:
				central_americaButton
						.setBackgroundResource(R.drawable.central_america_country_red);
				break;
			case Player.BLUE:
				central_americaButton
						.setBackgroundResource(R.drawable.central_america_country_blue);
				break;
			case Player.GREEN:
				central_americaButton
						.setBackgroundResource(R.drawable.central_america_country_green);
				break;
			case Player.YELLOW:
				central_americaButton
						.setBackgroundResource(R.drawable.central_america_country_yellow);
				break;
			case Player.PURPLE:
				central_americaButton
						.setBackgroundResource(R.drawable.central_america_country_purple);
				break;
			case Player.ORANGE:
				central_americaButton
						.setBackgroundResource(R.drawable.central_america_country_orange);
				break;
			}
			break;
		case Board.CHINA:
			Button chinaButton = (Button) findViewById(R.id.china_button);
			switch (ownerColor) {
			case Player.RED:
				chinaButton.setBackgroundResource(R.drawable.china_country_red);
				break;
			case Player.BLUE:
				chinaButton
						.setBackgroundResource(R.drawable.china_country_blue);
				break;
			case Player.GREEN:
				chinaButton
						.setBackgroundResource(R.drawable.china_country_green);
				break;
			case Player.YELLOW:
				chinaButton
						.setBackgroundResource(R.drawable.china_country_yellow);
				break;
			case Player.PURPLE:
				chinaButton
						.setBackgroundResource(R.drawable.china_country_purple);
				break;
			case Player.ORANGE:
				chinaButton
						.setBackgroundResource(R.drawable.china_country_orange);
				break;
			}
			break;
		case Board.CONGO:
			Button congoButton = (Button) findViewById(R.id.congo_button);
			switch (ownerColor) {
			case Player.RED:
				congoButton.setBackgroundResource(R.drawable.congo_country_red);
				break;
			case Player.BLUE:
				congoButton
						.setBackgroundResource(R.drawable.congo_country_blue);
				break;
			case Player.GREEN:
				congoButton
						.setBackgroundResource(R.drawable.congo_country_green);
				break;
			case Player.YELLOW:
				congoButton
						.setBackgroundResource(R.drawable.congo_country_yellow);
				break;
			case Player.PURPLE:
				congoButton
						.setBackgroundResource(R.drawable.congo_country_purple);
				break;
			case Player.ORANGE:
				congoButton
						.setBackgroundResource(R.drawable.congo_country_orange);
				break;
			}
			break;
		case Board.EAST_AFRICA:
			Button east_africaButton = (Button) findViewById(R.id.east_africa_button);
			switch (ownerColor) {
			case Player.RED:
				east_africaButton
						.setBackgroundResource(R.drawable.east_africa_country_red);
				break;
			case Player.BLUE:
				east_africaButton
						.setBackgroundResource(R.drawable.east_africa_country_blue);
				break;
			case Player.GREEN:
				east_africaButton
						.setBackgroundResource(R.drawable.east_africa_country_green);
				break;
			case Player.YELLOW:
				east_africaButton
						.setBackgroundResource(R.drawable.east_africa_country_yellow);
				break;
			case Player.PURPLE:
				east_africaButton
						.setBackgroundResource(R.drawable.east_africa_country_purple);
				break;
			case Player.ORANGE:
				east_africaButton
						.setBackgroundResource(R.drawable.east_africa_country_orange);
				break;
			}
			break;
		case Board.EASTERN_AUST:
			Button east_austButton = (Button) findViewById(R.id.east_aust_button);
			switch (ownerColor) {
			case Player.RED:
				east_austButton
						.setBackgroundResource(R.drawable.east_aust_country_red);
				break;
			case Player.BLUE:
				east_austButton
						.setBackgroundResource(R.drawable.east_aust_country_blue);
				break;
			case Player.GREEN:
				east_austButton
						.setBackgroundResource(R.drawable.east_aust_country_green);
				break;
			case Player.YELLOW:
				east_austButton
						.setBackgroundResource(R.drawable.east_aust_country_yellow);
				break;
			case Player.PURPLE:
				east_austButton
						.setBackgroundResource(R.drawable.east_aust_country_purple);
				break;
			case Player.ORANGE:
				east_austButton
						.setBackgroundResource(R.drawable.east_aust_country_orange);
				break;
			}
			break;
		case Board.EASTERN_US:
			Button east_usButton = (Button) findViewById(R.id.east_us_button);
			switch (ownerColor) {
			case Player.RED:
				east_usButton
						.setBackgroundResource(R.drawable.east_us_country_red);
				break;
			case Player.BLUE:
				east_usButton
						.setBackgroundResource(R.drawable.east_us_country_blue);
				break;
			case Player.GREEN:
				east_usButton
						.setBackgroundResource(R.drawable.east_us_country_green);
				break;
			case Player.YELLOW:
				east_usButton
						.setBackgroundResource(R.drawable.east_us_country_yellow);
				break;
			case Player.PURPLE:
				east_usButton
						.setBackgroundResource(R.drawable.east_us_country_purple);
				break;
			case Player.ORANGE:
				east_usButton
						.setBackgroundResource(R.drawable.east_us_country_orange);
				break;
			}
			break;
		case Board.EGYPT:
			Button egyptButton = (Button) findViewById(R.id.egypt_button);
			switch (ownerColor) {
			case Player.RED:
				egyptButton.setBackgroundResource(R.drawable.egypt_country_red);
				break;
			case Player.BLUE:
				egyptButton
						.setBackgroundResource(R.drawable.egypt_country_blue);
				break;
			case Player.GREEN:
				egyptButton
						.setBackgroundResource(R.drawable.egypt_country_green);
				break;
			case Player.YELLOW:
				egyptButton
						.setBackgroundResource(R.drawable.egypt_country_yellow);
				break;
			case Player.PURPLE:
				egyptButton
						.setBackgroundResource(R.drawable.egypt_country_purple);
				break;
			case Player.ORANGE:
				egyptButton
						.setBackgroundResource(R.drawable.egypt_country_orange);
				break;
			}
			break;
		case Board.GREAT_BRITAIN:
			Button great_britainButton = (Button) findViewById(R.id.great_britain_button);
			switch (ownerColor) {
			case Player.RED:
				great_britainButton
						.setBackgroundResource(R.drawable.great_britain_country_red);
				break;
			case Player.BLUE:
				great_britainButton
						.setBackgroundResource(R.drawable.great_britain_country_blue);
				break;
			case Player.GREEN:
				great_britainButton
						.setBackgroundResource(R.drawable.great_britain_country_green);
				break;
			case Player.YELLOW:
				great_britainButton
						.setBackgroundResource(R.drawable.great_britain_country_yellow);
				break;
			case Player.PURPLE:
				great_britainButton
						.setBackgroundResource(R.drawable.great_britain_country_purple);
				break;
			case Player.ORANGE:
				great_britainButton
						.setBackgroundResource(R.drawable.great_britain_country_orange);
				break;
			}
			break;
		case Board.GREENLAND:
			Button greenlandButton = (Button) findViewById(R.id.greenland_button);
			switch (ownerColor) {
			case Player.RED:
				greenlandButton
						.setBackgroundResource(R.drawable.greenland_country_red);
				break;
			case Player.BLUE:
				greenlandButton
						.setBackgroundResource(R.drawable.greenland_country_blue);
				break;
			case Player.GREEN:
				greenlandButton
						.setBackgroundResource(R.drawable.greenland_country_green);
				break;
			case Player.YELLOW:
				greenlandButton
						.setBackgroundResource(R.drawable.greenland_country_yellow);
				break;
			case Player.PURPLE:
				greenlandButton
						.setBackgroundResource(R.drawable.greenland_country_purple);
				break;
			case Player.ORANGE:
				greenlandButton
						.setBackgroundResource(R.drawable.greenland_country_orange);
				break;
			}
			break;
		case Board.ICELAND:
			Button icelandButton = (Button) findViewById(R.id.iceland_button);
			switch (ownerColor) {
			case Player.RED:
				icelandButton
						.setBackgroundResource(R.drawable.iceland_country_red);
				break;
			case Player.BLUE:
				icelandButton
						.setBackgroundResource(R.drawable.iceland_country_blue);
				break;
			case Player.GREEN:
				icelandButton
						.setBackgroundResource(R.drawable.iceland_country_green);
				break;
			case Player.YELLOW:
				icelandButton
						.setBackgroundResource(R.drawable.iceland_country_yellow);
				break;
			case Player.PURPLE:
				icelandButton
						.setBackgroundResource(R.drawable.iceland_country_purple);
				break;
			case Player.ORANGE:
				icelandButton
						.setBackgroundResource(R.drawable.iceland_country_orange);
				break;
			}
			break;
		case Board.INDIA:
			Button indiaButton = (Button) findViewById(R.id.india_button);
			switch (ownerColor) {
			case Player.RED:
				indiaButton.setBackgroundResource(R.drawable.india_country_red);
				break;
			case Player.BLUE:
				indiaButton
						.setBackgroundResource(R.drawable.india_country_blue);
				break;
			case Player.GREEN:
				indiaButton
						.setBackgroundResource(R.drawable.india_country_green);
				break;
			case Player.YELLOW:
				indiaButton
						.setBackgroundResource(R.drawable.india_country_yellow);
				break;
			case Player.PURPLE:
				indiaButton
						.setBackgroundResource(R.drawable.india_country_purple);
				break;
			case Player.ORANGE:
				indiaButton
						.setBackgroundResource(R.drawable.india_country_orange);
				break;
			}
			break;
		case Board.INDONESIA:
			Button indonesiaButton = (Button) findViewById(R.id.indonesia_button);
			switch (ownerColor) {
			case Player.RED:
				indonesiaButton
						.setBackgroundResource(R.drawable.indonesia_country_red);
				break;
			case Player.BLUE:
				indonesiaButton
						.setBackgroundResource(R.drawable.indonesia_country_blue);
				break;
			case Player.GREEN:
				indonesiaButton
						.setBackgroundResource(R.drawable.indonesia_country_green);
				break;
			case Player.YELLOW:
				indonesiaButton
						.setBackgroundResource(R.drawable.indonesia_country_yellow);
				break;
			case Player.PURPLE:
				indonesiaButton
						.setBackgroundResource(R.drawable.indonesia_country_purple);
				break;
			case Player.ORANGE:
				indonesiaButton
						.setBackgroundResource(R.drawable.indonesia_country_orange);
				break;
			}
			break;
		case Board.IRKUTSK:
			Button irkutskButton = (Button) findViewById(R.id.irkutsk_button);
			switch (ownerColor) {
			case Player.RED:
				irkutskButton
						.setBackgroundResource(R.drawable.irkutsk_country_red);
				break;
			case Player.BLUE:
				irkutskButton
						.setBackgroundResource(R.drawable.irkutsk_country_blue);
				break;
			case Player.GREEN:
				irkutskButton
						.setBackgroundResource(R.drawable.irkutsk_country_green);
				break;
			case Player.YELLOW:
				irkutskButton
						.setBackgroundResource(R.drawable.irkutsk_country_yellow);
				break;
			case Player.PURPLE:
				irkutskButton
						.setBackgroundResource(R.drawable.irkutsk_country_purple);
				break;
			case Player.ORANGE:
				irkutskButton
						.setBackgroundResource(R.drawable.irkutsk_country_orange);
				break;
			}
			break;
		case Board.JAPAN:
			Button japanButton = (Button) findViewById(R.id.japan_button);
			switch (ownerColor) {
			case Player.RED:
				japanButton.setBackgroundResource(R.drawable.japan_country_red);
				break;
			case Player.BLUE:
				japanButton
						.setBackgroundResource(R.drawable.japan_country_blue);
				break;
			case Player.GREEN:
				japanButton
						.setBackgroundResource(R.drawable.japan_country_green);
				break;
			case Player.YELLOW:
				japanButton
						.setBackgroundResource(R.drawable.japan_country_yellow);
				break;
			case Player.PURPLE:
				japanButton
						.setBackgroundResource(R.drawable.japan_country_purple);
				break;
			case Player.ORANGE:
				japanButton
						.setBackgroundResource(R.drawable.japan_country_orange);
				break;
			}
			break;
		case Board.KAMCHATKA:
			Button kamchatkaButton = (Button) findViewById(R.id.kamchatka_button);
			switch (ownerColor) {
			case Player.RED:
				kamchatkaButton
						.setBackgroundResource(R.drawable.kamchatka_country_red);
				break;
			case Player.BLUE:
				kamchatkaButton
						.setBackgroundResource(R.drawable.kamchatka_country_blue);
				break;
			case Player.GREEN:
				kamchatkaButton
						.setBackgroundResource(R.drawable.kamchatka_country_green);
				break;
			case Player.YELLOW:
				kamchatkaButton
						.setBackgroundResource(R.drawable.kamchatka_country_yellow);
				break;
			case Player.PURPLE:
				kamchatkaButton
						.setBackgroundResource(R.drawable.kamchatka_country_purple);
				break;
			case Player.ORANGE:
				kamchatkaButton
						.setBackgroundResource(R.drawable.kamchatka_country_orange);
				break;
			}
			break;
		case Board.MADAGASCAR:
			Button madagascarButton = (Button) findViewById(R.id.madagascar_button);
			switch (ownerColor) {
			case Player.RED:
				madagascarButton
						.setBackgroundResource(R.drawable.madagascar_country_red);
				break;
			case Player.BLUE:
				madagascarButton
						.setBackgroundResource(R.drawable.madagascar_country_blue);
				break;
			case Player.GREEN:
				madagascarButton
						.setBackgroundResource(R.drawable.madagascar_country_green);
				break;
			case Player.YELLOW:
				madagascarButton
						.setBackgroundResource(R.drawable.madagascar_country_yellow);
				break;
			case Player.PURPLE:
				madagascarButton
						.setBackgroundResource(R.drawable.madagascar_country_purple);
				break;
			case Player.ORANGE:
				madagascarButton
						.setBackgroundResource(R.drawable.madagascar_country_orange);
				break;
			}
			break;
		case Board.MIDDLE_EAST:
			Button middle_eastButton = (Button) findViewById(R.id.middle_east_button);
			switch (ownerColor) {
			case Player.RED:
				middle_eastButton
						.setBackgroundResource(R.drawable.middle_east_country_red);
				break;
			case Player.BLUE:
				middle_eastButton
						.setBackgroundResource(R.drawable.middle_east_country_blue);
				break;
			case Player.GREEN:
				middle_eastButton
						.setBackgroundResource(R.drawable.middle_east_country_green);
				break;
			case Player.YELLOW:
				middle_eastButton
						.setBackgroundResource(R.drawable.middle_east_country_yellow);
				break;
			case Player.PURPLE:
				middle_eastButton
						.setBackgroundResource(R.drawable.middle_east_country_purple);
				break;
			case Player.ORANGE:
				middle_eastButton
						.setBackgroundResource(R.drawable.middle_east_country_orange);
				break;
			}
			break;
		case Board.MONGOLIA:
			Button mongoliaButton = (Button) findViewById(R.id.mongolia_button);
			switch (ownerColor) {
			case Player.RED:
				mongoliaButton
						.setBackgroundResource(R.drawable.mongolia_country_red);
				break;
			case Player.BLUE:
				mongoliaButton
						.setBackgroundResource(R.drawable.mongolia_country_blue);
				break;
			case Player.GREEN:
				mongoliaButton
						.setBackgroundResource(R.drawable.mongolia_country_green);
				break;
			case Player.YELLOW:
				mongoliaButton
						.setBackgroundResource(R.drawable.mongolia_country_yellow);
				break;
			case Player.PURPLE:
				mongoliaButton
						.setBackgroundResource(R.drawable.mongolia_country_purple);
				break;
			case Player.ORANGE:
				mongoliaButton
						.setBackgroundResource(R.drawable.mongolia_country_orange);
				break;
			}
			break;
		case Board.NEW_GUINEA:
			Button new_guineaButton = (Button) findViewById(R.id.new_guinea_button);
			switch (ownerColor) {
			case Player.RED:
				new_guineaButton
						.setBackgroundResource(R.drawable.new_guinea_country_red);
				break;
			case Player.BLUE:
				new_guineaButton
						.setBackgroundResource(R.drawable.new_guinea_country_blue);
				break;
			case Player.GREEN:
				new_guineaButton
						.setBackgroundResource(R.drawable.new_guinea_country_green);
				break;
			case Player.YELLOW:
				new_guineaButton
						.setBackgroundResource(R.drawable.new_guinea_country_yellow);
				break;
			case Player.PURPLE:
				new_guineaButton
						.setBackgroundResource(R.drawable.new_guinea_country_purple);
				break;
			case Player.ORANGE:
				new_guineaButton
						.setBackgroundResource(R.drawable.new_guinea_country_orange);
				break;
			}
			break;
		case Board.NORTH_AFRICA:
			Button west_africaButton = (Button) findViewById(R.id.west_africa_button);
			switch (ownerColor) {
			case Player.RED:
				west_africaButton
						.setBackgroundResource(R.drawable.west_africa_country_red);
				break;
			case Player.BLUE:
				west_africaButton
						.setBackgroundResource(R.drawable.west_africa_country_blue);
				break;
			case Player.GREEN:
				west_africaButton
						.setBackgroundResource(R.drawable.west_africa_country_green);
				break;
			case Player.YELLOW:
				west_africaButton
						.setBackgroundResource(R.drawable.west_africa_country_yellow);
				break;
			case Player.PURPLE:
				west_africaButton
						.setBackgroundResource(R.drawable.west_africa_country_purple);
				break;
			case Player.ORANGE:
				west_africaButton
						.setBackgroundResource(R.drawable.west_africa_country_orange);
				break;
			}
			break;
		case Board.NORTHERN_EUROPE:
			Button north_europeButton = (Button) findViewById(R.id.north_europe_button);
			switch (ownerColor) {
			case Player.RED:
				north_europeButton
						.setBackgroundResource(R.drawable.north_europe_country_red);
				break;
			case Player.BLUE:
				north_europeButton
						.setBackgroundResource(R.drawable.north_europe_country_blue);
				break;
			case Player.GREEN:
				north_europeButton
						.setBackgroundResource(R.drawable.north_europe_country_green);
				break;
			case Player.YELLOW:
				north_europeButton
						.setBackgroundResource(R.drawable.north_europe_country_yellow);
				break;
			case Player.PURPLE:
				north_europeButton
						.setBackgroundResource(R.drawable.north_europe_country_purple);
				break;
			case Player.ORANGE:
				north_europeButton
						.setBackgroundResource(R.drawable.north_europe_country_orange);
				break;
			}
			break;
		case Board.NORTHWEST_TERR:
			Button nw_terrButton = (Button) findViewById(R.id.nw_terr_button);
			switch (ownerColor) {
			case Player.RED:
				nw_terrButton
						.setBackgroundResource(R.drawable.nw_terr_country_red);
				break;
			case Player.BLUE:
				nw_terrButton
						.setBackgroundResource(R.drawable.nw_terr_country_blue);
				break;
			case Player.GREEN:
				nw_terrButton
						.setBackgroundResource(R.drawable.nw_terr_country_green);
				break;
			case Player.YELLOW:
				nw_terrButton
						.setBackgroundResource(R.drawable.nw_terr_country_yellow);
				break;
			case Player.PURPLE:
				nw_terrButton
						.setBackgroundResource(R.drawable.nw_terr_country_purple);
				break;
			case Player.ORANGE:
				nw_terrButton
						.setBackgroundResource(R.drawable.nw_terr_country_orange);
				break;
			}
			break;
		case Board.ONTARIO:
			Button ontarioButton = (Button) findViewById(R.id.ontario_button);
			switch (ownerColor) {
			case Player.RED:
				ontarioButton
						.setBackgroundResource(R.drawable.ontario_country_red);
				break;
			case Player.BLUE:
				ontarioButton
						.setBackgroundResource(R.drawable.ontario_country_blue);
				break;
			case Player.GREEN:
				ontarioButton
						.setBackgroundResource(R.drawable.ontario_country_green);
				break;
			case Player.YELLOW:
				ontarioButton
						.setBackgroundResource(R.drawable.ontario_country_yellow);
				break;
			case Player.PURPLE:
				ontarioButton
						.setBackgroundResource(R.drawable.ontario_country_purple);
				break;
			case Player.ORANGE:
				ontarioButton
						.setBackgroundResource(R.drawable.ontario_country_orange);
				break;
			}
			break;
		case Board.PERU:
			Button peruButton = (Button) findViewById(R.id.peru_button);
			switch (ownerColor) {
			case Player.RED:
				peruButton.setBackgroundResource(R.drawable.peru_country_red);
				break;
			case Player.BLUE:
				peruButton.setBackgroundResource(R.drawable.peru_country_blue);
				break;
			case Player.GREEN:
				peruButton.setBackgroundResource(R.drawable.peru_country_green);
				break;
			case Player.YELLOW:
				peruButton
						.setBackgroundResource(R.drawable.peru_country_yellow);
				break;
			case Player.PURPLE:
				peruButton
						.setBackgroundResource(R.drawable.peru_country_purple);
				break;
			case Player.ORANGE:
				peruButton
						.setBackgroundResource(R.drawable.peru_country_orange);
				break;
			}
			break;
		case Board.QUEBEC:
			Button quebecButton = (Button) findViewById(R.id.quebec_button);
			switch (ownerColor) {
			case Player.RED:
				quebecButton
						.setBackgroundResource(R.drawable.quebec_country_red);
				break;
			case Player.BLUE:
				quebecButton
						.setBackgroundResource(R.drawable.quebec_country_blue);
				break;
			case Player.GREEN:
				quebecButton
						.setBackgroundResource(R.drawable.quebec_country_green);
				break;
			case Player.YELLOW:
				quebecButton
						.setBackgroundResource(R.drawable.quebec_country_yellow);
				break;
			case Player.PURPLE:
				quebecButton
						.setBackgroundResource(R.drawable.quebec_country_purple);
				break;
			case Player.ORANGE:
				quebecButton
						.setBackgroundResource(R.drawable.quebec_country_orange);
				break;
			}
			break;
		case Board.SCANDINAVIA:
			Button scandinaviaButton = (Button) findViewById(R.id.scandinavia_button);
			switch (ownerColor) {
			case Player.RED:
				scandinaviaButton
						.setBackgroundResource(R.drawable.scandinavia_country_red);
				break;
			case Player.BLUE:
				scandinaviaButton
						.setBackgroundResource(R.drawable.scandinavia_country_blue);
				break;
			case Player.GREEN:
				scandinaviaButton
						.setBackgroundResource(R.drawable.scandinavia_country_green);
				break;
			case Player.YELLOW:
				scandinaviaButton
						.setBackgroundResource(R.drawable.scandinavia_country_yellow);
				break;
			case Player.PURPLE:
				scandinaviaButton
						.setBackgroundResource(R.drawable.scandinavia_country_purple);
				break;
			case Player.ORANGE:
				scandinaviaButton
						.setBackgroundResource(R.drawable.scandinavia_country_orange);
				break;
			}
			break;
		case Board.SIBERIA:
			Button siberiaButton = (Button) findViewById(R.id.siberia_button);
			switch (ownerColor) {
			case Player.RED:
				siberiaButton
						.setBackgroundResource(R.drawable.siberia_country_red);
				break;
			case Player.BLUE:
				siberiaButton
						.setBackgroundResource(R.drawable.siberia_country_blue);
				break;
			case Player.GREEN:
				siberiaButton
						.setBackgroundResource(R.drawable.siberia_country_green);
				break;
			case Player.YELLOW:
				siberiaButton
						.setBackgroundResource(R.drawable.siberia_country_yellow);
				break;
			case Player.PURPLE:
				siberiaButton
						.setBackgroundResource(R.drawable.siberia_country_purple);
				break;
			case Player.ORANGE:
				siberiaButton
						.setBackgroundResource(R.drawable.siberia_country_orange);
				break;
			}
			break;
		case Board.SOUTH_AFRICA:
			Button south_africaButton = (Button) findViewById(R.id.south_africa_button);
			switch (ownerColor) {
			case Player.RED:
				south_africaButton
						.setBackgroundResource(R.drawable.south_africa_country_red);
				break;
			case Player.BLUE:
				south_africaButton
						.setBackgroundResource(R.drawable.south_africa_country_blue);
				break;
			case Player.GREEN:
				south_africaButton
						.setBackgroundResource(R.drawable.south_africa_country_green);
				break;
			case Player.YELLOW:
				south_africaButton
						.setBackgroundResource(R.drawable.south_africa_country_yellow);
				break;
			case Player.PURPLE:
				south_africaButton
						.setBackgroundResource(R.drawable.south_africa_country_purple);
				break;
			case Player.ORANGE:
				south_africaButton
						.setBackgroundResource(R.drawable.south_africa_country_orange);
				break;
			}
			break;
		case Board.SOUTHEAST_ASIA:
			Button siamButton = (Button) findViewById(R.id.siam_button);
			switch (ownerColor) {
			case Player.RED:
				siamButton.setBackgroundResource(R.drawable.siam_country_red);
				break;
			case Player.BLUE:
				siamButton.setBackgroundResource(R.drawable.siam_country_blue);
				break;
			case Player.GREEN:
				siamButton.setBackgroundResource(R.drawable.siam_country_green);
				break;
			case Player.YELLOW:
				siamButton
						.setBackgroundResource(R.drawable.siam_country_yellow);
				break;
			case Player.PURPLE:
				siamButton
						.setBackgroundResource(R.drawable.siam_country_purple);
				break;
			case Player.ORANGE:
				siamButton
						.setBackgroundResource(R.drawable.siam_country_orange);
				break;
			}
			break;
		case Board.SOUTHERN_EUROPE:
			Button south_europeButton = (Button) findViewById(R.id.south_europe_button);
			switch (ownerColor) {
			case Player.RED:
				south_europeButton
						.setBackgroundResource(R.drawable.south_europe_country_red);
				break;
			case Player.BLUE:
				south_europeButton
						.setBackgroundResource(R.drawable.south_europe_country_blue);
				break;
			case Player.GREEN:
				south_europeButton
						.setBackgroundResource(R.drawable.south_europe_country_green);
				break;
			case Player.YELLOW:
				south_europeButton
						.setBackgroundResource(R.drawable.south_europe_country_yellow);
				break;
			case Player.PURPLE:
				south_europeButton
						.setBackgroundResource(R.drawable.south_europe_country_purple);
				break;
			case Player.ORANGE:
				south_europeButton
						.setBackgroundResource(R.drawable.south_europe_country_orange);
				break;
			}
			break;
		case Board.UKRAINE:
			Button ukraineButton = (Button) findViewById(R.id.ukraine_button);
			switch (ownerColor) {
			case Player.RED:
				ukraineButton
						.setBackgroundResource(R.drawable.ukraine_country_red);
				break;
			case Player.BLUE:
				ukraineButton
						.setBackgroundResource(R.drawable.ukraine_country_blue);
				break;
			case Player.GREEN:
				ukraineButton
						.setBackgroundResource(R.drawable.ukraine_country_green);
				break;
			case Player.YELLOW:
				ukraineButton
						.setBackgroundResource(R.drawable.ukraine_country_yellow);
				break;
			case Player.PURPLE:
				ukraineButton
						.setBackgroundResource(R.drawable.ukraine_country_purple);
				break;
			case Player.ORANGE:
				ukraineButton
						.setBackgroundResource(R.drawable.ukraine_country_orange);
				break;
			}
			break;
		case Board.URAL:
			Button uralButton = (Button) findViewById(R.id.ural_button);
			switch (ownerColor) {
			case Player.RED:
				uralButton.setBackgroundResource(R.drawable.ural_country_red);
				break;
			case Player.BLUE:
				uralButton.setBackgroundResource(R.drawable.ural_country_blue);
				break;
			case Player.GREEN:
				uralButton.setBackgroundResource(R.drawable.ural_country_green);
				break;
			case Player.YELLOW:
				uralButton
						.setBackgroundResource(R.drawable.ural_country_yellow);
				break;
			case Player.PURPLE:
				uralButton
						.setBackgroundResource(R.drawable.ural_country_purple);
				break;
			case Player.ORANGE:
				uralButton
						.setBackgroundResource(R.drawable.ural_country_orange);
				break;
			}
			break;
		case Board.VENEZUELA:
			Button venezuelaButton = (Button) findViewById(R.id.venezuela_button);
			switch (ownerColor) {
			case Player.RED:
				venezuelaButton
						.setBackgroundResource(R.drawable.venezuela_country_red);
				break;
			case Player.BLUE:
				venezuelaButton
						.setBackgroundResource(R.drawable.venezuela_country_blue);
				break;
			case Player.GREEN:
				venezuelaButton
						.setBackgroundResource(R.drawable.venezuela_country_green);
				break;
			case Player.YELLOW:
				venezuelaButton
						.setBackgroundResource(R.drawable.venezuela_country_yellow);
				break;
			case Player.PURPLE:
				venezuelaButton
						.setBackgroundResource(R.drawable.venezuela_country_purple);
				break;
			case Player.ORANGE:
				venezuelaButton
						.setBackgroundResource(R.drawable.venezuela_country_orange);
				break;
			}
			break;
		case Board.WESTERN_AUST:
			Button west_austButton = (Button) findViewById(R.id.west_aust_button);
			switch (ownerColor) {
			case Player.RED:
				west_austButton
						.setBackgroundResource(R.drawable.west_aust_country_red);
				break;
			case Player.BLUE:
				west_austButton
						.setBackgroundResource(R.drawable.west_aust_country_blue);
				break;
			case Player.GREEN:
				west_austButton
						.setBackgroundResource(R.drawable.west_aust_country_green);
				break;
			case Player.YELLOW:
				west_austButton
						.setBackgroundResource(R.drawable.west_aust_country_yellow);
				break;
			case Player.PURPLE:
				west_austButton
						.setBackgroundResource(R.drawable.west_aust_country_purple);
				break;
			case Player.ORANGE:
				west_austButton
						.setBackgroundResource(R.drawable.west_aust_country_orange);
				break;
			}
			break;
		case Board.WESTERN_EUROPE:
			Button west_europeButton = (Button) findViewById(R.id.west_europe_button);
			switch (ownerColor) {
			case Player.RED:
				west_europeButton
						.setBackgroundResource(R.drawable.west_europe_country_red);
				break;
			case Player.BLUE:
				west_europeButton
						.setBackgroundResource(R.drawable.west_europe_country_blue);
				break;
			case Player.GREEN:
				west_europeButton
						.setBackgroundResource(R.drawable.west_europe_country_green);
				break;
			case Player.YELLOW:
				west_europeButton
						.setBackgroundResource(R.drawable.west_europe_country_yellow);
				break;
			case Player.PURPLE:
				west_europeButton
						.setBackgroundResource(R.drawable.west_europe_country_purple);
				break;
			case Player.ORANGE:
				west_europeButton
						.setBackgroundResource(R.drawable.west_europe_country_orange);
				break;
			}
			break;
		case Board.WESTERN_US:
			Button west_usButton = (Button) findViewById(R.id.west_us_button);
			switch (ownerColor) {
			case Player.RED:
				west_usButton
						.setBackgroundResource(R.drawable.west_us_country_red);
				break;
			case Player.BLUE:
				west_usButton
						.setBackgroundResource(R.drawable.west_us_country_blue);
				break;
			case Player.GREEN:
				west_usButton
						.setBackgroundResource(R.drawable.west_us_country_green);
				break;
			case Player.YELLOW:
				west_usButton
						.setBackgroundResource(R.drawable.west_us_country_yellow);
				break;
			case Player.PURPLE:
				west_usButton
						.setBackgroundResource(R.drawable.west_us_country_purple);
				break;
			case Player.ORANGE:
				west_usButton
						.setBackgroundResource(R.drawable.west_us_country_orange);
				break;
			}
			break;
		case Board.YAKUTSK:
			Button yakutskButton = (Button) findViewById(R.id.yakutsk_button);
			switch (ownerColor) {
			case Player.RED:
				yakutskButton
						.setBackgroundResource(R.drawable.yakutsk_country_red);
				break;
			case Player.BLUE:
				yakutskButton
						.setBackgroundResource(R.drawable.yakutsk_country_blue);
				break;
			case Player.GREEN:
				yakutskButton
						.setBackgroundResource(R.drawable.yakutsk_country_green);
				break;
			case Player.YELLOW:
				yakutskButton
						.setBackgroundResource(R.drawable.yakutsk_country_yellow);
				break;
			case Player.PURPLE:
				yakutskButton
						.setBackgroundResource(R.drawable.yakutsk_country_purple);
				break;
			case Player.ORANGE:
				yakutskButton
						.setBackgroundResource(R.drawable.yakutsk_country_orange);
				break;
			}
			break;
		}
	}
	
	
	/**
	* Associates a country with the proper army count of the armies situated in that country.
	* @param country - the country the determine.
	* @param armyCount - the number of armies in the country.
	* @author Charles Snyder				   
	*/
	private void determineCountryText(int country, int armyCount) {
		switch (country) {
		case Board.ALASKA:
			Button alaskaButton = (Button) findViewById(R.id.alaska_button);
			alaskaButton.setText(Integer.toString(armyCount));
			break;
		case Board.AFGHANISTAN:
			Button afghanButton = (Button) findViewById(R.id.afghanistan_button);
			afghanButton.setText(Integer.toString(armyCount));
			break;
		case Board.ALBERTA:
			Button albertaButton = (Button) findViewById(R.id.alberta_button);
			albertaButton.setText(Integer.toString(armyCount));
			break;
		case Board.ARGENTINA:
			Button argentinaButton = (Button) findViewById(R.id.argentina_button);
			argentinaButton.setText(Integer.toString(armyCount));
			break;
		case Board.BRAZIL:
			Button brazilButton = (Button) findViewById(R.id.brazil_button);
			brazilButton.setText(Integer.toString(armyCount));
			break;
		case Board.CENTRAL_AMERICA:
			Button centralButton = (Button) findViewById(R.id.central_america_button);
			centralButton.setText(Integer.toString(armyCount));
			break;
		case Board.CHINA:
			Button chinaButton = (Button) findViewById(R.id.china_button);
			chinaButton.setText(Integer.toString(armyCount));
			break;
		case Board.CONGO:
			Button congoButton = (Button) findViewById(R.id.congo_button);
			congoButton.setText(Integer.toString(armyCount));
			break;
		case Board.EAST_AFRICA:
			Button eastAfricaButton = (Button) findViewById(R.id.east_africa_button);
			eastAfricaButton.setText(Integer.toString(armyCount));
			break;
		case Board.EASTERN_AUST:
			Button eastAustButton = (Button) findViewById(R.id.east_aust_button);
			eastAustButton.setText(Integer.toString(armyCount));
			break;
		case Board.EASTERN_US:
			Button eastUsButton = (Button) findViewById(R.id.east_us_button);
			eastUsButton.setText(Integer.toString(armyCount));
			break;
		case Board.EGYPT:
			Button egyptButton = (Button) findViewById(R.id.egypt_button);
			egyptButton.setText(Integer.toString(armyCount));
			break;
		case Board.GREAT_BRITAIN:
			Button gbButton = (Button) findViewById(R.id.great_britain_button);
			gbButton.setText(Integer.toString(armyCount));
			break;
		case Board.GREENLAND:
			Button greenlandButton = (Button) findViewById(R.id.greenland_button);
			greenlandButton.setText(Integer.toString(armyCount));
			break;
		case Board.ICELAND:
			Button icelandButton = (Button) findViewById(R.id.iceland_button);
			icelandButton.setText(Integer.toString(armyCount));
			break;
		case Board.INDIA:
			Button indiaButton = (Button) findViewById(R.id.india_button);
			indiaButton.setText(Integer.toString(armyCount));
			break;
		case Board.INDONESIA:
			Button indoButton = (Button) findViewById(R.id.indonesia_button);
			indoButton.setText(Integer.toString(armyCount));
			break;
		case Board.IRKUTSK:
			Button irkButton = (Button) findViewById(R.id.irkutsk_button);
			irkButton.setText(Integer.toString(armyCount));
			break;
		case Board.JAPAN:
			Button japButton = (Button) findViewById(R.id.japan_button);
			japButton.setText(Integer.toString(armyCount));
			break;
		case Board.KAMCHATKA:
			Button kamButton = (Button) findViewById(R.id.kamchatka_button);
			kamButton.setText(Integer.toString(armyCount));
			break;
		case Board.MADAGASCAR:
			Button madButton = (Button) findViewById(R.id.madagascar_button);
			madButton.setText(Integer.toString(armyCount));
			break;
		case Board.MIDDLE_EAST:
			Button mideastButton = (Button) findViewById(R.id.middle_east_button);
			mideastButton.setText(Integer.toString(armyCount));
			break;
		case Board.MONGOLIA:
			Button mongoliaButton = (Button) findViewById(R.id.mongolia_button);
			mongoliaButton.setText(Integer.toString(armyCount));
			break;
		case Board.NEW_GUINEA:
			Button newguineaButton = (Button) findViewById(R.id.new_guinea_button);
			newguineaButton.setText(Integer.toString(armyCount));
			break;
		case Board.NORTH_AFRICA:
			Button northAfricaButton = (Button) findViewById(R.id.west_africa_button);
			northAfricaButton.setText(Integer.toString(armyCount));
			break;
		case Board.NORTHERN_EUROPE:
			Button northEuropeButton = (Button) findViewById(R.id.north_europe_button);
			northEuropeButton.setText(Integer.toString(armyCount));
			break;
		case Board.NORTHWEST_TERR:
			Button nwTerrButton = (Button) findViewById(R.id.nw_terr_button);
			nwTerrButton.setText(Integer.toString(armyCount));
			break;
		case Board.ONTARIO:
			Button ontarioButton = (Button) findViewById(R.id.ontario_button);
			ontarioButton.setText(Integer.toString(armyCount));
			break;
		case Board.PERU:
			Button peruButton = (Button) findViewById(R.id.peru_button);
			peruButton.setText(Integer.toString(armyCount));
			break;
		case Board.QUEBEC:
			Button quebecButton = (Button) findViewById(R.id.quebec_button);
			quebecButton.setText(Integer.toString(armyCount));
			break;
		case Board.SCANDINAVIA:
			Button scandinaviaButton = (Button) findViewById(R.id.scandinavia_button);
			scandinaviaButton.setText(Integer.toString(armyCount));
			break;
		case Board.SIBERIA:
			Button siberiaButton = (Button) findViewById(R.id.siberia_button);
			siberiaButton.setText(Integer.toString(armyCount));
			break;
		case Board.SOUTH_AFRICA:
			Button southAfricaButton = (Button) findViewById(R.id.south_africa_button);
			southAfricaButton.setText(Integer.toString(armyCount));
			break;
		case Board.SOUTHEAST_ASIA:
			Button siamButton = (Button) findViewById(R.id.siam_button);
			siamButton.setText(Integer.toString(armyCount));
			break;
		case Board.SOUTHERN_EUROPE:
			Button southEuropeButton = (Button) findViewById(R.id.south_europe_button);
			southEuropeButton.setText(Integer.toString(armyCount));
			break;
		case Board.UKRAINE:
			Button ukraineButton = (Button) findViewById(R.id.ukraine_button);
			ukraineButton.setText(Integer.toString(armyCount));
			break;
		case Board.URAL:
			Button uralButton = (Button) findViewById(R.id.ural_button);
			uralButton.setText(Integer.toString(armyCount));
			break;
		case Board.VENEZUELA:
			Button venButton = (Button) findViewById(R.id.venezuela_button);
			venButton.setText(Integer.toString(armyCount));
			break;
		case Board.WESTERN_AUST:
			Button westAustButton = (Button) findViewById(R.id.west_aust_button);
			westAustButton.setText(Integer.toString(armyCount));
			break;
		case Board.WESTERN_EUROPE:
			Button westEuropeButton = (Button) findViewById(R.id.west_europe_button);
			westEuropeButton.setText(Integer.toString(armyCount));
			break;
		case Board.WESTERN_US:
			Button westUsButton = (Button) findViewById(R.id.west_us_button);
			westUsButton.setText(Integer.toString(armyCount));
			break;
		case Board.YAKUTSK:
			Button yakButton = (Button) findViewById(R.id.yakutsk_button);
			yakButton.setText(Integer.toString(armyCount));
			break;
		}
	}

	
	/**
	* Updates the army display in a country by a certain amount.
	* @param country - the country in which to change the value.
	* @param numIncrease - the amount to increase the armies by.
	* @author Charles Snyder				   
	*/
	private void updateCountryText(int country, int numIncrease) {
		switch (country) {
		case Board.ALASKA:
			Button alaskaButton = (Button) findViewById(R.id.alaska_button);
			alaskaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.AFGHANISTAN:
			Button afghanButton = (Button) findViewById(R.id.afghanistan_button);
			afghanButton.setText(Integer.toString(numIncrease));
			break;
		case Board.ALBERTA:
			Button albertaButton = (Button) findViewById(R.id.alberta_button);
			albertaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.ARGENTINA:
			Button argentinaButton = (Button) findViewById(R.id.argentina_button);
			argentinaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.BRAZIL:
			Button brazilButton = (Button) findViewById(R.id.brazil_button);
			brazilButton.setText(Integer.toString(numIncrease));
			break;
		case Board.CENTRAL_AMERICA:
			Button centralButton = (Button) findViewById(R.id.central_america_button);
			centralButton.setText(Integer.toString(numIncrease));
			break;
		case Board.CHINA:
			Button chinaButton = (Button) findViewById(R.id.china_button);
			chinaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.CONGO:
			Button congoButton = (Button) findViewById(R.id.congo_button);
			congoButton.setText(Integer.toString(numIncrease));
			break;
		case Board.EAST_AFRICA:
			Button eastAfricaButton = (Button) findViewById(R.id.east_africa_button);
			eastAfricaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.EASTERN_AUST:
			Button eastAustButton = (Button) findViewById(R.id.east_aust_button);
			eastAustButton.setText(Integer.toString(numIncrease));
			break;
		case Board.EASTERN_US:
			Button eastUsButton = (Button) findViewById(R.id.east_us_button);
			eastUsButton.setText(Integer.toString(numIncrease));
			break;
		case Board.EGYPT:
			Button egyptButton = (Button) findViewById(R.id.egypt_button);
			egyptButton.setText(Integer.toString(numIncrease));
			break;
		case Board.GREAT_BRITAIN:
			Button gbButton = (Button) findViewById(R.id.great_britain_button);
			gbButton.setText(Integer.toString(numIncrease));
			break;
		case Board.GREENLAND:
			Button greenlandButton = (Button) findViewById(R.id.greenland_button);
			greenlandButton.setText(Integer.toString(numIncrease));
			break;
		case Board.ICELAND:
			Button icelandButton = (Button) findViewById(R.id.iceland_button);
			icelandButton.setText(Integer.toString(numIncrease));
			break;
		case Board.INDIA:
			Button indiaButton = (Button) findViewById(R.id.india_button);
			indiaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.INDONESIA:
			Button indoButton = (Button) findViewById(R.id.indonesia_button);
			indoButton.setText(Integer.toString(numIncrease));
			break;
		case Board.IRKUTSK:
			Button irkButton = (Button) findViewById(R.id.irkutsk_button);
			irkButton.setText(Integer.toString(numIncrease));
			break;
		case Board.JAPAN:
			Button japButton = (Button) findViewById(R.id.japan_button);
			japButton.setText(Integer.toString(numIncrease));
			break;
		case Board.KAMCHATKA:
			Button kamButton = (Button) findViewById(R.id.kamchatka_button);
			kamButton.setText(Integer.toString(numIncrease));
			break;
		case Board.MADAGASCAR:
			Button madButton = (Button) findViewById(R.id.madagascar_button);
			madButton.setText(Integer.toString(numIncrease));
			break;
		case Board.MIDDLE_EAST:
			Button mideastButton = (Button) findViewById(R.id.middle_east_button);
			mideastButton.setText(Integer.toString(numIncrease));
			break;
		case Board.MONGOLIA:
			Button mongoliaButton = (Button) findViewById(R.id.mongolia_button);
			mongoliaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.NEW_GUINEA:
			Button newguineaButton = (Button) findViewById(R.id.new_guinea_button);
			newguineaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.NORTH_AFRICA:
			Button northAfricaButton = (Button) findViewById(R.id.west_africa_button);
			northAfricaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.NORTHERN_EUROPE:
			Button northEuropeButton = (Button) findViewById(R.id.north_europe_button);
			northEuropeButton.setText(Integer.toString(numIncrease));
			break;
		case Board.NORTHWEST_TERR:
			Button nwTerrButton = (Button) findViewById(R.id.nw_terr_button);
			nwTerrButton.setText(Integer.toString(numIncrease));
			break;
		case Board.ONTARIO:
			Button ontarioButton = (Button) findViewById(R.id.ontario_button);
			ontarioButton.setText(Integer.toString(numIncrease));
			break;
		case Board.PERU:
			Button peruButton = (Button) findViewById(R.id.peru_button);
			peruButton.setText(Integer.toString(numIncrease));
			break;
		case Board.QUEBEC:
			Button quebecButton = (Button) findViewById(R.id.quebec_button);
			quebecButton.setText(Integer.toString(numIncrease));
			break;
		case Board.SCANDINAVIA:
			Button scandinaviaButton = (Button) findViewById(R.id.scandinavia_button);
			scandinaviaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.SIBERIA:
			Button siberiaButton = (Button) findViewById(R.id.siberia_button);
			siberiaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.SOUTH_AFRICA:
			Button southAfricaButton = (Button) findViewById(R.id.south_africa_button);
			southAfricaButton.setText(Integer.toString(numIncrease));
			break;
		case Board.SOUTHEAST_ASIA:
			Button siamButton = (Button) findViewById(R.id.siam_button);
			siamButton.setText(Integer.toString(numIncrease));
			break;
		case Board.SOUTHERN_EUROPE:
			Button southEuropeButton = (Button) findViewById(R.id.south_europe_button);
			southEuropeButton.setText(Integer.toString(numIncrease));
			break;
		case Board.UKRAINE:
			Button ukraineButton = (Button) findViewById(R.id.ukraine_button);
			ukraineButton.setText(Integer.toString(numIncrease));
			break;
		case Board.URAL:
			Button uralButton = (Button) findViewById(R.id.ural_button);
			uralButton.setText(Integer.toString(numIncrease));
			break;
		case Board.VENEZUELA:
			Button venButton = (Button) findViewById(R.id.venezuela_button);
			venButton.setText(Integer.toString(numIncrease));
			break;
		case Board.WESTERN_AUST:
			Button westAustButton = (Button) findViewById(R.id.west_aust_button);
			westAustButton.setText(Integer.toString(numIncrease));
			break;
		case Board.WESTERN_EUROPE:
			Button westEuropeButton = (Button) findViewById(R.id.west_europe_button);
			westEuropeButton.setText(Integer.toString(numIncrease));
			break;
		case Board.WESTERN_US:
			Button westUsButton = (Button) findViewById(R.id.west_us_button);
			westUsButton.setText(Integer.toString(numIncrease));
			break;
		case Board.YAKUTSK:
			Button yakButton = (Button) findViewById(R.id.yakutsk_button);
			yakButton.setText(Integer.toString(numIncrease));
			break;
		}
	}
	
	
	/**
	* Makes a country appear selected or unselected.
	* @param country - the country that was selected.
	* @author Charles Snyder				   
	*/
	private void selectCountry(int country) {
		switch (country) {
		case Board.ALASKA:
			Button alaskaButton = (Button) findViewById(R.id.alaska_button);
			if(alaskaButton.isSelected()) {
				alaskaButton.setAlpha(unselected);
				alaskaButton.setSelected(false);
			}
			else {
				alaskaButton.setAlpha(selected);
				alaskaButton.setSelected(true);
			}
			break;
		case Board.AFGHANISTAN:
			Button afghanButton = (Button) findViewById(R.id.afghanistan_button);
			if(afghanButton.isSelected()) {
				afghanButton.setAlpha(unselected);
				afghanButton.setSelected(false);
			}
			else {
				afghanButton.setAlpha(selected);
				afghanButton.setSelected(true);
			}
			break;
		case Board.ALBERTA:
			Button albertaButton = (Button) findViewById(R.id.alberta_button);
			if(albertaButton.isSelected()) {
				albertaButton.setAlpha(unselected);
				albertaButton.setSelected(false);
			}
			else {
				albertaButton.setAlpha(selected);
				albertaButton.setSelected(true);
			}
			break;
		case Board.ARGENTINA:
			Button argentinaButton = (Button) findViewById(R.id.argentina_button);
			if(argentinaButton.isSelected()) {
				argentinaButton.setAlpha(unselected);
				argentinaButton.setSelected(false);
			}
			else {
				argentinaButton.setAlpha(selected);
				argentinaButton.setSelected(true);
			}
			break;
		case Board.BRAZIL:
			Button brazilButton = (Button) findViewById(R.id.brazil_button);
			if(brazilButton.isSelected()) {
				brazilButton.setAlpha(unselected);
				brazilButton.setSelected(false);
			}
			else {
				brazilButton.setAlpha(selected);
				brazilButton.setSelected(true);
			}
			break;
		case Board.CENTRAL_AMERICA:
			Button centralButton = (Button) findViewById(R.id.central_america_button);
			if(centralButton.isSelected()) {
				centralButton.setAlpha(unselected);
				centralButton.setSelected(false);
			}
			else {
				centralButton.setAlpha(selected);
				centralButton.setSelected(true);
			}
			break;
		case Board.CHINA:
			Button chinaButton = (Button) findViewById(R.id.china_button);
			if(chinaButton.isSelected()) {
				chinaButton.setAlpha(unselected);
				chinaButton.setSelected(false);
			}
			else {
				chinaButton.setAlpha(selected);
				chinaButton.setSelected(true);
			}
			break;
		case Board.CONGO:
			Button congoButton = (Button) findViewById(R.id.congo_button);
			if(congoButton.isSelected()) {
				congoButton.setAlpha(unselected);
				congoButton.setSelected(false);
			}
			else {
				congoButton.setAlpha(selected);
				congoButton.setSelected(true);
			}
			break;
		case Board.EAST_AFRICA:
			Button eastAfricaButton = (Button) findViewById(R.id.east_africa_button);
			if(eastAfricaButton.isSelected()) {
				eastAfricaButton.setAlpha(unselected);
				eastAfricaButton.setSelected(false);
			}
			else {
				eastAfricaButton.setAlpha(selected);
				eastAfricaButton.setSelected(true);
			}
			break;
		case Board.EASTERN_AUST:
			Button eastAustButton = (Button) findViewById(R.id.east_aust_button);
			if(eastAustButton.isSelected()) {
				eastAustButton.setAlpha(unselected);
				eastAustButton.setSelected(false);
			}
			else {
				eastAustButton.setAlpha(selected);
				eastAustButton.setSelected(true);
			}
			break;
		case Board.EASTERN_US:
			Button eastUsButton = (Button) findViewById(R.id.east_us_button);
			if(eastUsButton.isSelected()) {
				eastUsButton.setAlpha(unselected);
				eastUsButton.setSelected(false);
			}
			else {
				eastUsButton.setAlpha(selected);
				eastUsButton.setSelected(true);
			}
			break;
		case Board.EGYPT:
			Button egyptButton = (Button) findViewById(R.id.egypt_button);
			if(egyptButton.isSelected()) {
				egyptButton.setAlpha(unselected);
				egyptButton.setSelected(false);
			}
			else {
				egyptButton.setAlpha(selected);
				egyptButton.setSelected(true);
			}
			break;
		case Board.GREAT_BRITAIN:
			Button gbButton = (Button) findViewById(R.id.great_britain_button);
			if(gbButton.isSelected()) {
				gbButton.setAlpha(unselected);
				gbButton.setSelected(false);
			}
			else {
				gbButton.setAlpha(selected);
				gbButton.setSelected(true);
			}
			break;
		case Board.GREENLAND:
			Button greenlandButton = (Button) findViewById(R.id.greenland_button);
			if(greenlandButton.isSelected()) {
				greenlandButton.setAlpha(unselected);
				greenlandButton.setSelected(false);
			}
			else {
				greenlandButton.setAlpha(selected);
				greenlandButton.setSelected(true);
			}
			break;
		case Board.ICELAND:
			Button icelandButton = (Button) findViewById(R.id.iceland_button);
			if(icelandButton.isSelected()) {
				icelandButton.setAlpha(unselected);
				icelandButton.setSelected(false);
			}
			else {
				icelandButton.setAlpha(selected);
				icelandButton.setSelected(true);
			}
			break;
		case Board.INDIA:
			Button indiaButton = (Button) findViewById(R.id.india_button);
			if(indiaButton.isSelected()) {
				indiaButton.setAlpha(unselected);
				indiaButton.setSelected(false);
			}
			else {
				indiaButton.setAlpha(selected);
				indiaButton.setSelected(true);
			}
			break;
		case Board.INDONESIA:
			Button indoButton = (Button) findViewById(R.id.indonesia_button);
			if(indoButton.isSelected()) {
				indoButton.setAlpha(unselected);
				indoButton.setSelected(false);
			}
			else {
				indoButton.setAlpha(selected);
				indoButton.setSelected(true);
			}
			break;
		case Board.IRKUTSK:
			Button irkButton = (Button) findViewById(R.id.irkutsk_button);
			if(irkButton.isSelected()) {
				irkButton.setAlpha(unselected);
				irkButton.setSelected(false);
			}
			else {
				irkButton.setAlpha(selected);
				irkButton.setSelected(true);
			}
			break;
		case Board.JAPAN:
			Button japButton = (Button) findViewById(R.id.japan_button);
			if(japButton.isSelected()) {
				japButton.setAlpha(unselected);
				japButton.setSelected(false);
			}
			else {
				japButton.setAlpha(selected);
				japButton.setSelected(true);
			}
			break;
		case Board.KAMCHATKA:
			Button kamButton = (Button) findViewById(R.id.kamchatka_button);
			if(kamButton.isSelected()) {
				kamButton.setAlpha(unselected);
				kamButton.setSelected(false);
			}
			else {
				kamButton.setAlpha(selected);
				kamButton.setSelected(true);
			}
			break;
		case Board.MADAGASCAR:
			Button madButton = (Button) findViewById(R.id.madagascar_button);
			if(madButton.isSelected()) {
				madButton.setAlpha(unselected);
				madButton.setSelected(false);
			}
			else {
				madButton.setAlpha(selected);
				madButton.setSelected(true);
			}
			break;
		case Board.MIDDLE_EAST:
			Button mideastButton = (Button) findViewById(R.id.middle_east_button);
			if(mideastButton.isSelected()) {
				mideastButton.setAlpha(unselected);
				mideastButton.setSelected(false);
			}
			else {
				mideastButton.setAlpha(selected);
				mideastButton.setSelected(true);
			}
			break;
		case Board.MONGOLIA:
			Button mongoliaButton = (Button) findViewById(R.id.mongolia_button);
			if(mongoliaButton.isSelected()) {
				mongoliaButton.setAlpha(unselected);
				mongoliaButton.setSelected(false);
			}
			else {
				mongoliaButton.setAlpha(selected);
				mongoliaButton.setSelected(true);
			}
			break;
		case Board.NEW_GUINEA:
			Button newguineaButton = (Button) findViewById(R.id.new_guinea_button);
			if(newguineaButton.isSelected()) {
				newguineaButton.setAlpha(unselected);
				newguineaButton.setSelected(false);
			}
			else {
				newguineaButton.setAlpha(selected);
				newguineaButton.setSelected(true);
			}
			break;
		case Board.NORTH_AFRICA:
			Button northAfricaButton = (Button) findViewById(R.id.west_africa_button);
			if(northAfricaButton.isSelected()) {
				northAfricaButton.setAlpha(unselected);
				northAfricaButton.setSelected(false);
			}
			else {
				northAfricaButton.setAlpha(selected);
				northAfricaButton.setSelected(true);
			}
			break;
		case Board.NORTHERN_EUROPE:
			Button northEuropeButton = (Button) findViewById(R.id.north_europe_button);
			if(northEuropeButton.isSelected()) {
				northEuropeButton.setAlpha(unselected);
				northEuropeButton.setSelected(false);
			}
			else {
				northEuropeButton.setAlpha(selected);
				northEuropeButton.setSelected(true);
			}
			break;
		case Board.NORTHWEST_TERR:
			Button nwTerrButton = (Button) findViewById(R.id.nw_terr_button);
			if(nwTerrButton.isSelected()) {
				nwTerrButton.setAlpha(unselected);
				nwTerrButton.setSelected(false);
			}
			else {
				nwTerrButton.setAlpha(selected);
				nwTerrButton.setSelected(true);
			}
			break;
		case Board.ONTARIO:
			Button ontarioButton = (Button) findViewById(R.id.ontario_button);
			if(ontarioButton.isSelected()) {
				ontarioButton.setAlpha(unselected);
				ontarioButton.setSelected(false);
			}
			else {
				ontarioButton.setAlpha(selected);
				ontarioButton.setSelected(true);
			}
			break;
		case Board.PERU:
			Button peruButton = (Button) findViewById(R.id.peru_button);
			if(peruButton.isSelected()) {
				peruButton.setAlpha(unselected);
				peruButton.setSelected(false);
			}
			else {
				peruButton.setAlpha(selected);
				peruButton.setSelected(true);
			}
			break;
		case Board.QUEBEC:
			Button quebecButton = (Button) findViewById(R.id.quebec_button);
			if(quebecButton.isSelected()) {
				quebecButton.setAlpha(unselected);
				quebecButton.setSelected(false);
			}
			else {
				quebecButton.setAlpha(selected);
				quebecButton.setSelected(true);
			}
			break;
		case Board.SCANDINAVIA:
			Button scandinaviaButton = (Button) findViewById(R.id.scandinavia_button);
			if(scandinaviaButton.isSelected()) {
				scandinaviaButton.setAlpha(unselected);
				scandinaviaButton.setSelected(false);
			}
			else {
				scandinaviaButton.setAlpha(selected);
				scandinaviaButton.setSelected(true);
			}
			break;
		case Board.SIBERIA:
			Button siberiaButton = (Button) findViewById(R.id.siberia_button);
			if(siberiaButton.isSelected()) {
				siberiaButton.setAlpha(unselected);
				siberiaButton.setSelected(false);
			}
			else {
				siberiaButton.setAlpha(selected);
				siberiaButton.setSelected(true);
			}
			break;
		case Board.SOUTH_AFRICA:
			Button southAfricaButton = (Button) findViewById(R.id.south_africa_button);
			if(southAfricaButton.isSelected()) {
				southAfricaButton.setAlpha(unselected);
				southAfricaButton.setSelected(false);
			}
			else {
				southAfricaButton.setAlpha(selected);
				southAfricaButton.setSelected(true);
			}
			break;
		case Board.SOUTHEAST_ASIA:
			Button siamButton = (Button) findViewById(R.id.siam_button);
			if(siamButton.isSelected()) {
				siamButton.setAlpha(unselected);
				siamButton.setSelected(false);
			}
			else {
				siamButton.setAlpha(selected);
				siamButton.setSelected(true);
			}
			break;
		case Board.SOUTHERN_EUROPE:
			Button southEuropeButton = (Button) findViewById(R.id.south_europe_button);
			if(southEuropeButton.isSelected()) {
				southEuropeButton.setAlpha(unselected);
				southEuropeButton.setSelected(false);
			}
			else {
				southEuropeButton.setAlpha(selected);
				southEuropeButton.setSelected(true);
			}
			break;
		case Board.UKRAINE:
			Button ukraineButton = (Button) findViewById(R.id.ukraine_button);
			if(ukraineButton.isSelected()) {
				ukraineButton.setAlpha(unselected);
				ukraineButton.setSelected(false);
			}
			else {
				ukraineButton.setAlpha(selected);
				ukraineButton.setSelected(true);
			}
			break;
		case Board.URAL:
			Button uralButton = (Button) findViewById(R.id.ural_button);
			if(uralButton.isSelected()) {
				uralButton.setAlpha(unselected);
				uralButton.setSelected(false);
			}
			else {
				uralButton.setAlpha(selected);
				uralButton.setSelected(true);
			}
			break;
		case Board.VENEZUELA:
			Button venButton = (Button) findViewById(R.id.venezuela_button);
			if(venButton.isSelected()) {
				venButton.setAlpha(unselected);
				venButton.setSelected(false);
			}
			else {
				venButton.setAlpha(selected);
				venButton.setSelected(true);
			}
			break;
		case Board.WESTERN_AUST:
			Button westAustButton = (Button) findViewById(R.id.west_aust_button);
			if(westAustButton.isSelected()) {
				westAustButton.setAlpha(unselected);
				westAustButton.setSelected(false);
			}
			else {
				westAustButton.setAlpha(selected);
				westAustButton.setSelected(true);
			}
			break;
		case Board.WESTERN_EUROPE:
			Button westEuropeButton = (Button) findViewById(R.id.west_europe_button);
			if(westEuropeButton.isSelected()) {
				westEuropeButton.setAlpha(unselected);
				westEuropeButton.setSelected(false);
			}
			else {
				westEuropeButton.setAlpha(selected);
				westEuropeButton.setSelected(true);
			}
			break;
		case Board.WESTERN_US:
			Button westUsButton = (Button) findViewById(R.id.west_us_button);
			if(westUsButton.isSelected()) {
				westUsButton.setAlpha(unselected);
				westUsButton.setSelected(false);
			}
			else {
				westUsButton.setAlpha(selected);
				westUsButton.setSelected(true);
			}
			break;
		case Board.YAKUTSK:
			Button yakButton = (Button) findViewById(R.id.yakutsk_button);
			if(yakButton.isSelected()) {
				yakButton.setAlpha(unselected);
				yakButton.setSelected(false);
			}
			else {
				yakButton.setAlpha(selected);
				yakButton.setSelected(true);
			}
			break;
		}
	}
	
	/**
	* Puts all country buttons to the unselected state.
	* @author Charles Snyder				   
	*/
	private void resetSelectedButtons() {
		globalDestCountry = -1;
		globalOriginCountry = -1;
		
		findViewById(R.id.afghanistan_button).setAlpha(unselected);
		findViewById(R.id.afghanistan_button).setSelected(false);
		
		findViewById(R.id.alaska_button).setAlpha(unselected);
		findViewById(R.id.alaska_button).setSelected(false);
		
		findViewById(R.id.alberta_button).setAlpha(unselected);
		findViewById(R.id.alberta_button).setSelected(false);
		
		findViewById(R.id.argentina_button).setAlpha(unselected);
		findViewById(R.id.argentina_button).setSelected(false);
		
		findViewById(R.id.brazil_button).setAlpha(unselected);
		findViewById(R.id.brazil_button).setSelected(false);
		
		findViewById(R.id.central_america_button).setAlpha(unselected);
		findViewById(R.id.central_america_button).setSelected(false);
		
		findViewById(R.id.china_button).setAlpha(unselected);
		findViewById(R.id.china_button).setSelected(false);
		
		findViewById(R.id.congo_button).setAlpha(unselected);
		findViewById(R.id.congo_button).setSelected(false);
		
		findViewById(R.id.east_africa_button).setAlpha(unselected);
		findViewById(R.id.east_africa_button).setSelected(false);
		
		findViewById(R.id.east_aust_button).setAlpha(unselected);
		findViewById(R.id.east_aust_button).setSelected(false);
		
		findViewById(R.id.east_us_button).setAlpha(unselected);
		findViewById(R.id.east_us_button).setSelected(false);
		
		findViewById(R.id.egypt_button).setAlpha(unselected);
		findViewById(R.id.egypt_button).setSelected(false);
		
		findViewById(R.id.great_britain_button).setAlpha(unselected);
		findViewById(R.id.great_britain_button).setSelected(false);
		
		findViewById(R.id.greenland_button).setAlpha(unselected);
		findViewById(R.id.greenland_button).setSelected(false);
		
		findViewById(R.id.iceland_button).setAlpha(unselected);
		findViewById(R.id.iceland_button).setSelected(false);
		
		findViewById(R.id.india_button).setAlpha(unselected);
		findViewById(R.id.india_button).setSelected(false);
		
		findViewById(R.id.indonesia_button).setAlpha(unselected);
		findViewById(R.id.indonesia_button).setSelected(false);
		
		findViewById(R.id.irkutsk_button).setAlpha(unselected);
		findViewById(R.id.irkutsk_button).setSelected(false);
		
		findViewById(R.id.japan_button).setAlpha(unselected);
		findViewById(R.id.japan_button).setSelected(false);
		
		findViewById(R.id.kamchatka_button).setAlpha(unselected);
		findViewById(R.id.kamchatka_button).setSelected(false);
		
		findViewById(R.id.madagascar_button).setAlpha(unselected);
		findViewById(R.id.madagascar_button).setSelected(false);
		
		findViewById(R.id.middle_east_button).setAlpha(unselected);
		findViewById(R.id.middle_east_button).setSelected(false);
		
		findViewById(R.id.mongolia_button).setAlpha(unselected);
		findViewById(R.id.mongolia_button).setSelected(false);
		
		findViewById(R.id.new_guinea_button).setAlpha(unselected);
		findViewById(R.id.new_guinea_button).setSelected(false);
		
		findViewById(R.id.west_africa_button).setAlpha(unselected);
		findViewById(R.id.west_africa_button).setSelected(false);
		
		findViewById(R.id.north_europe_button).setAlpha(unselected);
		findViewById(R.id.north_europe_button).setSelected(false);
		
		findViewById(R.id.nw_terr_button).setAlpha(unselected);
		findViewById(R.id.nw_terr_button).setSelected(false);
		
		findViewById(R.id.ontario_button).setAlpha(unselected);
		findViewById(R.id.ontario_button).setSelected(false);
		
		findViewById(R.id.peru_button).setAlpha(unselected);
		findViewById(R.id.peru_button).setSelected(false);
		
		findViewById(R.id.quebec_button).setAlpha(unselected);
		findViewById(R.id.quebec_button).setSelected(false);
		
		findViewById(R.id.scandinavia_button).setAlpha(unselected);
		findViewById(R.id.scandinavia_button).setSelected(false);
		
		findViewById(R.id.siberia_button).setAlpha(unselected);
		findViewById(R.id.siberia_button).setSelected(false);
		
		findViewById(R.id.south_africa_button).setAlpha(unselected);
		findViewById(R.id.south_africa_button).setSelected(false);
		
		findViewById(R.id.siam_button).setAlpha(unselected);
		findViewById(R.id.siam_button).setSelected(false);
		
		findViewById(R.id.south_europe_button).setAlpha(unselected);
		findViewById(R.id.south_europe_button).setSelected(false);
		
		findViewById(R.id.ukraine_button).setAlpha(unselected);
		findViewById(R.id.ukraine_button).setSelected(false);
		
		findViewById(R.id.ural_button).setAlpha(unselected);
		findViewById(R.id.ural_button).setSelected(false);
		
		findViewById(R.id.venezuela_button).setAlpha(unselected);
		findViewById(R.id.venezuela_button).setSelected(false);
		
		findViewById(R.id.west_aust_button).setAlpha(unselected);
		findViewById(R.id.west_aust_button).setSelected(false);
		
		findViewById(R.id.west_europe_button).setAlpha(unselected);
		findViewById(R.id.west_europe_button).setSelected(false);
		
		findViewById(R.id.west_us_button).setAlpha(unselected);
		findViewById(R.id.west_us_button).setSelected(false);
		
		findViewById(R.id.yakutsk_button).setAlpha(unselected);
		findViewById(R.id.yakutsk_button).setSelected(false);
		
	}
	
	/**
	* Associates the correct image of the country to the corresponding button.
	* @param countryImage - the button to be set.
	* @param country - the country to set.
	* @param ownerColor - the color of the owner of the country.
	* @author Charles Snyder				   
	*/
	private void associateCountryImage(Button countryImage, int country, int ownerColor) {
		switch (country) {
		case Board.ALASKA:
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.alaska_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.alaska_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.alaska_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.alaska_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.alaska_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.alaska_country_orange);
				break;
			}
			break;
		case Board.AFGHANISTAN:
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.afghanistan_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.afghanistan_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.afghanistan_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.afghanistan_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.afghanistan_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.afghanistan_country_orange);
				break;
			}
			break;
		case Board.ALBERTA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.alberta_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.alberta_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.alberta_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.alberta_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.alberta_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.alberta_country_orange);
				break;
			}
			break;
		case Board.ARGENTINA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.argentina_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.argentina_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.argentina_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.argentina_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.argentina_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.argentina_country_orange);
				break;
			}
			break;
		case Board.BRAZIL:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.brazil_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.brazil_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.brazil_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.brazil_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.brazil_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.brazil_country_orange);
				break;
			}
			break;
		case Board.CENTRAL_AMERICA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.central_america_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.central_america_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.central_america_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.central_america_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.central_america_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.central_america_country_orange);
				break;
			}
			break;
		case Board.CHINA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.china_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.china_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.china_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.china_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.china_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.china_country_orange);
				break;
			}
			break;
		case Board.CONGO:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.congo_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.congo_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.congo_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.congo_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.congo_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.congo_country_orange);
				break;
			}
			break;
		case Board.EAST_AFRICA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.east_africa_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.east_africa_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.east_africa_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.east_africa_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.east_africa_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.east_africa_country_orange);
				break;
			}
			break;
		case Board.EASTERN_AUST:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.east_aust_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.east_aust_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.east_aust_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.east_aust_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.east_aust_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.east_aust_country_orange);
				break;
			}
			break;
		case Board.EASTERN_US:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.east_us_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.east_us_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.east_us_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.east_us_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.east_us_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.east_us_country_orange);
				break;
			}
			break;
		case Board.EGYPT:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.egypt_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.egypt_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.egypt_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.egypt_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.egypt_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.egypt_country_orange);
				break;
			}
			break;
		case Board.GREAT_BRITAIN:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.great_britain_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.great_britain_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.great_britain_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.great_britain_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.great_britain_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.great_britain_country_orange);
				break;
			}
			break;
		case Board.GREENLAND:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.greenland_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.greenland_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.greenland_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.greenland_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.greenland_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.greenland_country_orange);
				break;
			}
			break;
		case Board.ICELAND:
		
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.iceland_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.iceland_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.iceland_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.iceland_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.iceland_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.iceland_country_orange);
				break;
			}
			break;
		case Board.INDIA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.india_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.india_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.india_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.india_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.india_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.india_country_orange);
				break;
			}
			break;
		case Board.INDONESIA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.indonesia_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.indonesia_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.indonesia_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.indonesia_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.indonesia_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.indonesia_country_orange);
				break;
			}
			break;
		case Board.IRKUTSK:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.irkutsk_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.irkutsk_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.irkutsk_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.irkutsk_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.irkutsk_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.irkutsk_country_orange);
				break;
			}
			break;
		case Board.JAPAN:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.japan_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.japan_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.japan_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.japan_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.japan_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.japan_country_orange);
				break;
			}
			break;
		case Board.KAMCHATKA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.kamchatka_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.kamchatka_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.kamchatka_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.kamchatka_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.kamchatka_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.kamchatka_country_orange);
				break;
			}
			break;
		case Board.MADAGASCAR:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.madagascar_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.madagascar_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.madagascar_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.madagascar_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.madagascar_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.madagascar_country_orange);
				break;
			}
			break;
		case Board.MIDDLE_EAST:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.middle_east_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.middle_east_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.middle_east_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.middle_east_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.middle_east_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.middle_east_country_orange);
				break;
			}
			break;
		case Board.MONGOLIA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.mongolia_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.mongolia_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.mongolia_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.mongolia_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.mongolia_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.mongolia_country_orange);
				break;
			}
			break;
		case Board.NEW_GUINEA:
		
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.new_guinea_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.new_guinea_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.new_guinea_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.new_guinea_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.new_guinea_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.new_guinea_country_orange);
				break;
			}
			break;
		case Board.NORTH_AFRICA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.west_africa_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.west_africa_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.west_africa_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.west_africa_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.west_africa_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.west_africa_country_orange);
				break;
			}
			break;
		case Board.NORTHERN_EUROPE:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.north_europe_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.north_europe_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.north_europe_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.north_europe_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.north_europe_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.north_europe_country_orange);
				break;
			}
			break;
		case Board.NORTHWEST_TERR:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.nw_terr_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.nw_terr_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.nw_terr_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.nw_terr_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.nw_terr_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.nw_terr_country_orange);
				break;
			}
			break;
		case Board.ONTARIO:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.ontario_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.ontario_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.ontario_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.ontario_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.ontario_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.ontario_country_orange);
				break;
			}
			break;
		case Board.PERU:
		
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.peru_country_red);
				break;
			case Player.BLUE:
				countryImage.setBackgroundResource(R.drawable.peru_country_blue);
				break;
			case Player.GREEN:
				countryImage.setBackgroundResource(R.drawable.peru_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.peru_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.peru_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.peru_country_orange);
				break;
			}
			break;
		case Board.QUEBEC:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.quebec_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.quebec_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.quebec_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.quebec_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.quebec_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.quebec_country_orange);
				break;
			}
			break;
		case Board.SCANDINAVIA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.scandinavia_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.scandinavia_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.scandinavia_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.scandinavia_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.scandinavia_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.scandinavia_country_orange);
				break;
			}
			break;
		case Board.SIBERIA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.siberia_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.siberia_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.siberia_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.siberia_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.siberia_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.siberia_country_orange);
				break;
			}
			break;
		case Board.SOUTH_AFRICA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.south_africa_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.south_africa_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.south_africa_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.south_africa_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.south_africa_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.south_africa_country_orange);
				break;
			}
			break;
		case Board.SOUTHEAST_ASIA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.siam_country_red);
				break;
			case Player.BLUE:
				countryImage.setBackgroundResource(R.drawable.siam_country_blue);
				break;
			case Player.GREEN:
				countryImage.setBackgroundResource(R.drawable.siam_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.siam_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.siam_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.siam_country_orange);
				break;
			}
			break;
		case Board.SOUTHERN_EUROPE:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.south_europe_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.south_europe_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.south_europe_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.south_europe_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.south_europe_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.south_europe_country_orange);
				break;
			}
			break;
		case Board.UKRAINE:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.ukraine_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.ukraine_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.ukraine_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.ukraine_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.ukraine_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.ukraine_country_orange);
				break;
			}
			break;
		case Board.URAL:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage.setBackgroundResource(R.drawable.ural_country_red);
				break;
			case Player.BLUE:
				countryImage.setBackgroundResource(R.drawable.ural_country_blue);
				break;
			case Player.GREEN:
				countryImage.setBackgroundResource(R.drawable.ural_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.ural_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.ural_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.ural_country_orange);
				break;
			}
			break;
		case Board.VENEZUELA:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.venezuela_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.venezuela_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.venezuela_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.venezuela_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.venezuela_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.venezuela_country_orange);
				break;
			}
			break;
		case Board.WESTERN_AUST:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.west_aust_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.west_aust_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.west_aust_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.west_aust_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.west_aust_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.west_aust_country_orange);
				break;
			}
			break;
		case Board.WESTERN_EUROPE:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.west_europe_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.west_europe_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.west_europe_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.west_europe_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.west_europe_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.west_europe_country_orange);
				break;
			}
			break;
		case Board.WESTERN_US:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.west_us_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.west_us_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.west_us_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.west_us_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.west_us_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.west_us_country_orange);
				break;
			}
			break;
		case Board.YAKUTSK:
			
			switch (ownerColor) {
			case Player.RED:
				countryImage
						.setBackgroundResource(R.drawable.yakutsk_country_red);
				break;
			case Player.BLUE:
				countryImage
						.setBackgroundResource(R.drawable.yakutsk_country_blue);
				break;
			case Player.GREEN:
				countryImage
						.setBackgroundResource(R.drawable.yakutsk_country_green);
				break;
			case Player.YELLOW:
				countryImage
						.setBackgroundResource(R.drawable.yakutsk_country_yellow);
				break;
			case Player.PURPLE:
				countryImage
						.setBackgroundResource(R.drawable.yakutsk_country_purple);
				break;
			case Player.ORANGE:
				countryImage
						.setBackgroundResource(R.drawable.yakutsk_country_orange);
				break;
			}
			break;
		}	
	}
	
	
	
	/**
	* Displays a prompt to the user if they have been attacked by a computer player.
	* It allows them to choose how many dice to roll during their defense.
	* @param attackDice - the dice roll results of the attacker.
	* @param attackLocations - the defending and attacking countries.
	* @param defenderColor - the color of the defending player.
	* @param attackColor - the color of the attacking player.
	* @author Charles Snyder				   
	*/
	public void displayDefendDialog(final ArrayList<Integer> attackDice, final ArrayList<Integer> attackLocations, final int defenderColor, final int attackColor) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.defend_dialog);
		dialog.setTitle(R.string.defend_title);
		
		// Get attacking and defending countries.
		final int defendCountry = attackLocations.get(1);
		final int attackCountry = attackLocations.get(0);
		
		final RadioButton defendDiceTwo = (RadioButton) dialog.findViewById(R.id.defend_dice_two);
		final RadioGroup diceGroup = (RadioGroup) dialog.findViewById(R.id.defend_radio_group);
		
		Button defendImage = (Button) dialog.findViewById(R.id.defend_country_image);
		Button attackImage = (Button) dialog.findViewById(R.id.attack_country_image);
		
		TextView defendArmyInfo = (TextView) dialog.findViewById(R.id.defend_army_count);
		TextView attackArmyInfo = (TextView) dialog.findViewById(R.id.attack_army_count);
		
		try {
			// Display proper amount of dice.
			if(currentMainGame.getBoard().getBoardSpaceArmy(defendCountry) < 2) {
				defendDiceTwo.setVisibility(View.GONE);
			}
			else {
				defendDiceTwo.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			associateCountryImage(defendImage, defendCountry, currentMainGame.getBoard().getBoardSpaceOwner(defendCountry));
			associateCountryImage(attackImage, attackCountry, currentMainGame.getBoard().getBoardSpaceOwner(attackCountry));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			// Display number of armies in each country.
			defendArmyInfo.setText("Number of armies: " + Integer.toString(currentMainGame.getBoard().getBoardSpaceArmy(defendCountry)));
			attackArmyInfo.setText("Number of armies: " + Integer.toString(currentMainGame.getBoard().getBoardSpaceArmy(attackCountry)));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		Button dialogButton = (Button) dialog.findViewById(R.id.defend_button);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selectedId = diceGroup.getCheckedRadioButtonId();
				int playerIndex = currentMainGame.findPlayerIndex(defenderColor);
				ArrayList<Integer> defendDice = new ArrayList<Integer>();
				
				// Roll selected number of dice.
				switch(selectedId) {
				case R.id.defend_dice_one:
					int dieRoll = currentMainGame.getPlayersArray().get(playerIndex).rollOneDie();
					defendDice.add(dieRoll);
					break;
					
				case R.id.defend_dice_two:
					defendDice = currentMainGame.getPlayersArray().get(playerIndex).rollTwoDice();
					break;
				default:
					return;	
				}
				
				// Execute move.
				currentPlayer = currentMainGame.getPlayersArray().get(currentMainGame.findPlayerIndex(defenderColor));
				int result = currentMainGame.attackResult(attackCountry, defendCountry, attackDice, defendDice);
				
				// Display results.
				displayAttackDefendResults(result, attackDice, defendDice, false);
				
				// Check if the attacker captured the territory.
				if(currentMainGame.canCaptureTerritory(defendCountry) == true) {
					try {
						currentMainGame.getBoard().setBoardSpaceOwner(defendCountry, attackColor);
						int cpuIndex = currentMainGame.findPlayerIndex(attackColor);
						currentMainGame.getPlayersArray().get(cpuIndex).addCountryControl(defendCountry);
						currentPlayer.removeCountryControl(defendCountry);
						if(currentMainGame.getPlayersArray().get(cpuIndex) instanceof Cpu) {
							cpuPlayer = (Cpu) currentMainGame.getPlayersArray().get(cpuIndex);
							cpuPlayer.moveArmiesToCaptureCountry(currentMainGame.getBoard(), attackCountry, defendCountry, attackDice.size());	
						}
						if(currentPlayer.getCountriesControlled().size() == 0) {
							displayEndGameResult(false);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				refreshBoard();
				dialog.dismiss();
				
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}

	
	/**
	* Displays the results of a battle, the dice rolls and who won.
	* @param result - the result of the battle.
	* @param attackDice - the dice roll results of the attacker.
	* @param defendDice - the dice roll results of the defender.
	* @param attackDefend - specifies if battle was a defense or attack for the human player.
	* @author Charles Snyder				   
	*/
	private void displayAttackDefendResults(int result, ArrayList<Integer> attackDice, ArrayList<Integer> defendDice, boolean attackDefend) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.attack_defend_results_dialog);
		dialog.setTitle(R.string.battle_results);
		String message;
		// True is attack, false is defend.
		if(attackDefend == true) {
			switch(result) {
			case 0:
				message = "The defense lost one army";
				break;
			case 1:
				message = "You lost one army";
				break;
			case 2:
				message = "The defense lost two armies";
				break;
			case 3:
				message = "You lost two armies";
				break;
			case 4:
				message = "Both sides lost one army";
				break;
			default:
				message = "";
			}
		}
		else {
			switch(result) {
			case 0:
				message = "You lost one army";
				break;
			case 1:
				message = "The attacker lost one army";
				break;
			case 2:
				message = "You lost two armies";
				break;
			case 3:
				message = "The attacker lost two armies";
				break;
			case 4:
				message = "Both sides lost one army";
				break;
			default:
				message = "";
			}
		}
		
		TextView messageText = (TextView) dialog.findViewById(R.id.battle_results_text);
		messageText.setText(message);
		
		ImageView defendDiceOne = (ImageView) dialog.findViewById(R.id.defend_dice_result_one);
		ImageView defendDiceTwo = (ImageView) dialog.findViewById(R.id.defend_dice_result_two);
		ImageView attackDiceOne = (ImageView) dialog.findViewById(R.id.attack_dice_result_one);
		ImageView attackDiceTwo = (ImageView) dialog.findViewById(R.id.attack_dice_result_two);
		ImageView attackDiceThree = (ImageView) dialog.findViewById(R.id.attack_dice_result_three);
		
		// Display proper amount of dice images.
		try {
			if(defendDice.size() < 2) {
				FirstPlayer.selectDieToDisplay(defendDiceOne, defendDice.get(0));
				defendDiceTwo.setVisibility(View.GONE);
			}
			else if(defendDice.size() == 2) {
				FirstPlayer.selectDieToDisplay(defendDiceOne, defendDice.get(0));
				FirstPlayer.selectDieToDisplay(defendDiceTwo, defendDice.get(1));
				defendDiceTwo.setVisibility(View.VISIBLE);
			}
		
			if(attackDice.size() < 2) {
				FirstPlayer.selectDieToDisplay(attackDiceOne, attackDice.get(0));
				attackDiceTwo.setVisibility(View.GONE);
				attackDiceThree.setVisibility(View.GONE);
			}
			else if(attackDice.size() == 2) {
				FirstPlayer.selectDieToDisplay(attackDiceOne, attackDice.get(0));
				FirstPlayer.selectDieToDisplay(attackDiceTwo, attackDice.get(1));
				attackDiceTwo.setVisibility(View.VISIBLE);
				attackDiceThree.setVisibility(View.GONE);
			}
			else if(attackDice.size() == 3) {
				FirstPlayer.selectDieToDisplay(attackDiceOne, attackDice.get(0));
				FirstPlayer.selectDieToDisplay(attackDiceTwo, attackDice.get(1));
				FirstPlayer.selectDieToDisplay(attackDiceThree, attackDice.get(2));
				attackDiceTwo.setVisibility(View.VISIBLE);
				attackDiceThree.setVisibility(View.VISIBLE);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		Button dialogButton = (Button) dialog.findViewById(R.id.ok_button_battle_results);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
				cpuPhaseThreeTurn(playerIndex, false);
				dialog.dismiss();
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);

		
	}
	
	
	
	/**
	* Converts a integer color into its correct string representation.
	* @param color - the integer color value to convert.
	* @return the string value of the color.
	* @author Charles Snyder				   
	*/
	private String convertColorToString(int color) {
		switch(color) {
		case 0:
			return "Red";
		case 1:
			return "Blue";
		case 2:
			return "Green";
		case 3:
			return "Yellow";
		case 4:
			return "Orange";
		case 5:
			return "Purple";
		default:
			return "";
		}
	}
	
	
	
	/**
	* Responds to the view cards button being pressed and sends the game
	* to the view cards screen.
	* @param view - view of the screen, base class for widgets.
	* @author Charles Snyder				   
	*/
	public void viewCards(View view) {
		Intent intent = new Intent(PlayScreen.this, CardActivity.class);
		intent.putExtra(GAME_PLAY_SCREEN, currentMainGame);
		startActivity(intent);
		finish();
	}
	
	
	/**
	* Responds to the end turn button being pressed.
	* @param view - view of the screen, base class for widgets.
	* @author Charles Snyder				   
	*/
	public void endTurn(View view) {
		// Get current player.
		int playerIndex = currentMainGame.findPlayerIndex(currentMainGame.getFirstFromTurnDeque());
		currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
		
		// Try to end the army placement portion of the players turn, but the user still has armies to place.
		if(currentMainGame.getIsPhaseThree() == true && currentPlayer.getArmyTotal() != 0 && userDeployPhase == true) {
			if(currentPlayer.getCardHand().size() >= 5) {
				displayMustTurnInCards();
				return;
			}
			displayArmiesRemaining();
			return;
		}
		// End the army placement phase of players turn.
		else if(currentMainGame.getIsPhaseThree() == true && currentPlayer.getArmyTotal() == 0 && userDeployPhase == true) {
			userAttackPhase = true;
			userDeployPhase = false;
			TextView instruct = (TextView) findViewById(R.id.instructions);
			instruct.setText(R.string.phase_three_instructions_capture);
			
			Button cardButton = (Button) findViewById(R.id.view_cards_button);
			cardButton.setVisibility(View.GONE);
		}
		// End the attack phase of the players turn.
		else if(userAttackPhase == true) {
			userAttackPhase = false;
			userDeployPhase = false;
			currentMainGame.setIsPhaseThree(false);
			currentMainGame.setIsPhaseFour(true);
			resetSelectedButtons();
			refreshBoard();
			updateCardGameInfo();
			updateUserInfoDisplay();
			
			TextView instruct = (TextView) findViewById(R.id.instructions);
			instruct.setText(R.string.phase_four_instructions);
			
			if(currentPlayer.getDidCaptureTerritory() == true) {
				displayReceiveCardCapture();
			}
			
		}
		// End the fortify phase of players turn, thus ending their turn and the next player goes.
		else if(currentMainGame.getIsPhaseFour() == true) {
			currentPlayer.setDidCaptureTerritory(false);
			currentMainGame.endPlayerTurn(currentPlayer.getColor());
			resetSelectedButtons();
			currentMainGame.setIsPhaseFour(false);
			currentMainGame.setIsPhaseThree(true);
			userDeployPhase = true;
			initializePhaseThree();
			displayPhaseThree();
		}
	}
	
	
	/**
	* Display a prompt to the player showing them the card the received at the end of their turn
	* if they captured a territory during their turn.
	* @author Charles Snyder				   
	*/
	private void displayReceiveCardCapture() {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.player_receive_card_dialog);
		dialog.setTitle(R.string.receive_card_title);
		
		if(currentMainGame.getCardDeck().getDeckSize() != 0) {
			currentPlayer.takeCard(currentMainGame.getCardDeck());
		}
		else {
			currentMainGame.resetCardDeck();
			currentPlayer.takeCard(currentMainGame.getCardDeck());
		}
		
		Button cardImage = (Button) dialog.findViewById(R.id.receive_card_capture);
		CardActivity.associateImageToCard(cardImage, currentPlayer.getCardHand().get(currentPlayer.getCardHand().size() - 1));
		
		Button dialogButton = (Button) dialog.findViewById(R.id.receive_card_ok);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);

		
	}
	
	
	
	/**
	* Display a prompt to the player telling them that they have too many cards
	* and must trade in a set.
	* @author Charles Snyder				   
	*/
	private void displayMustTurnInCards() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.turn_in_cards);

		builder.setMessage(R.string.turn_in_card_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Display a prompt to the player telling them that they still have
	* armies remaining to place.
	* @author Charles Snyder				   
	*/
	private void displayArmiesRemaining() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.armies_remaining);

		builder.setMessage(R.string.armies_remaining_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Display a prompt to the player telling them how they
	* received the number of armies they got.
	* @author Charles Snyder				   
	*/
	private void displayArmyBreakdown(int base, int contBonus) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.army_breakdown);
		String message ="";
		message = "New troops based on countries controlled: " + Integer.toString(base) + "\n";
		message += "New troops based on continents controlled: " + Integer.toString(contBonus);

		builder.setMessage(message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Display a prompt to the player asking them how many units they
	* want to place in the country they selected.
	* @param armyCount - the number of total armies the player has.
	* @param country - the country the player selected.
	* @author Charles Snyder				   
	*/
	public void displayArmyPlacementDialog(int armyCount, final int country) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.deploy_army_count_dialog);
		dialog.setTitle(R.string.army_placement);
		final Spinner armyNum = (Spinner) dialog.findViewById(R.id.army_select_spinner);
		
		// Populate the spinner with the number of armies the player has.
		addItemsOnUnitPlacementSpinner(armyCount, armyNum);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.ok_button_army_select);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String numString = (String) armyNum.getSelectedItem();
				int choice = Integer.parseInt(numString);
				try {
					currentPlayer.placeUnits(currentMainGame.getBoard(), country, choice);
					refreshBoard();
					updateUserInfoDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Display a dialog to the player asking them how many armies they want to move
	* during a fortify action.
	* @param armyCount - the number of armies the player can move.
	* @param originCountry - the country the player is removing armies from.
	* @param destCounty - the country the player is fortifying.
	* @author Charles Snyder				   
	*/
	public void displayFortifyArmyMovementDialog(int armyCount, final int originCountry, final int destCountry) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.fortify_count_dialog);
		dialog.setTitle(R.string.fortify_country);
		final Spinner armyNum = (Spinner) dialog.findViewById(R.id.fortify_army_select_spinner);
		
		// Populate fortify spinner with armies.
		addItemsOnFortifyMovementSpinner(armyCount, armyNum);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.fortify_ok_button_army_select);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get choice from user.
				String numString = (String) armyNum.getSelectedItem();
				int choice = Integer.parseInt(numString);
				if(choice == 0) {
					dialog.dismiss();
					return;
				}
				try {
					// Execute move.
					currentPlayer.moveUnits(currentMainGame.getBoard(), originCountry, destCountry, choice);
					refreshBoard();
					currentPlayer.setDidCaptureTerritory(false);
					currentMainGame.endPlayerTurn(currentPlayer.getColor());
					currentMainGame.setIsPhaseFour(false);
					currentMainGame.setIsPhaseThree(true);
					userDeployPhase = true;
					resetSelectedButtons();
					initializePhaseThree();
					displayPhaseThree();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Display a dialog to the user when the have initiated an attacking move.  It allows them to choose
	* how many dice they want to roll.
	* @param defendCountry - the country that is defending.
	* @param attackCountry - the country that is attacking.
	* @param defenderColor - the color of the defender.
	* @param attackColor - the color of the attacker.
	* @author Charles Snyder				   
	*/
	public void displayAttackDialog(final int defendCountry, final int attackCountry, final int defenderColor, final int attackColor) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.attack_dialog);
		dialog.setTitle(R.string.attack_title);
		
		final RadioButton attackDiceTwo = (RadioButton) dialog.findViewById(R.id.attack_dice_two);
		final RadioButton attackDiceThree = (RadioButton) dialog.findViewById(R.id.attack_dice_three);
		final RadioGroup diceGroup = (RadioGroup) dialog.findViewById(R.id.attack_radio_group);
		
		Button defendImage = (Button) dialog.findViewById(R.id.defend_country_image_cpu);
		Button attackImage = (Button) dialog.findViewById(R.id.attack_country_image_human);
		
		TextView defendArmyInfo = (TextView) dialog.findViewById(R.id.defend_army_count_cpu);
		TextView attackArmyInfo = (TextView) dialog.findViewById(R.id.attack_army_count_human);
		
		// Display proper amount of radio buttons for die selection.
		try {
			if(currentMainGame.getBoard().getBoardSpaceArmy(attackCountry) == 2) {
				attackDiceThree.setVisibility(View.GONE);
				attackDiceTwo.setVisibility(View.GONE);
			}
			else if(currentMainGame.getBoard().getBoardSpaceArmy(attackCountry) == 3) {
				attackDiceThree.setVisibility(View.GONE);
				attackDiceTwo.setVisibility(View.VISIBLE);
			}
			else if(currentMainGame.getBoard().getBoardSpaceArmy(attackCountry) > 3) {
				attackDiceThree.setVisibility(View.VISIBLE);
				attackDiceTwo.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			associateCountryImage(defendImage, defendCountry, currentMainGame.getBoard().getBoardSpaceOwner(defendCountry));
			associateCountryImage(attackImage, attackCountry, currentMainGame.getBoard().getBoardSpaceOwner(attackCountry));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			// Display army counts of both countries.
			defendArmyInfo.setText("Number of armies: " + Integer.toString(currentMainGame.getBoard().getBoardSpaceArmy(defendCountry)));
			attackArmyInfo.setText("Number of armies: " + Integer.toString(currentMainGame.getBoard().getBoardSpaceArmy(attackCountry)));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		Button dialogButton = (Button) dialog.findViewById(R.id.attack_button);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selectedId = diceGroup.getCheckedRadioButtonId();
				int playerIndex = currentMainGame.findPlayerIndex(attackColor);
				currentPlayer = currentMainGame.getPlayersArray().get(playerIndex);
				ArrayList<Integer> attackDice = new ArrayList<Integer>();
				
				// Get user selection of number of dice to roll.		
				switch(selectedId) {
				case R.id.attack_dice_one:
					int dieRoll = currentPlayer.rollOneDie();
					attackDice.add(dieRoll);
					break;					
				case R.id.attack_dice_two:
					attackDice = currentPlayer.rollTwoDice();
					break;
				case R.id.attack_dice_three:
					attackDice = currentPlayer.rollThreeDice();
					break;
				default:
					return;	
				}
				
				// Get the defender to roll.
				ArrayList<Integer> defendDice = new ArrayList<Integer>();
				if(currentMainGame.getPlayersArray().get(currentMainGame.findPlayerIndex(defenderColor)) instanceof Cpu) {
					cpuPlayer = (Cpu) currentMainGame.getPlayersArray().get(currentMainGame.findPlayerIndex(defenderColor));
					defendDice = cpuPlayer.cpuDiceRollDefend(currentMainGame.getBoard(), defendCountry);
				}
				// Determine battle result.
				int result = currentMainGame.attackResult(attackCountry, defendCountry, attackDice, defendDice);
				
				// Display results.
				displayAttackDefendResults(result, attackDice, defendDice, true);
				
				// Check to see if player captured the territory.
				if(currentMainGame.canCaptureTerritory(defendCountry) == true) {
					currentPlayer.setDidCaptureTerritory(true);
					try {
						currentMainGame.getBoard().setBoardSpaceOwner(defendCountry, attackColor);
						int cpuIndex = currentMainGame.findPlayerIndex(defenderColor);
						currentPlayer.addCountryControl(defendCountry);
						currentMainGame.getPlayersArray().get(cpuIndex).removeCountryControl(defendCountry);	
						ArrayList<Player> eliminated = currentMainGame.eliminatePlayersFromGame();
						
						// If no other players remain in game, game is over.
						if(currentMainGame.getPlayersArray().size() <= 1) {
							displayEndGameResult(true);
							dialog.dismiss();
							return;
						}
						displayCaptureTerritoryDialog(currentMainGame.getBoard().getBoardSpaceArmy(attackCountry), attackDice.size(), attackCountry, defendCountry, eliminated);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				updateUserInfoDisplay();
				refreshBoard();
				resetSelectedButtons();
				dialog.dismiss();
				
			}
		});
		
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel_attack_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Displays a dialog to the user to ask them how many armies they want to move
	* into a captured territory.
	* @param armyCount - the armies the user can move into captured territory.
	* @param diceThrown - the number of dice the attacker used.
	* @param originCountry - the country the player attacked from.
	* @param destCountry - the country the player captured.
	* @param eliminated - list that holds any eliminated players.
	* @author Charles Snyder				   
	*/
	public void displayCaptureTerritoryDialog(int armyCount, int diceThrown, final int originCountry, final int destCountry, final ArrayList<Player> eliminated) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.capture_territory_dialog);
		dialog.setTitle(R.string.captured_message);
		final Spinner armyNum = (Spinner) dialog.findViewById(R.id.army_occupy_spinner);
		
		// Populate the spinner that allows the user to choose how many armies to move into
		// capture territory.
		addItemsCaptureAllocationSpinner(armyCount, diceThrown, armyNum);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.ok_button_occupy_select);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get player choice.
				String numString = (String) armyNum.getSelectedItem();
				int choice = Integer.parseInt(numString);
				try {
					currentPlayer.captureTerritory(originCountry, destCountry, currentMainGame.getBoard(), choice);
					refreshBoard();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(eliminated.isEmpty() == false) {
					displayPlayerEliminated(eliminated.get(0).getColor());
					// Take the eliminated players cards.
					ArrayList<Card> cards = new ArrayList<Card>(eliminated.get(0).getCardHand());
					for(int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
						currentPlayer.addCardHand(cards.get(cardIndex));
					}
					if(currentPlayer.getCardHand().size() >= 6) {
				
						// Must trade in cards here.
						displayMustTradeInCards();
					}
				}
				dialog.dismiss();
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Displays a prompt saying which player was eliminated.
	* @param playerColor - the color of the player that was eliminated.
	* @author Charles Snyder				   
	*/
	private void displayPlayerEliminated(int playerColor) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.player_eliminated);
		
		String message;
		
		message = convertColorToString(playerColor);
		message += " player has been eliminated from the game";

		builder.setMessage(message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Displays a prompt saying that the player either won or lost.
	* @param winOrLoss - indicates whether the human player won or lost the game.
	* @author Charles Snyder				   
	*/
	private void displayEndGameResult(boolean winOrLoss) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(winOrLoss == true) {
			builder.setTitle(R.string.victory);
			builder.setMessage(R.string.victory_message);
		}
		else {
			builder.setTitle(R.string.game_over);
			builder.setMessage(R.string.game_over_message);
		}

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				Intent intent = new Intent(PlayScreen.this, MainActivity.class);
				startActivity(intent);
				finish();
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	
	/**
	* Displays a prompt saying that user must trade in a card set.
	* @author Charles Snyder				   
	*/
	private void displayMustTradeInCards() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.must_trade_in);

		builder.setMessage(R.string.must_trade_in_message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				Intent intent = new Intent(PlayScreen.this, CardActivity.class);
				intent.putExtra(GAME_PLAY_SCREEN, currentMainGame);
				intent.addFlags(1);
				startActivity(intent);
				finish();
				dialog.cancel();
			}
		});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	/**
	* Adds items to the capture territory dialog spinner.
	* It ensures the proper amount of armies are selectable.
	* @param armyCount - the number of armies the user used in attack.
	* @param diceThrown - the number of dice used in attack move.
	* @param armySelect - the spinner to populate.
	* @author Charles Snyder				   
	*/
	public void addItemsCaptureAllocationSpinner(int armyCount, int diceThrown, Spinner armySelect) {
		ArrayList<String> list = new ArrayList<String>();
		for(int index = diceThrown; index < armyCount; index++) {
			list.add(Integer.toString(index));
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			armySelect.setAdapter(dataAdapter);
	}
	
	
}
