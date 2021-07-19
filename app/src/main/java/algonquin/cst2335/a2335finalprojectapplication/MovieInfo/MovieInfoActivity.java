package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

public class MovieInfoActivity extends AppCompatActivity {


    MovieSearchFragment searchFrag = new MovieSearchFragment();
    MovieSearchFragment.MovieInfo movieInfo;
    FinalOpenHelper opener = new FinalOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, searchFrag).commit();
        SQLiteDatabase db = opener.getWritableDatabase();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_toolbar_menu, menu);
        return true;
    }


    public void usrSearchedMovie(MovieSearchFragment.MovieInfo movieInfo) {

        MovieDetailsFragment detailsFrag = new MovieDetailsFragment(movieInfo);
        this.movieInfo = movieInfo;
        getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, detailsFrag).commit();
    }

    public void usrSaveMovie() {
        SQLiteDatabase db = opener.getWritableDatabase();
        ContentValues newRow = new ContentValues();
        newRow.put(FinalOpenHelper.movie_title, movieInfo.getTitle());
        newRow.put(FinalOpenHelper.movie_year, movieInfo.getYear());
        newRow.put(FinalOpenHelper.movie_rating, movieInfo.getRating());
        newRow.put(FinalOpenHelper.movie_runtime, movieInfo.getRuntime());
        newRow.put(FinalOpenHelper.movie_actors, movieInfo.getActors());
        newRow.put(FinalOpenHelper.movie_plot, movieInfo.getPlot());
        newRow.put(FinalOpenHelper.movie_poster, movieInfo.getURL());
        db.insert(FinalOpenHelper.MOVIE_TABLE_NAME, FinalOpenHelper.movie_title, newRow);

    }

}
