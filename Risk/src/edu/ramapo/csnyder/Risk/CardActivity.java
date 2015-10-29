package edu.ramapo.csnyder.Risk;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.ramapo.csnyder.gameLogic.*;
import edu.ramapo.csnyder.Risk.R;

public class CardActivity extends Activity {
	public final static String GAME_CARDS = "edu.ramapo.csnyder.Risk.GAME_CARDS";
	private static Game currentGameCard;
	private static Player currentPlayer;
	private final static float selected = (float) 0.5;
	private final static float unselected = (float) 1.0;
	
	private Card selectedCardOne;
	private Card selectedCardTwo;
	private Card selectedCardThree;
	
	private static Card cardSpaceOne;
	private static Card cardSpaceTwo;
	private static Card cardSpaceThree;
	private static Card cardSpaceFour;
	private static Card cardSpaceFive;
	private static Card cardSpaceSix;
	private static Card cardSpaceSeven;
	private static Card cardSpaceEight;
	private static Card cardSpaceNine;
	private static Card cardSpaceTen;
	private static Card cardSpaceEleven;
	private static Card cardSpaceTwelve;
	
	private boolean mustTradeIn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		
		selectedCardOne = null;
		selectedCardTwo = null;
		selectedCardThree = null;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_card);
		
		selectedCardOne = null;
		selectedCardTwo = null;
		selectedCardThree = null;
	
		Intent intent = getIntent();
		
		// Check to see if intent from play screen came from scenario where player must trade in cards
		// after eliminating a player.
		if(intent.getFlags() == 1) {
			mustTradeIn = true;
		}
		else {
			mustTradeIn = false;
		}
		currentGameCard = intent.getExtras().getParcelable(PlayScreen.GAME_PLAY_SCREEN);
		
		int playerIndex = currentGameCard.findPlayerIndex(currentGameCard.getFirstFromTurnDeque());
		currentPlayer = currentGameCard.getPlayersArray().get(playerIndex);
		ArrayList<Card> cards = currentPlayer.getCardHand();

		displayCards(cards);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card, menu);
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
	 * Attempts to trade in three selected cards by the player.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void tradeIn(View view) {
		if(!areAllCardsSelected()) {
			return;
		}
		ArrayList<Boolean> results = null;
		
		// See if the selected cards are a valid match.
		if(!currentGameCard.addArmyFromCardsHuman(currentPlayer, selectedCardOne, selectedCardTwo, selectedCardThree)) {
			displayInvalidTradeIn();
			resetSelectedCards();
			return;
		}
		// Check to see if player received 2 army bonus from owning a country on a card that was traded in.
		else if (currentPlayer.getCardBonus() == false) {
			ArrayList<Boolean> ownResults = new ArrayList<Boolean>(currentGameCard.doesPlayerOwnCardCountry(currentPlayer, selectedCardOne, selectedCardTwo, selectedCardThree));
			
			results = new ArrayList<Boolean>(ownResults);
			
			// See if the player owns two or more cards thus giving them a choice of where to place the bonus.
			if((ownResults.get(0) == true && ownResults.get(1) == true)
					|| (ownResults.get(0) == true && ownResults.get(2) == true)
					|| (ownResults.get(1) == true && ownResults.get(2) == true)) {
				try {
					currentPlayer.addToArmyTotal(2);
					currentPlayer.setCardBonus(true);
					displayChoiceCardBonus(ownResults, selectedCardOne, selectedCardTwo, selectedCardThree);				
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
			else if(ownResults.get(0) == true) {
				try {
					currentPlayer.addToArmyTotal(2);
					currentPlayer.setCardBonus(true);
					currentPlayer.placeUnits(currentGameCard.getBoard(), selectedCardOne.getCardCountry(), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				displayAutoCardBonus(selectedCardOne);
			}
			else if(ownResults.get(1) == true) {
				try {
					currentPlayer.addToArmyTotal(2);
					currentPlayer.setCardBonus(true);
					currentPlayer.placeUnits(currentGameCard.getBoard(), selectedCardTwo.getCardCountry(), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				displayAutoCardBonus(selectedCardTwo);
			}
			else if(ownResults.get(2) == true) {
				try {
					currentPlayer.addToArmyTotal(2);
					currentPlayer.setCardBonus(true);
					currentPlayer.placeUnits(currentGameCard.getBoard(), selectedCardThree.getCardCountry(), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				displayAutoCardBonus(selectedCardThree);
			}
			else if(mustTradeIn == true && currentPlayer.getCardHand().size() <= 4 && results.get(0) == false && results.get(1) == false && results.get(2) == false) {
				Intent intent = new Intent(CardActivity.this, PlayScreen.class);
				intent.putExtra(GAME_CARDS, currentGameCard);
				startActivity(intent);
				finish();
			}
		}
		
		// If the player traded in a set after having come from the main game screen after eliminating a player,
		// did not own any of the cards, and has reduced their deck size to 4 or less.
		else if(mustTradeIn == true && currentPlayer.getCardHand().size() <= 4 && currentPlayer.getCardBonus() == true) {
			Intent intent = new Intent(CardActivity.this, PlayScreen.class);
			intent.putExtra(GAME_CARDS, currentGameCard);
			startActivity(intent);
			finish();
		}

		
		// Set the display of the cards back to default state.
		resetSelectedCards();
		resetCardButtons();
		displayCards(currentPlayer.getCardHand());



	}
	
	
	
	/** 
	 * Goes back to the play game screen.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void goBack(View view) {
		// If came to the screen after eliminating a player, then you can not go back you must trade in cards.
		if(mustTradeIn == false) {
			Intent intent = new Intent(CardActivity.this, PlayScreen.class);
			intent.putExtra(GAME_CARDS, currentGameCard);
			startActivity(intent);
			finish();
		}
		else {
			displayMustTradeInCards();
		}
	}
	
	
	
	/** 
	 * Dialog that prompts the user they must trade in a set of cards.
	 * @author Charles Snyder
	 */
	private void displayMustTradeInCards() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.must_trade_in);

		builder.setMessage(R.string.must_trade_in_message);

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
	 * Check to see if the first card is selected.
	 * @return True if selected, false if not.
	 * @author Charles Snyder
	 */
	private boolean isCardOneSelected() {
		if(selectedCardOne == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	/** 
	 * Check to see if the second card is selected.
	 * @return True if selected, false if not.
	 * @author Charles Snyder
	 */
	private boolean isCardTwoSelected() {
		if(selectedCardTwo == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/** 
	 * Check to see if the third card is selected.
	 * @return True if selected, false if not.
	 * @author Charles Snyder
	 */
	private boolean isCardThreeSelected() {
		if(selectedCardThree == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	/** 
	 * Check to see if all three cards have been chosen.
	 * @return True if all three are in selected state, false if not.
	 * @author Charles Snyder
	 */
	private boolean areAllCardsSelected() {
		if(isCardOneSelected() == true && isCardTwoSelected() == true && isCardThreeSelected() == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/** 
	 * Put the global selected cards into the selected state if not already chosen.
	 * @param cardValue - the card object that was chosen.
	 * @author Charles Snyder
	 */
	private void setSelectedCard(Card cardValue) {
		if(!isCardOneSelected()) {
			selectedCardOne = new Card(cardValue);
		}
		else if(!isCardTwoSelected()) {
			selectedCardTwo = new Card(cardValue);
		}
		else if(!isCardThreeSelected()) {
			selectedCardThree = new Card(cardValue);
		}
	}
	
	
	/** 
	 * Put the global selected cards into the deselected state if already chosen.
	 * @param cardValue - the card object that was chosen.
	 * @author Charles Snyder
	 */
	private void deselectCard(Card cardValue) {
		if(selectedCardOne != null) {
			if(selectedCardOne.equals(cardValue)) {
				selectedCardOne = null;
			}
		}
		if(selectedCardTwo != null) {
			if(selectedCardTwo.equals(cardValue)) {
				selectedCardTwo = null;
			}
		}
		if(selectedCardThree != null) {
			if(selectedCardThree.equals(cardValue)) {
				selectedCardThree = null;
			}
		}
	}
	
	
	
	/** 
	 * Set the card in slot one to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceOne(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_one);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceOne);
		}
		else if(!areAllCardsSelected()) {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceOne);
		}
	}

	
	/** 
	 * Set the card in slot two to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceTwo(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_two);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceTwo);
		}
		else if(!areAllCardsSelected()) {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceTwo);
		}
	}
	
	
	/** 
	 * Set the card in slot three to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceThree(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_three);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceThree);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceThree);
		}
	}

	
	/** 
	 * Set the card in slot four to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceFour(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_four);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceFour);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceFour);
		}
	}

	
	/** 
	 * Set the card in slot five to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceFive(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_five);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceFive);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceFive);
		}
	}

	
	/** 
	 * Set the card in slot six to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceSix(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_six);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceSix);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceSix);
		}
	}

	
	/** 
	 * Set the card in slot seven to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceSeven(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_seven);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceSeven);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceSeven);
		}
	}

	
	/** 
	 * Set the card in slot eight to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceEight(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_eight);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceEight);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceEight);
		}
	}

	
	/** 
	 * Set the card in slot nine to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceNine(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_nine);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceNine);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceNine);
		}
	}
	
	
	/** 
	 * Set the card in slot ten to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceTen(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_ten);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceTen);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceTen);
		}
	}

	
	/** 
	 * Set the card in slot eleven to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceEleven(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_eleven);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceEleven);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceEleven);
		}
	}

	
	/** 
	 * Set the card in slot twelve to selected, or deselect it if it is already selected.
	 * @param view - view of the screen, base class for widgets.
	 * @author Charles Snyder
	 */
	public void selectCardSpaceTwelve(View view) {
		Button cardSpace = (Button) findViewById(R.id.card_space_twelve);
		if(cardSpace.isSelected()) {
			cardSpace.setSelected(false);
			cardSpace.setAlpha(unselected);
			deselectCard(cardSpaceTwelve);
		}
		else if(!areAllCardsSelected())  {
			cardSpace.setSelected(true);
			cardSpace.setAlpha(selected);
			setSelectedCard(cardSpaceTwelve);
		}
	}
	
	
	/** 
	 * Set all card buttons to the default state.
	 * @author Charles Snyder
	 */
	private void resetSelectedCards() {
		selectedCardOne = null;
		selectedCardTwo = null;
		selectedCardThree = null;
		
		findViewById(R.id.card_space_twelve).setSelected(false);
		findViewById(R.id.card_space_twelve).setAlpha(unselected);

		findViewById(R.id.card_space_eleven).setSelected(false);
		findViewById(R.id.card_space_eleven).setAlpha(unselected);

		findViewById(R.id.card_space_ten).setSelected(false);
		findViewById(R.id.card_space_ten).setAlpha(unselected);
			
		findViewById(R.id.card_space_nine).setSelected(false);
		findViewById(R.id.card_space_nine).setAlpha(unselected);

		findViewById(R.id.card_space_eight).setSelected(false);
		findViewById(R.id.card_space_eight).setAlpha(unselected);

		findViewById(R.id.card_space_seven).setSelected(false);
		findViewById(R.id.card_space_seven).setAlpha(unselected);

		findViewById(R.id.card_space_six).setSelected(false);
		findViewById(R.id.card_space_six).setAlpha(unselected);

		findViewById(R.id.card_space_five).setSelected(false);
		findViewById(R.id.card_space_five).setAlpha(unselected);

		findViewById(R.id.card_space_four).setSelected(false);
		findViewById(R.id.card_space_four).setAlpha(unselected);
		
		findViewById(R.id.card_space_three).setSelected(false);
		findViewById(R.id.card_space_three).setAlpha(unselected);
	
		findViewById(R.id.card_space_two).setSelected(false);
		findViewById(R.id.card_space_two).setAlpha(unselected);
		
		findViewById(R.id.card_space_one).setSelected(false);
		findViewById(R.id.card_space_one).setAlpha(unselected);
	}
	
	
	/** 
	 * Makes all card buttons invisible.
	 * @author Charles Snyder
	 */
	private void resetCardButtons() {
		findViewById(R.id.card_space_one).setVisibility(View.GONE);
		findViewById(R.id.card_space_two).setVisibility(View.GONE);
		findViewById(R.id.card_space_three).setVisibility(View.GONE);
		findViewById(R.id.card_space_four).setVisibility(View.GONE);
		findViewById(R.id.card_space_five).setVisibility(View.GONE);
		findViewById(R.id.card_space_six).setVisibility(View.GONE);
		findViewById(R.id.card_space_seven).setVisibility(View.GONE);
		findViewById(R.id.card_space_eight).setVisibility(View.GONE);
		findViewById(R.id.card_space_nine).setVisibility(View.GONE);
		findViewById(R.id.card_space_ten).setVisibility(View.GONE);
		findViewById(R.id.card_space_eleven).setVisibility(View.GONE);
		findViewById(R.id.card_space_twelve).setVisibility(View.GONE);
	}
	
	
	/** 
	 * Displays the same amount of cards that are in the players hand.
	 * @param cards - the players hand of cards.
	 * @author Charles Snyder
	 */
	private void displayCards(ArrayList<Card> cards) {
		switch(cards.size()) {
		case 12:
			findViewById(R.id.card_space_twelve).setVisibility(View.VISIBLE);
			Button button_twelve = (Button) findViewById(R.id.card_space_twelve);
			cardSpaceTwelve = new Card(cards.get(11));
			associateImageToCard(button_twelve, cardSpaceTwelve);
		case 11:
			findViewById(R.id.card_space_eleven).setVisibility(View.VISIBLE);
			Button button_eleven = (Button) findViewById(R.id.card_space_eleven);
			cardSpaceEleven = new Card(cards.get(10));
			associateImageToCard(button_eleven,cardSpaceEleven);
		case 10:
			findViewById(R.id.card_space_ten).setVisibility(View.VISIBLE);
			Button button_ten = (Button) findViewById(R.id.card_space_ten);
			cardSpaceTen = new Card(cards.get(9));
			associateImageToCard(button_ten,cardSpaceTen);
		case 9:
			findViewById(R.id.card_space_nine).setVisibility(View.VISIBLE);
			Button button_nine = (Button) findViewById(R.id.card_space_nine);
			cardSpaceNine = new Card(cards.get(8));
			associateImageToCard(button_nine, cardSpaceNine);
		case 8:
			findViewById(R.id.card_space_eight).setVisibility(View.VISIBLE);
			Button button_eight = (Button) findViewById(R.id.card_space_eight);
			cardSpaceEight = new Card(cards.get(7));
			associateImageToCard(button_eight, cardSpaceEight);
		case 7:
			findViewById(R.id.card_space_seven).setVisibility(View.VISIBLE);
			Button button_seven = (Button) findViewById(R.id.card_space_seven);
			cardSpaceSeven = new Card(cards.get(6));
			associateImageToCard(button_seven, cardSpaceSeven);
		case 6:
			findViewById(R.id.card_space_six).setVisibility(View.VISIBLE);
			Button button_six = (Button) findViewById(R.id.card_space_six);
			cardSpaceSix = new Card(cards.get(5));
			associateImageToCard(button_six, cardSpaceSix);
		case 5:
			findViewById(R.id.card_space_five).setVisibility(View.VISIBLE);
			Button button_five = (Button) findViewById(R.id.card_space_five);
			cardSpaceFive = new Card(cards.get(4));
			associateImageToCard(button_five, cardSpaceFive);
		case 4:
			findViewById(R.id.card_space_four).setVisibility(View.VISIBLE);
			Button button_four = (Button) findViewById(R.id.card_space_four);
			cardSpaceFour = new Card(cards.get(3));
			associateImageToCard(button_four, cardSpaceFour);
		case 3:
			findViewById(R.id.card_space_three).setVisibility(View.VISIBLE);
			Button button_three = (Button) findViewById(R.id.card_space_three);
			cardSpaceThree = new Card(cards.get(2));
			associateImageToCard(button_three, cardSpaceThree);
		case 2:
			findViewById(R.id.card_space_two).setVisibility(View.VISIBLE);
			Button button_two = (Button) findViewById(R.id.card_space_two);
			cardSpaceTwo = new Card(cards.get(1));
			associateImageToCard(button_two, cardSpaceTwo);
		case 1:
			findViewById(R.id.card_space_one).setVisibility(View.VISIBLE);
			Button button_one = (Button) findViewById(R.id.card_space_one);
			cardSpaceOne = new Card(cards.get(0));
			associateImageToCard(button_one, cardSpaceOne);
			break;
		default:
			break;
		}
	}
	
	
	/** 
	 * Associates the correct card image to the card object from the player's hand.
	 * @param cardButton - the button that will represent the card and hold the image.
	 * @param card - the player's card.
	 * @author Charles Snyder
	 */
	public static void associateImageToCard(Button cardButton, Card card) {
		int country = card.getCardCountry();
		switch(country) {
		case 0:
			cardButton.setBackgroundResource(R.drawable.alaska);
			break;
		case 1:
			cardButton.setBackgroundResource(R.drawable.alberta);
			break;
		case 2:
			cardButton.setBackgroundResource(R.drawable.ontario);
			break;
		case 3:
			cardButton.setBackgroundResource(R.drawable.nw_terr);
			break;
		case 4:
			cardButton.setBackgroundResource(R.drawable.greenland);
			break;
		case 5:
			cardButton.setBackgroundResource(R.drawable.quebec);
			break;
		case 6:
			cardButton.setBackgroundResource(R.drawable.west_us);
			break;
		case 7:
			cardButton.setBackgroundResource(R.drawable.east_us);
			break;
		case 8:
			cardButton.setBackgroundResource(R.drawable.central_america);
			break;
		case 9:
			cardButton.setBackgroundResource(R.drawable.venezuela);
			break;
		case 10:
			cardButton.setBackgroundResource(R.drawable.peru);
			break;
		case 11:
			cardButton.setBackgroundResource(R.drawable.brazil);
			break;
		case 12:
			cardButton.setBackgroundResource(R.drawable.argentina);
			break;
		case 13:
			cardButton.setBackgroundResource(R.drawable.iceland);
			break;
		case 14:
			cardButton.setBackgroundResource(R.drawable.great_britain);
			break;
		case 15:
			cardButton.setBackgroundResource(R.drawable.scandinavia);
			break;
		case 16:
			cardButton.setBackgroundResource(R.drawable.north_europe);
			break;
		case 17:
			cardButton.setBackgroundResource(R.drawable.west_europe);
			break;
		case 18:
			cardButton.setBackgroundResource(R.drawable.southern_europe);
			break;
		case 19:
			cardButton.setBackgroundResource(R.drawable.ukraine);
			break;
		case 20:
			cardButton.setBackgroundResource(R.drawable.north_africa);
			break;
		case 21:
			cardButton.setBackgroundResource(R.drawable.egypt);
			break;
		case 22:
			cardButton.setBackgroundResource(R.drawable.east_africa);
			break;
		case 23:
			cardButton.setBackgroundResource(R.drawable.congo);
			break;
		case 24:
			cardButton.setBackgroundResource(R.drawable.south_africa);
			break;
		case 25:
			cardButton.setBackgroundResource(R.drawable.madagascar);
			break;
		case 26:
			cardButton.setBackgroundResource(R.drawable.middle_east);
			break;
		case 27:
			cardButton.setBackgroundResource(R.drawable.afghanistan);
			break;
		case 28:
			cardButton.setBackgroundResource(R.drawable.ural);
			break;
		case 29:
			cardButton.setBackgroundResource(R.drawable.siberia);
			break;
		case 30:
			cardButton.setBackgroundResource(R.drawable.india);
			break;
		case 31:
			cardButton.setBackgroundResource(R.drawable.china);
			break;
		case 32:
			cardButton.setBackgroundResource(R.drawable.siam);
			break;
		case 33:
			cardButton.setBackgroundResource(R.drawable.mongolia);
			break;
		case 34:
			cardButton.setBackgroundResource(R.drawable.irkutsk);
			break;
		case 35:
			cardButton.setBackgroundResource(R.drawable.yakutsk);
			break;
		case 36:
			cardButton.setBackgroundResource(R.drawable.kamchatka);
			break;
		case 37:
			cardButton.setBackgroundResource(R.drawable.japan);
			break;
		case 38:
			cardButton.setBackgroundResource(R.drawable.indonesia);
			break;
		case 39:
			cardButton.setBackgroundResource(R.drawable.new_guinea);
			break;
		case 40:
			cardButton.setBackgroundResource(R.drawable.west_aust);
			break;
		case 41:
			cardButton.setBackgroundResource(R.drawable.east_aust);
			break;
		case 42:
			cardButton.setBackgroundResource(R.drawable.wild_card);
			break;
		case 43:
			cardButton.setBackgroundResource(R.drawable.wild_card);
			break;
		default:
			break;
		}
	}
	
	
	
	/** 
	 * Display dialog to player that the cards they selected are not a valid match.
	 * @author Charles Snyder
	 */
	private void displayInvalidTradeIn() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.invalid_trade);				
		builder.setMessage(R.string.invalid_trade_message);

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
	 * Display dialog to player that they owned one of the countries on the cards they traded in,
	 * and thus receive a two army bonus there.
	 * @param selectedCard - the card that holds the country the player owned.
	 * @author Charles Snyder
	 */
	private void displayAutoCardBonus(Card selectedCard) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.automatic_card_bonus_dialog);
		dialog.setTitle(R.string.card_bonus);
		
		Button cardImage = (Button) dialog.findViewById(R.id.auto_bonus_card);
		associateImageToCard(cardImage, selectedCard);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.auto_bonus_ok);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// If this was displayed after eliminating a player from the play screen, hitting ok will go back to the play screen.
				if(mustTradeIn == true && currentPlayer.getCardHand().size() <= 4) {
					Intent intent = new Intent(CardActivity.this, PlayScreen.class);
					intent.putExtra(GAME_CARDS, currentGameCard);
					startActivity(intent);
					finish();
				}
				dialog.dismiss();
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		
	}
	
	
	
	/** 
	 * Display dialog to player that they owned two or more of the countries on the cards they traded in,
	 * and thus receive a two army bonus in one of them, they have a choice as to where.
	 * @param choices - the three indices correspond the the three cards, each represents whether that card holds a country the player owns.
	 * @param cardOne - the first selected card.
	 * @param cardTwo - the second selected card.
	 * @param cardThree - the third selected card.
	 * @author Charles Snyder
	 */
	private void displayChoiceCardBonus(ArrayList<Boolean> choices, final Card cardOne, final Card cardTwo, final Card cardThree) {
		final Dialog dialog = new Dialog(this);			
		dialog.setContentView(R.layout.card_bonus_choice_dialog);
		dialog.setTitle(R.string.card_bonus);
		final Button cardImageOne = (Button) dialog.findViewById(R.id.card_choice_one);
		final Button cardImageTwo = (Button) dialog.findViewById(R.id.card_choice_two);
		final Button cardImageThree = (Button) dialog.findViewById(R.id.card_choice_three);
		
		// Display the relevant cards.
		if(choices.get(0) == true) {
			cardImageOne.setVisibility(View.VISIBLE);
			associateImageToCard(cardImageOne, cardOne);
		}
		
		if(choices.get(1) == true) {
			cardImageTwo.setVisibility(View.VISIBLE);
			associateImageToCard(cardImageTwo, cardTwo);
		}
		
		if(choices.get(2) == true) {
			cardImageThree.setVisibility(View.VISIBLE);
			associateImageToCard(cardImageThree, cardThree);
		}
		
		// Create on click listeners to respond to each card being touched.
		cardImageOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cardImageOne.isSelected() == false) {
					cardImageOne.setSelected(true);
					cardImageOne.setAlpha(selected);
					cardImageTwo.setSelected(false);
					cardImageTwo.setAlpha(unselected);
					cardImageThree.setSelected(false);
					cardImageThree.setAlpha(unselected);
				}
				else {
					cardImageOne.setSelected(false);
					cardImageOne.setAlpha(unselected);
				}
			}
		});
		
		cardImageTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cardImageTwo.isSelected() == false) {
					cardImageTwo.setSelected(true);
					cardImageTwo.setAlpha(selected);
					cardImageOne.setSelected(false);
					cardImageOne.setAlpha(unselected);
					cardImageThree.setSelected(false);
					cardImageThree.setAlpha(unselected);
				}
				else {
					cardImageTwo.setSelected(false);
					cardImageTwo.setAlpha(unselected);
				}
			}
		});
		
		cardImageThree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cardImageThree.isSelected() == false) {
					cardImageThree.setSelected(true);
					cardImageThree.setAlpha(selected);
					cardImageOne.setSelected(false);
					cardImageOne.setAlpha(unselected);
					cardImageTwo.setSelected(false);
					cardImageTwo.setAlpha(unselected);
				}
				else {
					cardImageThree.setSelected(false);
					cardImageThree.setAlpha(unselected);
				}
			}
		});
		
		
		// When hitting ok make sure a card was selected and place the army bonus there.
		Button dialogButton = (Button) dialog.findViewById(R.id.choice_bonus_ok);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					if(cardImageOne.isSelected()) {
						try {
							currentPlayer.placeUnits(currentGameCard.getBoard(), cardOne.getCardCountry(), 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(mustTradeIn == true && currentPlayer.getCardHand().size() <= 4) {
							Intent intent = new Intent(CardActivity.this, PlayScreen.class);
							intent.putExtra(GAME_CARDS, currentGameCard);
							startActivity(intent);
							finish();
						}
						dialog.dismiss();
					}
					else if(cardImageTwo.isSelected()) {
						try {
							currentPlayer.placeUnits(currentGameCard.getBoard(), cardTwo.getCardCountry(), 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(mustTradeIn == true && currentPlayer.getCardHand().size() <= 4) {
							Intent intent = new Intent(CardActivity.this, PlayScreen.class);
							intent.putExtra(GAME_CARDS, currentGameCard);
							startActivity(intent);
							finish();
						}
						dialog.dismiss();
					}
					else if(cardImageThree.isSelected()) {
						try {
							currentPlayer.placeUnits(currentGameCard.getBoard(), cardThree.getCardCountry(), 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(mustTradeIn == true && currentPlayer.getCardHand().size() <= 4) {
							Intent intent = new Intent(CardActivity.this, PlayScreen.class);
							intent.putExtra(GAME_CARDS, currentGameCard);
							startActivity(intent);
							finish();
						}
						dialog.dismiss();
					}
			}
		});
		
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}
	

	
}
