package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;
import java.util.Random;

import android.os.Parcelable;
import android.os.Parcel;

public class Player implements Parcelable {
	
	// Colors and their associated integer values.
	public final static int RED = 0;
	public final static int BLUE = 1;
	public final static int GREEN = 2;
	public final static int YELLOW = 3;
	public final static int ORANGE = 4;
	public final static int PURPLE = 5;
	private static Random randomNum = new Random();
	
	protected int m_color;
	protected int m_armies;
	protected ArrayList<Card> m_cardHand;
	protected ArrayList<Integer> m_countryControl;
	protected boolean m_turn;
	protected boolean m_cardBonus;
	protected boolean m_didCaptureTerritory;
	
	
	/** 
	 * Class constructor.
	 * @author Charles Snyder
	 */
	public Player() {
		m_color = RED;
		m_armies = 0;
		m_cardHand = new ArrayList<Card>();
		m_countryControl = new ArrayList<Integer>();
		m_turn = false;
		m_cardBonus = false;
		m_didCaptureTerritory = false;
	}
	
	
	/** 
	 * Class constructor with color parameter.
	 * @param a_color - the color the player has selected.
	 * @author Charles Snyder
	 */
	public Player(int a_color) {
		m_color = a_color;
		m_armies = 0;
		m_cardHand = new ArrayList<Card>();
		m_countryControl = new ArrayList<Integer>();
		m_turn = false;
		m_cardBonus = false;
		m_didCaptureTerritory = false;
	}
	
	/** 
	 * Class copy constructor.
	 * @param a_player - the Player object to be copied.
	 * @author Charles Snyder
	 */
	public Player(Player a_player) {
		m_color = a_player.getColor();
		m_armies = a_player.getArmyTotal();
		m_cardHand = new ArrayList<Card>(a_player.getCardHand());
		m_countryControl = new ArrayList<Integer>(a_player.getCountriesControlled());
		m_turn = a_player.getTurn();
		m_cardBonus = a_player.getCardBonus();
		m_didCaptureTerritory = a_player.getDidCaptureTerritory();
	}
	
	
	/** 
	 * Selector for m_didCaptureTerritory member variable.
	 * @return Whether player has captured a territory during their turn.
	 * @author Charles Snyder
	 */
	public final boolean getDidCaptureTerritory() {
		return m_didCaptureTerritory;
	}
	
	
	/** 
	 * Selector for m_color member variable.
	 * @return the color of the player as an integer.
	 * @author Charles Snyder
	 */
	public final int getColor() {
		return m_color;
	}
	
	/** 
	 * Selector for m_armies member variable.
	 * @return the number of armies the player has to place on the board.
	 * @author Charles Snyder
	 */
	public final int getArmyTotal() {
		return m_armies;
	}
	
	
	/** 
	 * Selector for m_cardHand member variable.
	 * @return tmp - a copy of the m_cardHand list, it is the current hand of cards the player owns.
	 * @author Charles Snyder
	 */
	public ArrayList<Card> getCardHand() {
		ArrayList<Card> tmp = new ArrayList<Card>(m_cardHand);
		return tmp;
	}
	
	
	/** 
	 * Finds a particular card from the player's hand.
	 * @param a_card - card object to be searched for.
	 * @return tmp - The card that was searched for from the players hand.
	 * @throws noCard - the specified card was not found in the player's hand.
	 * @author Charles Snyder
	 */
	public Card getCardFromHand(Card a_card) throws Exception {
		if(m_cardHand.contains(a_card) == true) {
			int cardIndex = m_cardHand.indexOf(a_card);
			Card tmp = new Card(m_cardHand.get(cardIndex));
			return tmp;
		}
		else {
			Exception noCard = new Exception("Card not in hand");
			throw noCard;
		}
	}
	
	
	/** 
	 * Selector for the m_countryControl member variable.
	 * @return tmp - copy of the m_countryControl array list, holds all the countries currently owned by the player.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> getCountriesControlled() {
		ArrayList<Integer> tmp = new ArrayList<Integer>(m_countryControl);
		return tmp;
	}
	
	
	/** 
	 * Finds the number of countries a player controls.
	 * @return integer value based on number of objects in m_countryControl member variable.
	 * @author Charles Snyder
	 */
	public final int getNumberCountriesControl() {
		return m_countryControl.size();
	}
	
	
	/** 
	 * Selector for m_turn member variable.
	 * @return The current status of the player's turn. True it is their turn, false it is not.
	 * @author Charles Snyder
	 */
	public final boolean getTurn() {
		return m_turn;
	}
	
	
	/** 
	 * Selector for m_cardBonus member variable.
	 * @return Whether the player received their two army bonus for owning a country on one of the cards they traded in.
	 * @author Charles Snyder
	 */
	public final boolean getCardBonus() {
		return m_cardBonus;
	}
	
	/** 
	 * Mutator for m_didCaptureTerritory member variable.
	 * @param a_capture - new value to be set to.
	 * @author Charles Snyder
	 */
	public void setDidCaptureTerritory(boolean a_capture) {
		m_didCaptureTerritory = a_capture;
	}
	
	
	/** 
	 * Mutator for m_color member variable.
	 * @param a_color - new value to be set to.
	 * @throws invalidColor - if a_color is less than 0 or greater than 5.
	 * @author Charles Snyder
	 */
	public void setColor(int a_color) throws Exception {
		if(a_color < 0 || a_color > 5) {
			Exception invalidColor = new Exception("Invalid color");
			throw invalidColor;
		}
		else {
			m_color = a_color;
		}
	}
	
	
	
	/** 
	 * Mutator for m_armies member variable.
	 * @param a_newTotal - new value to be set to.
	 * @throws invalidValue - if a_newTotal is less than 0.
	 * @author Charles Snyder
	 */
	public void setArmyTotal(int a_newTotal) throws Exception {
		if(a_newTotal < 0) {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
		else {
			m_armies = a_newTotal;
		}
	}
	
	
	/** 
	 * Mutator for m_cardHand member variable.
	 * @param a_cards - new value to be set to.
	 * @author Charles Snyder
	 */
	public void setCardHand(ArrayList<Card> a_cards) {
		m_cardHand = new ArrayList<Card>(a_cards);
	}
	
	
	/** 
	 * Mutator for m_countryControl member variable.
	 * @param a_countries - new value to be set to.
	 * @author Charles Snyder
	 */
	public void setCountryControl(ArrayList<Integer> a_countries) {
		m_countryControl = new ArrayList<Integer>(a_countries);
	}
	
	
	/** 
	 * Add to current total of m_armies.
	 * @param a_addition - amount to add.
	 * @throws invalidValue - if a_addition is less than 0.
	 * @author Charles Snyder
	 */
	public void addToArmyTotal(int a_addition) throws Exception{
		if(a_addition < 0) {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
		else {
			m_armies = m_armies + a_addition;
		}
	}
	
	
	/** 
	 * Removes from the current total of m_armies.
	 * @param a_subtraction - amount to subtract.
	 * @throws invalidValue - if a_subtraction is less than 0.
	 * @author Charles Snyder
	 */
	public void removeFromArmyTotal(int a_subtraction) throws Exception {
		if(a_subtraction < 0) {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
		else {
			m_armies = m_armies - a_subtraction;
		}
	}
	
	
	/** 
	 * Adds a new card to the player's hand.
	 * @param a_newCard - Card object to add to m_cardHand array list.
	 * @author Charles Snyder
	 */
	public void addCardHand(Card a_newCard) {
		m_cardHand.add(a_newCard);
	}
	
	
	
	/** 
	 * Takes a card from the deck and adds it to the players hand.
	 * @param a_cards - CardDeck object to remove card from.
	 * @author Charles Snyder
	 */
	public void takeCard(CardDeck a_cards) {
		try {
			addCardHand(a_cards.drawCard());
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * Removes a card from the player's hand.
	 * @param a_Card - Card object to be removed from the hand.
	 * @throws noCard - the selected card is not in the hand.
	 * @author Charles Snyder
	 */
	public void removeCardFromHand(Card a_Card) throws Exception {
		if(m_cardHand.contains(a_Card) == true) {
			m_cardHand.remove(a_Card);
		}
		else {
			Exception noCard = new Exception("Specified card not in hand");
			throw noCard;
		}
	}
	
	
	/** 
	 * Adds a country to the players control list.
	 * @param a_newCountry - the country to be added.
	 * @throws invalidValue - if a_newCountry is outside the valid country range of 0 through 41 or is already in the control list.
	 * @author Charles Snyder
	 */
	public void addCountryControl(int a_newCountry) throws Exception {
		if(a_newCountry >= 0 && a_newCountry < 42 && m_countryControl.contains((Object)a_newCountry) == false) {
			m_countryControl.add(a_newCountry);
		}
		else {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
	}
	
	
	
	/** 
	 * Removes a country from the players control list.
	 * @param a_removeCountry - the country to be removed.
	 * @throws invalidValue - if a_newCountry is outside the valid country range of 0 through 41 or is not in the control list.
	 * @author Charles Snyder
	 */
	public void removeCountryControl(int a_removeCountry) throws Exception {
		if(a_removeCountry >= 0 && a_removeCountry < 42 && m_countryControl.contains((Object)a_removeCountry) == true) {
			m_countryControl.remove((Object) a_removeCountry);
		}
		else {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
	}
	
	
	
	/** 
	 * Determines whether a country is in the players control list.
	 * @param a_country - the country being searched for.
	 * @return True if a_country is in the players control list, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean isCountryControlled(int a_country) {
		if(m_countryControl.contains((Object)a_country)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/** 
	 * Mutator for m_turn member variable.
	 * @param a_turn - the new value to be set to.
	 * @author Charles Snyder
	 */
	public void setTurn(boolean a_turn) {
		m_turn = a_turn;
	}
	
	
	/** 
	 * Mutator for m_cardBonus member variable.
	 * @param a_cardBonus - the new value to be set to.
	 * @author Charles Snyder
	 */
	public void setCardBonus(boolean a_cardBonus) {
		m_cardBonus = a_cardBonus;
	}
	
	
	/** 
	 * Determines status of player's turn.
	 * @return True if currently player's turn, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean isTurn() {
		if(m_turn == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/** 
	 * Determines if certain card is in players hand
	 * @param a_card - Card object to be searched for.
	 * @return True if a_card is in players hand, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean isCardInHand(Card a_card) {
		if(m_cardHand.contains(a_card) == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/** 
	 * Gets the number of continents controlled by a player.
	 * @return returnVal - integer value that represents how many continents a player controls.
	 * @author Charles Snyder
	 */
	public final int getContinentsControlled() {
		int returnVal = 0;
		ArrayList<Integer> countries = new ArrayList<Integer>(getCountriesControlled());
		
		// Asia
		if(countries.contains((Object)Board.KAMCHATKA) && countries.contains((Object)Board.YAKUTSK)
				&& countries.contains((Object)Board.IRKUTSK) && countries.contains((Object)Board.MONGOLIA)
				&& countries.contains((Object)Board.JAPAN) && countries.contains((Object)Board.CHINA)
				&& countries.contains((Object)Board.SOUTHEAST_ASIA) && countries.contains((Object)Board.INDIA)
				&& countries.contains((Object)Board.MIDDLE_EAST) && countries.contains((Object)Board.AFGHANISTAN)
				&& countries.contains((Object)Board.URAL) && countries.contains((Object)Board.SIBERIA)) {
			
			returnVal++;
			
		}
		// Europe
		if(countries.contains((Object)Board.UKRAINE) && countries.contains((Object)Board.SCANDINAVIA)
				&& countries.contains((Object)Board.ICELAND) && countries.contains((Object)Board.GREAT_BRITAIN)
				&& countries.contains((Object)Board.WESTERN_EUROPE) && countries.contains((Object)Board.NORTHERN_EUROPE)
				&& countries.contains((Object)Board.SOUTHERN_EUROPE)) {
				
			returnVal++;
			
		}
		// Australia
		if(countries.contains((Object)Board.INDONESIA) && countries.contains((Object)Board.NEW_GUINEA)
				&& countries.contains((Object)Board.WESTERN_AUST) && countries.contains((Object)Board.EASTERN_AUST)){
				
			returnVal++;
			
		}
		// Africa
		if(countries.contains((Object)Board.EAST_AFRICA) && countries.contains((Object)Board.EGYPT)
				&& countries.contains((Object)Board.NORTH_AFRICA) && countries.contains((Object)Board.CONGO)
				&& countries.contains((Object)Board.SOUTH_AFRICA) && countries.contains((Object)Board.MADAGASCAR)) {
			returnVal++;
		}
		// South America
		if(countries.contains((Object)Board.VENEZUELA) && countries.contains((Object)Board.PERU)
					&& countries.contains((Object)Board.BRAZIL) && countries.contains((Object)Board.ARGENTINA)) {
			returnVal++;
		}
		// North America
		if(countries.contains((Object)Board.CENTRAL_AMERICA) && countries.contains((Object)Board.WESTERN_US)
				&& countries.contains((Object)Board.EASTERN_US) && countries.contains((Object)Board.QUEBEC)
				&& countries.contains((Object)Board.ONTARIO) && countries.contains((Object)Board.ALBERTA)
				&& countries.contains((Object)Board.NORTHWEST_TERR) && countries.contains((Object)Board.ALASKA)
				&& countries.contains((Object)Board.GREENLAND)) {
			returnVal++;
		}
		
		return returnVal;
	}

	
	/** 
	 * For first army placement, army must be placed into empty territory.
	 * @param a_gameBoard - the current board of the game.
	 * @param a_country - the country to select.
	 * @throws noUnits - if m_armies is less than one.
	 * @throws occupiedCountry - if the selected country is already occupied.
	 * @author Charles Snyder
	 */
	public void placeUnitInitial(Board a_gameBoard, int a_country) throws Exception {
		if(m_armies < 1) {
			Exception noUnits = new Exception("No units available");
			throw noUnits;
		}
		
		// Initialize variables to arbitrary values.
		int boardSpaceOwner = -2;
		int boardSpaceArmy = 0;
		try {
			boardSpaceOwner = a_gameBoard.getBoardSpaceOwner(a_country);
			boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(a_country);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// -1 means no one owns the space yet.
		if(boardSpaceOwner == -1) {
			try {
				a_gameBoard.setBoardSpaceOwner(a_country, m_color);
				a_gameBoard.setBoardSpaceArmy(a_country, boardSpaceArmy + 1);
				m_armies--;
				m_countryControl.add(a_country);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Exception occupiedCountry = new Exception("Country Occupied");
			throw occupiedCountry;
		}
	}
	
	
	/** 
	 * For first army placement, once every territory is occupied.
	 * @param a_gameBoard - the current board of the game.
	 * @param a_country - the country to select.
	 * @throws noUnits - if m_armies is less than one.
	 * @throws invalidCountry - if the selected country is not owned by the player.
	 * @author Charles Snyder
	 */
	public void placeUnitAllOccupied(Board a_gameBoard, int a_country) throws Exception {
		if(m_armies < 1) {
			Exception noUnits = new Exception("No units available");
			throw noUnits;
		}
		
		// Initialize variables to arbitrary values.
		int boardSpaceOwner = -2;
		int boardSpaceArmy = 0;
		try {
			boardSpaceOwner = a_gameBoard.getBoardSpaceOwner(a_country);
			boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(a_country);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// The player owns the selected board space.
		if(boardSpaceOwner == m_color) {
			try {
				a_gameBoard.setBoardSpaceArmy(a_country, boardSpaceArmy + 1);
				m_armies--;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Exception invalidCountry = new Exception("Invalid Country");
			throw invalidCountry;
		}
	}
	
	
	/** 
	 * For army placement after first two phases of the game.
	 * Meant for the start of each players turn.
	 * Assumes that player already owns territory so does not add to occupied countries array list.
	 * @param a_gameBoard - the current board of the game.
	 * @param a_country - the country to select.
	 * @param unitCount - number of units to place in a_country.
	 * @throws invalidValue - if m_armies is less than one or unitCount is greater than m_armies.
	 * @throws invalidCountry - if the selected country is not owned by the player.
	 * @author Charles Snyder
	 */
	public void placeUnits(Board a_gameBoard, int a_country, int unitCount) throws Exception {
		if(unitCount < 1 || unitCount > m_armies) {
			Exception invalidValue = new Exception("Invalid number of units specified");
			throw invalidValue;
		}
		
		// Initialize variables to arbitrary values.
		int boardSpaceOwner = -2;
		int boardSpaceArmy = 0;
		try {
			boardSpaceOwner = a_gameBoard.getBoardSpaceOwner(a_country);
			boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(a_country);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// The player owns the selected board space.
		if(boardSpaceOwner == m_color) {
			try {
				a_gameBoard.setBoardSpaceArmy(a_country, boardSpaceArmy + unitCount);
				m_armies = m_armies - unitCount;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Exception invalidCountry = new Exception("Invalid Country");
			throw invalidCountry;
		}
	}
	
	
	/** 
	 * For the fortify position phase of the players turn.  Move units from one country to another country inside
	 * any directly owned path.
	 * @param a_gameBoard - the current board of the game.
	 * @param a_originCountry - the country to move armies from.
	 * @param a_destCountry - the country to move armies to.
	 * @param a_unitCount - the number of armies to move.
	 * @throws invalidCountries - if the two selected countries are not in an owned path.
	 * @throws invalidValue - if m_armies is less than one or unitCount is greater than m_armies.
	 * @throws invalidCountry - if the selected country is not owned by the player.
	 * @author Charles Snyder
	 */
	public void moveUnits(Board a_gameBoard, int a_originCountry, int a_destCountry, int a_unitCount) throws Exception {
		// Empty array that will hold countries that have been searched through.
		ArrayList<Integer> searched = new ArrayList<Integer>();
		
		// Check to see if the two countries are in a direct path.
		if(isInFortifyTerritory(a_gameBoard, a_originCountry, a_destCountry, searched) == false) {
			Exception invalidCountries = new Exception("Two countries are not in directly owned path");
			throw invalidCountries;
		}
		
		if(a_unitCount < 1) {
			Exception invalidValue = new Exception("Invalid number of units specified");
			throw invalidValue;
		}
		// Instantiate variables to arbitrary values.
		int boardSpaceOwnerOrigin = -2;
		int boardSpaceOwnerDest = -2;
		int boardSpaceArmyOrigin = 0;
		int boardSpaceArmyDest = 0;
		try {
			boardSpaceOwnerOrigin = a_gameBoard.getBoardSpaceOwner(a_originCountry);
			boardSpaceOwnerDest = a_gameBoard.getBoardSpaceOwner(a_destCountry);
			boardSpaceArmyOrigin = a_gameBoard.getBoardSpaceArmy(a_originCountry);
			boardSpaceArmyDest = a_gameBoard.getBoardSpaceArmy(a_destCountry);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// Player owns both territories
		if(boardSpaceOwnerOrigin == m_color && boardSpaceOwnerDest == m_color) {
			// Must leave at least one army behind
			if(a_unitCount >= boardSpaceArmyOrigin) {
				Exception invalidUnitCount = new Exception("Invalid number of units to move");
				throw invalidUnitCount;
			}
			a_gameBoard.setBoardSpaceArmy(a_originCountry, boardSpaceArmyOrigin - a_unitCount);
			a_gameBoard.setBoardSpaceArmy(a_destCountry, boardSpaceArmyDest + a_unitCount);
		}
		else {
			Exception invalidCountry = new Exception("Invalid Country");
			throw invalidCountry;
		}
	}
	
	
	
	
	/** 
	 * Recursive function that starts from the origin country and calls the function again on each border country that is owned by the player
	 * looking to see if the destination country is ever found.
	 * @param a_gameBoard - the current board of the game.
	 * @param a_originCountry - the country to move armies from.
	 * @param a_destCountry - the country to move armies to.
	 * @param a_searched - array to populate with searched countries.
	 * @return True if the destination country is found, false otherwise.
	 * @author Charles Snyder
	 */
	public final boolean isInFortifyTerritory(Board a_gameBoard, int a_originCountry, int a_destCountry, ArrayList<Integer> a_searched) {
		// The origin country is added to searched so each time the function is called the same countries do not keep being searched.
		a_searched.add(a_originCountry);
		
		ArrayList<Integer> borderCountries = new ArrayList<Integer>(a_gameBoard.getBoardSpaces().get(a_originCountry).getBorderCountries());
		for(int borderIndex = 0; borderIndex < borderCountries.size(); borderIndex++) {
			try {
				if(a_gameBoard.getBoardSpaceOwner(borderCountries.get(borderIndex)) == m_color) {
					// Destination country was found in owned path.
					if(borderCountries.get(borderIndex) == a_destCountry) {
						return true;
					}
					else {
						// Continue searching the next border country.
						if(!a_searched.contains((Object)borderCountries.get(borderIndex))) {
							if(isInFortifyTerritory(a_gameBoard, borderCountries.get(borderIndex), a_destCountry, a_searched)) {
								return true;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// All border countries and their borders and so on were searched from the origin, the destination country was not found.
		return false;
	}
	
	
	
	/** 
	 * Simulates the roll of one die.
	 * @return randomInt - an integer between 0 and 5.
	 * @author Charles Snyder
	 */
	public final int rollOneDie() {
		int randomInt = randomNum.nextInt(6);
		return randomInt;
	}
	
	
	/** 
	 * Simulates the rolling of two dice.
	 * @return results - an array list that holds the results of the two dice rolls.
	 * @author Charles Snyder
	 */
	public final ArrayList<Integer> rollTwoDice() {
		int randomDieOne = randomNum.nextInt(6);
		int randomDieTwo = randomNum.nextInt(6);
		ArrayList<Integer> results = new ArrayList<Integer>();
		results.add(randomDieOne);
		results.add(randomDieTwo);
		return results;
	}
	
	
	/** 
	 * Simulates the rolling of three dice.
	 * @return results - an array list that holds the results of the three dice rolls.
	 * @author Charles Snyder
	 */
	public final ArrayList<Integer> rollThreeDice() {
		int randomDieOne = randomNum.nextInt(6);
		int randomDieTwo = randomNum.nextInt(6);
		int randomDieThree = randomNum.nextInt(6);
		ArrayList<Integer> results = new ArrayList<Integer>();
		results.add(randomDieOne);
		results.add(randomDieTwo);
		results.add(randomDieThree);
		return results;
	}
	
	
	
	/** 
	 * Performs the action of trading in a card set.
	 * @param a_cardOne - the first card to be chosen.
	 * @param a_cardTwo - the second card to be chosen.
	 * @param a_cardThree - the third card to be chosen.
	 * @return True if the cards were a valid match, false otherwise.
	 * @author Charles Snyder
	 */
	public final boolean tradeInCards(Card a_cardOne, Card a_cardTwo, Card a_cardThree) {
		// Check to see if cards are valid match.
		if(Card.areCardsMatch(a_cardOne, a_cardTwo, a_cardThree) == false) {
			return false;
		}
		else {
			// Remove the cards from the players hand.
			try {
				removeCardFromHand(a_cardOne);
				removeCardFromHand(a_cardTwo);
				removeCardFromHand(a_cardThree);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return true;
		}
	}
	
	
	
	/** 
	 * Determines whether an attack move chosen by the player is valid.
	 * @param originCountry - the country to attack from.
	 * @param destCountry - the country to attack.
	 * @param gameBoard - the current board of the game.
	 * @return True if the move can be executed within the rules of the game, false otherwise.
	 * @author Charles Snyder
	 */
	public final boolean canAttackCountry(int originCountry, int destCountry, Board gameBoard) {
		try {
			// Are two countries next to each other.
			if(gameBoard.isCountryAdjacent(originCountry, destCountry) == false) {
				return false;
			}
			else {
				// Check to see if the player owns the country attacking from.
				if(isCountryControlled(originCountry) == false) {
					return false;
				}
				else {
					// Check to see if the player owns the country it is trying to attack.
					if(isCountryControlled(destCountry) == true) {
						return false;
					}
					// Must have at least 2 armies in order to attack.
					else if(gameBoard.getBoardSpaceArmy(originCountry) < 2) {
						return false;
					}
					// The move was within all rules.
					else {
						return true;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/** 
	 * Moves armies into a captured territory from the attacking country.
	 * @param originCountry - the country to attack from.
	 * @param attackCountry - the country to attack.
	 * @param gameBoard - the current board of the game.
	 * @param numToMove - the number of armies to move into the conquered region.
	 * @throws invalidNum - if the number of armies to move is negative or does not leave at least one army in the attacking territory.
	 * @author Charles Snyder
	 */
	public void captureTerritory(int originCountry, int attackCountry, Board gameBoard, int numToMove) throws Exception {
		// Instantiate to arbitrary value.
		int originArmies = -1;
		try {
			originArmies = gameBoard.getBoardSpaceArmy(originCountry);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(numToMove >= originArmies || numToMove < 0) {
			Exception invalidNum = new Exception("Invalid number to move");
			throw invalidNum;
		}
		try {
			gameBoard.setBoardSpaceArmy(attackCountry, numToMove);
			gameBoard.setBoardSpaceArmy(originCountry, originArmies - numToMove);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public boolean equals(Object a_other) {
		if(!(a_other instanceof Player)) {
			return false;
		}
		Player otherPlayer = (Player)a_other;
		
		if(m_color == otherPlayer.getColor() && m_armies == otherPlayer.getArmyTotal() 
			&& m_cardHand.size() == otherPlayer.getCardHand().size()
			&& m_countryControl.size() == otherPlayer.getCountriesControlled().size()
			&& m_turn == otherPlayer.getTurn() && m_didCaptureTerritory == otherPlayer.getDidCaptureTerritory()) {
			for(int index = 0; index < m_cardHand.size(); index++) {
				if(m_cardHand.get(index) != otherPlayer.getCardHand().get(index)) {
					return false;
				}
			}
			for(int index = 0; index < m_countryControl.size(); index++) {
				if(m_countryControl.get(index) != otherPlayer.getCountriesControlled().get(index)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	
	@Override
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("(" + this.getClass().getName() + ", ");
		sbuff.append(m_color + ", ");
		sbuff.append(m_armies + ", ");
		sbuff.append(m_cardHand + ", ");
		sbuff.append(m_countryControl + ", ");
		sbuff.append(m_turn + ", ");
		sbuff.append(m_cardBonus + ", ");
		sbuff.append(m_didCaptureTerritory + ")");
		
		return sbuff.toString();
	}
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(m_color);
		dest.writeInt(m_armies);
		dest.writeTypedList(m_cardHand);
		dest.writeList(m_countryControl);
		dest.writeValue(m_turn);
		dest.writeValue(m_cardBonus);
		dest.writeValue(m_didCaptureTerritory);
	}
	
	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
		public Player createFromParcel(Parcel in) {
			return new Player(in);
		}
		public Player[] newArray(int size) {
			return new Player[size];
		}
	};
	
	protected Player(Parcel in) {
		m_color = in.readInt();
		m_armies = in.readInt();
		m_cardHand = new ArrayList<Card>();
		in.readTypedList(m_cardHand, Card.CREATOR);
		m_countryControl = new ArrayList<Integer>();
		in.readList(m_countryControl, Integer.class.getClassLoader());
		m_turn = (Boolean) in.readValue(Boolean.class.getClassLoader());
		m_cardBonus = (Boolean) in.readValue(Boolean.class.getClassLoader());
		m_didCaptureTerritory = (Boolean) in.readValue(Boolean.class.getClassLoader());
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player myPlayer = new Player(RED);
		try {
			myPlayer.setArmyTotal(45);
		} catch (Exception e) {
			e.printStackTrace();
		}
		CardDeck cd = new CardDeck();
		Card myCard = null;
		Card otherCard = new Card();
		try {
			myCard = new Card(43);
		} catch (Exception e1) {
		
			e1.printStackTrace();
		}
		cd.shuffleDeck();
		myPlayer.takeCard(cd);
		myPlayer.takeCard(cd);
//		try {
//			myPlayer.removeCardFromHand(myCard);
//		} catch (Exception e1) {
//			
//			e1.printStackTrace();
//		}
		@SuppressWarnings("unused")
		boolean cardTest = myPlayer.isCardInHand(myCard);
		@SuppressWarnings("unused")
		boolean cardTest2 = myPlayer.isCardInHand(otherCard);
		Board gameBoard = new Board();
		
//		try {
//			myPlayer.setArmyTotal(10);
//			myPlayer.placeUnitInitial(gameBoard, Board.BRAZIL);
//			myPlayer.placeUnitInitial(gameBoard, Board.ARGENTINA);
//			myPlayer.placeUnitInitial(gameBoard, Board.UKRAINE);
//			myPlayer.placeUnitAllOccupied(gameBoard, Board.BRAZIL);
//			myPlayer.placeUnits(gameBoard, Board.BRAZIL, 5);
//			myPlayer.moveUnits(gameBoard, Board.BRAZIL, Board.ARGENTINA, 1);
//			//myPlayer.moveUnits(gameBoard, Board.BRAZIL, Board.ARGENTINA, 10);
//			//myPlayer.moveUnits(gameBoard, Board.BRAZIL, Board.UKRAINE, 1);
//			myPlayer.addCountryControl(5);
//			myPlayer.addCountryControl(6);
//			//myPlayer.addCountryControl(5);
//			myPlayer.removeCountryControl(5);
//			//myPlayer.removeCountryControl(7);
//			@SuppressWarnings("unused")
//			boolean cTest = myPlayer.isCountryControlled(5);
//			@SuppressWarnings("unused")
//			boolean cTest2 = myPlayer.isCountryControlled(6);
//			System.out.println("stuff");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println("stuff");
		
		System.out.println(myPlayer.toString());
		try {
			gameBoard.setAlaskaOwner(RED);
			gameBoard.setAlbertaOwner(RED);
			gameBoard.setWesternUSOwner(RED);
			gameBoard.setCentralAmericaOwner(RED);
			myPlayer.addCountryControl(Board.ALASKA);
			myPlayer.addCountryControl(Board.ALBERTA);
			myPlayer.addCountryControl(Board.WESTERN_US);
			myPlayer.addCountryControl(Board.CENTRAL_AMERICA);
			gameBoard.setAlaskaArmy(10);
			
			
			myPlayer.moveUnits(gameBoard, Board.ALASKA, Board.GREENLAND, 5);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

}
