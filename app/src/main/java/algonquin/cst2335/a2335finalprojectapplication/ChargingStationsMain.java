package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ChargingStationsMain extends AppCompatActivity {
    private static Context context;
    SharedPreferences prefs;

    /**
     * The purpose of this method is to get the context and return it
     *
     * @return
     */
    public static Context getAppContext() {
        return algonquin.cst2335.a2335finalprojectapplication.ChargingStationsMain.context;
    }

    /**
     * This is the onCreate function where we initialize the activity.
     * In the onCreate function, we set the contentView to look at the landing page and we provide intents
     * The intent sends the user from the main page to the second page where they will see their list of stops
     * Shared preferences allow the app to store data so that it can be retrieved the next time the user uses the app
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**set the view to the first page where the user will enter the longitude and latitude
         *
         */
        setContentView(R.layout.charging_landing_page);
//       Toast toast = Toast.makeText(this, "Welcome to Charging Stations!", Toast.LENGTH_LONG);
//       toast.show();

        /**find the various widgets on the page
         *
         */
        EditText latEditText = findViewById(R.id.latitudeEditText);
        EditText longitudeEditText = findViewById(R.id.longitudeEditText);
        Button goButton = findViewById(R.id.goButton);

        /**
         * Make a snackbar that displays when the page loads
         */
        Snackbar.make(goButton, "Welcome to Charging Stations! Enter a latitude and longitude to begin.", Snackbar.LENGTH_LONG).show();


        /**shared preferences store data to be used at a later time
         *
         */
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefs.getString("MyData", "");
        String latitude = prefs.getString("Latitude", "");
        String longitude = prefs.getString("Longitude", "");
        latEditText.setText(latitude);
        longitudeEditText.setText(longitude);


        /**to go from this page to the second page
         *
         */
        goButton.setOnClickListener(click -> {
            Intent chargingSecondPage = new Intent(algonquin.cst2335.a2335finalprojectapplication.ChargingStationsMain.this, ChargingSecondPage.class);
            chargingSecondPage.putExtra("Latitude", latEditText.getText().toString());
            chargingSecondPage.putExtra("Longitude", longitudeEditText.getText().toString());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Latitude", latEditText.getText().toString());
            editor.putString("Longitude", longitudeEditText.getText().toString());
            editor.apply();
            startActivity(chargingSecondPage);
        });

        /**finding the toolbar and then setting the toolbar
         *
         */
        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        /**creating the nav drawer
         *
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        /**first line creates the hamburger button on the top left
         *
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        /**these two lines of code make the button and pop-out menu sync so that it can open and close correctly
         *
         */
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item); //call the function for the other toolbar
            drawer.closeDrawer(GravityCompat.START);

            return false;
        });

    }

    @Override
    /**
     * This portion of the code is used to inflate the menu that was created previously.
     * It looks at the charging_stations_toolbar to create the menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charging_stations_toolbar, menu);
        return true;
    }

    /**
     * The purpose of this method is to direct the program when a user selects an item from the menu
     * It will give instructions on how and what to do if a certain option is selected
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.oc_transpo_activity:
                Intent ocTranspoActivity = new Intent(ChargingStationsMain.this, OCTranspoActivity.class);
                startActivity(ocTranspoActivity);
                break;
            case R.id.soccer_activity:
                Intent soccerActivity = new Intent(ChargingStationsMain.this, SoccerGames.class);
                startActivity(soccerActivity);
                break;
            case R.id.movie_activity:
                Intent movieActivity = new Intent(ChargingStationsMain.this, MovieInfoActivity.class);
                startActivity(movieActivity);
                break;
            case R.id.electric_help:
                AlertDialog helpDialog = new AlertDialog.Builder(ChargingStationsMain.this)
                        .setTitle("Help")
                        .setMessage("Please type in a latitude and longitude value and hit go. You will be redirected to " +
                                "a page that will show you a list of results near you. Select an item from the list and you will see details about that location." +
                                "You can then click the directions button to get directions in Google Maps, or click 'Add to Favourites' to add this location to your favourites.")
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void userClickedMessage(String location, int position) {
        ChargingDetailsFragment listFragment = new ChargingDetailsFragment(location, position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.add(R.id.chargingFragmentRoom, listFragment);
        tx.commit();

    }
}
