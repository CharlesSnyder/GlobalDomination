package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;
import java.util.Random;

import android.os.Parcelable;
import android.os.Parcel;

public class CardDeck implements Parcelable {

	private ArrayList<Card> m_riskCards;
	
	
	
	/** 
	 * Class constructor.  Creates an array list of size 44 (number of cards in a deck).
	 * @author Charles Snyder
	 */
	public CardDeck() {
		m_riskCards = new ArrayList<Card>();
		final int DECK_SIZE = 44;
		for(int index = 0; index < DECK_SIZE; index++) {
			try {
				m_riskCards.add(new Card(index));
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
	}
	
	
	/** 
	 * Class copy constructor.
	 * @param a_newDeck - CardDeck object to be copied.
	 * @author Charles Snyder
	 */
	public CardDeck(CardDeck a_newDeck) {
		m_riskCards = new ArrayList<Card>(a_newDeck.getCardDeck());
	}
	
	
	/** 
	 * Selector for m_riskCards member variable.
	 * @return tmp - A copy of the m_riskCards array list.
	 * @author Charles Snyder
	 */
	public ArrayList<Card> getCardDeck() {
		ArrayList<Card> tmp = new ArrayList<Card>(m_riskCards);
		return tmp;
	}
	
	
	/** 
	 * Mutator for m_riskCards member variable.
	 * @param a_deck - the array list of cards to be copied.
	 * @author Charles Snyder
	 */
	public void setCardDeck(ArrayList<Card> a_deck) {
		m_riskCards = new ArrayList<Card>(a_deck);
	}
	
	
	/** 
	 * Find the number of cards in the deck.
	 * @return integer value of number of objects in m_riskCards array list.
	 * @author Charles Snyder
	 */
	public final int getDeckSize() {
		return m_riskCards.size();
	}
	
	
	/** 
	 * Rearranges the card objects in the m_riskCards array list in a random ordering.
	 * @author Charles Snyder
	 */
	public void shuffleDeck() {
		Random randomNum = new Random();
		
		for(int index = 0; index < m_riskCards.size(); index++) {
			int randomInt = randomNum.nextInt(getDeckSize());
			Card tmp = new Card((Card) m_riskCards.get(randomInt));
			m_riskCards.set(randomInt, m_riskCards.get(index));
			m_riskCards.set(index, tmp);
		}
	}
	
	
	/** 
	 * Rearranges the card objects in the m_riskCards array list in a random ordering.
	 * @return returnCard - card object pulled from end of m_riskCards array list.
	 * @throws outOfBounds - if no cards are in the deck.
	 * @author Charles Snyder
	 */
	public Card drawCard() throws Exception {
		if(m_riskCards.isEmpty() == false) {
			//Removes card at end of array list.
			Card returnCard = new Card(m_riskCards.remove(m_riskCards.size() - 1));
			return returnCard;
		}
		else {
			Exception outOfBounds = new Exception("No cards in deck");
			throw outOfBounds;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append(m_riskCards);
		return sbuff.toString();
	}
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(m_riskCards);
	}
	
	public static final Parcelable.Creator<CardDeck> CREATOR = new Parcelable.Creator<CardDeck>() {
		public CardDeck createFromParcel(Parcel in) {
			return new CardDeck(in);
		}
		public CardDeck[] newArray(int size) {
			return new CardDeck[size];
		}
	};
	
	private CardDeck(Parcel in) {
		m_riskCards = new ArrayList<Card>();
		in.readTypedList(m_riskCards, Card.CREATOR);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CardDeck test = new CardDeck();
		test.shuffleDeck();
		try {
			test.drawCard();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(test.toString());
	}

}
