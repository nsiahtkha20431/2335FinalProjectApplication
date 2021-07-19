package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.opener;

public class MovieInfoActivity extends AppCompatActivity {


    MovieSearchFragment searchFrag = new MovieSearchFragment();
    MovieSearchFragment.MovieInfo movieInfo;
    SavedMovieFragment savedFrag = new SavedMovieFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search Movie");
        toolbar.inflateMenu(R.menu.movie_toolbar_menu);
        toolbar.getMenu().getItem(0).setChecked(true);
        //setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, searchFrag).commit();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId() == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, searchFrag).commit();
                    item.setChecked(true);
                    toolbar.setTitle("Search Movie");
                }
                else if(item.getItemId() == R.id.saved) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, savedFrag).commit();
                    item.setChecked(true);
                    toolbar.setTitle("Saved Movies");
                }
                else{

                }
                return false;
            }
        });

        SQLiteDatabase db = opener.getWritableDatabase();



    }

//Not needed if directly inflate on toolbar and not use setSupportActionBar(toolbar);
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.movie_toolbar_menu, menu);
//        return true;
//    }


    public void usrSearchedMovie(MovieSearchFragment.MovieInfo movieInfo) {
        this.movieInfo = movieInfo;

        MovieDetailsFragment detailsFrag = new MovieDetailsFragment(movieInfo);
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

    public void usrClickedSavedMovie(MovieSearchFragment.MovieInfo movieInfo, int position){
        SavedMovieDetailsFragment savedDetailsFrag = new SavedMovieDetailsFragment(movieInfo, position);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, savedDetailsFrag).commit();
    }


    public void movieDeleted(MovieSearchFragment.MovieInfo movieInfo, int position){
        savedFrag.movieDeleted(movieInfo, position);

    }
}
