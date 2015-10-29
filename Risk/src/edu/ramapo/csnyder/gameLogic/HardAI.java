package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import android.os.Parcel;
import android.os.Parcelable;

public class HardAI extends Cpu {

	public HardAI() {
		super();
	}

	public HardAI(int a_color) {
		super(a_color);
	}

	public HardAI(HardAI a_player) {
		super(a_player);
	}
	
	public HardAI(Parcel in) {
		super(in);
	}
	
	
	@Override
	public void placeUnitInitial(Board a_gameBoard, int a_country) {
		Random rand = new Random();
		int randNum = rand.nextInt(2);
		// If the board is empty then these two moves are most desirable in terms of the scoring algorithm being used.
		if(a_gameBoard.isBoardEmpty() == true) {
			if(randNum == 0) {
				a_country = Board.EASTERN_AUST;
			}
			else {
				a_country = Board.ARGENTINA;
			}
		}
		// Board is not empty
		else {
			ArrayList<Integer> blockContinent = new ArrayList<Integer>(a_gameBoard.isContinentAlmostControlled());
			
			// Check to see if any player nearly controls an entire continent.
			if(blockContinent.isEmpty() == false) {
				for(int index = 0; index < blockContinent.size(); index++) {
					try {
						// Check to see if the cpu can move into the country that would block the other player from controlling the entire
						// continent.
						if(a_gameBoard.getBoardSpaceOwner(blockContinent.get(index)) == -1) {
							a_country = blockContinent.get(index);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(a_country == -1) {
				a_country = findCountryToChoose(a_gameBoard);
			}
		}
		
		try {
			a_gameBoard.setBoardSpaceOwner(a_country, m_color);
			a_gameBoard.setBoardSpaceArmy(a_country, 1);
			m_armies--;
			m_countryControl.add(a_country);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void placeUnitAllOccupied(Board a_gameBoard, int a_country) {
		ArrayList<Integer> eligibleCountries = new ArrayList<Integer>();
		
		// Finds all countries that are not entirely surrounded by enemy or own player regions.
		findBorderCountries(a_gameBoard, eligibleCountries);
		
		int selectedCountry = -1;
		int maxScore = 0;
		
		for(int index = 0; index < eligibleCountries.size(); index++) {
			int score = 100;
			int numArmies = 0;			
			int enemyArmies = 0;		
			try {
				numArmies = a_gameBoard.getBoardSpaceArmy(eligibleCountries.get(index));
				enemyArmies = getArmyCountOfEnemyBorders(eligibleCountries.get(index), a_gameBoard);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Algorithm takes away score for regions with high army counts but adds to it if the enemy has large build up of forces
			// in surrounding regions.
			score = score - (3 * numArmies) + enemyArmies;
			if(score > maxScore) {
				maxScore = score;
				selectedCountry = eligibleCountries.get(index);
			}
		}
		
		try {
			a_gameBoard.setBoardSpaceArmy(selectedCountry, a_gameBoard.getBoardSpaceArmy(selectedCountry) + 1);
			m_armies--;
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void placeUnits(Board a_gameBoard, int a_country, int unitCount) {
		ArrayList<Integer> eligibleCountries = new ArrayList<Integer>();
		findBorderCountries(a_gameBoard, eligibleCountries);
		
		// Determine where the best attacking move is.
		ArrayList<Integer> moveSet = new ArrayList<Integer>(findBestAttackMove(a_gameBoard, eligibleCountries));
		
		// No best move was found.
		if(moveSet.isEmpty() == true) {
			// Find the country owned by the player that has the largest number of armies.
			int maxArmy = 0;
			int selectedCountry = -1;
			for(int index = 0; index < m_countryControl.size(); index++) {
				try {
					int armyNum = a_gameBoard.getBoardSpaceArmy(m_countryControl.get(index));
					if(armyNum > maxArmy) {
						maxArmy = armyNum;
						selectedCountry = m_countryControl.get(index);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			try {
				// Place all armies into that one territory.
				a_gameBoard.setBoardSpaceArmy(selectedCountry, maxArmy + m_armies);
				m_armies = 0;
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				// Place all armies into the region the best attack move was originated from.
				a_gameBoard.setBoardSpaceArmy(moveSet.get(0), a_gameBoard.getBoardSpaceArmy(moveSet.get(0)) + m_armies);
				m_armies = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	

	
	private ArrayList<Integer> findBestAttackMove(Board a_gameBoard, ArrayList<Integer> eligibleCountries) {
		// Create attack map based on eligible countries, so not surrounded by enemy or by own countries.
		HashMap<Integer, ArrayList<Integer>> attackMap = new HashMap<Integer, ArrayList<Integer>>(createAttackMap(a_gameBoard, eligibleCountries));
		
		// No moves possible.
		if(attackMap.isEmpty() == true) {
			return new ArrayList<Integer>();
		}
		
		ArrayList<Integer> returnArray = new ArrayList<Integer>();
		
		// Create array from keys.
		CopyOnWriteArraySet<Integer> attackOriginSet = new CopyOnWriteArraySet<Integer>(attackMap.keySet());
		Object[] attackOriginArray = attackOriginSet.toArray();

		int maxScore = -100;
		
		// Iterate through keys.
		for(int index = 0; index < attackOriginArray.length; index++) {
			int originCountry = (int) attackOriginArray[index];
			
			// Get associated values of particular key.
			ArrayList<Integer> attackLocations = new ArrayList<Integer>(attackMap.get(originCountry));
			
			int score = 100;
			// Bonus if cpu has opportunity to take a continent.
			if(doesCPUOwnMostContinent(originCountry, a_gameBoard)) {
				score += 50;
			}
			
			int originArmy = 0;
			try {
				originArmy = a_gameBoard.getBoardSpaceArmy(originCountry);
				// Good if large army is in the originating attack country.
				score += originArmy;
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
			// Iterate through attackable countries.
			for(int borderIndex = 0; borderIndex < attackLocations.size(); borderIndex++) {
				int attackCountry = attackLocations.get(borderIndex);
				int defendingArmy;
				try {
					defendingArmy = a_gameBoard.getBoardSpaceArmy(attackCountry);
					// Bad if defender has large force.
					score -= defendingArmy;
					if(score > maxScore) {
						maxScore = score;
						returnArray.clear();
						returnArray.add(originCountry);
						returnArray.add(attackCountry);
					}
					//To reset score before factoring in defending army size
					score += defendingArmy;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
		}		
		return returnArray;
	}
	
	
	
	public ArrayList<Integer> attackCoordinates(Board a_gameBoard) {
		// Create map of all attackable moves.
		HashMap<Integer, ArrayList<Integer>> attackMap = new HashMap<Integer, ArrayList<Integer>>(createAttackMap(a_gameBoard));

		// No moves possible.
		if(attackMap.isEmpty() == true) {
			return new ArrayList<Integer>();
		}
		
		ArrayList<Integer> returnArray = new ArrayList<Integer>();
		
		// Create array of keys or originating attack countries.
		CopyOnWriteArraySet<Integer> attackOriginSet = new CopyOnWriteArraySet<Integer>(attackMap.keySet());
		Object[] attackOriginArray = attackOriginSet.toArray();

		// Iterate through keys.
		for(int index = 0; index < attackOriginArray.length; index++) {
			int originCountry = (int) attackOriginArray[index];
			ArrayList<Integer> attackLocations = new ArrayList<Integer>(attackMap.get(originCountry));
			
			int score = 100;
			// Bonus if cpu has opportunity to take a continent.
			if(doesCPUOwnMostContinent(originCountry, a_gameBoard)) {
				score += 50;
			}
			
			int originArmy = 0;
			try {
				originArmy = a_gameBoard.getBoardSpaceArmy(originCountry);
				// Good if large force is in originating attack country.
				score += originArmy;
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
			// Iterate through attackable countries.
			for(int borderIndex = 0; borderIndex < attackLocations.size(); borderIndex++) {
				int attackCountry = attackLocations.get(borderIndex);
				try {
					// Only execute move if it achieves a certain score.
					if(score >= 104) {
						returnArray.add(originCountry);
						returnArray.add(attackCountry);
						return returnArray;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
		}
		// If returnArray is empty no moves met the criteria to execute.
		return returnArray;
	}
	
	
	
	public final int selectNumDiceAttack(Board gameBoard, int originCountry) {
		// The hard CPU will always choose the highest number of dice to throw as it possibly can as
		// this increases the probability of a successful result.
		int armySize = 0;
		try {
			armySize = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(armySize >= 4) {
			return 3;
		}
		else if(armySize == 3) {
			return 2;
		}
		else {
			return 1;
		}

	}
	
	public final int selectNumDiceDefend(Board gameBoard, int defendCountry) {
		// The hard CPU will always choose the highest number of dice to throw as it possibly can as
		// this increases the probability of a successful result.
		int armySize = 0;
		try {
			armySize = gameBoard.getBoardSpaceArmy(defendCountry);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(armySize >= 2) {
			return 2;
		}
		else {
			return 1;
		}
	}
	
	

	
	
	
	public void moveArmiesToCaptureCountry(Board gameBoard, int originCountry, int destCountry, int numDiceRolled) {
		int attackArmySize = 0;
		try {
			attackArmySize = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int numArmyMove = numDiceRolled;
		
		// Have to move at least the same number of dice rolled and must leave at least one behind, any remaining are movable.
		int possibleAdditionalArmiesToMove = (attackArmySize - 1) - numDiceRolled;
		
		// Check to see if there is any choice with armies to move.
		if(possibleAdditionalArmiesToMove == 0) {
			try {
				gameBoard.setBoardSpaceArmy(originCountry, attackArmySize - numArmyMove);
				gameBoard.setBoardSpaceArmy(destCountry, numArmyMove);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Check to see if the country that the cpu was attacking from is now surrounded by all friendly regions.
		if(ownAllBorderCountries(gameBoard, gameBoard.getBoardSpaces().get(originCountry).getBorderCountries()) == true) {
			numArmyMove = attackArmySize - 1;
		}
		// If there is still a threat it leaves two armies behind.
		else {		
			numArmyMove = attackArmySize - 2;
		}
		
		try {
			gameBoard.setBoardSpaceArmy(originCountry, attackArmySize - numArmyMove);
			gameBoard.setBoardSpaceArmy(destCountry, numArmyMove);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void selectCountryCardBonus(ArrayList<Boolean> cardMatchResults, ArrayList<Card> cards, Board gameBoard) {
		// Check to see if cpu owns any of the countries on the cards.
		if(cardMatchResults.get(0) == false && cardMatchResults.get(1) == false && cardMatchResults.get(2) == false) {
			return;
		}
		// If the cpu owns just one country on the cards the bonus will be applied to that country.
		else if((cardMatchResults.get(0) == true && cardMatchResults.get(1) == false && cardMatchResults.get(2) == false)
				|| (cardMatchResults.get(0) == false && cardMatchResults.get(1) == true && cardMatchResults.get(2) == false)
				|| (cardMatchResults.get(0) == false && cardMatchResults.get(1) == false && cardMatchResults.get(2) == true)) {
			m_cardBonus = true;
			m_armies += 2;
			if(cardMatchResults.get(0) == true) {
				int country = cards.get(0).getCardCountry();
				try {
					super.placeUnits(gameBoard, country, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(cardMatchResults.get(1) == true) {
				int country = cards.get(1).getCardCountry();
				try {
					super.placeUnits(gameBoard, country, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				int country = cards.get(2).getCardCountry();
				try {
					super.placeUnits(gameBoard, country, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// The cpu owns two of countries on the cards.
		else if((cardMatchResults.get(0) == true && cardMatchResults.get(1) == true && cardMatchResults.get(2) == false)
				|| (cardMatchResults.get(0) == false && cardMatchResults.get(1) == true && cardMatchResults.get(2) == true)
				|| (cardMatchResults.get(0) == true && cardMatchResults.get(1) == false && cardMatchResults.get(2) == true)) {
			m_cardBonus = true;
			m_armies += 2;
			if(cardMatchResults.get(0) == true && cardMatchResults.get(1) == true) {
				int countryOne = cards.get(0).getCardCountry();
				int countryTwo = cards.get(1).getCardCountry();
				
				// The country is chosen based on algorithm.
				int placementCountry = findCountryCardBonus(countryOne, countryTwo, gameBoard);
				try {
					super.placeUnits(gameBoard, placementCountry, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(cardMatchResults.get(1) == true && cardMatchResults.get(2) == true) {
				int countryOne = cards.get(1).getCardCountry();
				int countryTwo = cards.get(2).getCardCountry();
				
				// The country is chosen based on algorithm.
				int placementCountry = findCountryCardBonus(countryOne, countryTwo, gameBoard);
				try {
					super.placeUnits(gameBoard, placementCountry, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				int countryOne = cards.get(0).getCardCountry();
				int countryTwo = cards.get(2).getCardCountry();
				
				// The country is chosen based on algorithm.
				int placementCountry = findCountryCardBonus(countryOne, countryTwo, gameBoard);
				try {
					super.placeUnits(gameBoard, placementCountry, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// The cpu owns three of the countries on the cards.
		else {
			m_cardBonus = true;
			m_armies += 2;
			int countryOne = cards.get(0).getCardCountry();
			int countryTwo = cards.get(1).getCardCountry();
			int countryThree = cards.get(2).getCardCountry();
			
			// The country is chosen based on algorithm.
			int tmpCountry = findCountryCardBonus(countryOne, countryTwo, gameBoard);
			
			// The final country is chosen based on algorithm.
			int placementCountry = findCountryCardBonus(tmpCountry, countryThree, gameBoard);
			try {
				super.placeUnits(gameBoard, placementCountry, 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	
	public void fortifyPosition(Board gameBoard) {
		// Create a map of fortifiable territories.
		HashMap<Integer, ArrayList<Integer>> fortifyMap = new HashMap<Integer, ArrayList<Integer>>(createFortifyMap(gameBoard));
		
		// No countries to fortify from.
		if(fortifyMap.isEmpty() == true) {
			return;
		}
		else {
			
			// Create array of countries that can have armies taken from them.
			CopyOnWriteArraySet<Integer> fortifyOriginSet = new CopyOnWriteArraySet<Integer>(fortifyMap.keySet());
			Object[] fortifyOriginArray = fortifyOriginSet.toArray();
			
			int originCountry = -1;
			int maxOriginArmy = 0;
			boolean found = false;
			int selectedOriginCountry = -1;
			
			// Iterate through countries that can have armies taken.
			for(int index = 0; index < fortifyOriginArray.length; index++) {
				originCountry = (int) fortifyOriginArray[index];
				
				// Checks to see if each country is completely surrounded by friendly territories.
				if(ownAllBorderCountries(gameBoard, gameBoard.getBoardSpaces().get(originCountry).getBorderCountries()) == true) {
					try {
						int originArmy = gameBoard.getBoardSpaceArmy(originCountry);
						
						// Wants to choose the country that has the highest number of armies available to move.
						if(originArmy > maxOriginArmy) {
							maxOriginArmy = originArmy;
							selectedOriginCountry = originCountry;
						}
						found = true;
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			// No countries met the criteria to fortify from.
			if(found == false) {
				return;
			}
			
			// Get associated values of the country chosen to fortify from.
			ArrayList<Integer> fortifyLocations = new ArrayList<Integer>(fortifyMap.get(selectedOriginCountry));
			
			ArrayList<Integer> eligibleCountries = new ArrayList<Integer>();
			
			// Finds all countries that are fortifiable and have enemy borders.
			for(int index = 0; index < fortifyLocations.size(); index++) {
				ArrayList<Integer> borders = new ArrayList<Integer>(gameBoard.getBoardSpaces().get(fortifyLocations.get(index)).getBorderCountries());
				if(ownAllBorderCountries(gameBoard, borders) == false
						&& countrySurroundedByEnemies(gameBoard, borders) == false) {
					eligibleCountries.add(fortifyLocations.get(index));
				}
			}
			
			int maxEnemyArmyCount = 0;
			int selectedCountry = -1;
			
			// Iterates through fortifiable regions that met criteria.
			for(int index = 0; index < eligibleCountries.size(); index++) {
				int enemyCount = getArmyCountOfEnemyBorders(eligibleCountries.get(index), gameBoard);
				
				// Want to find the country that has the highest enemy army count around its borders.
				if(enemyCount > maxEnemyArmyCount) {
					maxEnemyArmyCount = enemyCount;
					selectedCountry = eligibleCountries.get(index);
				}
			}
			
			int fortifyCountry = selectedCountry;
			
			// Execute fortify move once both regions have been selected.
			try {
				moveUnits(gameBoard, selectedOriginCountry, fortifyCountry, numArmiesToFortify(gameBoard, selectedOriginCountry));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public final int numArmiesToFortify(Board gameBoard, int originCountry) {
		// Will always choose to leave behind only one army in the country that the player
		// is fortifying from.
		int numOriginArmy = 0;
		try {
			numOriginArmy = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return numOriginArmy - 1;
	}
	


	public static final Parcelable.Creator<HardAI> CREATOR = new Parcelable.Creator<HardAI>() {
		public HardAI createFromParcel(Parcel in) {
			return new HardAI(in);
		}
		public HardAI[] newArray(int size) {
			return new HardAI[size];
		}
	};

}
