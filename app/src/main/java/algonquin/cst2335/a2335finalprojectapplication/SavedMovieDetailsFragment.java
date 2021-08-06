package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SavedMovieDetailsFragment extends Fragment {

    TextView title;
    TextView year;
    TextView rating;
    TextView runtime;
    TextView actors;
    TextView plot;
    ImageView poster;
    Button del;
    Button close;
    MovieSearchFragment searchFrag = new MovieSearchFragment();
    SavedMovieFragment savedFrag = new SavedMovieFragment();
    MovieSearchFragment.MovieInfo movieInfo;
    int moviePosition;

    public SavedMovieDetailsFragment(MovieSearchFragment.MovieInfo movieInfo, int position) {
        this.movieInfo = movieInfo;
        this.moviePosition = position;
    }




    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View savedDetailsLayout =inflater.inflate(R.layout.saved_details_layout, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        title = savedDetailsLayout.findViewById(R.id.title_mes);
        title.setText(movieInfo.getTitle());
        year = savedDetailsLayout.findViewById(R.id.year_mes);
        year.setText(movieInfo.getYear());
        rating = savedDetailsLayout.findViewById(R.id.rating_mes);
        rating.setText(movieInfo.getRating());
        runtime = savedDetailsLayout.findViewById(R.id.runtime_mes);
        runtime.setText(movieInfo.getRuntime());
        actors = savedDetailsLayout.findViewById(R.id.actors_mes);
        actors.setText(movieInfo.getActors());
        plot = savedDetailsLayout.findViewById(R.id.plot_mes);
        plot.setText(movieInfo.getPlot());

        poster = savedDetailsLayout.findViewById(R.id.pos);
        Bitmap posterImage = BitmapFactory.decodeFile(getContext().getFilesDir() + "/" + movieInfo.getTitle().replace(" ", "") + ".jpeg");
        poster.setImageBitmap(posterImage);
        //new MovieDetailsFragment.DownloadImageTask(poster).execute(movieInfo.getURL());
        del = savedDetailsLayout.findViewById(R.id.del_button);
        close = savedDetailsLayout.findViewById(R.id.close_but);
        MovieInfoActivity activity = (MovieInfoActivity)getContext();

        del.setOnClickListener(clk -> {

            activity.movieDeleted(movieInfo, moviePosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();

        });



        close.setOnClickListener(clk -> {

            getParentFragmentManager().beginTransaction().remove(this).commit();
        });


        return savedDetailsLayout;
    }





}