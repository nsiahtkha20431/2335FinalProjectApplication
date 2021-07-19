package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

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

        save = detailsLayout.findViewById(R.id.save_button);
        close = detailsLayout.findViewById(R.id.close_button);

        save.setOnClickListener(clk -> {
            //TODO database input
              MovieInfoActivity activity = (MovieInfoActivity)getContext();
              activity.usrSaveMovie();
            Toast.makeText(getContext(), "Saved the selected movie.", Toast.LENGTH_SHORT).show();

        });

        close.setOnClickListener(clk -> {
            getParentFragmentManager().beginTransaction().replace(R.id.movie_room, searchFrag).commit();
        });


        return detailsLayout;
    }

    public MovieDetailsFragment(MovieSearchFragment.MovieInfo movieInfo){
            this.movieInfo = movieInfo;
    }
}
