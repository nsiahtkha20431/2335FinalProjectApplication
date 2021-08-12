package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

/**
 * OCTranspoActivity allows users to track OCTranspo Bus Routes by Stop Number.
 * Stop numbers and descriptions are saved in the application database and displayed in a RecyclerView.
 * When a stop is selected from the list, the routes that use it are displayed in a RecyclerView.
 * When a route is selected from the list the details are displayed, including the current trips location
 * and expected arrival time as well the arrival times and details of the next two trips.
 *
 * Written for CST2335 Mobile Graphical Interface Programming Final Project
 * Algonquin College
 * August 8th, 2021
 *
 * @author Emma McArthur
 */

public class OCTranspoActivity extends AppCompatActivity {

    /**
     * Shared preferences object is used to store the last searched stop number and deleted stops
     */
    private SharedPreferences prefs;

    /**
     * API details to access OCTranspo Data Server.
     * OCTranspo Developer Documentation: https://www.octranspo.com/en/plan-your-trip/travel-tools/developers/dev-doc
     */
    private String appId = "016a3846";
    private String apiKey = "688558ead5b36ba6318c11207ea85d0d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);
        setTitle("");
        prefs = getSharedPreferences("OCT_Data", Context.MODE_PRIVATE);
        StopListFragment listFrag = new StopListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.listFrag, listFrag, "list").commit();
        Toolbar toolbar = findViewById(R.id.oct_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.oct_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigation = findViewById(R.id.oct_nav);
        navigation.setNavigationItemSelectedListener( (item) -> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.oct_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_stop:
                StopDetailsFragment newFrag = new StopDetailsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.listFrag, newFrag, "detail").addToBackStack(null).commit();
                break;
            case R.id.menu_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.menu_help))
                        .setMessage(getResources().getString(R.string.help ))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.menu_exit:
                finish();
                break;
        }
        return true;
    }

    /**
     * Adds stop number and description to SQLiteDatabase OCTranspo Table using ContentValues Object.
     * @param stop the stop number to be stored.
     * @param description the stop description to be stored.
     */
    public void addStop(String stop, String description) {
        ContentValues newStop = new ContentValues();
        String message = getResources().getString(R.string.snackbar_added, stop);
        newStop.put(FinalOpenHelper.OCT_COL_NO, stop);
        newStop.put(FinalOpenHelper.OCT_COL_DESC, description);
        MainActivity.db.insert(FinalOpenHelper.OCTRANSPO_TABLE_NAME, FinalOpenHelper.OCT_COL_NO, newStop);
        Snackbar.make(findViewById(R.id.listFrag), message, Snackbar.LENGTH_SHORT)
                .setAction(getResources().getString(R.string.undo), click -> {
                    deleteStop(stop, description);
                }).show();
    }

    /**
     * Inflates new StopDetailsFragment
     * @param stopNo the stop number to display details for
     */
    public void stopSelected(String stopNo) {
        StopDetailsFragment newFrag = new StopDetailsFragment(stopNo);
        getSupportFragmentManager().beginTransaction().replace(R.id.listFrag, newFrag, "detail").addToBackStack(null).commit();
    }

    /**
     * Deletes stop from SQLiteDatabase, adding the details to Shared Preferences to be easily re-added.
     * @param stopNo the stop number to be deleted from the database and stored in Shared Preferences
     * @param description the stop description to be stored in Shared Preferences
     */
    public void deleteStop(String stopNo, String description) {
        String where = FinalOpenHelper.OCT_COL_NO + " = " + stopNo;
        String message = getResources().getString(R.string.snackbar_deleted, stopNo);
        MainActivity.db.delete(FinalOpenHelper.OCTRANSPO_TABLE_NAME, where, null);
        prefs.edit().putString("deleted_stop", String.valueOf(stopNo)).apply();
        prefs.edit().putString("deleted_stop_desc", description).apply();
        Snackbar.make(findViewById(R.id.listFrag), message, Snackbar.LENGTH_SHORT)
                .setAction(getResources().getString(R.string.undo), click -> {
                    undoDelete();
                }).show();
        getSupportFragmentManager().findFragmentByTag("list").onResume();
    }

    /**
     * Re-adds the most recently deleted stop from Shared Preferences and reloads the StopListFragment
     */
    public void undoDelete() {
        String stop = prefs.getString("deleted_stop", "");
        String stopDesc = prefs.getString("deleted_stop_desc", "");
        addStop(stop, stopDesc);
        getSupportFragmentManager().findFragmentByTag("list").onResume();
    }

    /**
     * Inflates new RouteDetailsFragment.
     * @param stop stop number to show route details for
     * @param route route number to show route details for
     */
    public void routeSelected(String stop, String route, String direction) {
        RouteDetailsFragment newFrag = new RouteDetailsFragment(stop, route, direction);
        getSupportFragmentManager().beginTransaction().replace(R.id.listFrag, newFrag, "rdetail").addToBackStack(null).commit();
    }

    /**
     * Returns API App ID value
     * @return API App ID value
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Returns API Key value
     * @return API Key value
     */
    public String getApiKey() {
        return apiKey;
    }
}
