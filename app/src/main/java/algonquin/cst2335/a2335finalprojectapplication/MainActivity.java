package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 *  Main Activity for CST2335 Final Project Application.
 *
 *  This application contains four activities selected through a toolbar or navigation menu.
 *  OCTranspo Stop Selector allows users to track live OCTranspo Bus Routes by stop number.
 *  Electric Car Charging Stations allows users to search for charging stations based on their GPS
 *  coordinates.
 *  Movie Information allows users to look up information about movies by title.
 *  Soccer Games allows users to download and store news articles about soccer.
 *
 *  Written for CST2335 Mobile Graphical Interface Programming Final Project
 *  Algonquin College
 *  August 8th, 2021
 *
 *  @author Aparna Tiwari, Emma McArthur, Nishat Khan, Raphael Leblanc
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Instance of custom SQLiteOpenHelper class for managing application database
     */
    public static FinalOpenHelper opener;

    /**
     * Application database
     */
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("");

        opener = new FinalOpenHelper(this);
        db = opener.getWritableDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater infltr = getMenuInflater();
        infltr.inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage;
        switch (item.getItemId()) {
            case R.id.ic_ocTranspo:
                nextPage = new Intent(MainActivity.this, OCTranspoActivity.class);
                startActivity(nextPage);
                break;
            case R.id.ic_electric:
                nextPage = new Intent(MainActivity.this, ChargingStationsMain.class);
                startActivity(nextPage);
                break;
            case R.id.ic_movie:
                nextPage = new Intent(MainActivity.this, MovieInfoActivity.class);
                startActivity(nextPage);
                break;
            case R.id.ic_soccer:
                nextPage = new Intent(MainActivity.this, SoccerGames.class);
                startActivity(nextPage);
                break;
        }

        return true;
    }
}