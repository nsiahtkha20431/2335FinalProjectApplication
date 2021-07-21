package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.a2335finalprojectapplication.ChargingStations.ChargingStationsMain;
import algonquin.cst2335.a2335finalprojectapplication.OCTranspo.OCTranspoActivity;
import algonquin.cst2335.a2335finalprojectapplication.SoccerGames.SoccerGames;
import algonquin.cst2335.a2335finalprojectapplication.MovieInfo.MovieInfoActivity;


public class MainActivity extends AppCompatActivity {
    public static FinalOpenHelper opener;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        opener = new FinalOpenHelper(this);
        db = opener.getWritableDatabase();
        Button ocTranspo = findViewById(R.id.ocTranspoButton);
        ocTranspo.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, OCTranspoActivity.class);
            startActivity(nextPage);
        });

        Button electricCar  = findViewById(R.id.electricCarButton);
        electricCar.setOnClickListener(click -> {
            Intent nextPage = new Intent(MainActivity.this, ChargingStationsMain.class);
            startActivity(nextPage);
        });

        Button movieInfo = findViewById(R.id.movieInfoButton);
        movieInfo.setOnClickListener(clk -> {
            Intent nextPage = new Intent( MainActivity.this, MovieInfoActivity.class);
            startActivity(nextPage);
        });

        Button soccerGames = findViewById(R.id.soccerGamesButton);
        soccerGames.setOnClickListener(clk -> {
            Intent nextPage = new Intent( MainActivity.this, SoccerGames.class);
            startActivity(nextPage);
        });

    }
}