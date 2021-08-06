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

/**This class allows to obtain data from the movieInfo object that was selected by its position and display it in a fragment, just
 * like MovieDetailsFragment.
 * Added functionality is to either close or delete the movie from the recyclerview.
 * @author Raphael Leblanc
 * @version 1.0
 */
public class SavedMovieDetailsFragment extends Fragment {

    /**Title of the movie */
    TextView title;
    /**Year of the movie */
    TextView year;
    /**Rating of the movie */
    TextView rating;
    /**Runtime of the movie */
    TextView runtime;
    /**Actors of the movie */
    TextView actors;
    /**Plot of the movie */
    TextView plot;
    /**Poster of the movie */
    ImageView poster;
    /**Button with text "Delete", allows to delete from database and recyclerview. */
    Button del;
    /**Button with text "Close", allows to close current fragment of the movie selected to return to the recyclerview list. */
    Button close;
    /** Search functionality fragment to be called or referenced from to use its functions. */
    MovieSearchFragment searchFrag = new MovieSearchFragment();
    /**Saved movie list fragment to be called or referenced from to use its functions. */
    SavedMovieFragment savedFrag = new SavedMovieFragment();
    /**MovieInfo object containing all relative information/details of a movie. */
    MovieSearchFragment.MovieInfo movieInfo;
    /**New field variable for the position to delete correct movie from the recyclerview. */
    int moviePosition;

    /**Constructor that allows to assign to class scope movieInfo object and position from other classes/functions.
     *
     * @param movieInfo Object containing information from a movie.
     * @param position Position of movie selected.
     */
    public SavedMovieDetailsFragment(MovieSearchFragment.MovieInfo movieInfo, int position) {
        this.movieInfo = movieInfo;
        this.moviePosition = position;
    }


    /**This function allows the creation of the SavedMovieDetailsFragment layout and assigns information to the correct text and image views.
     * Functionality added to the buttons close and delete.
     * Functionality to the delete button allows to close current fragment and provide instructions to previous fragment.
     *
     * @param inflater Inflater used to inflate a layout to the container.
     * @param container Container holding the specified layout.
     * @param savedInstanceState
     * @return Return view (savedDetailsLayout)
     */
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