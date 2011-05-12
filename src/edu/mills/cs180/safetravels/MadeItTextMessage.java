package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * MadeItTextMessage is an activity that allows the user to send a pre-set SMS message indicating 
 * the user has reached his/her destined location to contacts.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class MadeItTextMessage extends Activity implements OnLongClickListener, OnClickListener{
	
	/**
	 * A button View for sending an SMS message.
	 */
	private View mBtnSendSMS;
	
	/**
	 * An EditText object for the destined phone number.
	 */
	private EditText mTxtPhoneNo;
	
	/**
	 * An EditText object for the text of the message.
	 */
	private EditText mTxtMessage;
	
	/**
	 * An integer for the selected contact. Initial value is 1010. 
	 */
	private static final int PICK_CONTACT = 1010;
	
	/**
	 * Debug Tag for logging debug output to LogCat.
	 */
	private static final String DEBUG_TAG = null;
	
	/**
	 * Called when the activity is starting. Sets the onClickListeners of the View for the phone 
	 * number and the send SMS button.
	 * 
	 * @param savedInstanceState if the activity is being re-initialized after previously being 
	 * shut down then this Bundle contains the data it most recently supplied in 
	 * onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendtxt);
		
		//TO DO:if tracking ask if want to edit message
		//TO DO: if yes populate phone number and message
		//TO DO: if no populate phone number and message and auto send
		mTxtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		mTxtMessage = (EditText) findViewById(R.id.txtMessage);
		mTxtPhoneNo.setOnLongClickListener(this);
		mTxtMessage.setText(R.string.made_it_text_message);
		mBtnSendSMS = findViewById(R.id.btnSendSMS);
		mBtnSendSMS.setOnClickListener(this); 
	}//onCreate
	
	/**
	 * Called when a View is clicked and held. Gets the contacts if the phone number box was the 
	 * View v parameter and starts an activity for picking the contacts.
	 * 
	 * @param v the View that was clicked
	 * @return true if the contacts activity was successfully started
	 */
	@Override
	public boolean onLongClick(View v){
		if(v.getId()==R.id.txtPhoneNo){
			Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(intent, PICK_CONTACT);
			return true;
		}else return false;	
	}//onLongClick
	
	/**
	 * Sends an SMS message to another device.
	 * 
	 * @param phoneNumber the String holding the phone number to send the message to
	 * @param message the String holding the message to be sent
	 */
	private void sendSMS(String phoneNumber, String message){        
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				new Intent(this, MadeItTextMessage.class), 0);                
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	}//sendSMS
	
	/**
	 * Called when an activity you launched exits. Allows the user to pick the contacts to send a 
	 * SMS to and returns the data to this activity.
	 * 
	 * @param requestCode the integer request code originally supplied to startActivityForResult(), 
	 * allowing you to identify who this result came from
	 * @param resultCode The integer result code returned by the child activity through its 
	 * setResult()
	 * @param intent an Intent, which can return result data to the caller
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){	
		//may be error if there is no contacts
		if (resultCode == RESULT_OK){
			switch(requestCode){
			case PICK_CONTACT:
				Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
				cursor.moveToNext();
				String name = cursor.getString(cursor.getColumnIndexOrThrow
				        (ContactsContract.Contacts.DISPLAY_NAME));
				cursor = null;
				String number = "";
				try {
					Uri result = intent.getData();
					Log.v(DEBUG_TAG, "Got a contact result: "
							+ result.toString());
					// get the contact id from the Uri
					String id = result.getLastPathSegment();
					// query for phone number
					cursor = getContentResolver().query(Phone.CONTENT_URI,
							null, Phone.CONTACT_ID + "=?", new String[] { id },null);
					int phoneIdx = cursor.getColumnIndex(Phone.DATA);
					// get the phone number
					if (cursor.moveToFirst()) {
						number = cursor.getString(phoneIdx);
						Log.v(DEBUG_TAG, "Got number " + number);
					} else {
						Log.w(DEBUG_TAG, "No results");
					}
				} catch (Exception e) {
					Log.e(DEBUG_TAG, "Failed to get phone number data", e);
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
				mTxtPhoneNo.setText(number);
				Toast.makeText(this, "Contect LIST  =  "+name, Toast.LENGTH_LONG).show(); 
				break;
			}
		}else Toast.makeText(this, "Activity Result Error", Toast.LENGTH_LONG).show(); 
	}//onActivityResult
	
	/**
	 * Called when a View is clicked. If the send message button is clicked, the phone number and 
	 * message text are retrieved and sendSMS is called. The TrackingDisabler activity is then 
	 * started. 
	 * 
	 * @param v the View that was clicked
	 */
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnSendSMS:
			String phoneNo = mTxtPhoneNo.getText().toString();
			String message = mTxtMessage.getText().toString();                 
			if (phoneNo.length()>0 && message.length()>0){                
				sendSMS(phoneNo, message); 
				startActivity( new Intent(this,TrackingDisabler.class));
				finish();
			}
			else{
				//for testing
				startActivity( new Intent(this,TrackingDisabler.class));
				finish();
			}//else
		}//switch
	}//onClick
}//MadeItTextMessage
