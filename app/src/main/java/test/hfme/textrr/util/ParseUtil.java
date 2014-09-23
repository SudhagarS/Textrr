package test.hfme.textrr.util;

import android.location.Location;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import test.hfme.textrr.TextrrApplication;

public class ParseUtil {

    public static ParsePush getParsePushObject(String message, Location location) throws JSONException {
        JSONObject data = new JSONObject();
        data.put(Constants.KEY_ACTION, Constants.INTENT_ACTION);
        data.put(Constants.KEY_MSG, message);
        // Not possible to get mobile no in some cases, falling back to using email id
        data.put(Constants.KEY_SENDER, TextrrApplication.getEmail());
        data.put(Constants.KEY_TIME, System.currentTimeMillis());
        data.put(Constants.KEY_LAT, location.getLatitude());
        data.put(Constants.KEY_LONGI, location.getLongitude());
        ParsePush push = new ParsePush();
        push.setChannel(Constants.CHANNEL_DEFAULT);
//        push.setChannel(Constants.CHANNEL_TESTING);
        push.setData(data);
        return push;
    }

    public static void saveToLocalStore(JSONObject data) throws ParseException, JSONException {
        ParseObject pObj = new ParseObject(Constants.CLASS_MESSAGE);
        pObj.put(Constants.KEY_MSG, data.getString(Constants.KEY_MSG));
        pObj.put(Constants.KEY_SENDER, data.getString(Constants.KEY_SENDER));
        pObj.put(Constants.KEY_TIME, data.getLong(Constants.KEY_TIME));
        pObj.pin();
    }
}
