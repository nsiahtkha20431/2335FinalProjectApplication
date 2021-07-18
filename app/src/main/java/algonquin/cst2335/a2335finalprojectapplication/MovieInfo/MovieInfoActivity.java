package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

public class MovieInfoActivity extends AppCompatActivity {

    String titleText_input;
    String url;
    FinalOpenHelper opener = new FinalOpenHelper(this);

    EditText title_input;
    String title;
    int year;
    String rating;
    String runtime;
    String actors;
    String plot;
    String URL;
    MovieDetailsFragment detailsFrag = new MovieDetailsFragment();
    MovieSearchFragment searchFrag = new MovieSearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_room);
        getSupportFragmentManager().beginTransaction().add(R.id.movie_room, searchFrag).commit();




    }
}
