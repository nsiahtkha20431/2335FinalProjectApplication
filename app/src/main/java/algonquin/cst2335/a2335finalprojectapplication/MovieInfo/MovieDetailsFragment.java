package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

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
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

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




    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout =inflater.inflate(R.layout.movie_details_layout, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Log.i("Test movieinfo" , "" + movieInfo.getTitle());
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
        //TODO ImageView with ASyncTask
        poster = detailsLayout.findViewById(R.id.pos);
        new DownloadImageTask(poster).execute(movieInfo.getURL());
        save = detailsLayout.findViewById(R.id.del_button);
        close = detailsLayout.findViewById(R.id.close_but);

        save.setOnClickListener(clk -> {
            //TODO database input
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
                Toast.makeText(getContext(), "This movie is already saved.", Toast.LENGTH_SHORT).show();
            }
            else{
                activity.usrSaveMovie();
                Toast.makeText(getContext(), "Saved the selected movie.", Toast.LENGTH_SHORT).show();
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

        ImageView movieImage;

        public DownloadImageTask(ImageView movieImage) {
            this.movieImage = movieImage;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap image = null;
            try {
                InputStream input = new URL(url[0]).openStream();
                image = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            movieImage.setImageBitmap(bitmap);
        }
    }
}
