package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;



public class Board implements Parcelable {
	
	//Countries and their associated integer values
	public final static int ALASKA = 0;
	public final static int ALBERTA = 1;
	public final static int ONTARIO = 2;
	public final static int NORTHWEST_TERR = 3;
	public final static int GREENLAND = 4;
	public final static int QUEBEC = 5;
	public final static int WESTERN_US = 6;
	public final static int EASTERN_US = 7;
	public final static int CENTRAL_AMERICA = 8;
	public final static int VENEZUELA = 9;
	public final static int PERU = 10;
	public final static int BRAZIL = 11;
	public final static int ARGENTINA = 12;
	public final static int ICELAND = 13;
	public final static int GREAT_BRITAIN = 14;
	public final static int SCANDINAVIA = 15;
	public final static int NORTHERN_EUROPE = 16;
	public final static int WESTERN_EUROPE = 17;
	public final static int SOUTHERN_EUROPE = 18;
	public final static int UKRAINE = 19;
	public final static int NORTH_AFRICA = 20;
	public final static int EGYPT = 21;
	public final static int EAST_AFRICA = 22;
	public final static int CONGO = 23;
	public final static int SOUTH_AFRICA = 24;
	public final static int MADAGASCAR = 25;
	public final static int MIDDLE_EAST = 26;
	public final static int AFGHANISTAN = 27;
	public final static int URAL = 28;
	public final static int SIBERIA = 29;
	public final static int INDIA = 30;
	public final static int CHINA = 31;
	public final static int SOUTHEAST_ASIA = 32;
	public final static int MONGOLIA = 33;
	public final static int IRKUTSK = 34;
	public final static int YAKUTSK = 35;
	public final static int KAMCHATKA = 36;
	public final static int JAPAN = 37;
	public final static int INDONESIA = 38;
	public final static int NEW_GUINEA = 39;
	public final static int WESTERN_AUST = 40;
	public final static int EASTERN_AUST = 41;
	
	private ArrayList<CountrySpace> m_boardSpaces;
	
	
	/** 
	 * Class constructor.
	 * @author Charles Snyder
	 */
	public Board() {
		final int BOARD_SIZE = 42;
		m_boardSpaces = new ArrayList<CountrySpace>();
		for(int index = 0; index < BOARD_SIZE; index++) {
			m_boardSpaces.add(index, new CountrySpace(index));
		}
	}
	
	/** 
	 * Class copy constructor.
	 * @param newBoard - Board object to be copied.
	 * @author Charles Snyder
	 */
	public Board(Board newBoard) {
		m_boardSpaces = new ArrayList<CountrySpace>(newBoard.getBoardSpaces());
	}
	
	
	/** 
	 * Selector for m_boardSpaces member variable.
	 * @return copyBoard - An array list copy of the board spaces member variable
	 * @author Charles Snyder
	 */
	public ArrayList<CountrySpace> getBoardSpaces() {
		ArrayList<CountrySpace> copyBoard = new ArrayList<CountrySpace>(m_boardSpaces);
		return copyBoard;
	}
	
	/** 
	 * Mutator for m_boardSpaces member variable.
	 * @param a_board - a new array list of board spaces that will be copied to m_boardSpaces.
	 * @author Charles Snyder
	 */
	public void setBoardSpaces(ArrayList<CountrySpace> a_board) {
		m_boardSpaces = new ArrayList<CountrySpace>(a_board);
	}
	
	/** 
	 * Get the army total of a specified country.
	 * @param a_country - an integer value that represents the country to query.
	 * @throws outOfRange - if a_country is not within the accepted values of 0 and 41.
	 * @author Charles Snyder
	 */
	public final int getBoardSpaceArmy(int a_country) throws Exception {
		switch(a_country) {
		case ALASKA: return getAlaskaArmy();
		case ALBERTA: return getAlbertaArmy();
		case ONTARIO: return getOntarioArmy();
		case NORTHWEST_TERR: return getNorthwestTerrArmy();
		case GREENLAND: return getGreenlandArmy();
		case QUEBEC: return getQuebecArmy();
		case WESTERN_US: return getWesternUSArmy();
		case EASTERN_US: return getEasternUSArmy();
		case CENTRAL_AMERICA: return getCentralAmericaArmy();
		case VENEZUELA: return getVenezuelaArmy();
		case PERU: return getPeruArmy();
		case BRAZIL: return getBrazilArmy();
		case ARGENTINA: return getArgentinaArmy();
		case ICELAND: return getIcelandArmy();
		case GREAT_BRITAIN: return getGreatBritainArmy();
		case SCANDINAVIA: return getScandinaviaArmy();
		case NORTHERN_EUROPE: return getNorthernEuropeArmy();
		case WESTERN_EUROPE: return getWesternEuropeArmy();
		case SOUTHERN_EUROPE: return getSouthernEuropeArmy();
		case UKRAINE: return getUkraineArmy();
		case NORTH_AFRICA: return getNorthAfricaArmy();
		case EGYPT: return getEgyptArmy();
		case EAST_AFRICA: return getEastAfricaArmy();
		case CONGO: return getCongoArmy();
		case SOUTH_AFRICA: return getSouthAfricaArmy();
		case MADAGASCAR: return getMadagascarArmy();
		case MIDDLE_EAST: return getMiddleEastArmy();
		case AFGHANISTAN: return getAfghanistanArmy();
		case URAL: return getUralArmy();
		case SIBERIA: return getSiberiaArmy();
		case INDIA: return getIndiaArmy();
		case CHINA: return getChinaArmy();
		case SOUTHEAST_ASIA: return getSoutheastAsiaArmy();
		case MONGOLIA: return getMongoliaArmy();
		case IRKUTSK: return getIrkutskArmy();
		case YAKUTSK: return getYakutskArmy();
		case KAMCHATKA: return getKamchatkaArmy();
		case JAPAN: return getJapanArmy();
		case INDONESIA: return getIndonesiaArmy();
		case NEW_GUINEA: return getNewGuineaArmy();
		case WESTERN_AUST: return getWesternAustArmy();
		case EASTERN_AUST: return getEasternAustArmy();
		
		default:
			Exception outOfRange = new Exception("Invalid country");
			throw outOfRange;
		}
	}
	
	/** 
	 * Get the owner, by color as an integer, of a specified country.
	 * @param a_country - an integer value that represents the country to query.
	 * @throws outOfRange - if a_country is not within the accepted values of 0 and 41.
	 * @author Charles Snyder
	 */
	public final int getBoardSpaceOwner(int a_country) throws Exception {
		switch(a_country) {
		case ALASKA: return getAlaskaOwner();
		case ALBERTA: return getAlbertaOwner();
		case ONTARIO: return getOntarioOwner();
		case NORTHWEST_TERR: return getNorthwestTerrOwner();
		case GREENLAND: return getGreenlandOwner();
		case QUEBEC: return getQuebecOwner();
		case WESTERN_US: return getWesternUSOwner();
		case EASTERN_US: return getEasternUSOwner();
		case CENTRAL_AMERICA: return getCentralAmericaOwner();
		case VENEZUELA: return getVenezuelaOwner();
		case PERU: return getPeruOwner();
		case BRAZIL: return getBrazilOwner();
		case ARGENTINA: return getArgentinaOwner();
		case ICELAND: return getIcelandOwner();
		case GREAT_BRITAIN: return getGreatBritainOwner();
		case SCANDINAVIA: return getScandinaviaOwner();
		case NORTHERN_EUROPE: return getNorthernEuropeOwner();
		case WESTERN_EUROPE: return getWesternEuropeOwner();
		case SOUTHERN_EUROPE: return getSouthernEuropeOwner();
		case UKRAINE: return getUkraineOwner();
		case NORTH_AFRICA: return getNorthAfricaOwner();
		case EGYPT: return getEgyptOwner();
		case EAST_AFRICA: return getEastAfricaOwner();
		case CONGO: return getCongoOwner();
		case SOUTH_AFRICA: return getSouthAfricaOwner();
		case MADAGASCAR: return getMadagascarOwner();
		case MIDDLE_EAST: return getMiddleEastOwner();
		case AFGHANISTAN: return getAfghanistanOwner();
		case URAL: return getUralOwner();
		case SIBERIA: return getSiberiaOwner();
		case INDIA: return getIndiaOwner();
		case CHINA: return getChinaOwner();
		case SOUTHEAST_ASIA: return getSoutheastAsiaOwner();
		case MONGOLIA: return getMongoliaOwner();
		case IRKUTSK: return getIrkutskOwner();
		case YAKUTSK: return getYakutskOwner();
		case KAMCHATKA: return getKamchatkaOwner();
		case JAPAN: return getJapanOwner();
		case INDONESIA: return getIndonesiaOwner();
		case NEW_GUINEA: return getNewGuineaOwner();
		case WESTERN_AUST: return getWesternAustOwner();
		case EASTERN_AUST: return getEasternAustOwner();
		
		default:
			Exception outOfRange = new Exception("Invalid country");
			throw outOfRange;
		}
	}
	
	/** 
	 * Gets the army value of Alaska.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getAlaskaArmy() {
		return m_boardSpaces.get(ALASKA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Alberta.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getAlbertaArmy() {
		return m_boardSpaces.get(ALBERTA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Ontario.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getOntarioArmy() {
		return m_boardSpaces.get(ONTARIO).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Northwest Territory.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getNorthwestTerrArmy() {
		return m_boardSpaces.get(NORTHWEST_TERR).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Greenland.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getGreenlandArmy() {
		return m_boardSpaces.get(GREENLAND).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Quebec.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getQuebecArmy() {
		return m_boardSpaces.get(QUEBEC).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Western United States.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getWesternUSArmy() {
		return m_boardSpaces.get(WESTERN_US).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of Eastern United States.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getEasternUSArmy() {
		return m_boardSpaces.get(EASTERN_US).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of Central America.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getCentralAmericaArmy() {
		return m_boardSpaces.get(CENTRAL_AMERICA).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of Venezuela.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getVenezuelaArmy() {
		return m_boardSpaces.get(VENEZUELA).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of Peru.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getPeruArmy() {
		return m_boardSpaces.get(PERU).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Brazil.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getBrazilArmy() {
		return m_boardSpaces.get(BRAZIL).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Argentina.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getArgentinaArmy() {
		return m_boardSpaces.get(ARGENTINA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Iceland.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getIcelandArmy() {
		return m_boardSpaces.get(ICELAND).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Great Britain.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getGreatBritainArmy() {
		return m_boardSpaces.get(GREAT_BRITAIN).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Scandinavia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getScandinaviaArmy() {
		return m_boardSpaces.get(SCANDINAVIA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Northern Europe.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getNorthernEuropeArmy() {
		return m_boardSpaces.get(NORTHERN_EUROPE).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Western Europe.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getWesternEuropeArmy() {
		return m_boardSpaces.get(WESTERN_EUROPE).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Southern Europe.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getSouthernEuropeArmy() {
		return m_boardSpaces.get(SOUTHERN_EUROPE).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Ukraine.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getUkraineArmy() {
		return m_boardSpaces.get(UKRAINE).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of North Africa.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getNorthAfricaArmy() {
		return m_boardSpaces.get(NORTH_AFRICA).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of Egypt.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getEgyptArmy() {
		return m_boardSpaces.get(EGYPT).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of East Africa.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getEastAfricaArmy() {
		return m_boardSpaces.get(EAST_AFRICA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Congo.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getCongoArmy() {
		return m_boardSpaces.get(CONGO).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of South Africa.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getSouthAfricaArmy() {
		return m_boardSpaces.get(SOUTH_AFRICA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Madagascar.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getMadagascarArmy() {
		return m_boardSpaces.get(MADAGASCAR).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Middle East.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getMiddleEastArmy() {
		return m_boardSpaces.get(MIDDLE_EAST).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Afghanistan.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getAfghanistanArmy() {
		return m_boardSpaces.get(AFGHANISTAN).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Ural.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getUralArmy() {
		return m_boardSpaces.get(URAL).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Siberia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getSiberiaArmy() {
		return m_boardSpaces.get(SIBERIA).getCountryArmyCount();
	}
	
	/** 
	 * Gets the army value of India.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getIndiaArmy() {
		return m_boardSpaces.get(INDIA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of China.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getChinaArmy() {
		return m_boardSpaces.get(CHINA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Southeast Asia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getSoutheastAsiaArmy() {
		return m_boardSpaces.get(SOUTHEAST_ASIA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Mongolia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getMongoliaArmy() {
		return m_boardSpaces.get(MONGOLIA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Irkutsk.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getIrkutskArmy() {
		return m_boardSpaces.get(IRKUTSK).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Yakutsk.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getYakutskArmy() {
		return m_boardSpaces.get(YAKUTSK).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Kamchatka.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getKamchatkaArmy() {
		return m_boardSpaces.get(KAMCHATKA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Japan.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getJapanArmy() {
		return m_boardSpaces.get(JAPAN).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Indonesia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getIndonesiaArmy() {
		return m_boardSpaces.get(INDONESIA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of New Guinea.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getNewGuineaArmy() {
		return m_boardSpaces.get(NEW_GUINEA).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Western Australia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getWesternAustArmy() {
		return m_boardSpaces.get(WESTERN_AUST).getCountryArmyCount();
	}
	
	
	/** 
	 * Gets the army value of Eastern Australia.
	 * @return integer representing army total in the country.
	 * @author Charles Snyder
	 */
	public int getEasternAustArmy() {
		return m_boardSpaces.get(EASTERN_AUST).getCountryArmyCount();
	}
	
	//
	//
	//
	//
	//
	
	
	/** 
	 * Gets the owner color of Alaska.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getAlaskaOwner() {
		return m_boardSpaces.get(ALASKA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Alberta.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getAlbertaOwner() {
		return m_boardSpaces.get(ALBERTA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Ontario.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getOntarioOwner() {
		return m_boardSpaces.get(ONTARIO).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Northwest Territory.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getNorthwestTerrOwner() {
		return m_boardSpaces.get(NORTHWEST_TERR).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Greenland.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getGreenlandOwner() {
		return m_boardSpaces.get(GREENLAND).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Quebec.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getQuebecOwner() {
		return m_boardSpaces.get(QUEBEC).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Western United States.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getWesternUSOwner() {
		return m_boardSpaces.get(WESTERN_US).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Eastern United States.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getEasternUSOwner() {
		return m_boardSpaces.get(EASTERN_US).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Central America.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getCentralAmericaOwner() {
		return m_boardSpaces.get(CENTRAL_AMERICA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Venezuela.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getVenezuelaOwner() {
		return m_boardSpaces.get(VENEZUELA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Peru.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getPeruOwner() {
		return m_boardSpaces.get(PERU).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Brazil.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getBrazilOwner() {
		return m_boardSpaces.get(BRAZIL).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Argentina.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getArgentinaOwner() {
		return m_boardSpaces.get(ARGENTINA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Iceland.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getIcelandOwner() {
		return m_boardSpaces.get(ICELAND).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Great Britain.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getGreatBritainOwner() {
		return m_boardSpaces.get(GREAT_BRITAIN).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Scandinavia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getScandinaviaOwner() {
		return m_boardSpaces.get(SCANDINAVIA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Northern Europe.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getNorthernEuropeOwner() {
		return m_boardSpaces.get(NORTHERN_EUROPE).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Western Europe.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getWesternEuropeOwner() {
		return m_boardSpaces.get(WESTERN_EUROPE).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Southern Europe.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getSouthernEuropeOwner() {
		return m_boardSpaces.get(SOUTHERN_EUROPE).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Ukraine.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getUkraineOwner() {
		return m_boardSpaces.get(UKRAINE).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of North Africa.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getNorthAfricaOwner() {
		return m_boardSpaces.get(NORTH_AFRICA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Egypt.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getEgyptOwner() {
		return m_boardSpaces.get(EGYPT).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of East Africa.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getEastAfricaOwner() {
		return m_boardSpaces.get(EAST_AFRICA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Congo.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getCongoOwner() {
		return m_boardSpaces.get(CONGO).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of South Africa.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getSouthAfricaOwner() {
		return m_boardSpaces.get(SOUTH_AFRICA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Madagascar.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getMadagascarOwner() {
		return m_boardSpaces.get(MADAGASCAR).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Middle East.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getMiddleEastOwner() {
		return m_boardSpaces.get(MIDDLE_EAST).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Afghanistan.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getAfghanistanOwner() {
		return m_boardSpaces.get(AFGHANISTAN).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Ural.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getUralOwner() {
		return m_boardSpaces.get(URAL).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Siberia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getSiberiaOwner() {
		return m_boardSpaces.get(SIBERIA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of India.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getIndiaOwner() {
		return m_boardSpaces.get(INDIA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of China.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getChinaOwner() {
		return m_boardSpaces.get(CHINA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Southeast Asia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getSoutheastAsiaOwner() {
		return m_boardSpaces.get(SOUTHEAST_ASIA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Mongolia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getMongoliaOwner() {
		return m_boardSpaces.get(MONGOLIA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Irkutsk.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getIrkutskOwner() {
		return m_boardSpaces.get(IRKUTSK).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Yakutsk.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getYakutskOwner() {
		return m_boardSpaces.get(YAKUTSK).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Kamchatka.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getKamchatkaOwner() {
		return m_boardSpaces.get(KAMCHATKA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Japan.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getJapanOwner() {
		return m_boardSpaces.get(JAPAN).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Indonesia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getIndonesiaOwner() {
		return m_boardSpaces.get(INDONESIA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of New Guinea.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getNewGuineaOwner() {
		return m_boardSpaces.get(NEW_GUINEA).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Western Australia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getWesternAustOwner() {
		return m_boardSpaces.get(WESTERN_AUST).getCountryOwner();
	}
	
	
	/** 
	 * Gets the owner color of Eastern Australia.
	 * @return integer representing owner color of the country.
	 * @author Charles Snyder
	 */
	public int getEasternAustOwner() {
		return m_boardSpaces.get(EASTERN_AUST).getCountryOwner();
	}
	
	//
	//
	//
	//
	//
	
	
	
	/** 
	 * Set the army total of a specified country.
	 * @param a_country - an integer value that represents the country to set.
	 * @param a_armyCount - value to set the country spaces army count to.
	 * @throws outOfRange - if a_country is not within the accepted values of 0 and 41.
	 * @author Charles Snyder
	 */
	public void setBoardSpaceArmy(int a_country, int a_armyCount) throws Exception {
		switch(a_country) {
		case ALASKA: 
			setAlaskaArmy(a_armyCount);
			return;
		case ALBERTA: 
			setAlbertaArmy(a_armyCount);
			return;
		case ONTARIO: 
			setOntarioArmy(a_armyCount);
			return;
		case NORTHWEST_TERR: 
			setNorthwestTerrArmy(a_armyCount);
			return;
		case GREENLAND: 
			setGreenlandArmy(a_armyCount);
			return;
		case QUEBEC: 
			setQuebecArmy(a_armyCount);
			return;
		case WESTERN_US: 
			setWesternUSArmy(a_armyCount);
			return;
		case EASTERN_US: 
			setEasternUSArmy(a_armyCount);
			return;
		case CENTRAL_AMERICA: 
			setCentralAmericaArmy(a_armyCount);
			return;
		case VENEZUELA: 
			setVenezuelaArmy(a_armyCount);
			return;
		case PERU: 
			setPeruArmy(a_armyCount);
			return;
		case BRAZIL: 
			setBrazilArmy(a_armyCount);
			return;
		case ARGENTINA: 
			setArgentinaArmy(a_armyCount);
			return;
		case ICELAND: 
			setIcelandArmy(a_armyCount);
			return;
		case GREAT_BRITAIN: 
			setGreatBritainArmy(a_armyCount);
			return;
		case SCANDINAVIA: 
			setScandinaviaArmy(a_armyCount);
			return;
		case NORTHERN_EUROPE: 
			setNorthernEuropeArmy(a_armyCount);
			return;
		case WESTERN_EUROPE: 
			setWesternEuropeArmy(a_armyCount);
			return;
		case SOUTHERN_EUROPE: 
			setSouthernEuropeArmy(a_armyCount);
			return;
		case UKRAINE: 
			setUkraineArmy(a_armyCount);
			return;
		case NORTH_AFRICA: 
			setNorthAfricaArmy(a_armyCount);
			return;
		case EGYPT: 
			setEgyptArmy(a_armyCount);
			return;
		case EAST_AFRICA:
			setEastAfricaArmy(a_armyCount);
			return;
		case CONGO: 
			setCongoArmy(a_armyCount);
			return;
		case SOUTH_AFRICA: 
			setSouthAfricaArmy(a_armyCount);
			return;
		case MADAGASCAR: 
			setMadagascarArmy(a_armyCount);
			return;
		case MIDDLE_EAST: 
			setMiddleEastArmy(a_armyCount);
			return;
		case AFGHANISTAN: 
			setAfghanistanArmy(a_armyCount);
			return;
		case URAL: 
			setUralArmy(a_armyCount);
			return;
		case SIBERIA: 
			setSiberiaArmy(a_armyCount);
			return;
		case INDIA: 
			setIndiaArmy(a_armyCount);
			return;
		case CHINA : 
			setChinaArmy(a_armyCount);
			return;
		case SOUTHEAST_ASIA: 
			setSoutheastAsiaArmy(a_armyCount);
			return;
		case MONGOLIA: 
			setMongoliaArmy(a_armyCount);
			return;
		case IRKUTSK: 
			setIrkutskArmy(a_armyCount);
			return;
		case YAKUTSK: 
			setYakutskArmy(a_armyCount);
			return;
		case KAMCHATKA:
			setKamchatkaArmy(a_armyCount);
			return;
		case JAPAN: 
			setJapanArmy(a_armyCount);
			return;
		case INDONESIA: 
			setIndonesiaArmy(a_armyCount);
			return;
		case NEW_GUINEA: 
			setNewGuineaArmy(a_armyCount);
			return;
		case WESTERN_AUST: 
			setWesternAustArmy(a_armyCount);
			return;
		case EASTERN_AUST: 
			setEasternAustArmy(a_armyCount);
			return;
		
		default:
			Exception outOfRange = new Exception("Invalid country");
			throw outOfRange;
		}
	}
	
	
	
	/** 
	 * Set the owner color of a specified country.
	 * @param a_country - an integer value that represents the country to set.
	 * @param a_armyCount - value to set the country spaces owner color to.
	 * @throws outOfRange - if a_country is not within the accepted values of 0 and 41.
	 * @author Charles Snyder
	 */
	public void setBoardSpaceOwner(int a_country, int a_color) throws Exception {
		switch(a_country) {
		case ALASKA: 
			setAlaskaOwner(a_color);
			return;
		case ALBERTA: 
			setAlbertaOwner(a_color);
			return;
		case ONTARIO: 
			setOntarioOwner(a_color);
			return;
		case NORTHWEST_TERR: 
			setNorthwestTerrOwner(a_color);
			return;
		case GREENLAND: 
			setGreenlandOwner(a_color);
			return;
		case QUEBEC: 
			setQuebecOwner(a_color);
			return;
		case WESTERN_US: 
			setWesternUSOwner(a_color);
			return;
		case EASTERN_US: 
			setEasternUSOwner(a_color);
			return;
		case CENTRAL_AMERICA: 
			setCentralAmericaOwner(a_color);
			return;
		case VENEZUELA: 
			setVenezuelaOwner(a_color);
			return;
		case PERU: 
			setPeruOwner(a_color);
			return;
		case BRAZIL: 
			setBrazilOwner(a_color);
			return;
		case ARGENTINA: 
			setArgentinaOwner(a_color);
			return;
		case ICELAND: 
			setIcelandOwner(a_color);
			return;
		case GREAT_BRITAIN: 
			setGreatBritainOwner(a_color);
			return;
		case SCANDINAVIA: 
			setScandinaviaOwner(a_color);
			return;
		case NORTHERN_EUROPE: 
			setNorthernEuropeOwner(a_color);
			return;
		case WESTERN_EUROPE: 
			setWesternEuropeOwner(a_color);
			return;
		case SOUTHERN_EUROPE: 
			setSouthernEuropeOwner(a_color);
			return;
		case UKRAINE: 
			setUkraineOwner(a_color);
			return;
		case NORTH_AFRICA: 
			setNorthAfricaOwner(a_color);
			return;
		case EGYPT: 
			setEgyptOwner(a_color);
			return;
		case EAST_AFRICA:
			setEastAfricaOwner(a_color);
			return;
		case CONGO: 
			setCongoOwner(a_color);
			return;
		case SOUTH_AFRICA: 
			setSouthAfricaOwner(a_color);
			return;
		case MADAGASCAR: 
			setMadagascarOwner(a_color);
			return;
		case MIDDLE_EAST: 
			setMiddleEastOwner(a_color);
			return;
		case AFGHANISTAN: 
			setAfghanistanOwner(a_color);
			return;
		case URAL: 
			setUralOwner(a_color);
			return;
		case SIBERIA: 
			setSiberiaOwner(a_color);
			return;
		case INDIA: 
			setIndiaOwner(a_color);
			return;
		case CHINA : 
			setChinaOwner(a_color);
			return;
		case SOUTHEAST_ASIA: 
			setSoutheastAsiaOwner(a_color);
			return;
		case MONGOLIA: 
			setMongoliaOwner(a_color);
			return;
		case IRKUTSK: 
			setIrkutskOwner(a_color);
			return;
		case YAKUTSK: 
			setYakutskOwner(a_color);
			return;
		case KAMCHATKA:
			setKamchatkaOwner(a_color);
			return;
		case JAPAN: 
			setJapanOwner(a_color);
			return;
		case INDONESIA: 
			setIndonesiaOwner(a_color);
			return;
		case NEW_GUINEA: 
			setNewGuineaOwner(a_color);
			return;
		case WESTERN_AUST: 
			setWesternAustOwner(a_color);
			return;
		case EASTERN_AUST: 
			setEasternAustOwner(a_color);
			return;
		
		default:
			Exception outOfRange = new Exception("Invalid country");
			throw outOfRange;
		}
	}
	
	
	/** 
	 * Set the army count of a Alaska.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setAlaskaArmy(int a_value) throws Exception {
		m_boardSpaces.get(ALASKA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Alberta.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setAlbertaArmy(int a_value) throws Exception {
		m_boardSpaces.get(ALBERTA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Ontario.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setOntarioArmy(int a_value) throws Exception {
		m_boardSpaces.get(ONTARIO).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Northwest Territory.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setNorthwestTerrArmy(int a_value) throws Exception {
		m_boardSpaces.get(NORTHWEST_TERR).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Greenland.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setGreenlandArmy(int a_value) throws Exception {
		m_boardSpaces.get(GREENLAND).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Quebec.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setQuebecArmy(int a_value) throws Exception {
		m_boardSpaces.get(QUEBEC).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Western United States.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setWesternUSArmy(int a_value) throws Exception {
		m_boardSpaces.get(WESTERN_US).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Eastern United States.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setEasternUSArmy(int a_value) throws Exception {
		m_boardSpaces.get(EASTERN_US).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Central America.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setCentralAmericaArmy(int a_value) throws Exception {
		m_boardSpaces.get(CENTRAL_AMERICA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Venezuela.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setVenezuelaArmy(int a_value) throws Exception {
		m_boardSpaces.get(VENEZUELA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Peru.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setPeruArmy(int a_value) throws Exception {
		m_boardSpaces.get(PERU).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Brazil.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setBrazilArmy(int a_value) throws Exception {
		m_boardSpaces.get(BRAZIL).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Argentina.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setArgentinaArmy(int a_value) throws Exception {
		m_boardSpaces.get(ARGENTINA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Iceland.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setIcelandArmy(int a_value) throws Exception {
		m_boardSpaces.get(ICELAND).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Great Britain.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setGreatBritainArmy(int a_value) throws Exception {
		m_boardSpaces.get(GREAT_BRITAIN).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Scandinavia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setScandinaviaArmy(int a_value) throws Exception {
		m_boardSpaces.get(SCANDINAVIA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Northern Europe.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setNorthernEuropeArmy(int a_value) throws Exception {
		m_boardSpaces.get(NORTHERN_EUROPE).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Western Europe.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setWesternEuropeArmy(int a_value) throws Exception {
		m_boardSpaces.get(WESTERN_EUROPE).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Southern Europe.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setSouthernEuropeArmy(int a_value) throws Exception {
		m_boardSpaces.get(SOUTHERN_EUROPE).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Ukraine.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setUkraineArmy(int a_value) throws Exception {
		m_boardSpaces.get(UKRAINE).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a North Africa.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setNorthAfricaArmy(int a_value) throws Exception {
		m_boardSpaces.get(NORTH_AFRICA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Egypt.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setEgyptArmy(int a_value) throws Exception {
		m_boardSpaces.get(EGYPT).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a East Africa.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setEastAfricaArmy(int a_value) throws Exception {
		m_boardSpaces.get(EAST_AFRICA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Congo.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setCongoArmy(int a_value) throws Exception {
		m_boardSpaces.get(CONGO).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a South Africa.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setSouthAfricaArmy(int a_value) throws Exception {
		m_boardSpaces.get(SOUTH_AFRICA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Madagascar.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setMadagascarArmy(int a_value) throws Exception {
		m_boardSpaces.get(MADAGASCAR).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Middle East.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setMiddleEastArmy(int a_value) throws Exception {
		m_boardSpaces.get(MIDDLE_EAST).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Afghanistan.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setAfghanistanArmy(int a_value) throws Exception {
		m_boardSpaces.get(AFGHANISTAN).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Ural.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setUralArmy(int a_value) throws Exception {
		m_boardSpaces.get(URAL).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Siberia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setSiberiaArmy(int a_value) throws Exception {
		m_boardSpaces.get(SIBERIA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a India.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setIndiaArmy(int a_value) throws Exception {
		m_boardSpaces.get(INDIA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a China.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setChinaArmy(int a_value) throws Exception {
		m_boardSpaces.get(CHINA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Southeast Asia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setSoutheastAsiaArmy(int a_value) throws Exception {
		m_boardSpaces.get(SOUTHEAST_ASIA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Mongolia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setMongoliaArmy(int a_value) throws Exception {
		m_boardSpaces.get(MONGOLIA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Irkutsk.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setIrkutskArmy(int a_value) throws Exception {
		m_boardSpaces.get(IRKUTSK).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Yakutsk.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setYakutskArmy(int a_value) throws Exception {
		m_boardSpaces.get(YAKUTSK).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Kamchatka.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setKamchatkaArmy(int a_value) throws Exception {
		m_boardSpaces.get(KAMCHATKA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Japan.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setJapanArmy(int a_value) throws Exception {
		m_boardSpaces.get(JAPAN).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Indonesia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setIndonesiaArmy(int a_value) throws Exception {
		m_boardSpaces.get(INDONESIA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a New Guinea.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setNewGuineaArmy(int a_value) throws Exception {
		m_boardSpaces.get(NEW_GUINEA).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Western Australia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setWesternAustArmy(int a_value) throws Exception {
		m_boardSpaces.get(WESTERN_AUST).setArmyCount(a_value);
	}
	
	
	/** 
	 * Set the army count of a Eastern Australia.
	 * @param a_value - an integer value that represents the new army count total.
	 * @throws invalidValue - if a_value is less than zero.
	 * @author Charles Snyder
	 */
	public void setEasternAustArmy(int a_value) throws Exception {
		m_boardSpaces.get(EASTERN_AUST).setArmyCount(a_value);
	}
	
	
	//
	//
	//
	//
	//
	//
	
	
	
	
	/** 
	 * Set the owner color of Alaska.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setAlaskaOwner(int a_value) throws Exception {
		m_boardSpaces.get(ALASKA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Alberta.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setAlbertaOwner(int a_value) throws Exception {
		m_boardSpaces.get(ALBERTA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Ontario.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setOntarioOwner(int a_value) throws Exception {
		m_boardSpaces.get(ONTARIO).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Northwest Territory.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setNorthwestTerrOwner(int a_value) throws Exception {
		m_boardSpaces.get(NORTHWEST_TERR).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Greenland.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setGreenlandOwner(int a_value) throws Exception {
		m_boardSpaces.get(GREENLAND).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Quebec.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setQuebecOwner(int a_value) throws Exception {
		m_boardSpaces.get(QUEBEC).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Western United States.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setWesternUSOwner(int a_value) throws Exception {
		m_boardSpaces.get(WESTERN_US).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Eastern United States.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setEasternUSOwner(int a_value) throws Exception {
		m_boardSpaces.get(EASTERN_US).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Central America.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setCentralAmericaOwner(int a_value) throws Exception {
		m_boardSpaces.get(CENTRAL_AMERICA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Venezuela.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setVenezuelaOwner(int a_value) throws Exception {
		m_boardSpaces.get(VENEZUELA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Peru.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setPeruOwner(int a_value) throws Exception {
		m_boardSpaces.get(PERU).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Brazil.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setBrazilOwner(int a_value) throws Exception {
		m_boardSpaces.get(BRAZIL).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Argentina.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setArgentinaOwner(int a_value) throws Exception {
		m_boardSpaces.get(ARGENTINA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Iceland.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setIcelandOwner(int a_value) throws Exception {
		m_boardSpaces.get(ICELAND).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Great Britain.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setGreatBritainOwner(int a_value) throws Exception {
		m_boardSpaces.get(GREAT_BRITAIN).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Scandinavia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setScandinaviaOwner(int a_value) throws Exception {
		m_boardSpaces.get(SCANDINAVIA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Northern Europe.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setNorthernEuropeOwner(int a_value) throws Exception {
		m_boardSpaces.get(NORTHERN_EUROPE).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Western Europe.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setWesternEuropeOwner(int a_value) throws Exception {
		m_boardSpaces.get(WESTERN_EUROPE).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Southern Europe.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setSouthernEuropeOwner(int a_value) throws Exception {
		m_boardSpaces.get(SOUTHERN_EUROPE).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Ukraine.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setUkraineOwner(int a_value) throws Exception {
		m_boardSpaces.get(UKRAINE).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of North Africa.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setNorthAfricaOwner(int a_value) throws Exception {
		m_boardSpaces.get(NORTH_AFRICA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Egypt.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setEgyptOwner(int a_value) throws Exception {
		m_boardSpaces.get(EGYPT).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of East Africa.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setEastAfricaOwner(int a_value) throws Exception {
		m_boardSpaces.get(EAST_AFRICA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Congo.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setCongoOwner(int a_value) throws Exception {
		m_boardSpaces.get(CONGO).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of South Africa.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setSouthAfricaOwner(int a_value) throws Exception {
		m_boardSpaces.get(SOUTH_AFRICA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Madagascar.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setMadagascarOwner(int a_value) throws Exception {
		m_boardSpaces.get(MADAGASCAR).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Middle East.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setMiddleEastOwner(int a_value) throws Exception {
		m_boardSpaces.get(MIDDLE_EAST).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Afghanistan.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setAfghanistanOwner(int a_value) throws Exception {
		m_boardSpaces.get(AFGHANISTAN).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Ural.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setUralOwner(int a_value) throws Exception {
		m_boardSpaces.get(URAL).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Siberia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setSiberiaOwner(int a_value) throws Exception {
		m_boardSpaces.get(SIBERIA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of India.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setIndiaOwner(int a_value) throws Exception {
		m_boardSpaces.get(INDIA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of China.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setChinaOwner(int a_value) throws Exception {
		m_boardSpaces.get(CHINA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Southeast Asia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setSoutheastAsiaOwner(int a_value) throws Exception {
		m_boardSpaces.get(SOUTHEAST_ASIA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Mongolia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setMongoliaOwner(int a_value) throws Exception {
		m_boardSpaces.get(MONGOLIA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Irkutsk.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setIrkutskOwner(int a_value) throws Exception {
		m_boardSpaces.get(IRKUTSK).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Yakutsk.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setYakutskOwner(int a_value) throws Exception {
		m_boardSpaces.get(YAKUTSK).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Kamchatka.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setKamchatkaOwner(int a_value) throws Exception {
		m_boardSpaces.get(KAMCHATKA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Japan.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setJapanOwner(int a_value) throws Exception {
		m_boardSpaces.get(JAPAN).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Indonesia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setIndonesiaOwner(int a_value) throws Exception {
		m_boardSpaces.get(INDONESIA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of New Guinea.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setNewGuineaOwner(int a_value) throws Exception {
		m_boardSpaces.get(NEW_GUINEA).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Western Australia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setWesternAustOwner(int a_value) throws Exception {
		m_boardSpaces.get(WESTERN_AUST).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Set the owner color of Eastern Australia.
	 * @param a_value - an integer value that represents the new owner color.
	 * @throws invalidValue - if a_value is less than zero or greater than 5 (player color ranges).
	 * @author Charles Snyder
	 */
	public void setEasternAustOwner(int a_value) throws Exception {
		m_boardSpaces.get(EASTERN_AUST).setCountryOwner(a_value);
	}
	
	
	/** 
	 * Determines whether two countries are adjacent on the board.
	 * @param a_country - an integer value that represents the source country.
	 * @param a_otherCountry - an integer value that represents the test country.
	 * @return True if two countries are adjacent, false otherwise.
	 * @throws outOfRange - if a_country is less than 0 or greater than 41.
	 * @author Charles Snyder
	 */
	public boolean isCountryAdjacent(int a_country, int a_otherCountry) throws Exception {
		switch(a_country) {
		case ALASKA:
			return m_boardSpaces.get(ALASKA).getBorderCountries().contains((Object) a_otherCountry);
		case ALBERTA:
			return m_boardSpaces.get(ALBERTA).getBorderCountries().contains((Object) a_otherCountry);
		case ONTARIO:
			return m_boardSpaces.get(ONTARIO).getBorderCountries().contains((Object) a_otherCountry);
		case NORTHWEST_TERR:
			return m_boardSpaces.get(NORTHWEST_TERR).getBorderCountries().contains((Object) a_otherCountry);
		case GREENLAND:
			return m_boardSpaces.get(GREENLAND).getBorderCountries().contains((Object) a_otherCountry);
		case QUEBEC:
			return m_boardSpaces.get(QUEBEC).getBorderCountries().contains((Object) a_otherCountry);
		case WESTERN_US:
			return m_boardSpaces.get(WESTERN_US).getBorderCountries().contains((Object) a_otherCountry);
		case EASTERN_US:
			return m_boardSpaces.get(EASTERN_US).getBorderCountries().contains((Object) a_otherCountry);
		case CENTRAL_AMERICA:
			return m_boardSpaces.get(CENTRAL_AMERICA).getBorderCountries().contains((Object) a_otherCountry);
		case VENEZUELA:
			return m_boardSpaces.get(VENEZUELA).getBorderCountries().contains((Object) a_otherCountry);
		case PERU:
			return m_boardSpaces.get(PERU).getBorderCountries().contains((Object) a_otherCountry);
		case BRAZIL:
			return m_boardSpaces.get(BRAZIL).getBorderCountries().contains((Object) a_otherCountry);
		case ARGENTINA:
			return m_boardSpaces.get(ARGENTINA).getBorderCountries().contains((Object) a_otherCountry);
		case ICELAND:
			return m_boardSpaces.get(ICELAND).getBorderCountries().contains((Object) a_otherCountry);
		case GREAT_BRITAIN:
			return m_boardSpaces.get(GREAT_BRITAIN).getBorderCountries().contains((Object) a_otherCountry);
		case SCANDINAVIA:
			return m_boardSpaces.get(SCANDINAVIA).getBorderCountries().contains((Object) a_otherCountry);
		case NORTHERN_EUROPE:
			return m_boardSpaces.get(NORTHERN_EUROPE).getBorderCountries().contains((Object) a_otherCountry);
		case WESTERN_EUROPE:
			return m_boardSpaces.get(WESTERN_EUROPE).getBorderCountries().contains((Object) a_otherCountry);
		case SOUTHERN_EUROPE:
			return m_boardSpaces.get(SOUTHERN_EUROPE).getBorderCountries().contains((Object) a_otherCountry);
		case UKRAINE:
			return m_boardSpaces.get(UKRAINE).getBorderCountries().contains((Object) a_otherCountry);
		case NORTH_AFRICA:
			return m_boardSpaces.get(NORTH_AFRICA).getBorderCountries().contains((Object) a_otherCountry);
		case EGYPT:
			return m_boardSpaces.get(EGYPT).getBorderCountries().contains((Object) a_otherCountry);
		case EAST_AFRICA:
			return m_boardSpaces.get(EAST_AFRICA).getBorderCountries().contains((Object) a_otherCountry);
		case CONGO:
			return m_boardSpaces.get(CONGO).getBorderCountries().contains((Object) a_otherCountry);
		case SOUTH_AFRICA:
			return m_boardSpaces.get(SOUTH_AFRICA).getBorderCountries().contains((Object) a_otherCountry);
		case MADAGASCAR:
			return m_boardSpaces.get(MADAGASCAR).getBorderCountries().contains((Object) a_otherCountry);
		case MIDDLE_EAST:
			return m_boardSpaces.get(MIDDLE_EAST).getBorderCountries().contains((Object) a_otherCountry);
		case AFGHANISTAN:
			return m_boardSpaces.get(AFGHANISTAN).getBorderCountries().contains((Object) a_otherCountry);
		case URAL:
			return m_boardSpaces.get(URAL).getBorderCountries().contains((Object) a_otherCountry);
		case SIBERIA:
			return m_boardSpaces.get(SIBERIA).getBorderCountries().contains((Object) a_otherCountry);
		case INDIA:
			return m_boardSpaces.get(INDIA).getBorderCountries().contains((Object) a_otherCountry);
		case CHINA:
			return m_boardSpaces.get(CHINA).getBorderCountries().contains((Object) a_otherCountry);
		case SOUTHEAST_ASIA:
			return m_boardSpaces.get(SOUTHEAST_ASIA).getBorderCountries().contains((Object) a_otherCountry);
		case MONGOLIA:
			return m_boardSpaces.get(MONGOLIA).getBorderCountries().contains((Object) a_otherCountry);
		case IRKUTSK:
			return m_boardSpaces.get(IRKUTSK).getBorderCountries().contains((Object) a_otherCountry);
		case YAKUTSK:
			return m_boardSpaces.get(YAKUTSK).getBorderCountries().contains((Object) a_otherCountry);
		case KAMCHATKA:
			return m_boardSpaces.get(KAMCHATKA).getBorderCountries().contains((Object) a_otherCountry);
		case JAPAN:
			return m_boardSpaces.get(JAPAN).getBorderCountries().contains((Object) a_otherCountry);
		case INDONESIA:
			return m_boardSpaces.get(INDONESIA).getBorderCountries().contains((Object) a_otherCountry);
		case NEW_GUINEA:
			return m_boardSpaces.get(NEW_GUINEA).getBorderCountries().contains((Object) a_otherCountry);
		case WESTERN_AUST:
			return m_boardSpaces.get(WESTERN_AUST).getBorderCountries().contains((Object) a_otherCountry);
		case EASTERN_AUST:
			return m_boardSpaces.get(EASTERN_AUST).getBorderCountries().contains((Object) a_otherCountry);
		
		default:
			Exception outOfRange = new Exception("Invalid country");
			throw outOfRange;
		}
		
	}
	
	
	/** 
	 * Determines whether each country on the board has an owner.
	 * @return True if all countries have an owner, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean isBoardFull() {
		for(int index = ALASKA; index <= EASTERN_AUST; index++) {
			try {
				if(getBoardSpaceOwner(index) == -1) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	
	/** 
	 * Determines if any country has an owner.
	 * @return True if no countries have an owner, false otherwise.
	 * @author Charles Snyder
	 */
	public boolean isBoardEmpty() {
		for(int index = ALASKA; index <= EASTERN_AUST; index++) {
			try {
				if(getBoardSpaceOwner(index) != -1) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	
	/** 
	 * Finds number of countries in a continent based on where the selected country is located.
	 * @param a_country - integer value that represents country to be searched.
	 * @return integer value of number of countries in a particular continent.  Returns -1 if a_country is not valid.
	 * @author Charles Snyder
	 */
	public int getNumCountriesContinent(int a_country) {
		if(a_country == ALASKA || a_country == NORTHWEST_TERR || a_country == GREENLAND || a_country == ALBERTA
				 || a_country == ONTARIO || a_country == QUEBEC || a_country == WESTERN_US
				 || a_country == EASTERN_US || a_country == CENTRAL_AMERICA) {
			return 9;
		}
		else if(a_country == VENEZUELA || a_country == PERU || a_country == ARGENTINA || a_country == BRAZIL) {
			return 4;
		}
		else if(a_country == NORTH_AFRICA || a_country == EGYPT || a_country == CONGO || a_country == EAST_AFRICA
				 || a_country == SOUTH_AFRICA || a_country == MADAGASCAR) {
			return 6;
		}
		else if(a_country == ICELAND || a_country == SCANDINAVIA || a_country == UKRAINE || a_country == NORTHERN_EUROPE
				 || a_country == WESTERN_EUROPE || a_country == SOUTHERN_EUROPE || a_country == GREAT_BRITAIN) {
			return 7;
		}
		else if(a_country == MIDDLE_EAST || a_country == AFGHANISTAN || a_country == URAL || a_country == YAKUTSK
				 || a_country == KAMCHATKA || a_country == JAPAN || a_country == IRKUTSK || a_country == SIBERIA
				 || a_country == MONGOLIA || a_country == CHINA || a_country == SOUTHEAST_ASIA || a_country == INDIA) {
			return 12;
		}
		else if(a_country == INDONESIA || a_country == NEW_GUINEA || a_country == EASTERN_AUST || a_country == WESTERN_AUST) {
			return 4;
		}
		else {
			return -1;
		}
	}
	
	
	
	/** 
	 * Returns potential moves if a continent is almost controlled.
	 * Could be more than one continent and the space may be taken by another player already.
	 * @return An array list of integer values, each representing a country that could be a possible move.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> isContinentAlmostControlled() {
		// Final array list that will be returned.
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		
		// An array list that holds all player color values, will increase a particular color
		// based on who owns each country, initially each index is set to zero.
		ArrayList<Integer> colorCount = new ArrayList<Integer>();
		for(int index = 0; index < 6; index++) {
			colorCount.add(0);
		}
		
		//North America
		increaseColorCount(m_boardSpaces.get(ALASKA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(NORTHWEST_TERR).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(ALBERTA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(ONTARIO).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(QUEBEC).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(GREENLAND).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(WESTERN_US).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(EASTERN_US).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(CENTRAL_AMERICA).getCountryOwner(), colorCount);
		
		// Player color will be -1 if no one player controls one country less than the entire continent;
		// otherwise it will be the integer value representing a certain color.
		// In that case which ever one country was not owned by that player will be added to the return list.
		int playerColor = doesPlayerControlMostContinent(colorCount, 8);
		if(playerColor != -1) {
			if(m_boardSpaces.get(ALASKA).getCountryOwner() != playerColor) {
				returnList.add(ALASKA);
			}
			else if(m_boardSpaces.get(NORTHWEST_TERR).getCountryOwner() != playerColor) {
				returnList.add(NORTHWEST_TERR);
			}
			else if(m_boardSpaces.get(ALBERTA).getCountryOwner() != playerColor) {
				returnList.add(ALBERTA);
			}
			else if(m_boardSpaces.get(ONTARIO).getCountryOwner() != playerColor) {
				returnList.add(ONTARIO);
			}
			else if(m_boardSpaces.get(QUEBEC).getCountryOwner() != playerColor) {
				returnList.add(QUEBEC);
			}
			else if(m_boardSpaces.get(GREENLAND).getCountryOwner() != playerColor) {
				returnList.add(GREENLAND);
			}
			else if(m_boardSpaces.get(WESTERN_US).getCountryOwner() != playerColor) {
				returnList.add(WESTERN_US);
			}
			else if(m_boardSpaces.get(EASTERN_US).getCountryOwner() != playerColor) {
				returnList.add(EASTERN_US);
			}
			else if(m_boardSpaces.get(CENTRAL_AMERICA).getCountryOwner() != playerColor) {
				returnList.add(CENTRAL_AMERICA);
			}
		}
		
		
		// Set all players counts back to zero.
		resetColorCount(colorCount);
		
		//South America
		increaseColorCount(m_boardSpaces.get(VENEZUELA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(BRAZIL).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(PERU).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(ARGENTINA).getCountryOwner(), colorCount);
		
		playerColor = doesPlayerControlMostContinent(colorCount, 3);
		if(playerColor != -1) {
			if(m_boardSpaces.get(VENEZUELA).getCountryOwner() != playerColor) {
				returnList.add(VENEZUELA);
			}
			else if(m_boardSpaces.get(BRAZIL).getCountryOwner() != playerColor) {
				returnList.add(BRAZIL);
			}
			else if(m_boardSpaces.get(PERU).getCountryOwner() != playerColor) {
				returnList.add(PERU);
			}
			else if(m_boardSpaces.get(ARGENTINA).getCountryOwner() != playerColor) {
				returnList.add(ARGENTINA);
			}
		}
			
		resetColorCount(colorCount);
		
		//Europe
		
		increaseColorCount(m_boardSpaces.get(ICELAND).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(SCANDINAVIA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(NORTHERN_EUROPE).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(WESTERN_EUROPE).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(SOUTHERN_EUROPE).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(GREAT_BRITAIN).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(UKRAINE).getCountryOwner(), colorCount);
		
		playerColor = doesPlayerControlMostContinent(colorCount, 6);
		if(playerColor != -1) {
			if(m_boardSpaces.get(ICELAND).getCountryOwner() != playerColor) {
				returnList.add(ICELAND);
			}
			else if(m_boardSpaces.get(SCANDINAVIA).getCountryOwner() != playerColor) {
				returnList.add(SCANDINAVIA);
			}
			else if(m_boardSpaces.get(NORTHERN_EUROPE).getCountryOwner() != playerColor) {
				returnList.add(NORTHERN_EUROPE);
			}
			else if(m_boardSpaces.get(WESTERN_EUROPE).getCountryOwner() != playerColor) {
				returnList.add(WESTERN_EUROPE);
			}
			else if(m_boardSpaces.get(SOUTHERN_EUROPE).getCountryOwner() != playerColor) {
				returnList.add(SOUTHERN_EUROPE);
			}
			else if(m_boardSpaces.get(GREAT_BRITAIN).getCountryOwner() != playerColor) {
				returnList.add(GREAT_BRITAIN);
			}
			else if(m_boardSpaces.get(UKRAINE).getCountryOwner() != playerColor) {
				returnList.add(UKRAINE);
			}
		}
			
		resetColorCount(colorCount);
		
		
		//Africa
		
		increaseColorCount(m_boardSpaces.get(NORTH_AFRICA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(EGYPT).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(CONGO).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(EAST_AFRICA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(SOUTH_AFRICA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(MADAGASCAR).getCountryOwner(), colorCount);
		
		playerColor = doesPlayerControlMostContinent(colorCount, 5);
		if(playerColor != -1) {
			if(m_boardSpaces.get(NORTH_AFRICA).getCountryOwner() != playerColor) {
				returnList.add(NORTH_AFRICA);
			}
			else if(m_boardSpaces.get(EGYPT).getCountryOwner() != playerColor) {
				returnList.add(EGYPT);
			}
			else if(m_boardSpaces.get(CONGO).getCountryOwner() != playerColor) {
				returnList.add(CONGO);
			}
			else if(m_boardSpaces.get(EAST_AFRICA).getCountryOwner() != playerColor) {
				returnList.add(EAST_AFRICA);
			}
			else if(m_boardSpaces.get(SOUTH_AFRICA).getCountryOwner() != playerColor) {
				returnList.add(SOUTH_AFRICA);
			}
			else if(m_boardSpaces.get(MADAGASCAR).getCountryOwner() != playerColor) {
				returnList.add(MADAGASCAR);
			}
		}
			
		resetColorCount(colorCount);
		
		
		//Asia
		
		increaseColorCount(m_boardSpaces.get(URAL).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(AFGHANISTAN).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(MIDDLE_EAST).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(INDIA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(CHINA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(SOUTHEAST_ASIA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(MONGOLIA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(SIBERIA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(IRKUTSK).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(JAPAN).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(KAMCHATKA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(YAKUTSK).getCountryOwner(), colorCount);
		
		playerColor = doesPlayerControlMostContinent(colorCount, 11);
		if(playerColor != -1) {
			if(m_boardSpaces.get(URAL).getCountryOwner() != playerColor) {
				returnList.add(URAL);
			}
			else if(m_boardSpaces.get(AFGHANISTAN).getCountryOwner() != playerColor) {
				returnList.add(AFGHANISTAN);
			}
			else if(m_boardSpaces.get(MIDDLE_EAST).getCountryOwner() != playerColor) {
				returnList.add(MIDDLE_EAST);
			}
			else if(m_boardSpaces.get(INDIA).getCountryOwner() != playerColor) {
				returnList.add(INDIA);
			}
			else if(m_boardSpaces.get(CHINA).getCountryOwner() != playerColor) {
				returnList.add(CHINA);
			}
			else if(m_boardSpaces.get(SOUTHEAST_ASIA).getCountryOwner() != playerColor) {
				returnList.add(SOUTHEAST_ASIA);
			}
			else if(m_boardSpaces.get(MONGOLIA).getCountryOwner() != playerColor) {
				returnList.add(MONGOLIA);
			}
			else if(m_boardSpaces.get(SIBERIA).getCountryOwner() != playerColor) {
				returnList.add(SIBERIA);
			}
			else if(m_boardSpaces.get(IRKUTSK).getCountryOwner() != playerColor) {
				returnList.add(IRKUTSK);
			}
			else if(m_boardSpaces.get(JAPAN).getCountryOwner() != playerColor) {
				returnList.add(JAPAN);
			}
			else if(m_boardSpaces.get(KAMCHATKA).getCountryOwner() != playerColor) {
				returnList.add(KAMCHATKA);
			}
			else if(m_boardSpaces.get(YAKUTSK).getCountryOwner() != playerColor) {
				returnList.add(YAKUTSK);
			}
		}
		
		resetColorCount(colorCount);
		
		//Australia
		
		increaseColorCount(m_boardSpaces.get(INDONESIA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(NEW_GUINEA).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(WESTERN_AUST).getCountryOwner(), colorCount);
		increaseColorCount(m_boardSpaces.get(EASTERN_AUST).getCountryOwner(), colorCount);
		
		playerColor = doesPlayerControlMostContinent(colorCount, 3);
		if(playerColor != -1) {
			if(m_boardSpaces.get(INDONESIA).getCountryOwner() != playerColor) {
				returnList.add(INDONESIA);
			}
			else if(m_boardSpaces.get(NEW_GUINEA).getCountryOwner() != playerColor) {
				returnList.add(NEW_GUINEA);
			}
			else if(m_boardSpaces.get(WESTERN_AUST).getCountryOwner() != playerColor) {
				returnList.add(WESTERN_AUST);
			}
			else if(m_boardSpaces.get(EASTERN_AUST).getCountryOwner() != playerColor) {
				returnList.add(EASTERN_AUST);
			}
		}
		
		return returnList;
	}
	
	
	/** 
	 * Returns integer value representing particular color if a player owns all but one countries in a continent.
	 * @param a_colorCount - array list of integers where each index represents a player color.
	 * @param a_continentValue - one less than the total number of countries in a continent.  
	 * @return An integer value representing player color or -1 if no player is found to own all but one countries in a continent.
	 * @author Charles Snyder
	 */
	private int doesPlayerControlMostContinent(ArrayList<Integer> a_colorCount, int a_continentValue) {
		for(int index = 0; index < 6; index++) {
			if(a_colorCount.get(index) == a_continentValue) {
				switch(index) {
				case 0:
					return Player.RED;
				case 1:
					return Player.BLUE;
				case 2:
					return Player.GREEN;
				case 3:
					return Player.YELLOW;
				case 4:
					return Player.ORANGE;
				case 5:
					return Player.PURPLE;
				}
			}
		}
		return -1;
	}
	
	
	
	/** 
	 * Sets the color count array list back to all zeroes for each index.
	 * @param a_colorCount - array list of integers where each index represents a player color.
	 * @author Charles Snyder
	 */
	private void resetColorCount(ArrayList<Integer> a_colorCount) {
		a_colorCount.clear();
		for(int index = 0; index < 6; index++) {
			a_colorCount.add(0);
		}
	}
	
	
	/** 
	 * Increases a certain index by one if the color represented by that index matches the country owner.
	 * @param a_colorCount - array list of integers where each index represents a player color.
	 * @param a_countryOwner - integer value representing color of owner of a certain country. 
	 * @author Charles Snyder
	 */
	private void increaseColorCount(int a_countryOwner, ArrayList<Integer> a_colorCount) {
		switch(a_countryOwner) {
		case Player.RED:
			a_colorCount.set(0, a_colorCount.get(0) + 1);
			break;
		case Player.BLUE:
			a_colorCount.set(1, a_colorCount.get(1) + 1);
			break;
		case Player.GREEN:
			a_colorCount.set(2, a_colorCount.get(2) + 1);
			break;
		case Player.YELLOW:
			a_colorCount.set(3, a_colorCount.get(3) + 1);
			break;
		case Player.ORANGE:
			a_colorCount.set(4, a_colorCount.get(4) + 1);
			break;
		case Player.PURPLE:
			a_colorCount.set(5, a_colorCount.get(5) + 1);
			break;
		default:
			return;
		}
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		
		sbuff.append(m_boardSpaces);
		return sbuff.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(m_boardSpaces);
	}
	
	
	public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
		public Board createFromParcel(Parcel in) {
			return new Board(in);
		}
		public Board[] newArray(int size) {
			return new Board[size];
		}
	};
	
	private Board(Parcel in) {
		m_boardSpaces = new ArrayList<CountrySpace>();
		in.readTypedList(m_boardSpaces, CountrySpace.CREATOR);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board test = new Board();
		try {
			test.setAfghanistanArmy(5);
			@SuppressWarnings("unused")
			boolean test1 = test.isCountryAdjacent(ALASKA, ALBERTA);
			@SuppressWarnings("unused")
			boolean test2 = test.isCountryAdjacent(ALASKA, ARGENTINA);
			System.out.println("stuff");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(test.getAfghanistanArmy());
		
		System.out.println(test.toString());
	}

}
