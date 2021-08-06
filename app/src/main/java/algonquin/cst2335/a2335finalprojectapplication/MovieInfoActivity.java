package algonquin.cst2335.a2335finalprojectapplication;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.opener;
/**Class that holds an application page providing a tool bar and a navigation drawer to browse through options and between other internal applications
 * on the app, as well as a fragment room to display each page below the toolbar.
 * This is the main page that will start with having the MovieSearchFragment class's fragment loaded in the fragment room and will continue to load the appropriate fragments
 * as you explore and browse through the application.
 * @author Raphael Leblanc
 * @version 1.0
 */
public class MovieInfoActivity extends AppCompatActivity {

    /**This represents the MovieSearchFragment class to use internal functions or assign to the new fragment. */
    MovieSearchFragment searchFrag = new MovieSearchFragment();
    /**This represents the MovieInfo class from MovieSearchFragment as an object. */
    MovieSearchFragment.MovieInfo movieInfo;
    /**This represents the MovieSearchFragment class to use internal functions or assign to the new fragment. */
    SavedMovieFragment savedFrag = new SavedMovieFragment();


    /** This function will allow the main page content to be loaded on screen upon creation, providing a toolbar, navigation drawer, onclick functions
     * alert dialogs.
     * Provided functions for onClicks include either fragment changes, displaying dialogs and option selection.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_room);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.movie_toolbar_menu);
        toolbar.getMenu().findItem(R.id.search).setChecked(true);

        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, searchFrag).commit();

        DrawerLayout drawer = findViewById(R.id.nav_drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_popup);

        navigationView.setCheckedItem(R.id.ic_movie);


        navigationView.setNavigationItemSelectedListener((item) ->{


            navigationView.setCheckedItem(R.id.ic_movie);

            switch(item.getItemId()) {
                case R.id.ic_ocTranspo:
                    Intent ocPage = new Intent(getApplicationContext(), OCTranspoActivity.class);
                    startActivity(ocPage);


                    break;
                case R.id.ic_electric:
                    Intent elecPage = new Intent(getApplicationContext(), ChargingMainActivity.class);
                    startActivity(elecPage);


                    break;
                case R.id.ic_movie:
                    Intent movPage = new Intent(getApplicationContext(), MovieInfoActivity.class);
                    startActivity(movPage);

                    break;
                case R.id.ic_soccer:
                    Intent soccPage = new Intent(getApplicationContext(), SoccerGames.class);
                    startActivity(soccPage);


                    break;
                case R.id.search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, searchFrag).commit();
                    item.setChecked(true);
                    toolbar.getMenu().findItem(R.id.search).setChecked(true);
                    navigationView.getMenu().findItem(R.id.saved).setChecked(false);
                    toolbar.getMenu().findItem(R.id.saved).setChecked(false);
                    drawer.closeDrawer(GravityCompat.START);


                    break;
                case R.id.saved:
                    getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, savedFrag).commit();
                    item.setChecked(true);
                    toolbar.getMenu().findItem(R.id.saved).setChecked(true);
                    navigationView.getMenu().findItem(R.id.search).setChecked(false);
                    toolbar.getMenu().findItem(R.id.search).setChecked(false);
                    drawer.closeDrawer(GravityCompat.START);


                    break;
                case R.id.help:
                    if (Locale.getDefault().getDisplayLanguage().equals("français")) {
                        new AlertDialog.Builder(MovieInfoActivity.this)
                                .setTitle("Aide")
                                .setMessage("   Cette application, Movie Information, permet à l'utilisateur d'effectuer une recherche dans la base de " +
                                        "données utilisée sur omdbapi.com en tapant simplement le texte contenu dans un titre de film." +
                                        " \n   Si le titre du film saisi est introuvable ou n'existe tout simplement pas, un message s'affiche pour informer qu'aucun film n'a été trouvé." +
                                        "\n   Une fois le film trouvé, il fournit des détails sur le film, qui peuvent être enregistrés localement sur l'appareil avec les informations et " +
                                        "l'image du film. \n   Les films sauvegardés peuvent être trouvés en utilisant les options de menu à partir desquelles vous avez " +
                                        "trouvé ce bouton d'aide. La liste sauvegardée s'affiche à la place de la page actuelle avec le nom du film, l'année et le classement," +
                                        " ainsi que l'image. \n   En cliquant sur un film, les détails enregistrés s'affichent. \n   Une fois encore, vous pouvez fermer les détails" +
                                        " ou continuer à consulter votre liste sauvegardée et à effectuer des recherches comme bon vous semble!")
                                .setNegativeButton("Fermer", (diallog, cl) -> {
                                    //does nothing, closes.
                                })
                                .create().show();
                    }
                    else{
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

                    drawer.closeDrawer(GravityCompat.START);

                    break;


            }
            return false;
        });


        toolbar.setOnMenuItemClickListener((item) -> {


                switch(item.getItemId()) {
                    case R.id.ic_ocTranspo:
                        Intent ocPage = new Intent(getApplicationContext(), OCTranspoActivity.class);
                        startActivity(ocPage);


                        break;
                    case R.id.ic_electric:
                        Intent elecPage = new Intent(getApplicationContext(), ChargingMainActivity.class);
                        startActivity(elecPage);


                        break;
                    case R.id.ic_movie:
                        Intent movPage = new Intent(getApplicationContext(), MovieInfoActivity.class);
                        startActivity(movPage);

                        break;
                    case R.id.ic_soccer:
                        Intent soccPage = new Intent(getApplicationContext(), SoccerGames.class);
                        startActivity(soccPage);


                        break;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, searchFrag).commit();
                        item.setChecked(true);
                        navigationView.getMenu().findItem(R.id.search).setChecked(true);
                        navigationView.getMenu().findItem(R.id.saved).setChecked(false);
                        toolbar.getMenu().findItem(R.id.saved).setChecked(false);




                        break;
                    case R.id.saved:
                        getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, savedFrag).commit();
                        item.setChecked(true);
                        navigationView.getMenu().findItem(R.id.saved).setChecked(true);
                        navigationView.getMenu().findItem(R.id.search).setChecked(false);
                        toolbar.getMenu().findItem(R.id.search).setChecked(false);




                        break;
                    case R.id.help:
                        if (Locale.getDefault().getDisplayLanguage().equals("français")) {
                            new AlertDialog.Builder(MovieInfoActivity.this)
                                    .setTitle("Aide")
                                    .setMessage("   Cette application, Movie Information, permet à l'utilisateur d'effectuer une recherche dans la base de " +
                                            "données utilisée sur omdbapi.com en tapant simplement le texte contenu dans un titre de film." +
                                            " \n   Si le titre du film saisi est introuvable ou n'existe tout simplement pas, un message s'affiche pour informer qu'aucun film n'a été trouvé." +
                                            "\n   Une fois le film trouvé, il fournit des détails sur le film, qui peuvent être enregistrés localement sur l'appareil avec les informations et " +
                                            "l'image du film. \n   Les films sauvegardés peuvent être trouvés en utilisant les options de menu à partir desquelles vous avez " +
                                            "trouvé ce bouton d'aide. La liste sauvegardée s'affiche à la place de la page actuelle avec le nom du film, l'année et le classement," +
                                            " ainsi que l'image. \n   En cliquant sur un film, les détails enregistrés s'affichent. \n   Une fois encore, vous pouvez fermer les détails" +
                                            " ou continuer à consulter votre liste sauvegardée et à effectuer des recherches comme bon vous semble!")
                                    .setNegativeButton("Fermer", (diallog, cl) -> {
                                        //does nothing, closes.
                                    })
                                    .create().show();
                        }
                        else{
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


                        break;

                }

                return false;


        });
    }





//Not needed if directly inflate on toolbar and not use setSupportActionBar(toolbar);
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.movie_toolbar_menu, menu);
//        return true;
//    }

    /**This function will assign the MovieInfo object information from MovieSearchFragment to the class scope when called.
     *
     * @param movieInfo Object from MovieInfo class within MovieSearchFragment, providing movie details.
     */
    public void usrSearchedMovie(MovieSearchFragment.MovieInfo movieInfo) {
        this.movieInfo = movieInfo;

        MovieDetailsFragment detailsFrag = new MovieDetailsFragment(movieInfo);
        getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, detailsFrag).commit();
    }

    /**This function allows input of movie details into the local database.
     * This function is called within the MovieDetailsFragment class on the save button.
     *
     * @throws IOException Exception handling Input/Output (in this case it would be to the database). Something went wrong providing data.
     */
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

    /**This function allows the fragment room to change to the SavedMovieDetailsFragment.
     * This function is called when the user clicks on a saved movie within the recycler view presented in the SavedMovieFragment.
     *
     * @param movieInfo Passes along the movieInfo object of the current item clicked on.
     * @param position Passes along the position within the recyclerview of the current movie clicked on (movieInfo object) to ensure correct movie.
     */
    public void usrClickedSavedMovie(MovieSearchFragment.MovieInfo movieInfo, int position){
        SavedMovieDetailsFragment savedDetailsFrag = new SavedMovieDetailsFragment(movieInfo, position);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, savedDetailsFrag).commit();
    }

    /**This function allows the deletion from the database and recyclerview of the current movie.
     * This function is called within SavedMovieDetailsFragment to then be called back to the SavedMovieFragment to delete/update from
     * the recycler view.
     *
     * @param movieInfo Object (movie) to be deleted.
     * @param position Position from which the object (movie) is to be deleted within the recyclerview.
     */
    public void movieDeleted(MovieSearchFragment.MovieInfo movieInfo, int position){
        savedFrag.movieDeleted(movieInfo, position);

    }

}
