package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

public class ChargingSecondPage extends AppCompatActivity {

    /**
     * These are the activities that will occur when the page is first created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charging_recycler_page);

        TextView top = findViewById(R.id.secondPageTV); //the textview that will display "you're looking for locations near lat and long"
        Button enterAnother = findViewById(R.id.otherLocationButton); //button to say "Go Back" to the previous page

        Intent fromPrevious = getIntent(); // gets the intent object that started the transition
        String latitudeInformation = fromPrevious.getStringExtra("Latitude");//saves the lat entered in the previous page
        String longitudeInformation = fromPrevious.getStringExtra("Longitude");//saves the long entered in the previous page
        top.setText("You are looking for charging stations near " + latitudeInformation + " and " + longitudeInformation + ".");


        enterAnother.setOnClickListener(click -> {
            AlertDialog builder = new AlertDialog.Builder(ChargingSecondPage.this)
                    .setTitle("Go back")
                    .setMessage("Are you sure you want to enter another location?")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        Intent previousPage = new Intent(ChargingSecondPage.this, ChargingStationsMain.class);
                        startActivity(previousPage);
                    })
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .show();
        });
        ChargingStationFragment listFragment = new ChargingStationFragment(latitudeInformation, longitudeInformation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.add(R.id.chargingFragmentHolder, listFragment);
        tx.commit();
    }

    /**
     * When the user clicks a row, the following information will be displayed on another page: Station Name, Latitude, Longitude, and Phone Number
     * @param location
     * @param position
     */

    public void userClickedMessage(ChargingStationFragment.ChargingStation location, int position) {
        Intent chargingThirdPage = new Intent(ChargingSecondPage.this, ChargingThirdPage.class);
        chargingThirdPage.putExtra("StationName", location.getLocationTitle());
        chargingThirdPage.putExtra("Latitude", location.getLatitude());
        chargingThirdPage.putExtra("Longitude", location.getLongitude());
        chargingThirdPage.putExtra("Phone", location.getContactPhone());

        startActivity(chargingThirdPage);


    }
}
