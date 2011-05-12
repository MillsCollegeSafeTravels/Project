package edu.mills.cs180.safetravels;

import android.content.ContentValues;

public class CrimeData {

    public static final int CRIME_NUMBER = 13; // the number of different crime
                                               // types
    public static int[] countsOfCrimes = new int[CRIME_NUMBER];

    public static void enterTestData(CrimeDbAdapter adapter) {
        adapter.createCrime("Robbery", "blah1", "2011-03-11", 34.1111, 35.1111);
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
        adapter.createCrime("Vandalism", "blah15", "2011-03-11", 34.1111, 35.1111);
    }

    /*
     * TODO: change for crime database; need an additional class(es) that will
     * get info from the tsv/csv file and info used to populate table
     */

}// CrimeData
