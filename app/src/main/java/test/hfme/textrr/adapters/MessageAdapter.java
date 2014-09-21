package test.hfme.textrr.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.Date;

import test.hfme.textrr.R;
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

        ((TextView) v.findViewById(R.id.sender)).setText(object.getString(Constants.KEY_SENDER));
        ((TextView) v.findViewById(R.id.message)).setText(object.getString(Constants.KEY_MSG));
        ((TextView) v.findViewById(R.id.sent_time)).setText(date.getHours() + ":" + date.getMinutes());
        // TODO change color if email matches with this user's email
        return v;
    }
}
