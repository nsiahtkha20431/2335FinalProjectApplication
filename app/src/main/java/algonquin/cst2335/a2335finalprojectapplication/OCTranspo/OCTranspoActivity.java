package algonquin.cst2335.a2335finalprojectapplication.OCTranspo;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.MainActivity;
import algonquin.cst2335.a2335finalprojectapplication.R;

public class OCTranspoActivity extends AppCompatActivity {

    SharedPreferences prefs;
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
                builder.setTitle("Help")
                        .setMessage("OC Transpo Stop Selector displays live stop and route data from" +
                                "the OCTranspo server.\n\nSelect a stop number to display incoming bus routes." +
                                "\n\nSelect a route number to display route details and upcoming trips." +
                                "\n\nTo add a stop to your list select the plus sign from the toolbar or \"Add Stop\" " +
                                "from the navigation menu.")
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

    public void addStop(String stop, String description) {
        ContentValues newStop = new ContentValues();
        newStop.put(FinalOpenHelper.OCT_COL_NO, stop);
        newStop.put(FinalOpenHelper.OCT_COL_DESC, description);
        MainActivity.db.insert(FinalOpenHelper.OCTRANSPO_TABLE_NAME, FinalOpenHelper.OCT_COL_NO, newStop);
        Snackbar.make(findViewById(R.id.listFrag), "Bus Stop #" + stop + " has been added.", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", click -> {
                    deleteStop(Integer.parseInt(stop), description);
                }).show();
    }

    public void stopSelected(int stopNo) {
        StopDetailsFragment newFrag = new StopDetailsFragment(stopNo);
        getSupportFragmentManager().beginTransaction().replace(R.id.listFrag, newFrag, "detail").addToBackStack(null).commit();
    }

    public void deleteStop(int stopNo, String description) {
        String where = FinalOpenHelper.OCT_COL_NO + " = " + stopNo;
        MainActivity.db.delete(FinalOpenHelper.OCTRANSPO_TABLE_NAME, where, null);
        prefs.edit().putString("deleted_stop", String.valueOf(stopNo)).apply();
        prefs.edit().putString("deleted_stop_desc", description).apply();
        Snackbar.make(findViewById(R.id.listFrag), "Bus Stop #" + stopNo + " has been deleted.", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", click -> {
                    undoDelete();
                }).show();
        getSupportFragmentManager().findFragmentByTag("list").onResume();
    }

    public void undoDelete() {
        String stop = prefs.getString("deleted_stop", "");
        String stopDesc = prefs.getString("deleted_stop_desc", "");
        addStop(stop, stopDesc);
        getSupportFragmentManager().findFragmentByTag("list").onResume();
    }

    public void routeSelected(int stop, int route) {
        RouteDetailsFragment newFrag = new RouteDetailsFragment(stop, route);
        getSupportFragmentManager().beginTransaction().replace(R.id.listFrag, newFrag, "rdetail").addToBackStack(null).commit();
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }
}
