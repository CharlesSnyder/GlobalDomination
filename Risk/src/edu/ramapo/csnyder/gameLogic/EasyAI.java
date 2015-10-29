package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import android.os.Parcel;
import android.os.Parcelable;


public class EasyAI extends Cpu {

	public EasyAI() {
		super();
	}
	
	public EasyAI(int a_color) {
		super(a_color);
	}
	
	public EasyAI(EasyAI a_player) {
		super(a_player);
	}
	
	public EasyAI(Parcel in) {
		super(in);
	}
	
	@Override
	public void placeUnitInitial(Board a_gameBoard, int a_country) {
		// Will choose a country at random.
		Random randomNum = new Random();
		int randomCountry = 0;
		
		if(m_armies < 1) {
			return;
		}
		
		// Initialize to arbitrary values.
		int boardSpaceOwner = -2;
		int boardSpaceArmy = 0;
		
		// Keep searching all countries until an empty one is found.
		while(boardSpaceOwner != -1) {
			randomCountry = randomNum.nextInt(42);
			try {
				boardSpaceOwner = a_gameBoard.getBoardSpaceOwner(randomCountry);
				boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(randomCountry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			// Check to see if the country is claimed.
			if(boardSpaceOwner == -1) {
				try {
					a_gameBoard.setBoardSpaceOwner(randomCountry, m_color);
					a_gameBoard.setBoardSpaceArmy(randomCountry, boardSpaceArmy + 1);
					m_armies--;
					m_countryControl.add(randomCountry);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Override
	public void placeUnitAllOccupied(Board a_gameBoard, int a_country) {
		if(m_armies < 1) {
			return;
		}
		
		int boardSpaceArmy = 0;
		
		// Chooses a random country that the cpu controls.
		Random randomNum = new Random();
		int randomCountryIndex = randomNum.nextInt(m_countryControl.size());
		int randomCountry = m_countryControl.get(randomCountryIndex);
		
		try {
			boardSpaceArmy = a_gameBoard.getBoardSpaceArmy(randomCountry);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		try {
			// Place an army at the randomly chosen country.
			a_gameBoard.setBoardSpaceArmy(randomCountry, boardSpaceArmy + 1);
			m_armies--;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void placeUnits(Board a_gameBoard, int a_country, int unitCount) {
		// Choose a random country the cpu owns.
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
		
		// Choose a random amount between 1 and the number of total armies it has.
		int randomUnitCount = rand.nextInt(m_armies) + 1;
		
		try {
			a_gameBoard.setBoardSpaceArmy(randomCountry, boardSpaceArmy + randomUnitCount);
			m_armies = m_armies - randomUnitCount;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void selectCountryCardBonus(ArrayList<Boolean> cardMatchResults, ArrayList<Card> cards, Board gameBoard) {
		Random random = new Random();
		// Check if the computer owns any of the cards
		if(cardMatchResults.get(0) == false && cardMatchResults.get(1) == false && cardMatchResults.get(2) == false) {
			return;
		}
		// If the cpu owns just one of the cards, apply the bonus to the that country.
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
		// If the computer owns two of the cards it chooses where to apply the bonus randomly.
		else if((cardMatchResults.get(0) == true && cardMatchResults.get(1) == true && cardMatchResults.get(2) == false)
				|| (cardMatchResults.get(0) == false && cardMatchResults.get(1) == true && cardMatchResults.get(2) == true)
				|| (cardMatchResults.get(0) == true && cardMatchResults.get(1) == false && cardMatchResults.get(2) == true)) {
			int randomNum = random.nextInt(2);
			m_cardBonus = true;
			m_armies += 2;
			if(cardMatchResults.get(0) == true && cardMatchResults.get(1) == true) {
				int country = cards.get(randomNum).getCardCountry();
				try {
					super.placeUnits(gameBoard, country, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(cardMatchResults.get(1) == true && cardMatchResults.get(2) == true) {
				if(randomNum == 0) {
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
			else {
				if(randomNum == 0) {
					int country = cards.get(0).getCardCountry();
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
		}
		// If the computer owns all three of the cards it chooses where to apply the bonus randomly.
		else {
			int randomNum = random.nextInt(3);
			m_cardBonus = true;
			m_armies += 2;
			int country = cards.get(randomNum).getCardCountry();
			try {
				super.placeUnits(gameBoard, country, 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public ArrayList<Integer> attackCoordinates(Board gameBoard) {
		HashMap<Integer, ArrayList<Integer>> attackMap = new HashMap<Integer, ArrayList<Integer>>(createAttackMap(gameBoard));
		Random rand = new Random();
		// The computer decides if it even wants to attack at random.
		int decideAttack = rand.nextInt(100);
		
		if(attackMap.isEmpty() == true || decideAttack < 20) {
			// If returnArray is empty there is no attack.
			return new ArrayList<Integer>();
		}
		
		// Make an array of key values.
		CopyOnWriteArraySet<Integer> attackOriginSet = new CopyOnWriteArraySet<Integer>(attackMap.keySet());
		Object[] attackOriginArray = attackOriginSet.toArray();
		
		// Pick a random key from the array.
		int randOriginIndex = rand.nextInt(attackOriginArray.length);
		int originCountry = (int) attackOriginArray[randOriginIndex];
		
		// Get the associated values of that key.
		ArrayList<Integer> attackLocations = new ArrayList<Integer>(attackMap.get(originCountry));
		
		
		// Pick a random value to attack.
		int randAttackIndex = rand.nextInt(attackLocations.size());
		int attackCountry = attackLocations.get(randAttackIndex);
		
		ArrayList<Integer> returnArray = new ArrayList<Integer>();
		returnArray.add(originCountry);
		returnArray.add(attackCountry);
		
		return returnArray;
	}
	
	public final int selectNumDiceAttack(Board gameBoard, int originCountry) {
		int numDice = 0;
		int armySize = 0;
		// Selects a random number of dice to throw.
		Random rand = new Random();
		try {
			armySize = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// In order to throw three dice must have at least 4 armies.
		if(armySize > 3) {
			numDice = rand.nextInt(3) + 1;
		}
		// In order to throw two dice must have at least 3 armies.
		else if(armySize == 3) {
			numDice = rand.nextInt(2) + 1;
		}
		// In order to throw one die must have at least 2 armies.
		else if(armySize == 2) {
			numDice = 1;
		}
		else {
			numDice = -1;
		}
		return numDice;
	}
	
	public final int selectNumDiceDefend(Board gameBoard, int defendCountry) {
		int numDice= 0;
		int armySize = 0;
		// Selects a random number of dice to throw.
		Random rand = new Random();
		try {
			armySize = gameBoard.getBoardSpaceArmy(defendCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// In order to throw two dice must have at least 2 armies.
		if(armySize >= 2) {
			numDice = rand.nextInt(2) + 1;
		}
		// Only has one army.
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
		Random rand = new Random();
		int numArmyMove;
		// Randomly choose a value from the attack army size that allows at least one army to remain back and is at least equal to the number of dice thrown
		do {
			numArmyMove = rand.nextInt(attackArmySize) + 1;
		} while(numArmyMove < numDiceRolled || (numArmyMove - attackArmySize) == 0);
		
		try {
			gameBoard.setBoardSpaceArmy(originCountry, attackArmySize - numArmyMove);
			gameBoard.setBoardSpaceArmy(destCountry, numArmyMove);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void fortifyPosition(Board gameBoard) {
		Random rand = new Random();
		// Randomly decide whether to fortify any territory.
		int selection = rand.nextInt(100);
		
		HashMap<Integer, ArrayList<Integer>> fortifyMap = new HashMap<Integer, ArrayList<Integer>>(createFortifyMap(gameBoard));
		
		if(selection < 50 || fortifyMap.isEmpty() == true) {
			return;
		}
		else {
			// Make array of keys.
			CopyOnWriteArraySet<Integer> fortifyOriginSet = new CopyOnWriteArraySet<Integer>(fortifyMap.keySet());
			Object[] attackOriginArray = fortifyOriginSet.toArray();
			
			// Randomly choose a key from the array.
			int randOriginIndex = rand.nextInt(attackOriginArray.length);
			int originCountry = (int) attackOriginArray[randOriginIndex];
			
			// Get associated values from that key.
			ArrayList<Integer> fortifyLocations = new ArrayList<Integer>(fortifyMap.get(originCountry));
			
			
			// Randomly choose a value.
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
		int numOriginArmy = 0;
		try {
			numOriginArmy = gameBoard.getBoardSpaceArmy(originCountry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Random rand = new Random();
		int numMove;
		// Randomly choose a number of armies to move that leaves at least one army in original territory.
		do {
			numMove = rand.nextInt(numOriginArmy) + 1;
		} while((numMove - numOriginArmy) == 0);
		
		return numMove;
	}
	

	

	
	public static final Parcelable.Creator<EasyAI> CREATOR = new Parcelable.Creator<EasyAI>() {
		public EasyAI createFromParcel(Parcel in) {
			return new EasyAI(in);
		}
		public EasyAI[] newArray(int size) {
			return new EasyAI[size];
		}
	};
	

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		EasyAI myPlayer = new EasyAI();
		Board gameBoard = new Board();
		
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		myPlayer.createFortifyMap(gameBoard);
		
	}

}
