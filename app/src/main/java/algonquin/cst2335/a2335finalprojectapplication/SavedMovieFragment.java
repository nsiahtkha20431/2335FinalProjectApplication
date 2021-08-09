package algonquin.cst2335.a2335finalprojectapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.opener;


/**This class withholds a layout with a recyclerview and  using the database to populate it with the saved movies selected within the search
 * option of MovieSearchFragment.
 * Functionality added to find and use each object in the recyclerview to load a detailed information of the specified movie
 * on a MovieSavedDetailsFragment.
 * @author Raphael Leblanc
 * @version 1.0
 */
public class SavedMovieFragment extends Fragment {

    /**ArrayList containing all MovieInfo object for the recyclerview. */
    ArrayList<MovieSearchFragment.MovieInfo> movieInfoArrayList = new ArrayList<>();
    /**Object of MovieAdapter, a Recyclerview adapter.  */
    MovieAdapter movieAdapter = new MovieAdapter();
    /**Recyclerview named movieList containing all saved movies by using the movieInfoArrayList ArrayList. */
    RecyclerView movieList;


    /** This function allows to inflate and populate a fragment for the saved movies list using a recyclerview.
     * From this function, functionality will be available to click on the correct movie within the list and attain the details of that
     * movie from an added SavedMovieDetailsFragment.
     *
     * @param inflater Inflater used to inflate a layout to the container.
     * @param container Container holding the specified layout.
     * @param savedInstanceState
     * @return Return view (savedLayout).
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View savedLayout = inflater.inflate(R.layout.saved_movies_layout, container, false);

        movieList = savedLayout.findViewById(R.id.myrecycler);


        SQLiteDatabase db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("SELECT * FROM " + FinalOpenHelper.MOVIE_TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int titleCol = results.getColumnIndex(FinalOpenHelper.movie_title);
        int yearCol = results.getColumnIndex(FinalOpenHelper.movie_year);
        int ratingCol = results.getColumnIndex(FinalOpenHelper.movie_rating);
        int runtimeCol = results.getColumnIndex(FinalOpenHelper.movie_runtime);
        int actorsCol = results.getColumnIndex(FinalOpenHelper.movie_actors);
        int plotCol = results.getColumnIndex(FinalOpenHelper.movie_plot);
        int posterUrlCol = results.getColumnIndex(FinalOpenHelper.movie_poster);

        movieInfoArrayList.clear();
        while (results.moveToNext()) {
            long id = results.getInt(_idCol);
            String title = results.getString(titleCol);
            String year = results.getString(yearCol);
            String rated = results.getString(ratingCol);
            String runtime = results.getString(runtimeCol);
            String actors = results.getString(actorsCol);
            String plot = results.getString(plotCol);
            String poster = results.getString(posterUrlCol);
            movieInfoArrayList.add(new MovieSearchFragment.MovieInfo(title, year, rated, runtime, actors, plot, poster));
        }

        movieList.setAdapter(movieAdapter);
        movieList.setLayoutManager(new LinearLayoutManager(getContext()));

        return savedLayout;
    }

    /**This function allows an alert dialog to be displayed whenever called and added functionality.
     * This function is called when a movie is deleted from the saved movies list.
     *
     * @param movieInfo Object to be deleted from the database and the recyclerview.
     * @param position Position from which the selected object to be deleted will be.
     */
    public void movieDeleted(MovieSearchFragment.MovieInfo movieInfo, int position){
        if (Locale.getDefault().getDisplayLanguage().equals("français")){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Question: ")
                    .setMessage("Êtes-vous certain de vouloir effacer: " + movieInfo.getTitle() + " de votre liste?")
                    .setNegativeButton("Non", (dialog, cl) -> {
                        //does nothing
                    })
                    .setPositiveButton("Oui", (dialog, cl) -> {
                        MovieSearchFragment.MovieInfo removedMovie = movieInfoArrayList.get(position);
                        movieInfoArrayList.remove(position);
                        movieAdapter.notifyItemRemoved(position);
                        SQLiteDatabase db = opener.getWritableDatabase();
                        db.delete(FinalOpenHelper.MOVIE_TABLE_NAME, "Title=? AND Year=?", new String[]{removedMovie.getTitle(), removedMovie.getYear()});
                        Snackbar.make(movieList, "Film: " + removedMovie.getTitle() + ", " + removedMovie.getYear() + " a été effacer.",  Snackbar.LENGTH_LONG).show();
                    })
                    .create().show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Question: ")
                    .setMessage("Are you sure you want to delete: " + movieInfo.getTitle() + " from your list?")
                    .setNegativeButton("No", (dialog, cl) -> {
                        //does nothing
                    })
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        MovieSearchFragment.MovieInfo removedMovie = movieInfoArrayList.get(position);
                        movieInfoArrayList.remove(position);
                        movieAdapter.notifyItemRemoved(position);
                        SQLiteDatabase db = opener.getWritableDatabase();
                        db.delete(FinalOpenHelper.MOVIE_TABLE_NAME, "Title=? AND Year=?", new String[]{removedMovie.getTitle(), removedMovie.getYear()});
                        Snackbar.make(movieList, "Movie: " + removedMovie.getTitle() + ", " + removedMovie.getYear() + " was deleted.", Snackbar.LENGTH_LONG).show();
                    })
                    .create().show();
        }

    }

    /**This class allows to populate the recyclerview and assign data correctly to each view component in the row layout.
     *
     */
    private class SavedView extends RecyclerView.ViewHolder {

        TextView title;
        TextView year;
        TextView rated;
        ImageView poster;
        int position = -1;

        /**Allows to load in a view to the recyclerview (Load row layout within the recyclerview).
         *
         * @param itemView View to be given in the recyclerview.
         */
        public SavedView(View itemView) {
            super(itemView);



            itemView.setOnClickListener(e -> {

                MovieInfoActivity activity = (MovieInfoActivity)getContext();
                position = getAbsoluteAdapterPosition();
                activity.usrClickedSavedMovie(movieInfoArrayList.get(position), position);

            });

            title = itemView.findViewById(R.id.save_title);
            year = itemView.findViewById(R.id.save_year);
            rated = itemView.findViewById(R.id.save_rating);
            poster = itemView.findViewById(R.id.save_poster);
        }

        /**This function allows to set the position of the view within the recyclerview.
         *
         * @param position Position of current view within the recyclerview.
         */
        public void setPosition(int position) {
            this.position = position;
        }
    }

    /**Adapter used for the recyclerview to assign appropriately the correct view upon creation and assigning the correct data to
     * the appropriate fields.
     *
     */
    private class MovieAdapter extends RecyclerView.Adapter<SavedView> {

        /**Allows to inflate and add the view to be given to the recyclerview.
         *
         * @param parent The viewgroup.
         * @param viewType Type of the view.
         * @return Returns view of type SavedView.
         */
        @Override
        public SavedView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();

            View loadMovie = inflater.inflate(R.layout.saved_movie_row, parent, false);
            return new SavedView(loadMovie);
        }

        /**This allows to assign the data to the appropriate TextView and their position.
         * Also allows to load the image poster from internal storage, unless it is not there, then it will download it again.
         *
         * @param hold The current view.
         * @param position The position of the current view within the recyclerview.
         */
        @Override
        public void onBindViewHolder(SavedView hold, int position) {
            MovieInfoActivity activity = new MovieInfoActivity();
            hold.title.setText(movieInfoArrayList.get(position).getTitle());
            hold.year.setText(movieInfoArrayList.get(position).getYear());
            hold.rated.setText(movieInfoArrayList.get(position).getRating());

            File file = new File(getContext().getFilesDir(), movieInfoArrayList.get(position).getTitle().replace(" ", "") + ".jpeg");
            Bitmap poster = null;

            if(file.exists()){
                poster = BitmapFactory.decodeFile(getContext().getFilesDir() + "/" +
                        movieInfoArrayList.get(position).getTitle().replace(" ", "") + ".jpeg");

            }
            else{
                    new MovieDetailsFragment.DownloadImageTask(hold.poster).execute(movieInfoArrayList.get(position).getURL());
            }

            if(poster != null){
                hold.poster.setImageBitmap(poster);
            }
            hold.setPosition(position);

        }

        /**Allows to obtain the total size/amount of objects within the movieInfoArrayList (recyclerview).
         *
         * @return Returns item count (size of the array (recyclerview)).
         */
        @Override
        public int getItemCount() {
            return movieInfoArrayList.size();
        }


    }
}

