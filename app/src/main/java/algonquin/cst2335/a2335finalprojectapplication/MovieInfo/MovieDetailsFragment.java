package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.opener;
/**Class that holds an application fragment providing details of the searched input movie from the MovieSearchFragment class.
 * This page will provide all necessary details of the movie that was selected along with the image poster, with the option to either
 * close the current fragment and return to the MovieSearchFragment or to save the movie to the local database.
 * @author Raphael Leblanc
 * @version 1.0
 */
public class MovieDetailsFragment extends Fragment {

    /**This holds the text for the movie title. */
    TextView title;
    /**This holds the text for the movie year. */
    TextView year;
    /**This holds the text for the movie's rating. */
    TextView rating;
    /**This holds the text for the movie's runtime. */
    TextView runtime;
    /**This holds the text for the movie's actors. */
    TextView actors;
    /**This holds the text for the movie's plot. */
    TextView plot;
    /**This holds the text for the movie's poster. */
    ImageView poster;
    /**This represents the button with the preset text "Save".
     * This button allows to save the movie to the local database. */
    Button save;
    /**This represents the button with the preset text "Delete".
     * This button allows to exit from the current fragment back to the MovieSearchFragment. */
    Button close;
    /**Declaration of MovieSearchFragment class to be used to change fragments or use internal functions.*/
    MovieSearchFragment searchFrag = new MovieSearchFragment();

    /**This represents the MovieInfo class within the MovieSearchFragment class to be used as an object. */
    MovieSearchFragment.MovieInfo movieInfo;
    /**This represents the loading progress window's dialog when downloading movie details (image) */
    private static ProgressDialog prog;


    /** This function will allow information to be loaded within each component within the MovieDetailsFragment's layout,
     * assign onClick functions to the buttons and set up toast messages when using the save button upon fragment creation.
     *
     * @param inflater Inflater used to inflate a layout to the container.
     * @param container Container holding the specified layout.
     * @param savedInstanceState
     * @return Return view (detailsLayout)
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout =inflater.inflate(R.layout.movie_details_layout, container, false);

        prog = new ProgressDialog(getContext());
        if (Locale.getDefault().getDisplayLanguage().equals("français")){
            prog.setMessage("Téléchargement de l'information du film et de l'image en cours");
        }
        else{
            prog.setMessage("Downloading movie information and poster..");
        }
        prog.setProgress(0);
        prog.setMax(600);


        title = detailsLayout.findViewById(R.id.title_mes);
        title.setText(movieInfo.getTitle());
        year = detailsLayout.findViewById(R.id.year_mes);
        year.setText(movieInfo.getYear());
        rating = detailsLayout.findViewById(R.id.rating_mes);
        rating.setText(movieInfo.getRating());
        runtime = detailsLayout.findViewById(R.id.runtime_mes);
        runtime.setText(movieInfo.getRuntime());
        actors = detailsLayout.findViewById(R.id.actors_mes);
        actors.setText(movieInfo.getActors());
        plot = detailsLayout.findViewById(R.id.plot_mes);
        plot.setText(movieInfo.getPlot());
        poster = detailsLayout.findViewById(R.id.pos);
        new DownloadImageTask(poster).execute(movieInfo.getURL());
        save = detailsLayout.findViewById(R.id.del_button);
        close = detailsLayout.findViewById(R.id.close_but);

        save.setOnClickListener(clk -> {
            MovieInfoActivity activity = (MovieInfoActivity)getContext();

            SQLiteDatabase db = opener.getWritableDatabase();
            Cursor check = db.rawQuery("SELECT " + FinalOpenHelper.movie_title + ", " + FinalOpenHelper.movie_year + " FROM " + FinalOpenHelper.MOVIE_TABLE_NAME + ";", null);

            boolean valid = false;

            int titleCol = check.getColumnIndex(FinalOpenHelper.movie_title);
            int yearCol = check.getColumnIndex(FinalOpenHelper.movie_year);

            while(check.moveToNext()) {
                if (movieInfo.getTitle().equals(check.getString(titleCol)) && movieInfo.getYear().equals(check.getString(yearCol))) {
                    valid = true;
                }
            }
            if(valid == true){
                //do not post
                if (Locale.getDefault().getDisplayLanguage().equals("français")){
                    Toast.makeText(getContext(), "Ce film est déjà été sauvegardée.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "This movie is already saved.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                try {
                    activity.usrSaveMovie();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Locale.getDefault().getDisplayLanguage().equals("français")){
                    Toast.makeText(getContext(), "Film sélectionné a été sauvegardée.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved the selected movie.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        close.setOnClickListener(clk -> {
            getParentFragmentManager().beginTransaction().replace(R.id.movie_room, searchFrag).commit();
        });


        return detailsLayout;
    }

    /**This is a constructor to assign the MovieInfo object information from MovieSearchFragment to the class scope when called.
     *
     * @param movieInfo Object from MovieInfo class within MovieSearchFragment, providing movie details.
     */
    public MovieDetailsFragment(MovieSearchFragment.MovieInfo movieInfo){
        this.movieInfo = movieInfo;
    }

    /**This class extends from AsyncTask to allow the execution and download of specifically the image given from the HTTP request
     * from the movie database as a URL. This class allows functions to represent what happens before, during and after the download.
     *
     * Types assigned to AsyncTask go for onPreExecute, doInBackground, onPostExecute, in that exact order.
     */
    public static class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        /**This ImageView represents a general ImageView that can be used to assign the Bitmap image from this class to. */
        private ImageView movieImage;
        /**This Bitmap represents the image bitmap that is used to decode and download the image from the URL.
         * Once completed, this Bitmap is used to be assigned to an ImageView to be used for display. */
        private static Bitmap image;

        /** Constructor used to assign a value to an ImageView with the bitmap assigned within this class after download.*/
        public DownloadImageTask(ImageView movieImage) {
            this.movieImage = movieImage;
        }

        /** This function returns the value of the bitmap within this class.
         *
         * @return Returns bitmap for the image downloaded.
         */
        public static Bitmap getMovieBitmap(){
            return image;
        }

        /**This function is always completed before the tasks done in this class, and will display the progress dialog.
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog.show();
        }

        /** This function will run in the background and is the main task for the download.
         *
         * @param url Takes the URL string from the movie details taken from the HTTP Request.
         * @return Returns bitmap for the image downloaded.
         */
        @Override //execute new thread:
        protected Bitmap doInBackground(String... url) {
            image = null;
            try {
                InputStream input = new URL(url[0]).openStream();

                image = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        /** This function will execute after doInBackground is complete and will assign the bitmap to the ImageView.
         *
         * @param bitmap Takes the bitmap returned from doInBackground.
         */
        @Override //runOnUI:
        protected void onPostExecute(Bitmap bitmap) {
            movieImage.setImageBitmap(bitmap);
            prog.dismiss();
        }
    }
}
