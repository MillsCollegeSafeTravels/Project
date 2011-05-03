package edu.mills.cs180.safetravels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ToFromCrimespotting {

	private static final String CRIME_WEB_QUERY = "http://oakland.crimespotting.org/crime-data?format=tsv";
	public static final int TYPE_NUMBER = 13; //the number of different crime types
	public static final int COLUMN_NUM = 13; //the number of columns in the returned tsv file
	public static final int DESC_NUM = 1;
	public static final int DATE_TIME_NUM = 2;
	public static final int CRIME_TYPE_NUM = 5;
	public static final int BEAT_NUM = 6;
	public static final int ADDRESS_NUM = 8;
	public static final int LATITUDE_NUM = 9;
	public static final int LONGITUDE_NUM = 10;
	
	private static final double WEST_OFFSET = 0.0082;
	private static final double SOUTH_OFFSET = -0.0021;
	private static final double EAST_OFFSET = -WEST_OFFSET;
	private static final double NORTH_OFFSET = -SOUTH_OFFSET;
	public String[] crimesArray = new String[TYPE_NUMBER];
	
	public static String userRequest;
	
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
	
	public String constructBBoxString(double latitude, double longitude) {
		double west = longitude + WEST_OFFSET;
		double south = latitude + SOUTH_OFFSET;
		double east = longitude + EAST_OFFSET;
		double north = latitude + NORTH_OFFSET;
		return west+","+south+","+east+","+north;
	}
	
	/*public void parseCrimeData(CrimeDbAdapter dbAdapter) {
		try {
			//TODO not sure if need this stuff for url
			URLConnection connection = uri.toURL().openConnection();
			connection.connect();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			URL url = new URL(constructQuery("","","",""));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
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

			in.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}*/
	
	/*
	 * Uses CrimeDbAdapter to enter info into database
	 * We only want 7 of the 13 columns
	 */
	private static boolean putIntoDatabase(CrimeDbAdapter dbAdapter, String[] myArray) {
		long returnedFromInsert = dbAdapter.createCrime(myArray[DESC_NUM], 
				myArray[DATE_TIME_NUM], myArray[CRIME_TYPE_NUM], myArray[BEAT_NUM], 
				myArray[ADDRESS_NUM], Double.parseDouble(myArray[LATITUDE_NUM]), 
				Double.parseDouble(myArray[LONGITUDE_NUM]));
		return returnedFromInsert > 0;
	}

}
