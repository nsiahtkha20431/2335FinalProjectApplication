package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;


import android.os.Bundle;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

public class MovieInfoActivity extends AppCompatActivity {




    MovieSearchFragment searchFrag = new MovieSearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_room);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, searchFrag).commit();




    }

    public void usrSearchedMovie(MovieSearchFragment.MovieInfo movieInfo) {

        MovieDetailsFragment detailsFrag = new MovieDetailsFragment(movieInfo);

        getSupportFragmentManager().beginTransaction().replace(R.id.movie_room, detailsFrag).commit();
    }
}
