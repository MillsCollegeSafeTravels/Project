package edu.mills.cs180.safetravels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * CrimeDbAdapter handles the passage of information to and from the database.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class CrimeDbAdapter {

	//Columns in Crimes database
	/**
	 * Column name for the id column.
	 */
	public static final String ID = "_id";

	/**
	 * Column name for the description column.
	 */
	public static final String DESCRIPTION = "description";

	/**
	 * Column name for the date,time column.
	 */
	public static final String DATE_TIME = "date, time";

	/**
	 * Column name for the crime type column.
	 */
	public static final String CRIME_TYPE = "crime type";

	/**
	 * Column name for the police beat column.
	 */
	public static final String POLICE_BEAT = "police beat";

	/**
	 * Column name for the address column.
	 */
	public static final String ADDRESS = "address";

	/**
	 * Column name for the latitude column.
	 */
	public static final String LATITUDE = "latitude";

	/**
	 * Column name for the longitude column.
	 */
	public static final String LONGITUDE = "longitude";

	/**
	 * Tag for noting updated database.
	 */
	private static final String TAG = "CrimeDbAdapter";

	/**
	 * A DatabaseHelper for forming queries.
	 */
	private DatabaseHelper mDbHelper;

	/**
	 * A SQLiteDatabase to create the database.
	 */
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement.
	 */
	private static final String CRIMES_CREATE =
		"create table crimes (" +ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		DESCRIPTION + " text not null, " + 
		DATE_TIME + " text not null, " + 
		CRIME_TYPE + " text not null," +
		POLICE_BEAT + " text," +
		ADDRESS + " text," +
		LATITUDE + " DOUBLE NOT NULL, "+ 
		LONGITUDE + " DOUBLE NOT NULL);";

	/**
	 * Name of the database.
	 */
	private static final String DATABASE_NAME = "crimedata";

	/**
	 * Name of the database table to add to.
	 */
	private static final String DATABASE_TABLE = "crimes";

	/**
	 * Current version number of the database.
	 */
	private static int DATABASE_VERSION = 1;

	private final Context mCtx;

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx the Context within which to work
	 */
	public CrimeDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}//constructor

	/**
	 * Opens the crimes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException if the database could be neither opened or created
	 */
	public CrimeDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}//open

	/**
	 * Closes the database.
	 */
	public void close() {
		mDbHelper.close();
	}//close

	/**
	 * Adds a crime using the type and count provided. If the crime is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param type the type of the crime
	 * @param description the description of the crime
	 * @param dateTime the date,time of the crime
	 * @param crimeType the crime type of the crime
	 * @param policeBeat the police beat of the crime
	 * @param address the address of the crime
	 * @param latitude the latitude of the crime
	 * @param longitude the longitude of the crime
	 * @return rowId or -1 if failed
	 */
	public long createCrime(String description, String dateTime, String crimeType, 
			String policeBeat, String address, double latitude, 
			double longitude) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DESCRIPTION, description);
		initialValues.put(DATE_TIME, dateTime);
		initialValues.put(CRIME_TYPE, crimeType);
		initialValues.put(POLICE_BEAT, policeBeat);
		initialValues.put(ADDRESS, address);
		initialValues.put(LATITUDE, latitude);
		initialValues.put(LONGITUDE, longitude);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}//createCrime

	/**
	 * Deletes the crime with the given rowId.
	 * 
	 * @param rowId id of crime to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteCrime(long rowId) {
		return mDb.delete(DATABASE_TABLE, ID + "=" + rowId, null) > 0;
	}//deleteCrime

	/**
	 * Deletes all crimes in the table.
	 * 
	 * @return true if all deleter, false otherwise
	 */
	public boolean deleteAll() {
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}//deleteCrime

	//TODO: check if really need this method since can use cursor's method
	/**
	 * Counts the number of rows with the given type.
	 * 
	 * @param type type of the crimes to count
	 * @return count of crimes
	 *//*
    public Cursor countByType(String type) {
    	String queryCount = "select count(*) from " + DATABASE_TABLE
    		+ " where " + TYPE + "=" + type + ";";
    	return mDb.rawQuery(queryCount, null);
    }*/

	/**
	 * Returns a Cursor over the list of all crimes in the database
	 * with all information.
	 * 
	 * @return Cursor over all crimes
	 */
	public Cursor fetchAllInfo() {
		return mDb.query(DATABASE_TABLE, null, null, 
				null, null, null, null);
	}//fetchAllCrimes

	/**
	 * Returns a Cursor over the list of all crimes in the database
	 * with only id and type information.
	 * 
	 * @return Cursor over all crimes
	 */
	public Cursor fetchAllType() {
		return mDb.query(DATABASE_TABLE, new String[] {ID,CRIME_TYPE}, 
				null, null, null, null, null);
	}//fetchAllCrimesType

	/**
	 * Returns a Cursor over the list of all crimes in the database
	 * with only id, description, type,latitude, and longitude information
	 * 
	 * @return Cursor over all crimes
	 */
	public Cursor fetchAllDescTypeLatLong() {
		return mDb.query(DATABASE_TABLE, new String[] {ID, DESCRIPTION,
				CRIME_TYPE, LATITUDE, LONGITUDE}, null, null, null, 
				null, null);
	}//fetchAllCrimesTypeLatLong

	/**
	 * Returns a Cursor positioned at the crimes that matches the given type
	 * 
	 * @param type type of crime to retrieve
	 * @return Cursor positioned to matching crime, if found
	 * @throws SQLException if crime could not be found/retrieved
	 */
	public Cursor fetchByType(String crimeType) throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, new String[] {ID, 
					DESCRIPTION, DATE_TIME, LATITUDE, LONGITUDE}, 
					CRIME_TYPE + "=" + crimeType, null, null, null, 
					null, null);
		return mCursor;
	}//fetchCrimesByType

	/**
	 * Updates the crime using the details provided. The crime to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param type the type of the crime
	 * @param description the description of the crime
	 * @param dateTime the date,time of the crime
	 * @param crimeType the crime type of the crime
	 * @param policeBeat the police beat of the crime
	 * @param address the address of the crime
	 * @param latitude the latitude of the crime
	 * @param longitude the longitude of the crime
	 * @return true if the crime was successfully updated, false otherwise
	 */
	public boolean updateCrime(long rowId,  String description, String dateTime,
			String crimeType, String policeBeat, String address, 
			double latitude, double longitude) {
		ContentValues args = new ContentValues();
		args.put(DESCRIPTION, description);
		args.put(DATE_TIME, dateTime);
		args.put(CRIME_TYPE, crimeType);
		args.put(POLICE_BEAT, policeBeat);
		args.put(ADDRESS, address);
		args.put(LATITUDE, latitude);
		args.put(LONGITUDE, longitude);

		return mDb.update(DATABASE_TABLE, args, ID + "=" + rowId, null) > 0;
	}//updateCrime

	/** 
	 * DatabaseHelper is a class that allows for querying to the crimesdata database.
	 * 
	 * @author Kate Feeny
	 * @author Jess Martin
	 * @author TeAirra Ward
	 * @author Jodessa Lanzadares
	 * @author Dani E-F
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * Class Constructor that takes a context.
		 * 
		 * @param ctx the Context within which to work
		 */
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}//constructor

		/**
		 * Executes the query to create the table in the database.
		 * 
		 * @param db the database to query
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CRIMES_CREATE);
		}//onCreate

		/**
		 * Upgrades the database by dropping the crimes table and calling onCreate.
		 * 
		 * @param db the database to query
		 * @param oldVersion the former version number of the database
		 * @param newVersion the new version number of the database
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS crimes");
			onCreate(db);
		}//onUpgrade
	}//DatabaseHelper

}//CrimeDbAdapter