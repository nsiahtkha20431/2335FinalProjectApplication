package algonquin.cst2335.a2335finalprojectapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.opener;

public class MovieDetailsFragment extends Fragment {

    TextView title;
    TextView year;
    TextView rating;
    TextView runtime;
    TextView actors;
    TextView plot;
    ImageView poster;
    Button save;
    Button close;
    MovieSearchFragment searchFrag = new MovieSearchFragment();
    MovieSearchFragment.MovieInfo movieInfo;
    private static ProgressDialog prog;



    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout =inflater.inflate(R.layout.movie_details_layout, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

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

    public MovieDetailsFragment(MovieSearchFragment.MovieInfo movieInfo){
        this.movieInfo = movieInfo;
    }


    public static class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        private ImageView movieImage;
        private static Bitmap image;

        public DownloadImageTask(ImageView movieImage) {
            this.movieImage = movieImage;
        }

        public static Bitmap getMovieBitmap(){
            return image;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog.show();
        }


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
        @Override //runOnUI:
        protected void onPostExecute(Bitmap bitmap) {
            movieImage.setImageBitmap(bitmap);
            prog.dismiss();
        }
    }
}
