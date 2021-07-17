package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.a2335finalprojectapplication.OCTranspo.OCTranspoActivity;
import algonquin.cst2335.a2335finalprojectapplication.SoccerGames.SoccerGames;

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
        Button movieInfo = findViewById(R.id.movieInfoButton);
        Button soccerGames = findViewById(R.id.soccerGamesButton);

        soccerGames.setOnClickListener(clk -> {
            Intent nextPage = new Intent( MainActivity.this, SoccerGames.class);
            startActivity(nextPage);
        });
    }
}