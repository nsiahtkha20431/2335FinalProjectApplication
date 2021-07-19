package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.opener;

public class SavedMovieFragment extends Fragment {

    ArrayList<MovieSearchFragment.MovieInfo> movieInfoArrayList = new ArrayList<>();
    MovieAdapter movieAdapter = new MovieAdapter();
    RecyclerView movieList;

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

    public void movieDeleted(MovieSearchFragment.MovieInfo movieInfo, int position){
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
                            Snackbar.make(movieList, "Movie: " + removedMovie.getTitle() + ", " + removedMovie.getYear() + " was deleted.",  Snackbar.LENGTH_LONG).show();
                        })
                        .create().show();


    }

    private class SavedView extends RecyclerView.ViewHolder {

        TextView title;
        TextView year;
        TextView rated;
        ImageView poster;
        int position = -1;


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

        public void setPosition(int position) {
            this.position = position;
        }
    }


    private class MovieAdapter extends RecyclerView.Adapter<SavedView> {

        @Override
        public SavedView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();

            View loadMovie = inflater.inflate(R.layout.saved_movie_row, parent, false);
            return new SavedView(loadMovie);
        }

        @Override
        public void onBindViewHolder(SavedView hold, int position) {
            hold.title.setText(movieInfoArrayList.get(position).getTitle());
            hold.year.setText(movieInfoArrayList.get(position).getYear());
            hold.rated.setText(movieInfoArrayList.get(position).getRating());
            new MovieDetailsFragment.DownloadImageTask(hold.poster).execute(movieInfoArrayList.get(position).getURL());

            hold.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return movieInfoArrayList.size();
        }


    }
}

