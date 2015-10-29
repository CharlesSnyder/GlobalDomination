package edu.ramapo.csnyder.gameLogic;

import android.os.Parcelable;
import android.os.Parcel;

public class Card implements Parcelable {

	
	private int m_army;
	private int m_country;
	
	
	/** 
	 * Class constructor.
	 * @author Charles Snyder
	 */
	public Card() {
		m_army = 0;
		m_country = 0;
	}
	
	
	/** 
	 * Class copy constructor.
	 * @param a_card - Card object to be copied
	 * @author Charles Snyder
	 */
	public Card(Card a_card) {
		m_army = a_card.getCardArmy();
		m_country = a_card.getCardCountry();
	}
	
	
	/** 
	 * Class constructor with country specified.
	 * @param a_country - the country value for the card.
	 * @throws invalidValue - if a_country is less than 0 or greater than 43, these are the minimum and maximum number of cards for the deck.
	 * @author Charles Snyder
	 */
	public Card(int a_country) throws Exception {
		final int MAX_CARD = 43;
		final int MIN_CARD = 0;
		if(a_country < MIN_CARD || a_country > MAX_CARD) {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
		else {
			m_country = a_country;
		}
		m_army = armySizeToCountry(a_country);
	}
	
	
	/** 
	 * Selector for m_army member variable.
	 * @return integer value of how many armies on a card.
	 * @author Charles Snyder
	 */
	public int getCardArmy() {
		return m_army;
	}
	
	
	/** 
	 * Selector for m_country member variable.
	 * @return integer value representing a particular country on a card.
	 * @author Charles Snyder
	 */
	public int getCardCountry() {
		return m_country;
	}
	
	
	
	/** 
	 * Returns a value based on the army value that corresponds to the country on a particular card.
	 * @param a_country - an integer value representing a certain country on a card. 
	 * @return An integer value representing the army count on a certain card.
	 * @author Charles Snyder
	 */
	public static int armySizeToCountry(int a_country) {
		switch(a_country) {
		case Board.AFGHANISTAN: return 1;
		case Board.ALASKA: return 10;
		case Board.ALBERTA: return 10;
		case Board.ARGENTINA: return 1;
		case Board.BRAZIL: return 1;
		case Board.CENTRAL_AMERICA: return 1;
		case Board.CHINA: return 10;
		case Board.CONGO: return 10;
		case Board.EAST_AFRICA: return 1;
		case Board.EASTERN_AUST:return 5;
		case Board.EASTERN_US: return 10;
		case Board.EGYPT: return 5;
		case Board.GREAT_BRITAIN: return 1;
		case Board.GREENLAND: return 5;
		case Board.ICELAND: return 5;
		case Board.INDIA: return 5;
		case Board.INDONESIA: return 1;
		case Board.IRKUTSK: return 10;
		case Board.JAPAN: return 5;
		case Board.KAMCHATKA: return 10;
		case Board.MADAGASCAR: return 5;
		case Board.MIDDLE_EAST: return 10;
		case Board.MONGOLIA: return 5;
		case Board.NEW_GUINEA: return 5;
		case Board.NORTH_AFRICA: return 1;
		case Board.NORTHERN_EUROPE: return 5;
		case Board.NORTHWEST_TERR: return 5;
		case Board.ONTARIO: return 10;
		case Board.PERU: return 5;
		case Board.QUEBEC: return 10;
		case Board.SCANDINAVIA: return 1;
		case Board.SIBERIA: return 1;
		case Board.SOUTH_AFRICA: return 10;
		case Board.SOUTHEAST_ASIA: return 5;
		case Board.SOUTHERN_EUROPE: return 1;
		case Board.UKRAINE:return 1;
		case Board.URAL: return 1;
		case Board.VENEZUELA: return 5;
		case Board.WESTERN_AUST: return 10;
		case Board.WESTERN_EUROPE: return 1;
		case Board.WESTERN_US: return 10;
		case Board.YAKUTSK: return 10;
		default:
			return 0;
		}
	}
	
	
	/** 
	 * Determines whether a set of three cards are considered a match according to game rules.
	 * @param a_cardOne - a card object of the first card to be tested.
	 * @param a_cardTwo - a card object of the second card to be tested.
	 * @param a_cardThree - a card object of the third card to be tested.
	 * @return True if the cards are considered a match, false otherwise.
	 * @author Charles Snyder
	 */
	public static boolean areCardsMatch(Card a_cardOne, Card a_cardTwo, Card a_cardThree) {
		final int WILDCARDONE = 42;
		final int WILDCARDTWO = 43;
		if(a_cardOne.getCardArmy() == a_cardTwo.getCardArmy() 
			&& a_cardOne.getCardArmy()  == a_cardThree.getCardArmy()
			&& a_cardTwo.getCardArmy()  == a_cardThree.getCardArmy()) {
			return true;
		}
		else if(a_cardOne.getCardArmy() != a_cardTwo.getCardArmy()
			&& a_cardOne.getCardArmy() != a_cardThree.getCardArmy()
			&& a_cardTwo.getCardArmy() != a_cardThree.getCardArmy()) {
			return true;
		}
		else if(a_cardOne.getCardCountry() == WILDCARDONE || a_cardOne.getCardCountry() == WILDCARDTWO) {
			if(a_cardTwo.getCardArmy() != a_cardThree.getCardArmy()) {
				return true;
			}
		}
		else if(a_cardTwo.getCardCountry() == WILDCARDONE || a_cardTwo.getCardCountry() == WILDCARDTWO) {
			if(a_cardOne.getCardArmy() != a_cardThree.getCardArmy()) {
				return true;
			}
		}
		else if(a_cardThree.getCardCountry() == WILDCARDONE || a_cardThree.getCardCountry() == WILDCARDTWO) {
			if(a_cardOne.getCardArmy() != a_cardTwo.getCardArmy()) {
				return true;
			}
		}
		return false;
	}
	
	

	@Override
	public boolean equals(Object a_other) {
		if(!(a_other instanceof Card)) {
			return false;
		}
		Card otherCard = (Card)a_other;
		
		return m_army == otherCard.getCardArmy() && m_country == otherCard.getCardCountry();
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("(" + m_army + ", " + m_country + ")");
		return returnString.toString();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Card x = null;
		Card y = null;
		Card z = null;
		try {
			x = new Card(0);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		try {
			y = new Card(0);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		try {
			z = new Card(1);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		if(x.equals(y)) {
			System.out.println("stuff");
		}
		if(x.equals(z)) {
			System.out.println("should not print");
		}
		
		System.out.println(x.toString());

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(m_army);
		dest.writeInt(m_country);	
	}
	
	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
		public Card createFromParcel(Parcel in) {
			return new Card(in);
		}
		public Card[] newArray(int size) {
			return new Card[size];
		}
	};
	
	private Card(Parcel in) {
		m_army = in.readInt();
		m_country = in.readInt();
	}

}
