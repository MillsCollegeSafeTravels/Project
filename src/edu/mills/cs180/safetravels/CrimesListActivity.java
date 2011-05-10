package edu.mills.cs180.safetravels;

import java.util.Random;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CrimesListActivity extends ListActivity {
	public static final int SHOW_MAP_ID = Menu.FIRST;

	private static final int END = 25; //for demo purposes

	private CrimeDbAdapter mDbHelper;

	private CharSequence defaultItemMsg = "Will load layout of activity for "
		+ "type of crime selected.";
	private CharSequence defaultMenuMsg = "Will take to map view with crimes "
		+ "shown.";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDbHelper = new CrimeDbAdapter(this);
		mDbHelper.open();
		//TODO: replace below method with method to populate with real data
		//enterTestingData(mDbHelper);
		
		String[] crimeNames = getResources().getStringArray(R.array.crimes_array);
		String[] crimesAndCount = addCounts(crimeNames);
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.crime_row, crimesAndCount));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		//TODO: Change to load a layout file for new Activity w/ list of types
		//of crime selected
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDefaultMsg(defaultItemMsg);
			}
		});
	}//onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SHOW_MAP_ID, 0, R.string.menu_see_on_map);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SHOW_MAP_ID:
			showDefaultMsg(defaultMenuMsg);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDefaultMsg(CharSequence msg) {
		// When clicked, show a toast with the default text
		Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_SHORT).show();
	}

	private String[] addCounts(String[] theCrimeNames) {
		/*TODO:put back in
		 * int j;
		 * for(int i=0;i<crimes.length;i++){
            Cursor c = mDbHelper.fetchByType(crimes[i]);
            c.moveToFirst();
            j = c.getCount();
            c.close();
            crimesAndCount[i] = crimes[i] + "(" + j +")";
    	}*/
		
		//for demo purposes
		String[] temp = new String[CrimeData.CRIME_NUMBER];
		Random random = new Random();
		for(int i=0;i<CrimeData.CRIME_NUMBER;i++){
			temp[i] = theCrimeNames[i] + " (" + random.nextInt(END) + ")";
		}
		return temp;
	}

	private void enterTestingData(CrimeDbAdapter adapter) {
		CrimeData.enterTestData(adapter);
	}

}//CrimesListActivity
