package test.hfme.textrr.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import test.hfme.textrr.TextrrApplication;

public class CommonUtil {

    public static String getMobileNumber() {
        TelephonyManager tm = (TelephonyManager)
                TextrrApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(Constants.LOG_TAG, "Phone number is " + tm.getLine1Number());
        return tm.getLine1Number();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager)
                TextrrApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getEmailOrEmptyStr() {
        AccountManager manager = (AccountManager)
                TextrrApplication.getAppContext().getSystemService(Context.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        String gmail = "";

        for(Account account: list)
        {
            if(account.type.equalsIgnoreCase("com.google"))
            {
                gmail = account.name;
                break;
            }
        }
        return gmail;
    }
}
