package test.hfme.textrr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import test.hfme.textrr.adapters.MessageAdapter;
import test.hfme.textrr.util.CommonUtil;
import test.hfme.textrr.util.Constants;
import test.hfme.textrr.util.ParseUtil;


public class MainActivity extends Activity {

    private MessageAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act_layout);

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

    private void addListeners() {
        Button sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isNetworkAvailable()) {
                    showToast(R.string.no_network);
                    return;
                }
                final EditText messageBox = (EditText) findViewById(R.id.edit_msg);
                try {
                    Log.d(Constants.LOG_TAG, "Send clicked, sending push notification...");
                    ParsePush push = ParseUtil.getParsePushObject(messageBox.getText().toString());
                    push.sendInBackground(new SendCallback() {
                        @Override
                        public void done(ParseException e) {
                            messageBox.setText("");
                        }
                    });
                } catch (JSONException e) {
                    showToast(R.string.error);
                }
            }
        });
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
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(Constants.LOG_TAG, "In onNewIntent()");
        mAdapter.loadObjects();
        mListView.setSelection(mAdapter.getCount() - 1);
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
}
