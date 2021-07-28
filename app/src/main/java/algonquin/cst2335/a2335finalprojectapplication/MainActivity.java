package algonquin.cst2335.a2335finalprojectapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import algonquin.cst2335.a2335finalprojectapplication.ChargingStations.ChargingMainActivity;

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

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        Button ocTranspo = findViewById(R.id.ocTranspoButton);
        ocTranspo.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, OCTranspoActivity.class);
            startActivity(nextPage);
        });

        Button electricCar = findViewById(R.id.electricCarButton);
        electricCar.setOnClickListener(click -> {
            Intent nextPage = new Intent(MainActivity.this, ChargingMainActivity.class);
            startActivity(nextPage);
        });

        Button movieInfo = findViewById(R.id.movieInfoButton);
        movieInfo.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, MovieInfoActivity.class);
            startActivity(nextPage);
        });

        Button soccerGames = findViewById(R.id.soccerGamesButton);
        soccerGames.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, SoccerGames.class);
            startActivity(nextPage);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage;
        switch(item.getItemId()) {
            case R.id.ic_ocTranspo:
                nextPage = new Intent(MainActivity.this, OCTranspoActivity.class);
                startActivity(nextPage);
                break;
            case R.id.ic_electric:
                nextPage = new Intent(MainActivity.this, ChargingMainActivity.class);
                startActivity(nextPage);
                break;
            case R.id.ic_movie:
                nextPage = new Intent( MainActivity.this, MovieInfoActivity.class);
                startActivity(nextPage);
                break;
            case R.id.ic_soccer:
                nextPage = new Intent( MainActivity.this, SoccerGames.class);
                startActivity(nextPage);
                break;
        }

        return true;
    }
}