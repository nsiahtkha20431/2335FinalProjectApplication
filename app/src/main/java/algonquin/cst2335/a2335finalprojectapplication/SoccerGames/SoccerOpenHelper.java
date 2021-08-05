package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SoccerOpenHelper extends SQLiteOpenHelper {
    public static final String name = "TheDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Favorites";

    public SoccerOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
