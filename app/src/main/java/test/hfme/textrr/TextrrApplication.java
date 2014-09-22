package test.hfme.textrr;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.PushService;

import test.hfme.textrr.util.CommonUtil;
import test.hfme.textrr.util.Constants;

public class TextrrApplication extends Application {

    private static Context appContext;
    private static String email;
    private static boolean mainActivityInFront;

    public TextrrApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_id));
        PushService.subscribe(this, Constants.CHANNEL_TESTING, MainActivity.class);
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static String getEmail() {
        if (email == null) {
            email = CommonUtil.getEmailOrEmptyStr();
        }
        return email;
    }

    public static boolean isMainActivityInFront() {
        return mainActivityInFront;
    }

    public static void setMainActivityInFront(boolean mainActivityInFront) {
        TextrrApplication.mainActivityInFront = mainActivityInFront;
    }
}
