package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingStationsMain extends AppCompatActivity {
    private static Context context;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view to the first page where the user will enter the longitude and latitude
        setContentView(R.layout.charging_landing_page);

        EditText landingPageEdit = findViewById(R.id.firstEditText);
        Button goButton = findViewById(R.id.goButton);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        prefs.getString("MyData","");
        String longAndLat = prefs.getString("LongitudeAndLatitude","");
        landingPageEdit.setText(longAndLat);
        //Toast.makeText(this, "Hello world", Toast.LENGTH_LONG).show();
       // Snackbar.makeText(this, "Hello world", Snackbar.LENGTH_LONG).show();
        goButton.setOnClickListener(click -> {
            //to go from the main page to the second page
            Intent chargingSecondPage = new Intent(ChargingStationsMain.this, ChargingSecondPage.class);
            chargingSecondPage.putExtra("LongitudeAndLatitude", landingPageEdit.getText().toString());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("DataValues", landingPageEdit.getText().toString());
            editor.apply();
            startActivity(chargingSecondPage);

        });

//        setContentView(R.layout.charging_empty_fragment);
//        ChargingStationFragment listFragment = new ChargingStationFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction tx = fragmentManager.beginTransaction();
//        tx.add(R.id.fragmentHolder, listFragment);
//        tx.commit();

    }
//        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, new ChargingStationFragment()).commit();
        //ChargingStationsMain.context = getApplicationContext();

    public static Context getAppContext() {
        return ChargingStationsMain.context;
    }

    public void userClickedMessage(String location, int position) {
        ChargingDetailsFragment chargingDetailsFragment = new ChargingDetailsFragment(location, position);
        //since it's only on a phone, we only have to
        getSupportFragmentManager().beginTransaction().add((R.id.chargingFragmentHolder), chargingDetailsFragment);
    }
}
