package test.hfme.textrr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SendCallback;

import org.json.JSONException;

import java.util.Calendar;

import test.hfme.textrr.adapters.MessageAdapter;
import test.hfme.textrr.util.CommonUtil;
import test.hfme.textrr.util.Constants;
import test.hfme.textrr.util.ParseUtil;


public class MainActivity extends Activity implements LocationListener {

    private MessageAdapter mAdapter;
    private ListView mListView;
    private LocationManager mLocationManager;
    private Location mLocation;
    private String mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act_layout);

        initLocationManager();
        initListView();
        addListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextrrApplication.setMainActivityInFront(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextrrApplication.setMainActivityInFront(true);
    }

    private void showToast(int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show();
    }

    private void initLocationManager() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            mProvider = LocationManager.NETWORK_PROVIDER;
            showAlert(R.string.loc_off, R.string.loc_turn_on_message);
        } else {
            if (isNetworkEnabled) {
                mProvider = LocationManager.NETWORK_PROVIDER;
            } else if (isGPSEnabled) {
                mProvider = LocationManager.GPS_PROVIDER;
            }
        }
    }
    // check if location is 5 min fresh
//    mLocation=mLocationManager.getLastKnownLocation(mBestProvider);
//    if(mLocation!=null&&mLocation.getTime()>
//            Calendar.getInstance().

//    getTimeInMillis()

//    -5*60*1000)

    private void addListeners() {
        final Button sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isNetworkAvailable()) {
                    showToast(R.string.no_network);
                    return;
                }

                // check if loc is null or 5 min old
                if (mLocation == null || mLocation.getTime() <
                        Calendar.getInstance().getTimeInMillis() - (5 * 60 * 1000)) {
                    mLocation = mLocationManager.getLastKnownLocation(mProvider);
                    if (mLocation == null) {
                        showAlert(R.string.loc_off, R.string.loc_turn_on_message);
                        return;
                    }
                }
                final EditText messageBox = (EditText) findViewById(R.id.edit_msg);
                if (messageBox.getText().toString().length() == 0) {
                    return;
                }
                try {
                    Log.d(Constants.LOG_TAG, "Send clicked, sending push notification...");
                    sendButton.setEnabled(false);
                    ParsePush push = ParseUtil.getParsePushObject(messageBox.getText().toString(), mLocation);
                    push.sendInBackground(new SendCallback() {
                        @Override
                        public void done(ParseException e) {
                            messageBox.setText("");
                            sendButton.setEnabled(true);
                        }
                    });
                } catch (JSONException e) {
                    showToast(R.string.error);
                    sendButton.setEnabled(true);
                }
            }
        });
    }


    private void showAlert(int title, int message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(settingsIntent);
                    }
                })
                .create();
        dialog.show();
    }

    private void initListView() {
        mAdapter = new MessageAdapter(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                ParseQuery query = new ParseQuery(Constants.CLASS_MESSAGE);
                query.fromLocalDatastore();
                return query;
            }
        });
        mAdapter.setPaginationEnabled(false);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(Constants.LOG_TAG, "In onNewIntent()");
        mAdapter.loadObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Constants.LOG_TAG, "In onLocationChanged()");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
