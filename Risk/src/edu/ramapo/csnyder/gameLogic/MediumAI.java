package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import android.os.Parcel;
import android.os.Parcelable;

public class MediumAI extends Cpu {

	public MediumAI() {
		super();
	}

	public MediumAI(int a_color) {
		super(a_color);
	}

	public MediumAI(MediumAI a_player) {
		super(a_player);
	}

	public MediumAI(Parcel in) {
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
		// Finds all countries that are not surrounded by enemies or friendly territories.
		findBorderCountries(a_gameBoard, eligibleCountries);
		
		// Iterates through all of the eligible countries and evenly distribute its armies to them.
		for(int index = 0; index < eligibleCountries.size() - 1; index++) {
			int armyInner = 1;
			int armyOuter = 1;
			for(int innerIndex = 1; innerIndex < eligibleCountries.size(); innerIndex++) {
				try {
					armyOuter = a_gameBoard.getBoardSpaceArmy(eligibleCountries.get(index));
					armyInner = a_gameBoard.getBoardSpaceArmy(eligibleCountries.get(innerIndex));
					if(armyOuter != armyInner) {
						a_gameBoard.setBoardSpaceArmy(eligibleCountries.get(innerIndex), armyInner + 1);
						m_armies--;
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				a_gameBoard.setBoardSpaceArmy(eligibleCountries.get(index), armyOuter + 1);
				m_armies--;
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	@Override
	public void placeUnits(Board a_gameBoard, int a_country, int unitCount) {
		ArrayList<Integer> eligibleCountries = new ArrayList<Integer>();
		// Finds all countries that are not surrounded by enemies or friendly territories.
		findBorderCountries(a_gameBoard, eligibleCountries);
		
		int minArmy = 10000;
		int selectedCountry = -1;
		// Tries to find the country that has the smallest army total.
		if(eligibleCountries.isEmpty() == false) {
			for(int index = 0; index < eligibleCountries.size(); index++) {
				try {
					int boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(eligibleCountries.get(index));
					if(boardSpaceArmy < minArmy) {
						minArmy = boardSpaceArmy;
						selectedCountry = eligibleCountries.get(index);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				// Adds one to the country with smallest number of armies.
				a_gameBoard.setBoardSpaceArmy(selectedCountry, minArmy + 1);
				m_armies--;
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// No eligible countries were found.
		else {
			// Copies the functionality of the easy AI.
			Random rand = new Random();
			int randomCountryIndex = rand.nextInt(m_countryControl.size());
			int randomCountry = m_countryControl.get(randomCountryIndex);
			
			int boardSpaceArmy = 0;
			try {
				boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(randomCountry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			int randomUnitCount = rand.nextInt(m_armies) + 1;
			
			try {
				a_gameBoard.setBoardSpaceArmy(randomCountry, boardSpaceArmy + randomUnitCount);
				m_armies = m_armies - randomUnitCount;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public ArrayList<Integer> attackCoordinates(Board a_gameBoard) {
		// Create map of all possible attack moves.
		HashMap<Integer, ArrayList<Integer>> attackMap = new HashMap<Integer, ArrayList<Integer>>(createAttackMap(a_gameBoard));

		if(attackMap.isEmpty() == true) {
			// No moves possible.
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> returnArray = new ArrayList<Integer>();
		
		// Create array of keys or originating countries.
		CopyOnWriteArraySet<Integer> attackOriginSet = new CopyOnWriteArraySet<Integer>(attackMap.keySet());
		Object[] attackOriginArray = attackOriginSet.toArray();

		// Iterate through keys
		for(int index = 0; index < attackOriginArray.length; index++) {
			int originCountry = (int) attackOriginArray[index];
			
			// Get keys associated values.
			ArrayList<Integer> attackLocations = new ArrayList<Integer>(attackMap.get(originCountry));
			
			int score = 100;
			// Multiplier if the cpu has the opportunity to capture a continent.
			if(doesCPUOwnMostContinent(originCountry, a_gameBoard)) {
				score += 50;
			}
			
			int originArmy = 0;
			try {
				originArmy = a_gameBoard.getBoardSpaceArmy(originCountry);
				score += originArmy;
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
			// Iterate through attackable countries
			for(int borderIndex = 0; borderIndex < attackLocations.size(); borderIndex++) {
				int attackCountry = attackLocations.get(borderIndex);
				int defendingArmy;
				try {
					defendingArmy = a_gameBoard.getBoardSpaceArmy(attackCountry);
					score -= defendingArmy;
					if(score > 101) {
						returnArray.add(originCountry);
						returnArray.add(attackCountry);
						return returnArray;
					}
					//To reset score before factoring in defending army size.
					score += defendingArmy;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
		}		
		return returnArray;
	}
	
	
	
	public final int selectNumDiceAttack(Board gameBoard, int originCountry) {
		// Same functionality as easy AI.
		int numDice = 0;
		int armySize = 0;
		Random rand = new Random();
		try {
			armySize = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(armySize > 3) {
			numDice = rand.nextInt(3) + 1;
		}
		else if(armySize == 3) {
			numDice = rand.nextInt(2) + 1;
		}
		else if(armySize == 2) {
			numDice = 1;
		}
		else {
			numDice = -1;
		}
		return numDice;
	}
	
	public final int selectNumDiceDefend(Board gameBoard, int defendCountry) {
		// Same functionality as easy AI.
		int numDice= 0;
		int armySize = 0;
		Random rand = new Random();
		try {
			armySize = gameBoard.getBoardSpaceArmy(defendCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(armySize >= 2) {
			numDice = rand.nextInt(2) + 1;
		}
		else if(armySize == 1) {
			numDice = 1;
		}
		else {
			numDice = -1;
		}
		return numDice;
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
		// Same functionality as EasyAI.
		Random rand = new Random();
		int selection = rand.nextInt(100);
		HashMap<Integer, ArrayList<Integer>> fortifyMap = new HashMap<Integer, ArrayList<Integer>>(createFortifyMap(gameBoard));
		if(selection < 50 || fortifyMap.isEmpty() == true) {
			return;
		}
		else {
			CopyOnWriteArraySet<Integer> fortifyOriginSet = new CopyOnWriteArraySet<Integer>(fortifyMap.keySet());
			Object[] attackOriginArray = fortifyOriginSet.toArray();
			int randOriginIndex = rand.nextInt(attackOriginArray.length);
			int originCountry = (int) attackOriginArray[randOriginIndex];
			
			ArrayList<Integer> fortifyLocations = new ArrayList<Integer>(fortifyMap.get(originCountry));
			
			int randFortifyIndex = rand.nextInt(fortifyLocations.size());
			int fortifyCountry = fortifyLocations.get(randFortifyIndex);
			
			try {
				moveUnits(gameBoard, originCountry, fortifyCountry, numArmiesToFortify(gameBoard, originCountry));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public final int numArmiesToFortify(Board gameBoard, int originCountry) {
		// Same functionality as EasyAI.
		int numOriginArmy = 0;
		try {
			numOriginArmy = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Random rand = new Random();
		int numMove;
		do {
			numMove = rand.nextInt(numOriginArmy) + 1;
		} while((numMove - numOriginArmy) == 0);
		
		return numMove;
	}
	

	

	
	
	
	public static final Parcelable.Creator<MediumAI> CREATOR = new Parcelable.Creator<MediumAI>() {
		public MediumAI createFromParcel(Parcel in) {
			return new MediumAI(in);
		}
		public MediumAI[] newArray(int size) {
			return new MediumAI[size];
		}
	};

}
