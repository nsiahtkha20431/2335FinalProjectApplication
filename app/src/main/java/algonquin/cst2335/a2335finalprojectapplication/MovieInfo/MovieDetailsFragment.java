package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class MovieDetailsFragment extends Fragment {

    TextView title;
    TextView year;
    TextView rating;
    TextView runtime;
    TextView actors;
    TextView plot;
    ImageView poster;

    MovieSearchFragment.MovieInfo movieInfo;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout =inflater.inflate(R.layout.movie_details_layout, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        title = detailsLayout.findViewById(R.id.title_message);
        title.setText(movieInfo.getTitle());
        year = detailsLayout.findViewById(R.id.year_message);
        year.setText(movieInfo.getYear());
        rating = detailsLayout.findViewById(R.id.rating_message);
        rating.setText(movieInfo.getRating());
        runtime = detailsLayout.findViewById(R.id.runtime_message);
        runtime.setText(movieInfo.getRuntime());
        actors = detailsLayout.findViewById(R.id.actors_message);
        actors.setText(movieInfo.getActors());
        plot = detailsLayout.findViewById(R.id.plot_message);
        plot.setText(movieInfo.getPlot());
        //TODO ImageView with ASyncTask

        return detailsLayout;
    }

    public MovieDetailsFragment(MovieSearchFragment.MovieInfo movieInfo){
            this.movieInfo = movieInfo;
    }
}
