package test.hfme.textrr.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.Date;

import test.hfme.textrr.R;
import test.hfme.textrr.TextrrApplication;
import test.hfme.textrr.util.Constants;

public class MessageAdapter extends ParseQueryAdapter<ParseObject> {

    public MessageAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.message_item, null);
        }

        long epoch = object.getLong(Constants.KEY_TIME);
        Date date = new Date(epoch);

        TextView senderTextView = (TextView) v.findViewById(R.id.sender);
        TextView messageTextView = (TextView) v.findViewById(R.id.message);
        TextView sentTimeTextView = (TextView) v.findViewById(R.id.sent_time);

        senderTextView.setText(object.getString(Constants.KEY_SENDER));
        messageTextView.setText(object.getString(Constants.KEY_MSG));
        sentTimeTextView.setText(date.getHours() + ":" + date.getMinutes());

        if (object.getString(Constants.KEY_SENDER).equals(TextrrApplication.getEmail())) {
            senderTextView.setGravity(Gravity.RIGHT);
            messageTextView.setGravity(Gravity.RIGHT);
            sentTimeTextView.setGravity(Gravity.RIGHT);
        } else {
            senderTextView.setGravity(Gravity.LEFT);
            messageTextView.setGravity(Gravity.LEFT);
            sentTimeTextView.setGravity(Gravity.LEFT);
        }
        return v;
    }
}
