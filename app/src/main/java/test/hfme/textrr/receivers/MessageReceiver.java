package test.hfme.textrr.receivers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import test.hfme.textrr.MainActivity;
import test.hfme.textrr.R;
import test.hfme.textrr.TextrrApplication;
import test.hfme.textrr.util.Constants;
import test.hfme.textrr.util.ParseUtil;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.LOG_TAG, "MessageReceiver, onReceive()...");
        Location location = getLocationOrNull();
        // if location not available ignore message
        if (location == null) {
            return;
        }

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            if (distance(location.getLatitude(), location.getLongitude(),
                    json.getDouble(Constants.KEY_LAT), json.getDouble(Constants.KEY_LONGI)) > 100) {
                return;
            }
            ParseUtil.saveToLocalStore(json);

            if (TextrrApplication.isMainActivityInFront()) {
                // trigger onNewIntent in main activity to refresh the listview
                Intent mainActivityIntent = new Intent(context, MainActivity.class);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainActivityIntent);
            } else {
                sendNotification(context, json);
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

    private Location getLocationOrNull() {
        LocationManager locationManager = (LocationManager)
                TextrrApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else if (isGPSEnabled) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist * 1.609344;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
