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

    public static final String MOVIE_TABLE_NAME = "SavedMovieInformation";
    public static final String movie_title = "Title";
    public static final String movie_year = "Year";
    public static final String movie_rating = "Rating";
    public static final String movie_runtime = "Runtime";
    public static final String movie_actors = "MainActors";
    public static final String movie_plot = "Plot";
    public static final String movie_poster = "PosterURL";

    public FinalOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + OCTRANSPO_TABLE_NAME + "(" + OCT_COL_ID + " VARCHAR PRIMARY KEY, " +
                OCT_COL_NO + " INTEGER, " + OCT_COL_DESC + " TEXT);");

        db.execSQL("CREATE TABLE " + MOVIE_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + movie_title
                + " TEXT, " + movie_year + " INTEGER, " + movie_rating + " TEXT, " + movie_runtime + " TEXT, "
                + movie_actors + " TEXT, " + movie_plot + " TEXT, " + movie_poster + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OCTRANSPO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        onCreate(db);
    }
}
