package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * FinalOpenHelper creates and manages the SQLite database for CST2335 Final Project Application
 *
 * Written for CST2335 Mobile Graphical Interface Programming Final Project
 * Algonquin College
 * August 8th, 2021
 *
 * @author Aparna Tiwari, Emma McArthur, Nishat Khan, Raphael Leblanc
 */

public class FinalOpenHelper extends SQLiteOpenHelper {

    /**
     * General Database Details
     */
    public static final String NAME = "FinalProjectDatabase";
    public static final int VERSION = 4;
    public static final String OCTRANSPO_TABLE_NAME = "BusStops";
    public static final String OCT_COL_ID = "ID";
    public static final String OCT_COL_NO = "BusStopNo";
    public static final String OCT_COL_DESC = "BusStopDescription";

    /**
     * Movie Information Database Details
     */
    public static final String MOVIE_TABLE_NAME = "SavedMovieInformation";
    public static final String movie_title = "Title";
    public static final String movie_year = "Year";
    public static final String movie_rating = "Rated";
    public static final String movie_runtime = "Runtime";
    public static final String movie_actors = "MainActors";
    public static final String movie_plot = "Plot";
    public static final String movie_poster = "PosterURL";
    public static final String SOCCER_TABLE_NAME = "Favorites";
    public static final String TITLE_COLUMN = "Title";
    public static final String DATE_COLUMN = "Date";
    public static final String URL_COLUMN = "URL";
    public static final String DESC_COLUMN = "Description";

    /**
     * Electric Car Charging Station Database Details
     */
    public static final String CHARGING_TABLE_NAME = "ChargingStations";
    public static final String location_name = "LocationName";
    public static final String location_latitude = "LocationLatitude";
    public static final String location_longitude = "LocationLongitude";
    public static final String contact_phone_number = "LocationPhoneNumber";

    public FinalOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //OCTranspo Stop Selector Table
        db.execSQL("CREATE TABLE " + OCTRANSPO_TABLE_NAME + "(" + OCT_COL_ID + " VARCHAR PRIMARY KEY, " +
                OCT_COL_NO + " INTEGER, " + OCT_COL_DESC + " TEXT);");
        //Movie Information Table
        db.execSQL("CREATE TABLE " + MOVIE_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + movie_title
                + " TEXT, " + movie_year + " INTEGER, " + movie_rating + " TEXT, " + movie_runtime + " TEXT, "
                + movie_actors + " TEXT, " + movie_plot + " TEXT, " + movie_poster + " TEXT);");
        db.execSQL("Create table " + SOCCER_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE_COLUMN + " TEXT,"
                + DATE_COLUMN + " TEXT,"
                + URL_COLUMN + " TEXT,"
                + DESC_COLUMN + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OCTRANSPO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL( "DROP TABLE IF EXISTS " + SOCCER_TABLE_NAME);
        onCreate(db);
    }
}
