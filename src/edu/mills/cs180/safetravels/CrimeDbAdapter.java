package edu.mills.cs180.safetravels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CrimeDbAdapter {

	public static final String TABLE1_NAME = "crimes";

	//Columns in Crimes database
	public static final String ID = "_id";
	public static final String TYPE = "type";
	public static final String DESCRIPTION = "description";
	public static final String DATE = "date";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String COUNT = "count";
	
	private static final String TAG = "CrimeDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	
	/**
     * Database creation sql statement
     */
    private static final String CRIMES_CREATE =
        "create table crimes (_id integer primary key autoincrement, "
        + "type text not null, desc text not null, date text not null, "
        + "lat real not null, long real not null);";

    private static final String DATABASE_NAME = "crimedata";
    private static final String DATABASE_TABLE = "crimes";
    private static final int DATABASE_VERSION = 1;

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
     * Open the crimes database. If it cannot be opened, try to create a new
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

    public void close() {
        mDbHelper.close();
    }//close
    
    /**
     * Add crime using the type and count provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param type the type of the crime
     * @param description the description of the crime
     * @return rowId or -1 if failed
     */
    public long createCrime(String type, String description, String date,
    		double latitude, double longitude) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TYPE, type);
        initialValues.put(DESCRIPTION, description);
        initialValues.put(DATE, date);
        initialValues.put(LATITUDE, latitude);
        initialValues.put(LONGITUDE, longitude);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }//createCrime
    
    /**
     * Delete the crime with the given rowId
     * 
     * @param rowId id of crime to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteCrime(long rowId) {
        return mDb.delete(DATABASE_TABLE, ID + "=" + rowId, null) > 0;
    }//deleteCrime
    
    /**
     * Delete all crimes in the table
     */
    public boolean deleteAllCrimes() {
        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }//deleteCrime
    
    /**
     * Count the number of rows with the given type
     * 
     * @param type type of the crimes to count
     * @return int count of crimes
     */
    public Cursor countByType(String type) {
    	String queryCount = "select count(*) from " + DATABASE_TABLE
    		+ " where " + TYPE + "=" + type + ";";
    	return mDb.rawQuery(queryCount, null);
    }
    
    /**
     * Return a Cursor over the list of all crimes in the database
     * with all information
     * 
     * @return Cursor over all crimes
     */
    public Cursor fetchAllCrimesAllInfo() {
        return mDb.query(DATABASE_TABLE, new String[] {ID, TYPE,
                DESCRIPTION, DATE, LATITUDE, LONGITUDE}, null, 
                null, null, null, null);
    }//fetchAllCrimes
    
    /**
     * Return a Cursor over the list of all crimes in the database
     * with only id and type information
     * 
     * @return Cursor over all crimes
     */
    public Cursor fetchAllCrimesType() {
        return mDb.query(DATABASE_TABLE, new String[] {ID, TYPE}, null, 
                null, null, null, null);
    }//fetchAllCrimesType
    
    /**
     * Return a Cursor over the list of all crimes in the database
     * with only id, type,latitude, and longitude information
     * 
     * @return Cursor over all crimes
     */
    public Cursor fetchAllCrimesTypeLatLong() {
        return mDb.query(DATABASE_TABLE, new String[] {ID, TYPE, LATITUDE, LONGITUDE}, null, 
                null, null, null, null);
    }//fetchAllCrimesTypeLatLong
    
    
    
    /**
     * Return a Cursor positioned at the crimes that matches the given type
     * 
     * @param type type of crime to retrieve
     * @return Cursor positioned to matching crime, if found
     * @throws SQLException if crime could not be found/retrieved
     */
    public Cursor fetchCrimesByType(String type) throws SQLException {
        Cursor mCursor =
            mDb.query(true, DATABASE_TABLE, new String[] {ID, 
            		DESCRIPTION, DATE, LATITUDE, LONGITUDE}, TYPE 
            		+ "=" + type, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }//fetchCrimesByType
    
    /**
     * Update the crime using the details provided. The crime to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of crime to update
     * @param title value to set crime type to
     * @param description value to set crime description to
     * @param date value to set crime date to
     * @param latitude value to set crime latitude to
     * @param longitude value to set crime longitude to
     * @return true if the crime was successfully updated, false otherwise
     */
    public boolean updateCrime(long rowId, String type, String description,
    		String date, double latitude, double longitude) {
        ContentValues args = new ContentValues();
        args.put(TYPE, type);
        args.put(DESCRIPTION, description);
        args.put(DATE, date);
        args.put(LATITUDE, latitude);
        args.put(LONGITUDE, longitude);
        
        return mDb.update(DATABASE_TABLE, args, ID + "=" + rowId, null) > 0;
    }//updateCrime
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }//constructor

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CRIMES_CREATE);
        }//onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS crimes");
            onCreate(db);
        }//onUpgrade
	}//DatabaseHelper
	
}//CrimeDbAdapter