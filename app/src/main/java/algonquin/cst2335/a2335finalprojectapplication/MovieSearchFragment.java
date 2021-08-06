package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

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
import java.util.Locale;

public class MovieSearchFragment extends Fragment {

    EditText title_input;
    Button find;
    String titleText_input;
    String url;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View searchLayout = inflater.inflate(R.layout.movie_search_layout, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        title_input = searchLayout.findViewById(R.id.title_input);
        title_input.setText(prefs.getString("TitleName", ""));


        find = searchLayout.findViewById(R.id.find_button);


        find.setOnClickListener(clk -> {
            try {

                editor.putString("TitleName", title_input.getText().toString());
                editor.apply();

                titleText_input = URLEncoder.encode(title_input.getText().toString(), "UTF-8");
                url = "http://www.omdbapi.com/?t=" + titleText_input + "&apikey=6c9862c2";
                Log.i("URL", url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpRequest();

//            getSupportFragmentManager().beginTransaction().add(R.id.movieDetailsFrag, detailsFrag).commit();


        });



        return searchLayout;
    }

    public void httpRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MovieInfo movieInfo;
        JsonObjectRequest httpRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response.getBoolean("Response")) {
                        MovieInfo movieInfo = new MovieInfo(response.getString("Title"), response.getString("Year"), response.getString("Rated"),
                                response.getString("Runtime"), response.getString("Actors"), response.getString("Plot"), response.getString("Poster"));
                        MovieInfoActivity activity = (MovieInfoActivity)getContext();

                        activity.usrSearchedMovie(movieInfo);

                    }
                    else{
                        if (Locale.getDefault().getDisplayLanguage().equals("français")){
                            throw new JSONException("Réponse faillit, pas de film retrouvée avec: " + title_input.getText());
                        }
                        else{
                            throw new JSONException("Response false, no movie found with: " + title_input.getText());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(find, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Locale.getDefault().getDisplayLanguage().equals("français")){
                    Snackbar.make(find, "Quelque chose de mal y a passer en récuperant l'information", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(find, "Something went wrong while retrieving data", Snackbar.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });

        requestQueue.add(httpRequest);
    }


    static class MovieInfo {
        String title;
        String year;
        String rating;
        String runtime;
        String actors;
        String plot;
        String URL;

        public MovieInfo( String title, String year, String rating, String runtime, String actors, String plot, String URL) {
            this.title = title;
            this.year = year;
            this.rating = rating;
            this.runtime = runtime;
            this.actors = actors;
            this.plot = plot;
            this.URL = URL;

        }
        public String getTitle() {
            return title;
        }

        public String getYear() {
            return year;
        }

        public String getRating() {
            return rating;
        }

        public String getRuntime() {
            return runtime;
        }

        public String getActors() {
            return actors;
        }

        public String getPlot() {
            return plot;
        }

        public String getURL() {
            return URL;
        }
    }
}

