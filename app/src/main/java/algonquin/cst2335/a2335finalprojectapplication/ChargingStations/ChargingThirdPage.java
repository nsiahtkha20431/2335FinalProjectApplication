package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingThirdPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charging_fragment_details);

        Button backButton = findViewById(R.id.stationBackButton);
        Button loadDirections = findViewById(R.id.directionsButton);

        backButton.setOnClickListener(clk -> {
            Intent goBackToPrevious = new Intent(ChargingThirdPage.this, ChargingSecondPage.class);
            startActivity(goBackToPrevious);
        });

        loadDirections.setOnClickListener(click -> {

        });

    }
}
