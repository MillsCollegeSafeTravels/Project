package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * DangerTextMessage is an activity that allows the user to send a pre-set SMS message indicating 
 * the user is in danger to his/her contacts.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class DangerTextMessage extends Activity implements OnLongClickListener {

	/**
	 * A button for sending an SMS message.
	 */
	private Button mBtnSendSMS;

	/**
	 * An EditText object for the destined phone number.
	 */
	private EditText mTxtPhoneNo;

	/**
	 * An EditText object for the text of the message.
	 */
	private EditText mTxtMessage;

	/**
	 * An integer for the selected contact. 
	 */
	private int PICK_CONTACT;

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
		mBtnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		mTxtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		mTxtMessage = (EditText) findViewById(R.id.txtMessage);
		mTxtPhoneNo.setOnLongClickListener(this);
		mBtnSendSMS.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){                
				String phoneNo = mTxtPhoneNo.getText().toString();
				String message = mTxtMessage.getText().toString();                 
				if (phoneNo.length()>0 && message.length()>0)                
					sendSMS(phoneNo, message);                
				else
					Toast.makeText(getBaseContext(), 
							"Please enter both phone number and message.", 
							Toast.LENGTH_SHORT).show();
			}//onClick
		});        
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
			Intent intent = new Intent(Intent.ACTION_PICK);
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
				new Intent(this, DangerTextMessage.class), 0);                
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
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		//may be error if there is no contacts
		if (requestCode == PICK_CONTACT){         
			Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
			cursor.moveToNext();
			String contactId = cursor.getString(cursor.getColumnIndex
					(ContactsContract.Contacts._ID));
			String  name = cursor.getString(cursor.getColumnIndexOrThrow
					(ContactsContract.Contacts.DISPLAY_NAME)); 
			Toast.makeText(this, "Contect LIST  =  "+name, Toast.LENGTH_LONG).show(); 
		}
	}//onActivityResult
}