package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;

public abstract class Cpu extends Player {

	
	/** 
	 * Class constructor.
	 * @author Charles Snyder
	 */
	public Cpu() {
		super();
	}

	/** 
	 * Class constructor with color parameter.
	 * @param a_color - the color the player has selected.
	 * @author Charles Snyder
	 */
	public Cpu(int a_color) {
		super(a_color);
	}

	
	/** 
	 * Class copy constructor.
	 * @param a_player - the Player object to be copied.
	 * @author Charles Snyder
	 */
	public Cpu(Cpu a_player) {
		super(a_player);
	}
	
	
	
	protected Cpu(Parcel in) {
		super(in);
	}
	
	
	
	
	/** 
	 * Selects which country to place its two army bonus in.
	 * @param cardMatchResults - will be filled with three indices, each one corresponding to whether the first card was owned, the second card was owned,
	 * and so on.
	 * @param cards - the three cards that were selected by the cpu.
	 * @param gameBoard - the current board for the game.
	 * @author Charles Snyder
	 */
	public abstract void selectCountryCardBonus(ArrayList<Boolean> cardMatchResults, ArrayList<Card> cards, Board gameBoard);
	
	
	
	/** 
	 * Selects the move the cpu is going to execute for an attack.
	 * @param gameBoard - the current board for the game.
	 * @return An array list where the first index is where the attack is originating from
	 * and the second index is where the cpu is attacking.
	 * @author Charles Snyder
	 */
	public abstract ArrayList<Integer> attackCoordinates(Board gameBoard);
	
	
	
	/** 
	 * Selects how many dice the cpu should throw for an attack.
	 * @param gameBoard - the current board for the game.
	 * @param originCountry - the country where the cpu is attacking from.
	 * @return The number of dice the cpu has elected to use, -1 if error.
	 * @author Charles Snyder
	 */
	public abstract int selectNumDiceAttack(Board gameBoard, int originCountry);
	
	
	/** 
	 * Selects how many dice the cpu should throw when defending.
	 * @param gameBoard - the current board for the game.
	 * @param defendCountry - the country the cpu is defending.
	 * @return The number of dice the cpu has elected to use, -1 if error.
	 * @author Charles Snyder
	 */
	public abstract int selectNumDiceDefend(Board gameBoard, int defendCountry);
	
	
	
	/** 
	 * Once the cpu has eliminated all defending armies from a country this function moves a portion of the attacking force
	 * into the conquered territory.
	 * @param gameBoard - the current board for the game.
	 * @param originCountry - the country the cpu attacked from.
	 * @param destCountry - the country the cpu has taken over.
	 * @param numDiceRolled - the number of dice thrown by the cpu that attack.  Relevant because you must have at least as many units as dice you used.
	 * @author Charles Snyder
	 */
	public abstract void moveArmiesToCaptureCountry(Board gameBoard, int originCountry, int destCountry, int numDiceRolled);
	
	
	
	/** 
	 * For the fortify position phase of the computers turn, it moves armies from one territory to another within a directly controlled path.
	 * @param gameBoard - the current board for the game.
	 * @author Charles Snyder
	 */
	public abstract void fortifyPosition(Board gameBoard);
	
	
	
	/** 
	 * Determines how many armies should be moved when fortifying a certain territory.
	 * @param gameBoard - the current board for the game.
	 * @param originCountry - the country the cpu has elected to move territories from.
	 * @author Charles Snyder
	 */
	public abstract int numArmiesToFortify(Board gameBoard, int originCountry);
	
	
	
	/** 
	 * Tries to find a matching set of cards in the cpu's hand.
	 * @return returnArray - the three Card objects that make up the matching set.
	 * @author Charles Snyder
	 */
	public ArrayList<Card> selectMatchingCards() {
		// Check if the cpu has less than three cards.
		if(m_cardHand.size() < 3) {
			return new ArrayList<Card>();
		}

		// Look through all permutations of cards in the cpu's hand.
		for(int index = 0; index < m_cardHand.size() - 2; index++) {
			for(int indexTwo = index + 1; indexTwo < m_cardHand.size() - 1; indexTwo++) {
				for(int indexThree = indexTwo + 1; indexThree < m_cardHand.size(); indexThree++) {
					Card tmp1 = new Card(m_cardHand.get(index));
					Card tmp2 = new Card(m_cardHand.get(indexTwo));
					Card tmp3 = new Card(m_cardHand.get(indexThree));
					// Check to see if the three selected cards are a match.
					if(tradeInCards(tmp1, tmp2, tmp3) == true) {
						ArrayList<Card> returnArray = new ArrayList<Card>();
						returnArray.add(tmp1);
						returnArray.add(tmp2);
						returnArray.add(tmp3);
						return returnArray;
					}
				}
			}
		}
		return new ArrayList<Card>();
	}
	

	/** 
	 * Computer rolls dice during the attacking portion of their turn.
	 * @param gameBoard - the current board for the game.
	 * @param originCountry - the country the cpu is attacking from.
	 * @return an array list made up of the die numbers that were rolled.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> cpuDiceRollAttack(Board gameBoard, int originCountry) {
		// Determine how many dice should be rolled.
		int numDice = selectNumDiceAttack(gameBoard, originCountry);
		
		switch(numDice) {
		case 1:
			int die = rollOneDie();
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			tmp.add(die);
			return tmp;
		case 2:
			return rollTwoDice();
		case 3:
			return rollThreeDice();
		default:
			return new ArrayList<Integer>();
		}
	}
	
	
	/** 
	 * Computer rolls dice when defending.
	 * @param gameBoard - the current board for the game.
	 * @param defendCountry - the country the cpu is defending.
	 * @return an array list made up of the die numbers that were rolled.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> cpuDiceRollDefend(Board gameBoard, int defendCountry) {
		// Determine how many dice should be rolled.
		int numDice = selectNumDiceDefend(gameBoard, defendCountry);
		
		switch(numDice) {
		case 1:
			int die = rollOneDie();
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			tmp.add(die);
			return tmp;
		case 2:
			return rollTwoDice();
		default:
			return new ArrayList<Integer>();
		}
	}
	

	
	/** 
	 * Create a map where the keys are each country that is controlled by the cpu player and the values
	 * are any countries that lie in a directly owned path.
	 * @param gameBoard - the current board for the game.
	 * @return returnMap - a map of countries and locations they can move units to.
	 * @author Charles Snyder
	 */
	public HashMap<Integer, ArrayList<Integer>> createFortifyMap(Board gameBoard) {
		HashMap<Integer, ArrayList<Integer>> returnMap = new HashMap<Integer, ArrayList<Integer>>();
		
		// Iterate through the countries the cpu owns.
		for(int index = 0; index < m_countryControl.size(); index++) {
			int armySize = 0;
			try {
				armySize = gameBoard.getBoardSpaceArmy(m_countryControl.get(index));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// Need at least two armies to be able to move units elsewhere.
			if(armySize >= 2) {
				ArrayList<Integer> fortList = new ArrayList<Integer>();
				ArrayList<Integer> searched = new ArrayList<Integer>();
				createFortifyList(m_countryControl.get(index), gameBoard, fortList, searched);
				
				if(fortList.isEmpty() == false) {
					returnMap.put(m_countryControl.get(index), fortList);
				}
			}
		}
		return returnMap;
	}
	
	
	/** 
	 * Recursive function that looks through all the border countries and the borders of those countries and so on to create
	 * a list of countries that make up a directly owned path for the player.
	 * @param a_originCountry - the country to have its borders searched.
	 * @param a_gameBoard - the current board for the game.
	 * @param a_fortList - the list to be populated with all the countries found to be in a directly owned path.
	 * @param a_searched - gets populated by origin countries to ensure the same country doesn't get searched twice.
	 * @author Charles Snyder
	 */
	public void createFortifyList(int a_originCountry, Board a_gameBoard, ArrayList<Integer> a_fortList, ArrayList<Integer> a_searched) {
		a_searched.add(a_originCountry);
		
		ArrayList<Integer> borderArray = new ArrayList<Integer>(a_gameBoard.getBoardSpaces().get(a_originCountry).getBorderCountries());
		
		for(int index = 0; index < borderArray.size(); index++) {
			try {
				// The player owns one of the border countries and it's borders have not yet been searched.
				if(a_gameBoard.getBoardSpaceOwner(borderArray.get(index)) == m_color && !(a_searched.contains((Object)borderArray.get(index)))) {
					a_fortList.add(borderArray.get(index));
					createFortifyList(borderArray.get(index), a_gameBoard, a_fortList, a_searched);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/** 
	 * Looks to see if the player owns one less than all of the countries in any continent.
	 * @param a_country - the country the cpu wants to query.
	 * @param a_gameBoard - the current board for the game.
	 * @return True if the cpu owns one less than all countries in any continent, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean doesCPUOwnMostContinent(int a_country, Board a_gameBoard) {
		
		// North America
		if(a_country == Board.ALASKA || a_country == Board.NORTHWEST_TERR || a_country == Board.GREENLAND || a_country == Board.ALBERTA
				 || a_country == Board.ONTARIO || a_country == Board.QUEBEC || a_country == Board.WESTERN_US
				 || a_country == Board.EASTERN_US || a_country == Board.CENTRAL_AMERICA) {
			int countryCount = 0;
			try {
				if(a_gameBoard.getAlaskaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getAlbertaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getNorthwestTerrOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getGreenlandOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getOntarioOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getQuebecOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getWesternUSOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getEasternUSOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getCentralAmericaOwner() == m_color) {
					countryCount++;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			// One less than total of countries in North America
			if(countryCount == 8) {
				return true;
			}
			else {
				return false;
			}
		}
		// South America
		else if(a_country == Board.VENEZUELA || a_country == Board.PERU || a_country == Board.ARGENTINA || a_country == Board.BRAZIL) {
			int countryCount = 0;
			try {
				if(a_gameBoard.getVenezuelaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getPeruOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getArgentinaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getBrazilOwner() == m_color) {
					countryCount++;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(countryCount == 3) {
				return true;
			}
			else {
				return false;
			}
		}
		// Africa
		else if(a_country == Board.NORTH_AFRICA || a_country == Board.EGYPT || a_country == Board.CONGO || a_country == Board.EAST_AFRICA
				 || a_country == Board.SOUTH_AFRICA || a_country == Board.MADAGASCAR) {
			int countryCount = 0;
			try {
				if(a_gameBoard.getNorthAfricaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getEgyptOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getCongoOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getEastAfricaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getSouthAfricaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getMadagascarOwner() == m_color) {
					countryCount++;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(countryCount == 5) {
				return true;
			}
			else {
				return false;
			}
		}
		// Europe
		else if(a_country == Board.ICELAND || a_country == Board.SCANDINAVIA || a_country == Board.UKRAINE || a_country == Board.NORTHERN_EUROPE
				 || a_country == Board.WESTERN_EUROPE || a_country == Board.SOUTHERN_EUROPE || a_country == Board.GREAT_BRITAIN) {
			int countryCount = 0;
			try {
				if(a_gameBoard.getIcelandOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getScandinaviaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getUkraineOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getNorthernEuropeOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getWesternEuropeOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getSouthernEuropeOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getGreatBritainOwner() == m_color) {
					countryCount++;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(countryCount == 6) {
				return true;
			}
			else {
				return false;
			}
		}
		// Asia
		else if(a_country == Board.MIDDLE_EAST || a_country == Board.AFGHANISTAN || a_country == Board.URAL || a_country == Board.YAKUTSK
				 || a_country == Board.KAMCHATKA || a_country == Board.JAPAN || a_country == Board.IRKUTSK || a_country == Board.SIBERIA
				 || a_country == Board.MONGOLIA || a_country == Board.CHINA || a_country == Board.SOUTHEAST_ASIA || a_country == Board.INDIA) {
			int countryCount = 0;
			try {
				if(a_gameBoard.getMiddleEastOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getAfghanistanOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getUralOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getYakutskOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getKamchatkaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getJapanOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getIrkutskOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getSiberiaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getMongoliaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getChinaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getSoutheastAsiaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getIndiaOwner() == m_color) {
					countryCount++;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(countryCount == 11) {
				return true;
			}
			else {
				return false;
			}
		}
		// Australia
		else if(a_country == Board.INDONESIA || a_country == Board.NEW_GUINEA || a_country == Board.EASTERN_AUST || a_country == Board.WESTERN_AUST) {
			int countryCount = 0;
			try {
				if(a_gameBoard.getIndonesiaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getNewGuineaOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getEasternAustOwner() == m_color) {
					countryCount++;
				}
				if(a_gameBoard.getWesternAustOwner() == m_color) {
					countryCount++;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(countryCount == 3) {
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
	
	
	
	/** 
	 * Finds all countries the cpu owns that have at least one enemy border and not all enemy borders.
	 * @param a_gameBoard - the current board for the game.
	 * @param eligibleCountries - empty array list that gets populated in the function if a particular country is determined to have at least one
	 * and not all enemy borders.
	 * @author Charles Snyder
	 */
	public void findBorderCountries(Board a_gameBoard, ArrayList<Integer> eligibleCountries) {
		
		// Iterate through owned countries of the player.
		for(int index = 0; index < m_countryControl.size(); index++) {
			
			// Get its borders.
			ArrayList<Integer> borders = new ArrayList<Integer>(a_gameBoard.getBoardSpaces().get(m_countryControl.get(index)).getBorderCountries());
			
			// If it meets the criteria add it to the list.
			if(ownAllBorderCountries(a_gameBoard, borders) == false
					&& countrySurroundedByEnemies(a_gameBoard, borders) == false) {
				eligibleCountries.add(m_countryControl.get(index));
			}
		}
	}
	
	
	/** 
	 * Determines whether the border countries of a given country are all owned by the player.
	 * @param a_gameBoard - the current board for the game.
	 * @param borders - a list of border countries for a particular country.
	 * @return True if the player owns every border country around a given country, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean ownAllBorderCountries(Board a_gameBoard, ArrayList<Integer> borders) {
		for(int index = 0; index < borders.size(); index++) {
			int owner = a_gameBoard.getBoardSpaces().get(borders.get(index)).getCountryOwner();
			if(owner != m_color) {
				return false;
			}
		}
		return true;
	}
	
	
	/** 
	 * Determines whether the border countries of a given country are all owned by enemy players.
	 * @param a_gameBoard - the current board for the game.
	 * @param borders - a list of border countries for a particular country.
	 * @return True if the enemy owns every border country, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean countrySurroundedByEnemies(Board a_gameBoard, ArrayList<Integer> borders) {
		for(int index = 0; index < borders.size(); index++) {
			int owner = a_gameBoard.getBoardSpaces().get(borders.get(index)).getCountryOwner();
			if(owner == m_color) {
				return false;
			}
		}
		return true;
	}
	
	
	
	/** 
	 * During the initial phase of the game, the cpu determines which country is best to choose based on
	 * an algorithm involving the number of borders it controls around the country, the number of countries
	 * that compose a continent, and any enemy controlled borders.
	 * @param a_gameBoard - the current board for the game.
	 * @return returnCountry - the country that is chosen.
	 * @author Charles Snyder
	 */
	public int findCountryToChoose(Board a_gameBoard) {
		// Initialize to arbitrary values.
		int maxScore = -1;
		int returnCountry = -1;
		
		// Iterate through entire board.
		for(int index = 0; index < a_gameBoard.getBoardSpaces().size(); index++) {
			int score = 100;
			
			// Number of borders around a country
			int borderNum = a_gameBoard.getBoardSpaces().get(index).getBorderCountries().size();
			
			// More borders means more borders to defend, that's a negative.
			score = score - borderNum;
			
			int bordersControlled = 0;
			int enemyControlledBorders = 0;
			
			// Iterate through the borders of each country.
			for(int borderIndex = 0; borderIndex < a_gameBoard.getBoardSpaces().get(index).getBorderCountries().size(); borderIndex++) {
				int country = a_gameBoard.getBoardSpaces().get(index).getBorderCountries().get(borderIndex);
				try {
					if(a_gameBoard.getBoardSpaceOwner(country) == m_color) {
						bordersControlled++;
					}
					else if(a_gameBoard.getBoardSpaceOwner(country) != -1) {
						enemyControlledBorders++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// Positive if the player controls more borders around a certain country.
			score = score + (7 * bordersControlled);
			
			// Negative if the country is on a large continent thus making it hard to control.
			score = score - a_gameBoard.getNumCountriesContinent(index);
			
			// Negative if there are enemy borders.
			score = score - (5 * enemyControlledBorders);
			if(score > maxScore && a_gameBoard.getBoardSpaces().get(index).getCountryOwner() == -1) {
				// Set the highest score found and the country that had it.
				maxScore = score;
				returnCountry = index;
			}
		}
		
		return returnCountry;
	}
	
	
	
	/** 
	 * When the cpu receives their two army card bonus and happens to own at least two of the countries it can apply,
	 * this function chooses the best place to apply it.  It determines this using the number of armies already in that country,
	 * the number of enemy borders, and the number of enemy armies across all borders.
	 * @param countryOne - the first country to be examined.
	 * @param countryTwo - the second country to be examined.
	 * @param gameBoard - the current board for the game.
	 * @return Whichever country is selected by the cpu.
	 * @author Charles Snyder
	 */
	public int findCountryCardBonus(int countryOne, int countryTwo, Board gameBoard) {
		int scoreOne = 100;
		int scoreTwo = 100;
		int armyOne = 0;
		int armyTwo = 0;
		try {
			armyOne = gameBoard.getBoardSpaceArmy(countryOne);
			armyTwo = gameBoard.getBoardSpaceArmy(countryTwo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int enemyBordersOne = getNumberEnemyBorders(countryOne, gameBoard);
		int enemyBordersTwo = getNumberEnemyBorders(countryTwo, gameBoard);
		
		int enemyArmiesBordersOne = getArmyCountOfEnemyBorders(countryOne, gameBoard);
		int enemyArmiesBordersTwo = getArmyCountOfEnemyBorders(countryTwo, gameBoard);
		
		scoreOne = scoreOne - armyOne + (50 * enemyBordersOne) + (100 * enemyArmiesBordersOne);
		scoreTwo = scoreTwo - armyTwo + (50 * enemyBordersTwo) + (100 * enemyArmiesBordersTwo);
		
		if(scoreOne >= scoreTwo) {
			return countryOne;
		}
		else {
			return countryTwo;
		}	
	}
	
	
	
	
	/** 
	 * Determines the number of enemy borders around a certain country.
	 * @param country - the country to be examined.
	 * @param gameBoard - the current board for the game.
	 * @return numEnemyBorders - the number of enemy borders around the country parameter.
	 * @author Charles Snyder
	 */
	public int getNumberEnemyBorders(int country, Board gameBoard) {
		int numEnemyBorders = 0;
		ArrayList<Integer> borders = new ArrayList<Integer>(gameBoard.getBoardSpaces().get(country).getBorderCountries());
		
		for(int index = 0; index < borders.size(); index++) {
			int owner = -1;
			try {
				owner = gameBoard.getBoardSpaceOwner(borders.get(index));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// If the country has yet to be claimed or is not the color of the player.
			if(owner != -1 && owner != m_color) {
				numEnemyBorders++;
			}
		}
		return numEnemyBorders;
	}
	
	
	/** 
	 * Determines the number of enemy armies in all enemy borders around a given country.
	 * @param country - the country to be examined.
	 * @param gameBoard - the current board for the game.
	 * @return numEnemyArmiesBorders - the number of enemy armies in all enemy borders around the country parameter.
	 * @author Charles Snyder
	 */
	public int getArmyCountOfEnemyBorders(int country, Board gameBoard) {
		int numEnemyArmiesBorders = 0;
		ArrayList<Integer> borders = new ArrayList<Integer>(gameBoard.getBoardSpaces().get(country).getBorderCountries());
		
		for(int index = 0; index < borders.size(); index++) {
			int owner = -1;
			try {
				owner = gameBoard.getBoardSpaceOwner(borders.get(index));
				
				// If the country has yet to be claimed or is not the color of the player.
				if(owner != -1 && owner != m_color) {
					numEnemyArmiesBorders += gameBoard.getBoardSpaceArmy(borders.get(index));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return numEnemyArmiesBorders;
	}
	
	
	
	/** 
	 * To be used for hard computer player.  It creates a map of all possible attack moves the cpu player
	 * can make using only what the cpu considers eligible countries.  Those are countries that are not entirely surrounded by
	 * the enemy.
	 * @param gameBoard - the current board for the game.
	 * @param eligibleCountries - list of countries the cpu can attack from.
	 * @return returnMap - a map of attack moves, where the keys are the originating countries, and the values are all possible countries
	 * that the cpu can attack from the key country.
	 * @author Charles Snyder
	 */
	public HashMap<Integer, ArrayList<Integer>> createAttackMap(Board gameBoard, ArrayList<Integer> eligibleCountries) {
		HashMap<Integer, ArrayList<Integer>> returnMap = new HashMap<Integer, ArrayList<Integer>>();
		
		for(int index = 0; index < eligibleCountries.size(); index++) {
			int armySize = 0;
			try {
				armySize = gameBoard.getBoardSpaceArmy(eligibleCountries.get(index));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// Must have at least two armies to attack.
			if(armySize >= 2) {
				
				// Holds border countries
				ArrayList<Integer> borderArray = new ArrayList<Integer>(gameBoard.getBoardSpaces().get(eligibleCountries.get(index)).getBorderCountries());
				
				ArrayList<Integer> attackLocations = new ArrayList<Integer>();
				
				// Iterate over all border countries to see where an attack is possible.
				for(int borderIndex = 0; borderIndex < borderArray.size(); borderIndex++) {
					try {
						int owner = gameBoard.getBoardSpaceOwner(borderArray.get(borderIndex));
						if(owner != m_color) {
							attackLocations.add(borderArray.get(borderIndex));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// Check to see if there are possible moves to make from this country.
				if(attackLocations.isEmpty() == false) {
					returnMap.put(eligibleCountries.get(index), attackLocations);
				}
			}
		}
		return returnMap;
	}
	
	
	
	/** 
	 * To be used for easy computer players.  It creates a map of all possible attack moves the cpu player
	 * can make.
	 * @param gameBoard - the current board for the game.
	 * @return returnMap - a map of attack moves, where the keys are the originating countries, and the values are all possible countries
	 * that the cpu can attack from the key country.
	 * @author Charles Snyder
	 */
	public HashMap<Integer, ArrayList<Integer>> createAttackMap(Board gameBoard) {
		HashMap<Integer, ArrayList<Integer>> returnMap = new HashMap<Integer, ArrayList<Integer>>();
		
		for(int index = 0; index < m_countryControl.size(); index++) {
			int armySize = 0;
			try {
				armySize = gameBoard.getBoardSpaceArmy(m_countryControl.get(index));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			// Must have at least two armies to attack.
			if(armySize >= 2) {
				// Holds border countries
				ArrayList<Integer> borderArray = new ArrayList<Integer>(gameBoard.getBoardSpaces().get(m_countryControl.get(index)).getBorderCountries());
				
				ArrayList<Integer> attackLocations = new ArrayList<Integer>();
				
				// Iterate over all border countries to see where an attack is possible.
				for(int borderIndex = 0; borderIndex < borderArray.size(); borderIndex++) {
					try {
						int owner = gameBoard.getBoardSpaceOwner(borderArray.get(borderIndex));
						if(owner != m_color) {
							attackLocations.add(borderArray.get(borderIndex));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// Check to see if there are possible moves to make from this country.
				if(attackLocations.isEmpty() == false) {
					returnMap.put(m_countryControl.get(index), attackLocations);
				}
			}
		}
		return returnMap;
	}
}
