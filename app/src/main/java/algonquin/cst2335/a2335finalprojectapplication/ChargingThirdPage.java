package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;



/**
 * This class provides functionality to the third page
 */
public class ChargingThirdPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charging_fragment_details);

        /**
         * Finding all the views on the page
         */
        TextView stationName = findViewById(R.id.locationNameGoesHere);
        TextView latitudeGoesHere = findViewById(R.id.latitudeGoesHere);
        TextView longitudeGoesHere = findViewById(R.id.longitudeGoesHere);
        TextView phoneNumberHere = findViewById(R.id.phoneNumberGoesHere);
        Button backButton = findViewById(R.id.stationBackButton);
        Button loadDirections = findViewById(R.id.directionsButton);

        /**
         * Getting an intent and using that to place information in the correct views
         */
        Intent intent = (this.getIntent());

        String name = intent.getStringExtra("StationName");
        Snackbar.make(stationName,"You are viewing information for " + name, Snackbar.LENGTH_LONG).show();
        String searchedLatitude = intent.getStringExtra("Latitude");
        String searchedLongitude = intent.getStringExtra("Longitude");
        String phoneNumber = intent.getStringExtra("Phone");

        stationName.setText(name);
        latitudeGoesHere.setText(searchedLatitude);
        longitudeGoesHere.setText(searchedLongitude);
        phoneNumberHere.setText(phoneNumber);


        /**
         * onClickListener to send the user back to the previous page
         */
        backButton.setOnClickListener(clk -> {
            Intent goBackToPrevious = new Intent(ChargingThirdPage.this, ChargingStationsMain.class);
            startActivity(goBackToPrevious);
        });

        /**
         * This is what opens up googleMaps
         */
        loadDirections.setOnClickListener(click -> {
            Uri googleMapsIntent = Uri.parse("http://maps.google.com/maps?q=loc:" + searchedLatitude + "," + searchedLongitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsIntent);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        });

    }
}
