package edu.ramapo.csnyder.gameLogic;

import java.util.ArrayList;

import android.os.Parcelable;
import android.os.Parcel;

public class CountrySpace implements Parcelable {
	
	private int m_armies;
	private int m_owner;
	private ArrayList<Integer> m_borderCountries;
	
	
	/** 
	 * Class constructor.
	 * @author Charles Snyder
	 */
	public CountrySpace() {
		m_armies = 0;
		m_owner = -1;
		m_borderCountries = new ArrayList<Integer>();
	}
	
	/** 
	 * Class constructor with specified country value.
	 * @param a_country - integer value that represents a particular country.
	 * @author Charles Snyder
	 */
	public CountrySpace(int a_country) {
		m_armies = 0;
		m_owner = -1;
		m_borderCountries = new ArrayList<Integer>();
		try {
			setBorderCountries(a_country);
		} catch (Exception e) {		
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * Class constructor with specified army, owner, and border values.
	 * @param a_army - integer value of army total.
	 * @param a_owner - integer value representing color of certain player.
	 * @param a_borders - array list of border countries for a country.
	 * @author Charles Snyder
	 */
	public CountrySpace(int a_army, int a_owner, ArrayList<Integer> a_borders) {
		try {
			setArmyCount(a_army);
			setCountryOwner(a_owner);
			m_borderCountries = new ArrayList<Integer>(a_borders);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	/** 
	 * Class copy constructor.
	 * @param a_space - CountrySpace object to be copied.
	 * @author Charles Snyder
	 */
	public CountrySpace(CountrySpace a_space) {
		m_armies = a_space.m_armies;
		m_owner = a_space.m_owner;
		m_borderCountries = new ArrayList<Integer>(a_space.getBorderCountries());
	}
	
	
	/** 
	 * Selector for m_armies member variable.
	 * @return m_armies - army total in a country.
	 * @author Charles Snyder
	 */
	public final int getCountryArmyCount() {
		return m_armies;
	}
	
	
	/** 
	 * Selector for m_owner member variable.
	 * @return m_owner - integer representing particular player color.
	 * @author Charles Snyder
	 */
	public final int getCountryOwner() {
		return m_owner;
	}
	
	
	/** 
	 * Selector for m_borderCountries member variable.
	 * @return tmp - copy of m_borderCountries member variable.
	 * @author Charles Snyder
	 */
	public ArrayList<Integer> getBorderCountries() {
		ArrayList<Integer> tmp = new ArrayList<Integer>(m_borderCountries);
		return tmp;
	}
	
	
	/** 
	 * Mutator for m_armies member variable.
	 * @param a_count - value to set the countries army total to.
	 * @throws invalidValue - if a_count is less than zero.
	 * @author Charles Snyder
	 */
	public void setArmyCount(int a_count) throws Exception {
		if(a_count >= 0) {
			m_armies = a_count;
		}
		else {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
	}
	
	
	/** 
	 * Mutator for m_owner member variable.
	 * @param a_owner - value to set the countries owner to.
	 * @throws invalidValue - if a_count is less than zero or greater than five.
	 * @author Charles Snyder
	 */
	public void setCountryOwner(int a_owner) throws Exception {
		if(a_owner >= 0 && a_owner < 6) {
			m_owner = a_owner;
		}
		else {
			Exception invalidValue = new Exception("Invalid value");
			throw invalidValue;
		}
	}
	
	
	/** 
	 * Mutator for m_borderCountries member variable.
	 * @param a_countries - value to set the list of border countries to.
	 * @author Charles Snyder
	 */
	public void setBorderCountryArray(ArrayList<Integer> a_countries) {
		m_borderCountries = new ArrayList<Integer>(a_countries);
	}
	
	
	/** 
	 * Adds to the m_borderCountries member variable a list of border (adjacent) countries for a particular country.
	 * @param a_country - the country that will have its borders examined.
	 * @throws invalidValue - if a_country is not a valid country value between 0 and 41.
	 * @author Charles Snyder
	 */
	private void setBorderCountries(int a_country) throws Exception {
		switch(a_country) {
		case Board.AFGHANISTAN:
			m_borderCountries.add(Board.UKRAINE);
			m_borderCountries.add(Board.URAL);
			m_borderCountries.add(Board.CHINA);
			m_borderCountries.add(Board.INDIA);
			m_borderCountries.add(Board.MIDDLE_EAST);
			return;
		case Board.ALASKA:
			m_borderCountries.add(Board.NORTHWEST_TERR);
			m_borderCountries.add(Board.ALBERTA);
			m_borderCountries.add(Board.KAMCHATKA);
			return;
		case Board.ALBERTA:
			m_borderCountries.add(Board.ALASKA);
			m_borderCountries.add(Board.NORTHWEST_TERR);
			m_borderCountries.add(Board.ONTARIO);
			m_borderCountries.add(Board.WESTERN_US);
			return;
		case Board.ARGENTINA:
			m_borderCountries.add(Board.PERU);
			m_borderCountries.add(Board.BRAZIL);
			return;
		case Board.BRAZIL:
			m_borderCountries.add(Board.VENEZUELA);
			m_borderCountries.add(Board.PERU);
			m_borderCountries.add(Board.ARGENTINA);
			m_borderCountries.add(Board.NORTH_AFRICA);
			return;
		case Board.CENTRAL_AMERICA:
			m_borderCountries.add(Board.WESTERN_US);
			m_borderCountries.add(Board.EASTERN_US);
			m_borderCountries.add(Board.VENEZUELA);
			return;
		case Board.CHINA:
			m_borderCountries.add(Board.AFGHANISTAN);
			m_borderCountries.add(Board.URAL);
			m_borderCountries.add(Board.SIBERIA);
			m_borderCountries.add(Board.MONGOLIA);
			m_borderCountries.add(Board.INDIA);
			m_borderCountries.add(Board.SOUTHEAST_ASIA);
			return;
		case Board.CONGO:
			m_borderCountries.add(Board.NORTH_AFRICA);
			m_borderCountries.add(Board.EAST_AFRICA);
			m_borderCountries.add(Board.SOUTH_AFRICA);
			return;
		case Board.EAST_AFRICA:
			m_borderCountries.add(Board.EGYPT);
			m_borderCountries.add(Board.NORTH_AFRICA);
			m_borderCountries.add(Board.CONGO);
			m_borderCountries.add(Board.SOUTH_AFRICA);
			m_borderCountries.add(Board.MADAGASCAR);
			m_borderCountries.add(Board.MIDDLE_EAST);
			return;
		case Board.EASTERN_AUST:
			m_borderCountries.add(Board.WESTERN_AUST);
			m_borderCountries.add(Board.NEW_GUINEA);
			return;
		case Board.EASTERN_US:
			m_borderCountries.add(Board.WESTERN_US);
			m_borderCountries.add(Board.ONTARIO);
			m_borderCountries.add(Board.QUEBEC);
			m_borderCountries.add(Board.CENTRAL_AMERICA);
			return;
		case Board.EGYPT: 
			m_borderCountries.add(Board.SOUTHERN_EUROPE);
			m_borderCountries.add(Board.NORTH_AFRICA);
			m_borderCountries.add(Board.EAST_AFRICA);
			m_borderCountries.add(Board.MIDDLE_EAST);
			return;
		case Board.GREAT_BRITAIN:
			m_borderCountries.add(Board.ICELAND);
			m_borderCountries.add(Board.SCANDINAVIA);
			m_borderCountries.add(Board.NORTHERN_EUROPE);
			m_borderCountries.add(Board.WESTERN_EUROPE);
			return;
		case Board.GREENLAND:
			m_borderCountries.add(Board.NORTHWEST_TERR);
			m_borderCountries.add(Board.ONTARIO);
			m_borderCountries.add(Board.QUEBEC);
			m_borderCountries.add(Board.ICELAND);
			return;
		case Board.ICELAND:
			m_borderCountries.add(Board.GREENLAND);
			m_borderCountries.add(Board.GREAT_BRITAIN);
			m_borderCountries.add(Board.SCANDINAVIA);
			return;
		case Board.INDIA:
			m_borderCountries.add(Board.MIDDLE_EAST);
			m_borderCountries.add(Board.AFGHANISTAN);
			m_borderCountries.add(Board.CHINA);
			m_borderCountries.add(Board.SOUTHEAST_ASIA);
			return;
		case Board.INDONESIA:
			m_borderCountries.add(Board.SOUTHEAST_ASIA);
			m_borderCountries.add(Board.WESTERN_AUST);
			m_borderCountries.add(Board.NEW_GUINEA);
			return;
		case Board.IRKUTSK:
			m_borderCountries.add(Board.SIBERIA);
			m_borderCountries.add(Board.YAKUTSK);
			m_borderCountries.add(Board.KAMCHATKA);
			m_borderCountries.add(Board.MONGOLIA);
			return;
		case Board.JAPAN:
			m_borderCountries.add(Board.KAMCHATKA);
			m_borderCountries.add(Board.MONGOLIA);
			return;
		case Board.KAMCHATKA:
			m_borderCountries.add(Board.YAKUTSK);
			m_borderCountries.add(Board.IRKUTSK);
			m_borderCountries.add(Board.MONGOLIA);
			m_borderCountries.add(Board.JAPAN);
			m_borderCountries.add(Board.ALASKA);
			return;
		case Board.MADAGASCAR:
			m_borderCountries.add(Board.EAST_AFRICA);
			m_borderCountries.add(Board.SOUTH_AFRICA);
			return;
		case Board.MIDDLE_EAST:
			m_borderCountries.add(Board.SOUTHERN_EUROPE);
			m_borderCountries.add(Board.UKRAINE);
			m_borderCountries.add(Board.AFGHANISTAN);
			m_borderCountries.add(Board.INDIA);
			m_borderCountries.add(Board.EAST_AFRICA);
			m_borderCountries.add(Board.EGYPT);
			return;
		case Board.MONGOLIA:
			m_borderCountries.add(Board.SIBERIA);
			m_borderCountries.add(Board.IRKUTSK);
			m_borderCountries.add(Board.JAPAN);
			m_borderCountries.add(Board.KAMCHATKA);
			m_borderCountries.add(Board.CHINA);
			return;
		case Board.NEW_GUINEA:
			m_borderCountries.add(Board.INDONESIA);
			m_borderCountries.add(Board.WESTERN_AUST);
			m_borderCountries.add(Board.EASTERN_AUST);
			return;
		case Board.NORTH_AFRICA:
			m_borderCountries.add(Board.BRAZIL);
			m_borderCountries.add(Board.WESTERN_EUROPE);
			m_borderCountries.add(Board.SOUTHERN_EUROPE);
			m_borderCountries.add(Board.EGYPT);
			m_borderCountries.add(Board.EAST_AFRICA);
			m_borderCountries.add(Board.CONGO);
			return;
		case Board.NORTHERN_EUROPE:
			m_borderCountries.add(Board.SCANDINAVIA);
			m_borderCountries.add(Board.UKRAINE);
			m_borderCountries.add(Board.SOUTHERN_EUROPE);
			m_borderCountries.add(Board.WESTERN_EUROPE);
			m_borderCountries.add(Board.GREAT_BRITAIN);
			return;
		case Board.NORTHWEST_TERR:
			m_borderCountries.add(Board.ALASKA);
			m_borderCountries.add(Board.GREENLAND);
			m_borderCountries.add(Board.ALBERTA);
			m_borderCountries.add(Board.ONTARIO);
			return;
		case Board.ONTARIO:
			m_borderCountries.add(Board.ALBERTA);
			m_borderCountries.add(Board.NORTHWEST_TERR);
			m_borderCountries.add(Board.GREENLAND);
			m_borderCountries.add(Board.QUEBEC);
			m_borderCountries.add(Board.WESTERN_US);
			m_borderCountries.add(Board.EASTERN_US);
			return;
		case Board.PERU:
			m_borderCountries.add(Board.VENEZUELA);
			m_borderCountries.add(Board.BRAZIL);
			m_borderCountries.add(Board.ARGENTINA);
			return;
		case Board.QUEBEC:
			m_borderCountries.add(Board.GREENLAND);
			m_borderCountries.add(Board.EASTERN_US);
			m_borderCountries.add(Board.ONTARIO);
			return;
		case Board.SCANDINAVIA:
			m_borderCountries.add(Board.ICELAND);
			m_borderCountries.add(Board.GREAT_BRITAIN);
			m_borderCountries.add(Board.NORTHERN_EUROPE);
			m_borderCountries.add(Board.UKRAINE);
			return;
		case Board.SIBERIA:
			m_borderCountries.add(Board.URAL);
			m_borderCountries.add(Board.YAKUTSK);
			m_borderCountries.add(Board.IRKUTSK);
			m_borderCountries.add(Board.MONGOLIA);
			m_borderCountries.add(Board.CHINA);
			return;
		case Board.SOUTH_AFRICA:
			m_borderCountries.add(Board.CONGO);
			m_borderCountries.add(Board.EAST_AFRICA);
			m_borderCountries.add(Board.MADAGASCAR);
			return;
		case Board.SOUTHEAST_ASIA:
			m_borderCountries.add(Board.INDONESIA);
			m_borderCountries.add(Board.INDIA);
			m_borderCountries.add(Board.CHINA);
			return;
		case Board.SOUTHERN_EUROPE:
			m_borderCountries.add(Board.WESTERN_EUROPE);
			m_borderCountries.add(Board.NORTHERN_EUROPE);
			m_borderCountries.add(Board.UKRAINE);
			m_borderCountries.add(Board.MIDDLE_EAST);
			m_borderCountries.add(Board.EGYPT);
			m_borderCountries.add(Board.NORTH_AFRICA);
			return;
		case Board.UKRAINE:
			m_borderCountries.add(Board.SCANDINAVIA);
			m_borderCountries.add(Board.URAL);
			m_borderCountries.add(Board.AFGHANISTAN);
			m_borderCountries.add(Board.MIDDLE_EAST);
			m_borderCountries.add(Board.SOUTHERN_EUROPE);
			m_borderCountries.add(Board.NORTHERN_EUROPE);
			return;
		case Board.URAL:
			m_borderCountries.add(Board.UKRAINE);
			m_borderCountries.add(Board.SIBERIA);
			m_borderCountries.add(Board.CHINA);
			m_borderCountries.add(Board.AFGHANISTAN);
			return;
		case Board.VENEZUELA:
			m_borderCountries.add(Board.CENTRAL_AMERICA);
			m_borderCountries.add(Board.PERU);
			m_borderCountries.add(Board.BRAZIL);
			return;
		case Board.WESTERN_AUST:
			m_borderCountries.add(Board.INDONESIA);
			m_borderCountries.add(Board.NEW_GUINEA);
			m_borderCountries.add(Board.EASTERN_AUST);
			return;
		case Board.WESTERN_EUROPE:
			m_borderCountries.add(Board.GREAT_BRITAIN);
			m_borderCountries.add(Board.NORTHERN_EUROPE);
			m_borderCountries.add(Board.SOUTHERN_EUROPE);
			m_borderCountries.add(Board.NORTH_AFRICA);
			return;
		case Board.WESTERN_US:
			m_borderCountries.add(Board.ALBERTA);
			m_borderCountries.add(Board.ONTARIO);
			m_borderCountries.add(Board.EASTERN_US);
			m_borderCountries.add(Board.CENTRAL_AMERICA);
			return;
		case Board.YAKUTSK:
			m_borderCountries.add(Board.SIBERIA);
			m_borderCountries.add(Board.KAMCHATKA);
			m_borderCountries.add(Board.IRKUTSK);
			return;
		default:
			Exception invalidValue = new Exception("Invalid Country");
			throw invalidValue;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("(" + m_armies + ", " + m_owner + ", " + m_borderCountries + ")");
		
		return sbuff.toString();
	}
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(m_armies);
		dest.writeInt(m_owner);
		dest.writeList(m_borderCountries);
	}
	
	public static final Parcelable.Creator<CountrySpace> CREATOR = new Parcelable.Creator<CountrySpace>() {
		public CountrySpace createFromParcel(Parcel in) {
			return new CountrySpace(in);
		}
		public CountrySpace[] newArray(int size) {
			return new CountrySpace[size];
		}
	};
	
	private CountrySpace(Parcel in) {
		m_armies = in.readInt();
		m_owner = in.readInt();
		m_borderCountries = new ArrayList<Integer>();
		in.readList(m_borderCountries, Integer.class.getClassLoader());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
