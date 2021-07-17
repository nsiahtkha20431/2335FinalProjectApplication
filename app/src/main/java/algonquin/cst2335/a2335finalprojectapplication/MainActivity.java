package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ocTranspo = findViewById(R.id.ocTranspoButton);
        ocTranspo.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, OCTranspoActivity.class);
            startActivity(nextPage);
        });
        Button electricCar  = findViewById(R.id.electricCarButton);
        Button movieInfo = findViewById(R.id.movieInfoButton);
        Button soccerGames = findViewById(R.id.soccerGamesButton);


    }
}