package edu.mills.cs180.safetravels;

import android.content.ContentValues;


/**
 * CrimeData is a class that was used for testing the database functions.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class CrimeData {

	/**
	 * Number of different crime types
	 */
	public static final int CRIME_NUMBER = 13;
	
	/**
	 * Inserts test values into the crimes table. The test set includes 4 Robberies, 2 Arsons, 1 
	 * Murder, 6 Thefts, 1 Alcoholism, and 1 Vandalism. These values were chosen randomly. The rest 
	 *  of the information was simply repeated, except for the description.
	 * 
	 * @param adapter the CrimeDbAdapter to use for inserting the crimes
	 */
	public static void enterTestData(CrimeDbAdapter adapter) {
		/*adapter.createCrime("Robbery", "blah1", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Robbery", "blah2", "2011-03-14", 34.1111, 35.1111);
		adapter.createCrime("Robbery", "blah3", "2011-02-11", 34.1111, 35.1111);
		adapter.createCrime("Robbery", "blah4", "2011-04-20", 34.1111, 35.1111);
		adapter.createCrime("Arson", "blah5", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Arson", "blah6", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Murder", "blah7", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Theft", "blah8", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Theft", "blah9", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Theft", "blah10", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Theft", "blah11", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Theft", "blah12", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Theft", "blah13", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Alcohol", "blah14", "2011-03-11", 34.1111, 35.1111);
		adapter.createCrime("Vandalism", "blah15", "2011-03-11", 34.1111, 35.1111);*/
	}//enterTestData
	
}//CrimeData
