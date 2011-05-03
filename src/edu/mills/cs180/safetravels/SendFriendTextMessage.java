package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendFriendTextMessage extends Activity implements OnLongClickListener {
	Button btnSendSMS;
	EditText txtPhoneNo;
	EditText txtMessage;
	private static final int PICK_CONTACT = 1010;
	private static final String DEBUG_TAG = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendtxt);        

		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);
		txtPhoneNo.setOnLongClickListener(this);
		txtMessage.setText(R.string.track_me_message);
		btnSendSMS.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{                
				String phoneNo = txtPhoneNo.getText().toString();
				String message = txtMessage.getText().toString();                 
				if (phoneNo.length()>0 && message.length()>0){                
					sendSMS(phoneNo, message);  
					finish();
				}
				else
					Toast.makeText(getBaseContext(), 
							"Please enter both phone number and message.", 
							Toast.LENGTH_SHORT).show();
			}
		});        
	} 
	@Override
	public boolean onLongClick(View v){

		if(v.getId()==R.id.txtPhoneNo){
			Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(intent, PICK_CONTACT);
			return true;
		}
		else return false;
	}
	//---sends an SMS message to another device---
	private void sendSMS(String phoneNumber, String message)
	{        
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				new Intent(this, SendFriendTextMessage.class), 0);                
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{	
		//may be error if there is no contacts
		if (resultCode == RESULT_OK){
			switch(requestCode){
			case PICK_CONTACT:
				Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
				cursor.moveToNext();
				String contactId = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
				String  name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
				//String number = cursor.getString(cursor.getColumnIndexOrThrow(Phone.DATA));
				//cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID +"=?", new String[]{contactId}, null);
				//int phoneIdx = cursor.getColumnIndexOrThrow(Phone.DATA);
				//String phoneNo = cursor.getString(phoneIdx);
				//txtPhoneNo.setText(name+  ","+contactId;
				//Toast.makeText(this, "Contect LIST  =  "+name, Toast.LENGTH_LONG).show(); 
				//break;
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
		                        null, Phone.CONTACT_ID + "=?", new String[] { id },
		                        null);
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
		            txtPhoneNo.setText(number);
					Toast.makeText(this, "Contect LIST  =  "+name, Toast.LENGTH_LONG).show(); 
					break;

			}
		}else{
			Toast.makeText(this, "Activity Result Error", Toast.LENGTH_LONG).show(); 
		}
	}//onActivityResult
}
