//package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import algonquin.cst2335.a2335finalprojectapplication.R;
//
//public class ChargingSecondActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//       super.onCreate(savedInstanceState);
//       setContentView(R.layout.charging_recycler_page);
//       TextView top = findViewById(R.id.secondPageTV);
//
//        Intent fromPrevious = getIntent(); // gets the intent object that started the transition
//        String longAndLat = fromPrevious.getStringExtra("LongitudeAndLatitude");
//        top.setText("You are looking for charging stations near " + longAndLat+ ".");
//
//
//    }
//}
