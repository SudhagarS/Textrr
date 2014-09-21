package test.hfme.textrr.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "textrr_messages";

    private static final String TABLE_MESSAGES = "texttrr_messages_table";

    private static final String KEY_ID = "id";

    private static final String KEY_SENDER = "sender";
    private static final String KEY_MSG = "message";
    private static final String KEY_TIME = "sent_time";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String WORDS_TABLE_CREATEQ = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SENDER + " TEXT,"
                + KEY_MSG + " TEXT,"
                + KEY_TIME + " TEXT" + ")";

        initMsgTableWithTempData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    private void initMsgTableWithTempData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_SENDER, "9500675292");
        values.put(KEY_MSG, "This is me sending first message");
        values.put(KEY_TIME, System.currentTimeMillis());

        db.insert(TABLE_MESSAGES, null, values);

        values = new ContentValues();
        values.put(KEY_SENDER, "9500672322");
        values.put(KEY_MSG, "This is me sending second message");
        values.put(KEY_TIME, System.currentTimeMillis());

        db.insert(TABLE_MESSAGES, null, values);

        values = new ContentValues();
        values.put(KEY_SENDER, "9555675292");
        values.put(KEY_MSG, "This is me sending third message");
        values.put(KEY_TIME, System.currentTimeMillis());

        db.insert(TABLE_MESSAGES, null, values);

        values = new ContentValues();
        values.put(KEY_SENDER, "9520675292");
        values.put(KEY_MSG, "Parse FTW finie message");
        values.put(KEY_TIME, System.currentTimeMillis());

        db.insert(TABLE_MESSAGES, null, values);
    }
}
