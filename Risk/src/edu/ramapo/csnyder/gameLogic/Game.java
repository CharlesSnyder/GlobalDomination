package edu.ramapo.csnyder.gameLogic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Parcelable;
import android.os.Parcel;

public class Game implements Parcelable {
	
	private ArrayList<Player> m_players;
	private ArrayList<Integer> m_turnOrder;
	private int m_tradedInCards;
	private int m_tradeInCardValue;
	private CardDeck m_cards;
	private Board m_gameBoard;
	private boolean isPhaseOne;
	private boolean isPhaseTwo;
	private boolean isPhaseThree;
	private boolean isPhaseFour;
	
	/** 
	 * Class constructor.
	 * @author Charles Snyder
	 */
	public Game() {
		m_players = new ArrayList<Player>();
		m_turnOrder = new ArrayList<Integer>();
		m_tradedInCards = 0;
		m_tradeInCardValue = 4;
		m_cards = new CardDeck();
		m_gameBoard = new Board();
		isPhaseOne = true;
		isPhaseTwo = false;
		isPhaseThree = false;
		isPhaseFour = false;
	}
	
	/** 
	 * Class constructor with list of players parameter.
	 * @param a_players - array list of players that will be copied to m_players member variable.
	 * @author Charles Snyder
	 */
	public Game(ArrayList<Player> a_players) {
		m_players = new ArrayList<Player>(a_players);
		m_turnOrder = new ArrayList<Integer>();
		m_tradedInCards = 0;
		m_tradeInCardValue = 4;
		m_cards = new CardDeck();
		m_gameBoard = new Board();
		isPhaseOne = true;
		isPhaseTwo = false;
		isPhaseThree = false;
		isPhaseFour = false;
	}
	
	
	/** 
	 * Class copy constructor.
	 * @param a_game - Game object to be copied.
	 * @author Charles Snyder
	 */
	public Game(Game a_game) {
		m_players = new ArrayList<Player>(a_game.getPlayersArray());
		m_turnOrder = new ArrayList<Integer>(a_game.getTurnDeque());
		m_tradedInCards = a_game.getTradedInCardTotal();
		m_tradeInCardValue = a_game.getTradeInCardValue();
		m_cards = new CardDeck(a_game.getCardDeck());
		m_gameBoard = new Board(a_game.getBoard());
		isPhaseOne = a_game.getIsPhaseOne();
		isPhaseTwo = a_game.getIsPhaseTwo();
		isPhaseThree = a_game.getIsPhaseThree();
		isPhaseFour = a_game.getIsPhaseFour();
	}
	
	
	/** 
	 * Selector for m_players member variable.
	 * @return tmp - copy of m_players array list.
	 * @author Charles Snyder
	 */
	public ArrayList<Player> getPlayersArray() {
		ArrayList<Player> tmp = new ArrayList<Player>(m_players);
		return tmp;
	}
	
	
	/** 
	 * Selector for m_turnOrder member variable.
	 * @return tmp - copy of m_turnOrder array list.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> getTurnDeque() {
		ArrayList<Integer> tmp = new ArrayList<Integer>(m_turnOrder);
		return tmp;
	}
	
	
	/** 
	 * Selector for m_tradedInCards member variable.
	 * @return int value totaling number of card sets that have been traded in.
	 * @author Charles Snyder
	 */
	public final int getTradedInCardTotal() {
		return m_tradedInCards;
	}
	
	
	/** 
	 * Selector for m_tradeInCardValue member variable.
	 * @return int value totaling number of bonus armies one gets when trading in the next card set.
	 * @author Charles Snyder
	 */
	public final int getTradeInCardValue() {
		return m_tradeInCardValue;
	}
	
	
	/** 
	 * Selector for m_cards member variable.  Mutable.
	 * @return m_cards CardDeck object, the deck of cards for the game.
	 * @author Charles Snyder
	 */
	public CardDeck getCardDeck() {
		return m_cards;
	}
	
	
	/** 
	 * Selector for m_cards member variable.  Mutable.
	 * @return m_gameBoard Board object.  An array list of all countries and their information in the game.
	 * @author Charles Snyder
	 */
	public Board getBoard() {
		return m_gameBoard;
	}
	
	
	/** 
	 * Gets the player color at the front of the turn list.
	 * @return integer representing player color.
	 * @author Charles Snyder
	 */
	public final int getFirstFromTurnDeque() {
		return m_turnOrder.get(0);
	}
	
	
	/** 
	 * Selector for isPhaseOne member variable.
	 * @return boolean isPhaseOne.
	 * @author Charles Snyder
	 */
	public final boolean getIsPhaseOne() {
		return isPhaseOne;
	}
	
	
	/** 
	 * Selector for isPhaseTwo member variable.
	 * @return boolean isPhaseTwo.
	 * @author Charles Snyder
	 */
	public final boolean getIsPhaseTwo() {
		return isPhaseTwo;
	}
	
	
	/** 
	 * Selector for isPhaseThree member variable.
	 * @return boolean isPhaseThree.
	 * @author Charles Snyder
	 */
	public final boolean getIsPhaseThree() {
		return isPhaseThree;
	}
	
	
	/** 
	 * Selector for isPhaseFour member variable.
	 * @return boolean isPhaseFour.
	 * @author Charles Snyder
	 */
	public final boolean getIsPhaseFour() {
		return isPhaseFour;
	}
	
	
	/** 
	 * Mutator for isPhaseOne member variable.
	 * @param a_phase - value to set isPhaseOne to.
	 * @author Charles Snyder
	 */
	public void setIsPhaseOne(boolean a_phase) {
		isPhaseOne = a_phase;
	}
	
	
	/** 
	 * Mutator for isPhaseTwo member variable.
	 * @param a_phase - value to set isPhaseTwo to.
	 * @author Charles Snyder
	 */
	public void setIsPhaseTwo(boolean a_phase) {
		isPhaseTwo = a_phase;
	}
	
	
	/** 
	 * Mutator for isPhaseThree member variable.
	 * @param a_phase - value to set isPhaseThree to.
	 * @author Charles Snyder
	 */
	public void setIsPhaseThree(boolean a_phase) {
		isPhaseThree = a_phase;
	}
	
	
	/** 
	 * Mutator for isPhaseFour member variable.
	 * @param a_phase - value to set isPhaseFour to.
	 * @author Charles Snyder
	 */
	public void setIsPhaseFour(boolean a_phase) {
		isPhaseFour = a_phase;
	}
	
	
	/** 
	 * Mutator for m_players member variable.
	 * @param a_players - array list of players to copy from.
	 * @author Charles Snyder
	 */
	public void setPlayersArray(ArrayList<Player> a_players) {
		m_players = new ArrayList<Player>(a_players);
	}
	
	
	/** 
	 * Mutator for m_turnOrder member variable.
	 * @param a_turns - array list of integers to copy from.
	 * @author Charles Snyder
	 */
	public void setTurnDeque(ArrayList<Integer> a_turns) {
		m_turnOrder = new ArrayList<Integer>(a_turns);
	}
	
	
	/** 
	 * Mutator for m_tradedInCards member variable.
	 * @param a_total - integer value representing the total number of card sets traded in.
	 * @throws invalidValue - if a_total is a negative number.
	 * @author Charles Snyder
	 */
	public void setTradedInCardTotal(int a_total) throws Exception {
		final int MIN_CARD = 0;
		
		if(a_total >= MIN_CARD) {
			m_tradedInCards = a_total;
		}
		else {
			Exception invalidValue = new Exception("Invalid value for traded in cards");
			throw invalidValue;
		}
	}
	
	
	/** 
	 * Mutator for m_tradeInCardValue member variable.
	 * @param a_value - integer value representing the new bonus army amount a player receives for trading in the next card set.
	 * @author Charles Snyder
	 */
	public void setTradeInCardValue(int a_value) {
		m_tradeInCardValue = a_value;
	}
	
	
	/** 
	 * Mutator for m_cards member variable.
	 * @param a_deck - the CardDeck object to copy from.
	 * @author Charles Snyder
	 */
	public void setCardDeck(CardDeck a_deck) {
		m_cards = new CardDeck(a_deck);
	}
	
	
	/** 
	 * Mutator for m_gameBoard member variable.
	 * @param a_board - the Board object to copy from.
	 * @author Charles Snyder
	 */
	public void setBoard(Board a_board) {
		m_gameBoard = new Board(a_board);
	}
	
	
	/** 
	 * Determine how much to raise the bonus army total for trading in a card set.
	 * @author Charles Snyder
	 */
	public void determineTradeInCardValueIncrease() {
		if(m_tradedInCards < 5) {
			m_tradeInCardValue += 2;
		}
		else if(m_tradedInCards == 5) {
			m_tradeInCardValue += 3;
		}
		else {
			m_tradeInCardValue += 5;
		}
	}
	
	
	/** 
	 * Increase the number of traded in card sets by one.
	 * @author Charles Snyder
	 */
	public void addTradedInCardSet() {
		m_tradedInCards++;
	}
	
	
	/** 
	 * Remove a player from the m_players and m_turnOrder array list using a player object.
	 * @param a_player - Player object to be removed.
	 * @author Charles Snyder
	 */
	public void removePlayerFromGame(Player a_player) {
		if(m_players.contains(a_player)) {
			m_players.remove(a_player);
			m_turnOrder.remove((Object)a_player.getColor());
		}
	}
	
	
	/** 
	 * Remove a player from the m_players and m_turnOrder array list using player color.
	 * @param a_color - integer value representing the player's color to be removed.
	 * @author Charles Snyder
	 */
	public void removePlayerFromGame(int a_color) {
		int playerIndex = findPlayerIndex(a_color);
		m_players.remove(playerIndex);
		m_turnOrder.remove((Object)a_color);
	}
	
	
	/** 
	 * Finds the index of a player in the m_players array using the players color.
	 * @param a_color - integer value representing the player's color to be found.
	 * @return index - the index in the m_players array, or -1 if no player with that a_color is found.
	 * @author Charles Snyder
	 */
	public final int findPlayerIndex(int a_color) {
		for(int index = 0; index < m_players.size(); index++) {
			if(m_players.get(index).getColor() == a_color) {
				return index;
			}
		}
		return -1;
	}
	
	
	/** 
	 * Each player in the m_players array list rolls a die, the results are added to an array list and used to determine the first player.
	 * @return results - an array list of integers that is filled with each player's die roll.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> playersRollForTurn() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int roll;
		for(int index = 0; index < m_players.size(); index++) {
			roll = m_players.get(index).rollOneDie();
			results.add(roll);
		}
		return results;
	}
	
	
	
	/** 
	 * Determines the first player of the game.  The player who rolled the highest die first will be the first to go.  The turn order will be
	 * whoever was selected first, and then in order all those that rolled after him/her until it reaches the person who rolled right before.
	 * So the person who rolled right before will be last.
	 * @param a_numbers - the values of all the dice rolls of each player.
	 * @author Charles Snyder
	 */
	public void determineFirstPlayer(ArrayList<Integer> a_numbers) {
		int maxValLoc = 0;
		for(int i = 1; i < a_numbers.size(); i++) {
			if(a_numbers.get(i) > a_numbers.get(maxValLoc)) {
				maxValLoc = i;
			}
		}
		m_turnOrder.add(m_players.get(maxValLoc).getColor());
		m_players.get(maxValLoc).setTurn(true);
		//Get players who rolled after.
		for(int index = maxValLoc + 1; index < a_numbers.size(); index++) {
			m_turnOrder.add(m_players.get(index).getColor());
		}
		//Get players who rolled before.
		for(int index = 0; index < maxValLoc; index++) {
			m_turnOrder.add(m_players.get(index).getColor());
		}
	}
	
	
	/** 
	 * Determines the starting army total of all the players based on the number of players.
	 * @param a_numerPlayers - number of players at the start of the game.
	 * @return integer value of starting army total.
	 * @throws invalidNumber - if a_numberPlayers is less than 2 or greater than 6.
	 * @author Charles Snyder
	 */
	public int determineStartArmyTotal(int a_numberPlayers) throws Exception {
		switch(a_numberPlayers) {
		case 2: return 40;
		case 3: return 35;
		case 4: return 30;
		case 5: return 25;
		case 6: return 20;
		default:
			Exception invalidNumber = new Exception("Invalid number of players");
			throw invalidNumber;
		}
	}
	
	
	/** 
	 * Takes the initial army total that was found at the start of the game and adds it to each players army count.
	 * @param a_total - the initial army count for each player.
	 * @author Charles Snyder
	 */
	public void addInitialArmiesToPlayers(int a_total) {
		for(int index = 0; index < m_players.size(); index++) {
			try {
				m_players.get(index).setArmyTotal(a_total);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}
	
	
	/** 
	 * Ends a players turn by changing their turn status to false and putting them at the end of the turn order list.
	 * @param a_playerColor - integer value representing color of the player.
	 * @author Charles Snyder
	 */
	public void endPlayerTurn(int a_playerColor) {
		int playerIndex = findPlayerIndex(a_playerColor);
		m_players.get(playerIndex).setTurn(false);
		m_players.get(playerIndex).setCardBonus(false);
		int color = m_turnOrder.get(0);
		m_turnOrder.remove((Object)color);
		m_turnOrder.add(color);
		int nextPlayerIndex = findPlayerIndex(m_turnOrder.get(0));
		m_players.get(nextPlayerIndex).setTurn(true);
	}
	
	
	
	/** 
	 * Determines whether any players still have armies to place during initial phase of game.
	 * @return True if at least one player still has armies to place, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean doPlayersHaveArmies() {
		for(int index = 0; index < m_players.size(); index++) {
			if(m_players.get(index).getArmyTotal() > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/** 
	 * At the start of a player's turn this determines how many armies to give them based on the number of territories they control.
	 * @param a_player - Player object to have armies added to their inventory.
	 * @return The number of armies that were added to the player if any, if none were then -1 is returned.
	 * @author Charles Snyder
	 */
	public int addBaseArmyStartTurn(Player a_player) {
		int numTerr = a_player.getNumberCountriesControl();
		int increase = numTerr / 3;
		try {
			if(increase < 3) {
				a_player.addToArmyTotal(3);
				return 3;
			}
			else {
				a_player.addToArmyTotal(increase);
				return increase;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	/** 
	 * At the start of a player's turn this determines how many armies to give them based on whether they control any continents.
	 * @param a_player - Player object to have armies added to their inventory.
	 * @return retVal - The number of armies that were added to the player if any.
	 * @author Charles Snyder
	 */
	public int addArmyContinentBonus(Player a_player) {
		ArrayList<Integer> countries = new ArrayList<Integer>(a_player.getCountriesControlled());
		int retVal = 0;
		
		//Asia continent bonus.
		if(countries.contains((Object)Board.KAMCHATKA) && countries.contains((Object)Board.YAKUTSK)
			&& countries.contains((Object)Board.IRKUTSK) && countries.contains((Object)Board.MONGOLIA)
			&& countries.contains((Object)Board.JAPAN) && countries.contains((Object)Board.CHINA)
			&& countries.contains((Object)Board.SOUTHEAST_ASIA) && countries.contains((Object)Board.INDIA)
			&& countries.contains((Object)Board.MIDDLE_EAST) && countries.contains((Object)Board.AFGHANISTAN)
			&& countries.contains((Object)Board.URAL) && countries.contains((Object)Board.SIBERIA)) {
			try {
				a_player.addToArmyTotal(7);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			retVal += 7;
		}
		//Europe continent bonus.
		if(countries.contains((Object)Board.UKRAINE) && countries.contains((Object)Board.SCANDINAVIA)
			&& countries.contains((Object)Board.ICELAND) && countries.contains((Object)Board.GREAT_BRITAIN)
			&& countries.contains((Object)Board.WESTERN_EUROPE) && countries.contains((Object)Board.NORTHERN_EUROPE)
			&& countries.contains((Object)Board.SOUTHERN_EUROPE)) {
			try {
				a_player.addToArmyTotal(5);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			retVal += 5;
		}
		//Australia continent bonus.
		if(countries.contains((Object)Board.INDONESIA) && countries.contains((Object)Board.NEW_GUINEA)
			&& countries.contains((Object)Board.WESTERN_AUST) && countries.contains((Object)Board.EASTERN_AUST)){
			try {
				a_player.addToArmyTotal(2);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			retVal += 2;
		}
		//Africa continent bonus.
		if(countries.contains((Object)Board.EAST_AFRICA) && countries.contains((Object)Board.EGYPT)
			&& countries.contains((Object)Board.NORTH_AFRICA) && countries.contains((Object)Board.CONGO)
			&& countries.contains((Object)Board.SOUTH_AFRICA) && countries.contains((Object)Board.MADAGASCAR)) {
			try {
				a_player.addToArmyTotal(3);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			retVal += 3;
		}
		//South America continent bonus.
		if(countries.contains((Object)Board.VENEZUELA) && countries.contains((Object)Board.PERU)
			&& countries.contains((Object)Board.BRAZIL) && countries.contains((Object)Board.ARGENTINA)) {
			try {
				a_player.addToArmyTotal(2);
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			retVal += 2;
		}
		//North America continent bonus.
		if(countries.contains((Object)Board.CENTRAL_AMERICA) && countries.contains((Object)Board.WESTERN_US)
			&& countries.contains((Object)Board.EASTERN_US) && countries.contains((Object)Board.QUEBEC)
			&& countries.contains((Object)Board.ONTARIO) && countries.contains((Object)Board.ALBERTA)
			&& countries.contains((Object)Board.NORTHWEST_TERR) && countries.contains((Object)Board.ALASKA)
			&& countries.contains((Object)Board.GREENLAND)) {
			try {
				a_player.addToArmyTotal(5);
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			retVal += 5;
		}
		return retVal;
	}
	
	
	/** 
	 * Gives the player bonus armies when they trade in a set of cards.
	 * @param a_player - Player object to have armies added to their inventory.
	 * @param a_cardOne
	 * @param a_cardTwo
	 * @param a_cardThree
	 * @return True if the player traded in a valid set of cards, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean addArmyFromCardsHuman(Player a_player, Card a_cardOne, Card a_cardTwo, Card a_cardThree) {
		//Determine validity of match
		if(a_player.tradeInCards(a_cardOne, a_cardTwo, a_cardThree) == false) {
			return false;
		}
		else {
			try {
				a_player.addToArmyTotal(m_tradeInCardValue);
				m_tradedInCards++;
				determineTradeInCardValueIncrease();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return true;
		}
	}
	
	
	/** 
	 * Gives the cpu bonus armies when they trade in a set of cards.  Cpu will attempt all permutations of the cards they own to
	 * try and find a match.
	 * @param a_player - Player object to have armies added to their inventory.
	 * @author Charles Snyder
	 */
	public void addArmyFromCardsCPU(Player a_player) {
		if(a_player instanceof Cpu) {
			Cpu cpuPlayer = (Cpu) a_player;
			do {
				ArrayList<Card> result = new ArrayList<Card>(cpuPlayer.selectMatchingCards());
				if(result.isEmpty() == true) {
					return;
				}
				else {
					try {
						cpuPlayer.addToArmyTotal(m_tradeInCardValue);
						m_tradedInCards++;
						determineTradeInCardValueIncrease();
						if(cpuPlayer.getCardBonus() == false) {
							cpuPlayer.selectCountryCardBonus(doesPlayerOwnCardCountry(a_player, result.get(0), result.get(1), result.get(2)), result, m_gameBoard);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
			} while(true);
		}
	}
	
	
	/** 
	 * Determines whether the player that traded in a set of cards owns any of the countries on the cards they traded in.
	 * @param a_player - Player object in question.
	 * @param a_cardOne
	 * @param a_cardTwo
	 * @param a_cardThree
	 * @return returnArray - An array list of booleans filled with three values, index one corresponds to a_cardOne and so on.
	 * The value will be true if the player owns the country on that card, otherwise it will be false.
	 * @author Charles Snyder
	 */
	public ArrayList<Boolean> doesPlayerOwnCardCountry(Player a_player, Card a_cardOne, Card a_cardTwo, Card a_cardThree) {
		ArrayList<Integer> countries = new ArrayList<Integer>(a_player.getCountriesControlled());
		ArrayList<Boolean> returnArray = new ArrayList<Boolean>();
		returnArray.add(false);
		returnArray.add(false);
		returnArray.add(false);
		for(int index = 0; index < countries.size(); index++) {
			if(countries.get(index) == a_cardOne.getCardCountry()) {
				returnArray.set(0, true);
			}
			else if(countries.get(index) == a_cardTwo.getCardCountry()) {
				returnArray.set(1, true);
			}
			else if(countries.get(index) == a_cardThree.getCardCountry()) {
				returnArray.set(2, true);
			}
		}
		return returnArray;
	}
	
	
	/** 
	 * Shuffles the cards in the m_cards CardDeck object.
	 * @author Charles Snyder
	 */
	public void shuffleCards() {
		m_cards.shuffleDeck();
	}
	
	
	/** 
	 * When the players take all the cards from the deck, this creates a new one and shuffles it.
	 * @author Charles Snyder
	 */
	public void resetCardDeck() {
		m_cards = new CardDeck();
		m_cards.shuffleDeck();
	}
	
	
	/** 
	 * This is used when a player attacks another player, to find the color of the defending player.  This is used to find that player's index in the
	 * m_players array list and call that player's respective defend dice roll method.
	 * @param defendCountry - integer value of the country being attacked.
	 * @return returnVal - the color of the player who owns defendCountry, or -1 if no owner found.
	 * @author Charles Snyder
	 */
	public final int getDefendingPlayerColor(int defendCountry) {
		int returnVal = -1;
		try {
			returnVal = m_gameBoard.getBoardSpaceOwner(defendCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	

	/** 
	 * This function determines the winner of a battle and executes to correct action for setting new army values.
	 * @param attackCountry - the country that is attacking.
	 * @param defendCountry - the country that is defending.
	 * @param attackDice - the results of the dice rolled by the attacking player.
	 * @param defendDice - the results of the dice rolled by the defending player.
	 * @param gameBoard - the current board of the game.
	 * @return integer value that corresponds to a certain result.  This is to let the gui know what the result of the battle was.
	 * @author Charles Snyder
	 */
	public final int attackResult(int attackCountry, int defendCountry, ArrayList<Integer> attackDice, ArrayList<Integer> defendDice) {
		// Attack result constants
		final int DEFENSE_LOSE_ONE = 0;
		final int ATTACK_LOSE_ONE = 1;
		final int DEFENSE_LOSE_TWO = 2;
		final int ATTACK_LOSE_TWO = 3;
		final int BOTH_LOSE_ONE = 4;
		
		int numAttackDice = attackDice.size();
		int numDefendDice = defendDice.size();
		int attackArmySize = 0;
		int defendArmySize = 0;
		try {
			attackArmySize = m_gameBoard.getBoardSpaceArmy(attackCountry);
			defendArmySize = m_gameBoard.getBoardSpaceArmy(defendCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Sort the dice rolls to make them easily comparable.
		Collections.sort(attackDice);
		Collections.sort(defendDice);
		
		// Go through all permutations of numbers of dice rolled by each player.
		
		//One die rolled by each player.
		if(numAttackDice == 1 && numDefendDice == 1) {
			if(attackDice.get(0) > defendDice.get(0)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, defendArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return DEFENSE_LOSE_ONE;
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, attackArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ATTACK_LOSE_ONE;
			}
		}
		// Attacker rolled one die, defender rolled two dice.
		else if(numAttackDice == 1 && numDefendDice == 2) {
			if(attackDice.get(0) > defendDice.get(1)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, defendArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return DEFENSE_LOSE_ONE;
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, attackArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ATTACK_LOSE_ONE;
			}
		}
		// Attacker rolled two dice, defender rolled one die.
		else if(numAttackDice == 2 && numDefendDice == 1) {
			if(attackDice.get(1) > defendDice.get(0)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, defendArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return DEFENSE_LOSE_ONE;
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, attackArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ATTACK_LOSE_ONE;
			}
		}
		// Attacker rolled two dice, defender rolled two dice.
		else if(numAttackDice == 2 && numDefendDice == 2) {
			int newDefendArmySize = defendArmySize;
			int newAttackArmySize = attackArmySize;
			// True attack wins, false defense wins.
			boolean highDice;
			if(attackDice.get(1) > defendDice.get(1)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, defendArmySize - 1);
					newDefendArmySize = defendArmySize - 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				highDice = true;
				
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, attackArmySize - 1);
					newAttackArmySize = attackArmySize - 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				highDice = false;
				
			}
			
			if(attackDice.get(0) > defendDice.get(0)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, newDefendArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(highDice == true) {
					return DEFENSE_LOSE_TWO;
				}
				else if(highDice == false) {
					return BOTH_LOSE_ONE;
				}
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, newAttackArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(highDice == true) {
					return BOTH_LOSE_ONE;
				}
				else if(highDice == false) {
					return ATTACK_LOSE_TWO;
				}
			}
		}
		// Attacker rolled three dice, defender rolled one.
		else if(numAttackDice == 3 && numDefendDice == 1) {
			if(attackDice.get(2) > defendDice.get(0)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, defendArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return DEFENSE_LOSE_ONE;
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, attackArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ATTACK_LOSE_ONE;
			}
		}
		// Attacker rolled three dice, defender rolled two.
		else if(numAttackDice == 3 && numDefendDice == 2) {
			int newDefendArmySize = defendArmySize;
			int newAttackArmySize = attackArmySize;
			// True attack wins, false defense wins.
			boolean highDice;
			if(attackDice.get(2) > defendDice.get(1)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, defendArmySize - 1);
					newDefendArmySize = defendArmySize - 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				highDice = true;
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, attackArmySize - 1);
					newAttackArmySize = attackArmySize - 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				highDice = false;
			}
			
			if(attackDice.get(1) > defendDice.get(0)) {
				try {
					m_gameBoard.setBoardSpaceArmy(defendCountry, newDefendArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(highDice == true) {
					return DEFENSE_LOSE_TWO;
				}
				else if(highDice == false) {
					return BOTH_LOSE_ONE;
				}
			}
			else {
				try {
					m_gameBoard.setBoardSpaceArmy(attackCountry, newAttackArmySize - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(highDice == true) {
					return BOTH_LOSE_ONE;
				}
				else if(highDice == false) {
					return ATTACK_LOSE_TWO;
				}
			}
		}
		// The number of dice rolled did not match any valid permutation.
		return -1;
	}
	
	
	
	/** 
	 * Determines whether the attacker wiped out all armies of the defending country.
	 * @param defendCountry - the country that was defending.
	 * @param gameBoard - the current board of the game.
	 * @return True if the defending countries army is zero, false otherwise.
	 * @author Charles Snyder
	 */
	public final boolean canCaptureTerritory(int defendCountry) {
		try {
			if(m_gameBoard.getBoardSpaceArmy(defendCountry) == 0) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/** 
	 * Removes players from the game if their country controlled array is empty.
	 * @return returnArray - an array list filled with any eliminated players.
	 * @author Charles Snyder
	 */
	public ArrayList<Player> eliminatePlayersFromGame() {
		ArrayList<Player> returnArray = new ArrayList<Player>();
		for(int index = 0; index < m_players.size(); index++) {
			if(m_players.get(index).getCountriesControlled().isEmpty() == true) {
				returnArray.add(m_players.get(index));
				removePlayerFromGame(m_players.get(index));
			}
		}
		return returnArray;
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		
		sbuff.append(m_players + ",\n");
		sbuff.append(m_turnOrder + ",\n");
		sbuff.append(m_tradedInCards + ",\n");
		sbuff.append(m_tradeInCardValue + ",\n");
		sbuff.append(m_cards + ",\n");
		sbuff.append(m_gameBoard + ",\n");
		sbuff.append(isPhaseOne + "\n");
		sbuff.append(isPhaseTwo + "\n");
		sbuff.append(isPhaseThree + "\n");
		sbuff.append(isPhaseFour);
		
		return sbuff.toString();
	}
	
	
	/** 
	 * Saves the current state of the game.
	 * @return string of all current objects in the game.
	 * @author Charles Snyder
	 */
	public String saveGame() {
		return this.toString();	
	}
	
	/** 
	 * Loads the game to a previously saved state using regular expressions to parse the information.
	 * @param inputStream - a buffered reader object to be parsed that contains all the game data.
	 * @throws invalidPlayer - if no correct player type is found while parsing input stream.
	 * @author Charles Snyder
	 */
	public void loadGame(BufferedReader inputStream) throws Exception {
		String inputString;
		inputString = inputStream.readLine();
		ArrayList<Player> players = new ArrayList<Player>();
		int index = 0;
		
		Pattern playerPattern = Pattern.compile("(?<=\\().+?e(?=\\))");
		Matcher findPlayers = playerPattern.matcher(inputString);
		while(findPlayers.find()) {
			//Parse through each player's info.
			Pattern playerTypePattern = Pattern.compile("(?<=\\.)[P|E|M|H]\\w+");
			Matcher findPlayerType = playerTypePattern.matcher(findPlayers.group());
			findPlayerType.find();
			switch(findPlayerType.group()) {
			case "Player":
				Player newHuman = new Player();
				players.add(newHuman);
				break;
			case "EasyAI":
				Player newEasy = new EasyAI();
				players.add(newEasy);
				break;
			case "MediumAI":
				Player newMed = new MediumAI();
				players.add(newMed);
				break;
			case "HardAI":
				Player newHard = new HardAI();
				players.add(newHard);
				break;
			default:
				Exception invalidPlayer = new Exception("Invalid player type");
				throw invalidPlayer;
			}
			
			//Set player color
			Pattern number = Pattern.compile("\\d{1,5}");
			Matcher findNumber = number.matcher(findPlayers.group());
			findNumber.find();
			players.get(index).setColor(Integer.parseInt(findNumber.group()));
			
			//Set player's army count
			findNumber.find();
			players.get(index).setArmyTotal(Integer.parseInt(findNumber.group()));
			
			//Set player's cards
			Pattern cardInfo = Pattern.compile("(?<=\\s)\\d{1,2}+(?=\\))");
			Matcher findCards = cardInfo.matcher(findPlayers.group());
			ArrayList<Card> playerCards = new ArrayList<Card>();
			while(findCards.find()) {
				Card tmp = new Card(Integer.parseInt(findCards.group()));
				playerCards.add(tmp);
			}
			players.get(index).setCardHand(playerCards);
			
			//Set player's countries
			Pattern countryInfo = Pattern.compile("(?<=\\[)(\\d{1,5}|,|\\s)+");
			Matcher findCountries = countryInfo.matcher(findPlayers.group());
			ArrayList<Integer> countries = new ArrayList<Integer>();
			if(findCountries.find() == true) {
				findNumber = number.matcher(findCountries.group());
				while(findNumber.find()) {
					countries.add(Integer.parseInt(findNumber.group()));
				}
				players.get(index).setCountryControl(countries);
			}
			
			//Set player turn
			Pattern boolPattern = Pattern.compile("true|false");
			Matcher findBools = boolPattern.matcher(findPlayers.group());
			findBools.find();
			players.get(index).setTurn(Boolean.parseBoolean(findBools.group()));
			
			
			//Set player card bonus
			findBools.find();
			players.get(index).setCardBonus(Boolean.parseBoolean(findBools.group()));
			
			//Did capture territory
			findBools.find();
			players.get(index).setDidCaptureTerritory(Boolean.parseBoolean(findBools.group()));
			
			
			index++;
		}
		setPlayersArray(players);
		
		//Set turn order list.
		inputString = inputStream.readLine();
		ArrayList<Integer> turnOrder = new ArrayList<Integer>();
		Pattern number = Pattern.compile("\\d{1,6}");
		Matcher findNumber = number.matcher(inputString);
		while(findNumber.find()) {
			turnOrder.add(Integer.parseInt(findNumber.group()));
		}
		setTurnDeque(turnOrder);
		
		//Set traded in cards total.
		inputString = inputStream.readLine();
		findNumber = number.matcher(inputString);
		findNumber.find();
		setTradedInCardTotal(Integer.parseInt(findNumber.group()));
		
		//Set trade in card value.
		inputString = inputStream.readLine();
		findNumber = number.matcher(inputString);
		findNumber.find();
		setTradeInCardValue(Integer.parseInt(findNumber.group()));
		
		//Set card deck.
		inputString = inputStream.readLine();
		CardDeck deck = new CardDeck();
		Pattern cardInfo = Pattern.compile("(?<=\\s)\\d{1,5}+(?=\\))");
		Matcher findCards = cardInfo.matcher(inputString);
		ArrayList<Card> playerCards = new ArrayList<Card>();
		while(findCards.find()) {
			Card tmp = new Card(Integer.parseInt(findCards.group()));
			playerCards.add(tmp);
		}
		deck.setCardDeck(playerCards);
		setCardDeck(deck);
		
		//Set game board.
		inputString = inputStream.readLine();
		Board tmpBoard = new Board();
		ArrayList<CountrySpace> boardCountries = new ArrayList<CountrySpace>();
		Pattern parseCountrySpace = Pattern.compile("\\((\\d{1,6}|,|-|\\s|\\[|\\])+\\)");
		Matcher findCountrySpace = parseCountrySpace.matcher(inputString);
		while(findCountrySpace.find()) {
			CountrySpace tmp = new CountrySpace();
			findNumber = number.matcher(findCountrySpace.group());
			findNumber.find();
			tmp.setArmyCount(Integer.parseInt(findNumber.group()));
			
			Pattern countryOwnerPattern = Pattern.compile("\\s-\\d{1,5}");
			Matcher findCountryOwner = countryOwnerPattern.matcher(findCountrySpace.group());
			if(findCountryOwner.find() == false) {
				findNumber.find();
				tmp.setCountryOwner(Integer.parseInt(findNumber.group()));
			}
			
			ArrayList<Integer> tmpCountries = new ArrayList<Integer>();
			Pattern parseBorderCountries = Pattern.compile("\\[(\\d{1,2}|,|\\s)+\\]");
			Matcher findBorderCountries = parseBorderCountries.matcher(findCountrySpace.group());
			findBorderCountries.find();
			findNumber = number.matcher(findBorderCountries.group());
			while(findNumber.find()) {
				tmpCountries.add(Integer.parseInt(findNumber.group()));
			}
			tmp.setBorderCountryArray(tmpCountries);
			
			boardCountries.add(tmp);
		}
		tmpBoard.setBoardSpaces(boardCountries);
		setBoard(tmpBoard);
		
		//Set phase booleans
		inputString = inputStream.readLine();
		if(inputString.equals("true")) {
			setIsPhaseOne(true);
		}
		else {
			setIsPhaseOne(false);
		}
		
		inputString = inputStream.readLine();
		if(inputString.equals("true")) {
			setIsPhaseTwo(true);
		}
		else {
			setIsPhaseTwo(false);
		}
		
		inputString = inputStream.readLine();
		if(inputString.equals("true")) {
			setIsPhaseThree(true);
		}
		else {
			setIsPhaseThree(false);
		}
		
		inputString = inputStream.readLine();
		if(inputString.equals("true")) {
			setIsPhaseFour(true);
		}
		else {
			setIsPhaseFour(false);
		}
	}



	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(m_players.size());
		for(int index = 0; index < m_players.size(); index++) {
			if(m_players.get(index) instanceof EasyAI) {
				dest.writeInt(1);
				dest.writeParcelable(m_players.get(index), flags);
			}
			else if(m_players.get(index) instanceof MediumAI) {
				dest.writeInt(2);
				dest.writeParcelable(m_players.get(index), flags);
			}
			else if(m_players.get(index) instanceof HardAI) {
				dest.writeInt(3);
				dest.writeParcelable(m_players.get(index), flags);
			}
			else {
				dest.writeInt(4);
				dest.writeParcelable(m_players.get(index), flags);
			}
		}
		dest.writeList(m_turnOrder);
		dest.writeInt(m_tradedInCards);
		dest.writeInt(m_tradeInCardValue);
		dest.writeParcelable(m_cards, flags);
		dest.writeParcelable(m_gameBoard, flags);
		dest.writeValue(isPhaseOne);
		dest.writeValue(isPhaseTwo);
		dest.writeValue(isPhaseThree);
		dest.writeValue(isPhaseFour);
	}
	
	
	public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
		public Game createFromParcel(Parcel in) {
			return new Game(in);
		}
		public Game[] newArray(int size) {
			return new Game[size];
		}
	};
	
	private Game(Parcel in) {
		int size = in.readInt();
		m_players = new ArrayList<Player>();
		for(int index = 0; index < size; index++) {
			int flag = in.readInt();
			if(flag == 1) {
				Player tmp = new EasyAI();
				tmp = (EasyAI) in.readParcelable(EasyAI.class.getClassLoader());
				m_players.add(tmp);
			}
			else if(flag == 2) {
				Player tmp = new MediumAI();
				tmp = (MediumAI) in.readParcelable(MediumAI.class.getClassLoader());
				m_players.add(tmp);
			}
			else if(flag == 3) {
				Player tmp = new HardAI();
				tmp = (HardAI) in.readParcelable(HardAI.class.getClassLoader());
				m_players.add(tmp);
			}
			else if(flag == 4) {
				Player tmp = new Player();
				tmp = (Player) in.readParcelable(Player.class.getClassLoader());
				m_players.add(tmp);
			}
		}
		m_turnOrder = new ArrayList<Integer>();
		in.readList(m_turnOrder, Integer.class.getClassLoader());
		m_tradedInCards = in.readInt();
		m_tradeInCardValue = in.readInt();
		m_cards = (CardDeck) in.readParcelable(CardDeck.class.getClassLoader());
		m_gameBoard = (Board) in.readParcelable(Board.class.getClassLoader());
		isPhaseOne = (Boolean) in.readValue(Boolean.class.getClassLoader());
		isPhaseTwo = (Boolean) in.readValue(Boolean.class.getClassLoader());
		isPhaseThree = (Boolean) in.readValue(Boolean.class.getClassLoader());
		isPhaseFour = (Boolean) in.readValue(Boolean.class.getClassLoader());
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		@SuppressWarnings("unused")
		Player human = new Player(Player.RED);
		Player ai = new EasyAI(Player.BLUE);
		Player aieasy = new EasyAI(Player.GREEN);
		Player aiAnother = new EasyAI(Player.ORANGE);
		ArrayList<Player> players = new ArrayList<Player>();
		//players.add(human);
		players.add(ai);
		players.add(aieasy);
		players.add(aiAnother);
		
		Game newGame = new Game(players);
		int total = 0;
		try {
			total = newGame.determineStartArmyTotal(players.size());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		newGame.addInitialArmiesToPlayers(total);
		ArrayList<Integer> numbers = newGame.playersRollForTurn();
		newGame.determineFirstPlayer(numbers);
		
		while(newGame.getBoard().isBoardFull() == false) {
//			if(newGame.getPlayersArray().get(0).getTurn() == true) {
//				try {
//					newGame.getPlayersArray().get(0).placeUnitInitial(newGame.getBoard(), fake_country);
//					newGame.endPlayerTurn(newGame.getTurnDeque().getFirst());
//				} catch (Exception e) {
//					continue;
//				}
//			}
//			else {
				int cpuIndex = newGame.findPlayerIndex(newGame.getFirstFromTurnDeque());
				try {
					newGame.getPlayersArray().get(cpuIndex).placeUnitInitial(newGame.getBoard(), -1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				newGame.endPlayerTurn(newGame.getFirstFromTurnDeque());
//			}
		}
		
		while(newGame.doPlayersHaveArmies() == true) {
			int cpuIndex = newGame.findPlayerIndex(newGame.getFirstFromTurnDeque());
			try {
				newGame.getPlayersArray().get(cpuIndex).placeUnitAllOccupied(newGame.getBoard(), -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			newGame.endPlayerTurn(newGame.getFirstFromTurnDeque());
		}
		newGame.shuffleCards();
		
		
		newGame.saveGame();
		
		try {
			BufferedReader buff = new BufferedReader(new FileReader("testSave.txt"));
			newGame.loadGame(buff);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// main game loop
		while(newGame.getPlayersArray().size() > 1) {
			int playerIndex = newGame.findPlayerIndex(newGame.getFirstFromTurnDeque());
			if(newGame.getPlayersArray().get(playerIndex) instanceof EasyAI) {
				EasyAI easy = (EasyAI) newGame.getPlayersArray().get(playerIndex);
				newGame.addBaseArmyStartTurn(easy);
				newGame.addArmyContinentBonus(easy);
				newGame.addArmyFromCardsCPU(easy);
				
				//placement phase
				while(easy.getArmyTotal() > 0) {
					easy.placeUnits(newGame.getBoard(), -1, -1);
				}
				
				
				ArrayList<Integer> attackLocations = new ArrayList<Integer>();
				//attack phase
				boolean didCapture = false;
				do {
					attackLocations = easy.attackCoordinates(newGame.getBoard());
					if(attackLocations.isEmpty() == false) {
						// execute move
						ArrayList<Integer> attackDice = new ArrayList<Integer>(easy.cpuDiceRollAttack(newGame.getBoard(), attackLocations.get(0)));
						int defenderColor = newGame.getDefendingPlayerColor(attackLocations.get(1));
						EasyAI defender = (EasyAI) newGame.getPlayersArray().get(newGame.findPlayerIndex(defenderColor));
						ArrayList<Integer> defendDice = new ArrayList<Integer>(defender.cpuDiceRollDefend(newGame.getBoard(), attackLocations.get(1)));
						newGame.attackResult(attackLocations.get(0), attackLocations.get(1), attackDice, defendDice);
						
						if(newGame.canCaptureTerritory(attackLocations.get(1)) == true) {
							try {
								newGame.getBoard().setBoardSpaceOwner(attackLocations.get(1), easy.getColor());
								easy.addCountryControl(attackLocations.get(1));
								defender.removeCountryControl(attackLocations.get(1));
								didCapture = true;
								easy.moveArmiesToCaptureCountry(newGame.getBoard(), attackLocations.get(0), attackLocations.get(1), attackDice.size());					
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					}
				} while(attackLocations.isEmpty() == false);

				
				ArrayList<Player> eliminated = newGame.eliminatePlayersFromGame();
				
				if(newGame.getPlayersArray().size() > 1) {
				
					if(eliminated.isEmpty() == false) {
						//collect cards
						for(int index = 0; index < eliminated.size(); index++) {
							ArrayList<Card> cards = new ArrayList<Card>(eliminated.get(index).getCardHand());
							for(int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
								easy.addCardHand(cards.get(cardIndex));
							}
						}
						if(easy.getCardHand().size() >= 6) {
							newGame.addArmyFromCardsCPU(easy);
						}
					}
				
				
				
					if(didCapture == true) {
						if(newGame.getCardDeck().getDeckSize() != 0) {
							easy.takeCard(newGame.getCardDeck());
						}
						else {
							newGame.resetCardDeck();
							easy.takeCard(newGame.getCardDeck());
						}
					}
					// fortify position phase
					easy.fortifyPosition(newGame.getBoard());
				
				
					newGame.endPlayerTurn(newGame.getFirstFromTurnDeque());
				}
			}
		}
		
		newGame.saveGame();
		//String s = newGame.saveGame();
		//System.out.println(s);
		Game test = new Game();
		try {
			BufferedReader buff = new BufferedReader(new FileReader("testSave.txt"));
			test.loadGame(buff);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		System.out.print("The winner is ");
		System.out.println(newGame.getPlayersArray().get(0).getColor());
		
		
	}

}
