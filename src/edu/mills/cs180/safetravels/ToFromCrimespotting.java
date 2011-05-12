package edu.mills.cs180.safetravels;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * ToFromCrimespotting is a class that handles querying the Oakland Crimspotting site 
 * and parsing the returned file.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class ToFromCrimespotting {

	/**
	 * String holding the beginning of the query to Oakland Crimespotting.
	 */
	private static final String CRIME_WEB_QUERY = 
		"http://oakland.crimespotting.org/crime-data?format=tsv";
	
	/**
	 * Number of different crime types.
	 */
	public static final int TYPE_NUMBER = 13;
	
	/**
	 * Number of columns in the returned tsv file.
	 */
	public static final int COLUMN_NUM = 13;
	
	/**
	 * The index tab number for the description of crime.
	 */
	public static final int DESC_NUM = 1;
	
	/**
	 * The index tab number for the date and time of crime.
	 */
	public static final int DATE_TIME_NUM = 2;
	
	/**
	 * The index tab number for crime type.
	 */
	public static final int CRIME_TYPE_NUM = 5;
	
	/**
	 * The index tab number for police beat of the crime.
	 */
	public static final int BEAT_NUM = 6;
	
	/**
	 * The index tab number for the address of the crime.
	 */
	public static final int ADDRESS_NUM = 8;
	
	/**
	 * The index tab number for the latitude of the crime.
	 */
	public static final int LATITUDE_NUM = 9;
	
	/**
	 * The index tab number for the longitude of the crime.
	 */
	public static final int LONGITUDE_NUM = 10; 

	/**
	 * West offset added to current longitude to be used for boundary box (bbox).
	 */
	private static final double WEST_OFFSET = 0.0082;
	
	/**
	 * South offset added to current latitude to be used for bbox.
	 */
	private static final double SOUTH_OFFSET = -0.0021;
	
	/**
	 * East offset added to current latitude for bbox.
	 */
	private static final double EAST_OFFSET = -WEST_OFFSET;
	
	/**
	 * North offset added to current longitude for bbox.
	 */
	private static final double NORTH_OFFSET = -SOUTH_OFFSET;
	
	/**
	 * Array for holding the split String returned by parsing the file line by line.
	 */
	public String[] crimesArray = new String[TYPE_NUMBER];

	/**
	 * String used to hold the user's query to Oakland Crimespotting.
	 */
	public static String userRequest;

	/**
     * Constructs the url address where data will be queried.
     * 
     * @param type the type of the crime
     * @param dStart crime to include after dStart
     * @param dEnd crime to include after dEnd
     * @param bbox the bounding box that includes only crime within radius
     * @return string representing the url address
     */
	public String constructQuery(String type, String dStart, String dEnd,
			String bbox) {
		String toReturn = CRIME_WEB_QUERY;
		if(!type.equals("")) {
			toReturn += "&type="+type;
		}
		if(!dStart.equals("")) {
			toReturn += "&dstart="+dStart;
		}
		if(!dEnd.equals("")) {
			toReturn += "&dEnd="+dEnd;
		}
		if(!bbox.equals("")) {
			toReturn += "&bbox="+bbox;
		}
		toReturn += "&count=50";
		return toReturn;
	}

	/**
	 * Constructs the bounding box.
	 * 
     * @param latitude current latitude
     * @param longtitude current longitude
     * @return bounding box parameters
     */
	public String constructBBoxString(double latitude, double longitude) {
		double west = longitude + WEST_OFFSET;
		double south = latitude + SOUTH_OFFSET;
		double east = longitude + EAST_OFFSET;
		double north = latitude + NORTH_OFFSET;
		return west+","+south+","+east+","+north;
	}

	/**
	 * Adds data crime to database.
	 * 
     * @param CrimeDbAdapter crime db that allows to query database
     * @param myArray array containing data of crime
     * @return true if added successfully, false otherwise
     */
	private static boolean putIntoDatabase(CrimeDbAdapter dbAdapter, String[] myArray) {
		long returnedFromInsert = dbAdapter.createCrime(myArray[DESC_NUM], 
				myArray[DATE_TIME_NUM], myArray[CRIME_TYPE_NUM], myArray[BEAT_NUM], 
				myArray[ADDRESS_NUM], Double.parseDouble(myArray[LATITUDE_NUM]), 
				Double.parseDouble(myArray[LONGITUDE_NUM]));
		return returnedFromInsert > 0;
	}

	/**
	 * Converts the data crime return by the url query to string a representation.
	 * 
     * @param CrimeDbAdapter crime db that allows to query database
     */
	public void parseCrimeData(CrimeDbAdapter dbAdapter) {
		try {
			URL url = new URL(constructQuery("","","",""));
			URLConnection connection = url.openConnection();
			connection.connect();
			connection.setDoOutput(true);
			String outputString = null;
			
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("string=" + outputString);
			out.close();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String strLine;
			br.readLine(); // skips first line since it is the column headers
			while((strLine = br.readLine()) != null) {
				crimesArray = strLine.split("\\t");
				if(putIntoDatabase(dbAdapter, crimesArray)) {
					System.out.println("Successfull insert of: " + crimesArray[0]);
				} else {
					System.err.println("Error: Could not insert " + crimesArray[0]);
				}
			}
			is.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}