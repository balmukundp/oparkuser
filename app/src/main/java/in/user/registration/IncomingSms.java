package in.user.registration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by daffodil on 10/23/2018.
 */

public class IncomingSms extends BroadcastReceiver {

    private final String TAG = "IncomingSms";
    private final String SMS_BULDLE = "pdus";
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get(SMS_BULDLE);

                if (pdusObj != null) {

                    for (Object apdusObj : pdusObj) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) apdusObj);
                        String message = currentMessage.getDisplayMessageBody();

                        Intent myIntent = new Intent("Message_Receiver");
                        myIntent.putExtra("message", message);
                        context.sendBroadcast(myIntent);

                    }
                }// end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}
