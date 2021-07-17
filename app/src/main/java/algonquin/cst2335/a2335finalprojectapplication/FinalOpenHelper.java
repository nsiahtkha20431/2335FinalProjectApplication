package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FinalOpenHelper extends SQLiteOpenHelper {

    public static final String NAME = "FinalProjectDatabase";
    public static final int VERSION = 1;
    public static final String OCTRANSPO_TABLE_NAME = "BusStops";
    public static final String OCT_COL_ID = "ID";
    public static final String OCT_COL_NO = "BusStopNo";
    public static final String OCT_COL_DESC = "BusStopDescription";

    public FinalOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + OCTRANSPO_TABLE_NAME + "(" + OCT_COL_ID + " VARCHAR PRIMARY KEY, " +
                OCT_COL_NO + " INTEGER, " + OCT_COL_DESC + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OCTRANSPO_TABLE_NAME);
        onCreate(db);
    }
}