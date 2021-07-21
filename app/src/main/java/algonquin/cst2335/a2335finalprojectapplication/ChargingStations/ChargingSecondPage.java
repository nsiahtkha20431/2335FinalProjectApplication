package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingSecondPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charging_recycler_page);

        TextView top = findViewById(R.id.secondPageTV);
        Button enterAnother = findViewById(R.id.otherLocationButton);

        Intent fromPrevious = getIntent(); // gets the intent object that started the transition
        String longAndLat = fromPrevious.getStringExtra("LongitudeAndLatitude");
        top.setText("You are looking for charging stations near " + longAndLat + ".");
        enterAnother.setOnClickListener(click -> {
            AlertDialog builder = new AlertDialog.Builder(ChargingSecondPage.this)
                    .setTitle("Go back")
                    .setMessage("Are you sure you want to enter another location?")
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        Intent previousPage = new Intent(ChargingSecondPage.this, ChargingStationsMain.class);
                        startActivity(previousPage);
                    })
                    .show();
        });
//        setContentView(R.layout.charging_empty_fragment);
//        ChargingStationFragment listFragment = new ChargingStationFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction tx = fragmentManager.beginTransaction();
//        tx.add(R.id.fragmentHolder, listFragment);
//        tx.commit();
    }
}
