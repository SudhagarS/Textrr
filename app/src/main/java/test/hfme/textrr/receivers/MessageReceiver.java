package test.hfme.textrr.receivers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import test.hfme.textrr.MainActivity;
import test.hfme.textrr.R;
import test.hfme.textrr.TextrrApplication;
import test.hfme.textrr.util.Constants;
import test.hfme.textrr.util.ParseUtil;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            ParseUtil.saveToLocalStore(json);

            if (TextrrApplication.isMainActivityInFront()) {
                // trigger onNewIntent in main activity to refresh the listview
                Intent mainActivityIntent = new Intent(context, MainActivity.class);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainActivityIntent);
            } else {
                sendNotification(context, json);
            }

            // DEBUG
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(Constants.LOG_TAG, "..." + key + " => " + json.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(Context context, JSONObject json) throws JSONException {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

        Notification n = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(json.getString(Constants.KEY_MSG))
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();

        notificationManager.notify(999, n);

    }
}
