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

public class DangerTextMessage extends Activity implements OnLongClickListener {
	    private Button mBtnSendSMS;
	    private EditText mTxtPhoneNo;
	    private EditText mTxtMessage;
	    private int PICK_CONTACT;
	    /** Called when the activity is first created. */
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
	            }
	        });        
	    } 
		@Override
		public boolean onLongClick(View v){
			if(v.getId()==R.id.txtPhoneNo){
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
				startActivityForResult(intent, PICK_CONTACT);
				return true;
			}else return false;
		}
	    //---sends an SMS message to another device---
	    private void sendSMS(String phoneNumber, String message){        
	        PendingIntent pi = PendingIntent.getActivity(this, 0,
	            new Intent(this, DangerTextMessage.class), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	    }
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