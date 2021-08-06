package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingStationsMain extends AppCompatActivity {
    private static Context context;
    SharedPreferences prefs;
    String stringURL;
    Object Executor;

    /**
     * This is the onCreate function where we initialize the activity.
     * In the onCreate function, we set the contentView to look at the landing page and we provide intents
     * The intent sends the user from the main page to the second page where they will see their list of stops
     * Shared preferences allow the app to store data so that it can be retrieved the next time the user uses the app
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view to the first page where the user will enter the longitude and latitude
        setContentView(R.layout.charging_landing_page);

        //find the various widgets on the page
        EditText landingPageEdit = findViewById(R.id.firstEditText);
        Button goButton = findViewById(R.id.goButton);

        //shared preferences store data to be used at a later time
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        prefs.getString("MyData","");
        String longAndLat = prefs.getString("LongitudeAndLatitude","");
        landingPageEdit.setText(longAndLat);
        //Toast.makeText(this, "Hello world", Toast.LENGTH_LONG).show();
       // Snackbar.makeText(this, "Hello world", Snackbar.LENGTH_LONG).show();


        //clicking this button will start a new thread
        goButton.setOnClickListener(click -> {
            //to go from this page to the second page
            Intent chargingSecondPage = new Intent(ChargingStationsMain.this, ChargingSecondPage.class);
            chargingSecondPage.putExtra("LongitudeAndLatitude", landingPageEdit.getText().toString());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("DataValues", landingPageEdit.getText().toString());
            editor.apply();
            startActivity(chargingSecondPage);

            //display an alert that tells the user that their information is being loaded
            AlertDialog dialog = new AlertDialog.Builder(ChargingStationsMain.this)
                    .setTitle("Getting Charging Stations")
                    .setMessage("We're looking for your charging stations. Hang tight!")
                    .setView(new ProgressBar(ChargingStationsMain.this))
                    .show();
        });
    }

    public static Context getAppContext() {
        return ChargingStationsMain.context;
    }

    public void userClickedMessage(String location, int position) {
        ChargingDetailsFragment listFragment = new ChargingDetailsFragment(location, position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.add(R.id.chargingFragmentRoom, listFragment);
        tx.commit();

    }
}
