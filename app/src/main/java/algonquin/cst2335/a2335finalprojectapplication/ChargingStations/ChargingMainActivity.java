package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import algonquin.cst2335.a2335finalprojectapplication.R;
public class ChargingMainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the view to the first page where the user will enter the longitude and latitude
        setContentView(R.layout.charging_landing_page);

        EditText landingPageEdit = findViewById(R.id.firstEditText);
        Button goButton = findViewById(R.id.goButton);

        preferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        preferences.getString("MyData","");
        String longAndLat = preferences.getString("longitudeAndLatitude","");
        landingPageEdit.setText(longAndLat);

        goButton.setOnClickListener(click -> {
            //to go from the main page to the second page
            Intent nextPage = new Intent(ChargingMainActivity.this, ChargingSecondActivity.class);
            nextPage.putExtra("LongitudeAndLatitude", landingPageEdit.getText().toString());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("LongitudeAndLatitude", landingPageEdit.getText().toString());
            editor.apply();
            startActivity(nextPage);

        });


    }

}
