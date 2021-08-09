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



/**Class that holds an application fragment providing an editText with user input for a movie to be searched and a Search button.
 * Once the user clicks on the search button, the application will take in the text placed in the editText and search the omdbapi database online
 * with the text to find a movie fitting the text given.
 * If a movie is not found, a message will be provided, if a movie is found, the user will find themselves on a new fragment, the MovieDetailsFragment.
 *
 * @author Raphael Leblanc
 * @version 1.0
 */
public class MovieSearchFragment extends Fragment {

    /** EditText with user input for movie search */
    EditText title_input;
    /** Button to search with editText input for the movie*/
    Button find;
    /** String assigned the value of the editText */
    String titleText_input;
    /** String of the url to create with the editText value to search */
    String url;

    /**This function will provide an editText and a button with onclick function using the editText to search for a movie upon creation of the fragment.
     *
     * @param inflater Inflater used to inflate a layout to the container.
     * @param container Container holding the specified layout.
     * @param savedInstanceState
     * @return Return view (searchLayout)
     */
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
                url = "https://www.omdbapi.com/?t=" + titleText_input + "&apikey=6c9862c2";
                Log.i("URL", url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpRequest();

//            getSupportFragmentManager().beginTransaction().add(R.id.movieDetailsFrag, detailsFrag).commit();


        });



        return searchLayout;
    }

    /**This function will allow to GET a JSON Object from a website using a URL.
     * Upon response from the text given from the URL, it will set the correct string to the correct field on MovieInfo class to create
     * an object to call upon.
     * If the response fails, onErrorResponse will take over and provide a message to the user.
     *
     */
    public void httpRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest httpRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            /** This function works upon response of the URl and will complete the tasks to provide the details necessary to the MovieInfo class.
             *
             * @param response JSON coding of the URL response regarding a movie.
             */
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

            /**This function will provide a message to the user if an error occurs upon retrieving the data from the URL.
             *
             * @param error Error given from the URL.
             */
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

    /**Class MovieInfo is a class that holds the information regarding specific movies searched or saved.
     * This class is used to transfer information and movie objects between pages and fragments.
     *
     */
    static class MovieInfo {
        /** Title of the movie */
        String title;
        /** Year of the movie */
        String year;
        /** Rating of the movie */
        String rating;
        /** Runtime of the movie */
        String runtime;
        /** Actors of the movie */
        String actors;
        /** Plot of the movie */
        String plot;
        /** URL of the movie */
        String URL;

        /**Constructor used to assign all fields upon URL response of a movie search.
         *
         * @param title Title of the movie.
         * @param year Year of the movie.
         * @param rating Rating of the movie.
         * @param runtime Runtime of the movie.
         * @param actors Actors of the movie.
         * @param plot Plot of the movie.
         * @param URL Image URL (poster) of the movie.
         */
        public MovieInfo( String title, String year, String rating, String runtime, String actors, String plot, String URL) {
            this.title = title;
            this.year = year;
            this.rating = rating;
            this.runtime = runtime;
            this.actors = actors;
            this.plot = plot;
            this.URL = URL;

        }

        /**Function to return the title of the movie.
         *
         * @return Title of the movie.
         */
        public String getTitle() {
            return title;
        }

        /**Function to return the year of the movie.
         *
         * @return Year of the movie.
         */
        public String getYear() {
            return year;
        }

        /**Function to return the rating of the movie.
         *
         * @return Rating of the movie.
         */
        public String getRating() {
            return rating;
        }

        /**Function to return the runtime of the movie.
         *
         * @return Runtime of the movie.
         */
        public String getRuntime() {
            return runtime;
        }

        /**Function to return the actors of the movie.
         *
         * @return Actors of the movie.
         */
        public String getActors() {
            return actors;
        }

        /**Function to return the plot of the movie.
         *
         * @return Plot of the movie.
         */
        public String getPlot() {
            return plot;
        }

        /**Function to return the image URL of the movie.
         *
         * @return Image URL of the movie.
         */
        public String getURL() {
            return URL;
        }
    }
}

