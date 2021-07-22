package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

                if (item.getItemId() == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, searchFrag).commit();
                    item.setChecked(true);
                    toolbar.setTitle("Search Movie");
                } else if (item.getItemId() == R.id.saved) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, savedFrag).commit();
                    item.setChecked(true);
                    toolbar.setTitle("Saved Movies");
                } else if (item.getItemId() == R.id.help){
                    new AlertDialog.Builder(MovieInfoActivity.this)
                    .setTitle("Help")
                            .setMessage("This app, Movie Information, allows the user to search throughout the database utilized on omdbapi.com by simply typing" +
                                    " text that is contained within a movie title. \n   If the movie title entered can't be found or simply does not exist, a message will" +
                                    " be shown to inform that no movie was found. \n   Once the movie has been found, it will provide details regarding the movie from" +
                                    " which can be saved onto the device locally along with the information and movie image. \n   Saved movies can be found using the" +
                                    " menu options from which you found this help button. The saved list will be displayed instead of the current page along with" +
                                    " the movie names, year and rating with the image. \n   Upon clicking a movie, the saved details will be displayed." +
                                    "\n   Once again, you may close the details or continue to look at your saved list and search as you like! ")
                            .setNegativeButton("Close", (dialog, cl) -> {
                                //does nothing, closes.
                            })
                            .create().show();

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

    public void usrSaveMovie() throws IOException {
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
        //TODO
        Bitmap poster = null;
        File file = new File(getFilesDir(), movieInfo.getTitle().replace(" ", "") + ".jpeg");


        if (file.exists()) {
            //poster = BitmapFactory.decodeFile(getFilesDir() + "/" + movieInfo.getTitle().replace(" ", "") + ".jpeg");
        } else {

            poster = MovieDetailsFragment.DownloadImageTask.getMovieBitmap();
//            URL imgUrl = new URL(movieInfo.getURL());
//            HttpURLConnection con = (HttpURLConnection) imgUrl.openConnection();
//            con.connect();
//            int response = con.getResponseCode();
//
//            if (response == 200) {
//                poster = BitmapFactory.decodeStream(con.getInputStream());
            poster.compress(Bitmap.CompressFormat.JPEG, 100,
                    openFileOutput(movieInfo.getTitle().replace(" ", "") + ".jpeg", Activity.MODE_PRIVATE));
//}
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(movieInfo.getTitle().replace(" ", "") + ".jpeg", Context.MODE_PRIVATE);
            poster.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

    }

}


    public void usrClickedSavedMovie(MovieSearchFragment.MovieInfo movieInfo, int position){
        SavedMovieDetailsFragment savedDetailsFrag = new SavedMovieDetailsFragment(movieInfo, position);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, savedDetailsFrag).commit();
    }


    public void movieDeleted(MovieSearchFragment.MovieInfo movieInfo, int position){
        savedFrag.movieDeleted(movieInfo, position);

    }

}
