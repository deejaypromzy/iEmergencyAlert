package com.prince.dj.iemergencyalert;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

public class smsService extends Service{

    Context mContext=this ;
    private SharedPreferences prefs;
    String fullName, snmBody,lastLocation,myLong,mylat,emergency;
    String ec1,ec2,ec3;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public smsService() {


    }

    @Override
    public void onCreate() {
        super.onCreate();




        //sendSMS(ec1,snmBody);
       //sendSMS(ec2,snmBody);
      // sendSMS(ec3,snmBody);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        fullName = prefs.getString("fname", "") + "  " +  prefs.getString("lname", "");
        lastLocation= prefs.getString("location",  "Oyibi-Accra @ Valley View Uni.");
        emergency= prefs.getString("emergency",  "0247018765");



        myLong= prefs.getString("long",  "1234544245");
        mylat= prefs.getString("lat",  "0247844452");

        final String EphoneNo1 = prefs.getString("num1", "self:0247018765");
       // final String EphoneNo2 = prefs.getString("num2", "self:0247018765");
       // final String EphoneNo3 = prefs.getString("num3", "self:0247018765");

        String arr1[] = EphoneNo1.split(":");
        ec1  = arr1[1].replaceAll("[^0-9]", "").trim();
      //  String arr2[] = EphoneNo2.split(":");
     //   ec2  = arr2[1].replaceAll("[^0-9]", "").trim();
     //   String arr3[] = EphoneNo3.split(":");
      //  ec3  = arr3[1].replaceAll("[^0-9]", "").trim();
       // String last_Location = prefs.getString("Last_location", "N/A");
        snmBody = "URGENT (SOS)!!! needs help"+ lastLocation +  "\n" + "Google Map Link:" + "https://www.google.co.in/maps/place/" + mylat+ "," + myLong;
      Toast.makeText(getBaseContext(), snmBody,Toast.LENGTH_SHORT).show();
      Toast.makeText(getBaseContext(), lastLocation,Toast.LENGTH_SHORT).show();




      if (lastLocation.toLowerCase().trim().contains("adeiso")){
          ec1="0299207853";
          //ec1="0240741137";
      }else if (lastLocation.toLowerCase().trim().contains("dodowa")){
          ec1="0245206702";

      }else if (lastLocation.toLowerCase().trim().contains("asamankese")){
          ec1="0505569332";

         // ec1="0240741137";

      }

      sendSMS(ec1,snmBody);
      sendSMS(emergency,snmBody);


         stopSelf();
       return START_STICKY;
    }

    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
                new Intent(mContext, SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
                new Intent(mContext, SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);
            Toast.makeText(getBaseContext(), "Sending SMS to police ..."+phoneNumber,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed..."+phoneNumber,Toast.LENGTH_SHORT).show();
        }

    }
}
