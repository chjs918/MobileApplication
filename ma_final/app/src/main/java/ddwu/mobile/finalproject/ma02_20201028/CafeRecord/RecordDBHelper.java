package ddwu.mobile.finalproject.ma02_20201028.CafeRecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class RecordDBHelper extends SQLiteOpenHelper {
    private final static String TAG = "RecordDBHelper";

    private final static String DB_NAME = "record_db";
    public final static String TABLE_NAME = "record_table";
    public final static String ID = "_id";
    public final static String CAFE_NAME = "cafe_name";
    public final static String LOCATION = "location";
    public final static String CONTENT = "content";
    public final static String IMG = "imgPath";
    public final static String DESK = "desk";
    public final static String POWER = "power";
    public final static String STAR = "star";


    public RecordDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLE_NAME + " (" + ID + " integer primary key autoincrement, "
                 + CAFE_NAME + " TEXT, " + LOCATION + " text, " + CONTENT + " text, "
                + IMG + " TEXT, " + DESK + " TEXT, "
                + POWER + " TEXT, " + STAR + " TEXT)";
        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

