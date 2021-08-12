package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is the main RecyclerView that displays all the parsed article titles in a list to scroll through
 * @author Nishat Khan
 * @version 1.0
 */
public class ArticleListFragment extends Fragment {

    /** The Adapter for the RecyclerView in this class */
    ArticleAdapter adapter; //declaring an ArticleAdapter object, but not initializing yet

    /** The ArrayList to hold the article titles in an array to display and to use in the database */
    ArrayList<String> articleTitlesList = new ArrayList<>(); //making an array to hold the titles of each article

    /** The URL of the RSS feed that will need to be parsed to get the article information */
    String stringURL = "https://www.goal.com/en/feeds/news?fmt=rss";

    /**
     * This is the onCreateView function for this class. It inflates the View and creates the RecyclerView object, creates a new thread to parse the RSS feed, instantiates the Adapter object and creates the AlertDialog to ask for a rating immediately when the app is opened.
     * @param inflater The layout that we want to inflate in this fragment
     * @param container The parent view that this fragment is attached to
     * @param savedInstanceState The previous saved state
     * @return Returns the Fragment view with the RecyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articlesListLayout = inflater.inflate(R.layout.article_recycler_layout, container, false);
        RecyclerView articlesRecyclerView = articlesListLayout.findViewById(R.id.myrecycler); //creating an instance of RecyclerView and attaching it to the XML tag

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            fetchAndParseRSS();
            getActivity().runOnUiThread( () -> {
                articlesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        });

        adapter = new ArticleAdapter(articleTitlesList, getContext()); //initializing the ArticleAdapter object and passing it the values of the array and the context (like that it comes from here)

        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)); //set the layout of the contents, i.e. list of repeating views in the recycler view
        articlesRecyclerView.setAdapter(adapter); //this line tells the adapter object that the array we have created is the collection of objects to display

        ratingAlertDialog(); //calling this function to get the AlertDialog running as soon as the app is opened

        return articlesListLayout;
    }

    /**
     * This function parses the RSS feed in order to display the titles of each article in the RecyclerView
     */
    public void fetchAndParseRSS () {
        InputStream is = fetchRSS(this.stringURL);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(is, null);

            articleTitlesList.clear();

            String tag = null;
            String title;
            String imageLink;

            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                String name = xpp.getName();

                switch (xpp.getEventType()) {
                    case XmlPullParser.TEXT:
                        tag = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("title")) {
                            title = tag;
                            articleTitlesList.add(title);
                        }

                }
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function creates the HTTPUrlConnection and InputStream in order to parse the RSS feed
     * @param stringURL The URL that we are trying to connect to
     * @return Returns the InputStream for this URL
     */
    public InputStream fetchRSS(String stringURL) {
        try {
            URL url = new URL(stringURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == urlConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return in;
            }
        } catch (IOException ioe) {
            Log.e("Connection Error", ioe.getMessage());
        }
        return null;
    }

    /**
     * This function creates the AlertDialog to ask for a star-rating of the app each time the Soccer Articles API is opened from the Main Activity
     */
    public void ratingAlertDialog() { //function for the rating AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //making an AlertDialog object called builder

        LinearLayout linearLayout = new LinearLayout(getContext()); //creating a LinearLayout to be implemented into the AlertDialog
        RatingBar rating = new RatingBar(getContext()); //creating a RatingBar

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( //defining the parameters for the LinearLayout
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rating.setLayoutParams(lp); //setting the parameters for the LinearLayout
        rating.setNumStars(5); //setting the number of stars for the RatingBar
        rating.setStepSize(1); //setting the number of stars you can increment by

        linearLayout.addView(rating); //add RatingBar to linearLayout

        builder.setTitle(getString(R.string.soccer_list_rating_title)); //setting title of the AlertDialog
        builder.setView(linearLayout); //adding the LinearLayout view to the AlertDialog

        SharedPreferences prefs = SoccerGames.getAppContext().getSharedPreferences("MyData", Context.MODE_PRIVATE); //creating a SharedPreferences object
        float ratingInt = prefs.getFloat("numStars", rating.getRating());
        rating.setRating(ratingInt);

        builder.setPositiveButton(getString(R.string.soccer_list_rating_done_button), (dialog, cl) -> {
            SharedPreferences.Editor editor = prefs.edit();
            float ratingGivenByUser = rating.getProgress();
            editor.putFloat("numStars", ratingGivenByUser); //put value
            editor.apply(); //commits the changes to the SharedPreferences to the editor
        });

        builder.create().show();
    }

    /**
     * This class is the ViewHolder for this RecyclerView, which takes the individual Views for each row in the list and displays them. It also creates a Toast when a row is clicked and opens the Details Fragment
     */
    private class ArticleViewHolder extends RecyclerView.ViewHolder{

        /** This holds the ImageView of the thumbnail image for the article */
        public ImageView articleThumb; //declaring a variable to hold the thumbnail image for each article in the list

        /** This holds the TextView object for the article title */
        public TextView articleTitle; //declaring a variable to hold the article title for each article in the list

        /**
         * This is the constructor for this class that creates the row views
         * @param itemView The View of the row to display
         */
        public ArticleViewHolder(View itemView) {
            super(itemView); //getting the super's View

            articleThumb = itemView.findViewById(R.id.articleThumb); //initializing
            articleTitle = itemView.findViewById(R.id.articleTitle); //initializing

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                SoccerGames parentActivity = (SoccerGames)getContext();
                parentActivity.userClickedTitle(articleTitlesList.get(position), position);
            });
        }
    }

    /**
     * The Adapter class for this RecyclerView. Adapts a collection of objects to display and tells the list how to build the items
     */
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

        /** The ArrayList to use for an ArticleAdapter object */
        ArrayList<String> array;

        /** The context variable to use to get the context */
        Context context;

        /**
         * The constructor for this class. Creates an adapter object.
         * @param array The array of the article titles to display
         * @param context The context of this adapter object
         */
        public ArticleAdapter(ArrayList array, Context context) { //constructor for this class
            this.array = array; //setting the articleTitlesList as the array for this instance of ArticleAdapter
            this.context = context; //setting 'this' as the context for this instance of ArticleAdapter
        }

        /**
         * The onCreateViewHolder for this Adapter
         * @param parent The parent View
         * @param viewType The int view type
         * @return Returns The ViewHolder of this loaded row
         */
        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View loadedRow = LayoutInflater.from(context).inflate(R.layout.article_row_layout, parent, false); //taking the loaded row and inflating it (takes layout xml files and converts into a View object)

            return new ArticleViewHolder(loadedRow); //returning the loaded row, now an ArticleViewHolder object
        }

        /**
         * The function to bind the view
         * @param viewHolder The ViewHolder
         * @param position The position for this row
         */
        @Override
        public void onBindViewHolder(ArticleViewHolder viewHolder, int position) {
            viewHolder.articleTitle.setText(array.get(position));
        }

        /**
         * The function to get the amount of items in this list
         * @return Returns the number of items in this list
         */
        @Override
        public int getItemCount() {
            return array.size(); //returns the number of rows in the RecyclerView
        }
    }

    //this class is similar to the ChatMessage class in the RecyclerView lab (week 5) - used to store and get the information for each article to be displayed in the RecyclerView
    //not a ViewHolder or an Adapter

    /**
     * This class is meant to hold information about each article that is parsed in order to display it in the List and in the Details Fragment
     */
    public static class Article {

        /** The String title of the article */
        String title;

        /** The String date of when the article was published */
        String datePublished;

        /** The String url of this article */
        String url;

        /** The String description of this article */
        String desc;

        /** The id of this article */
        long id;

        /**
         * The main constructor for this class
         * @param title Title of this article
         * @param datePublished Date this article was published
         * @param url The url of this article
         * @param desc The description of this article
         */
        public Article(String title, String datePublished, String url, String desc) {
            this.title = title;
            this.datePublished = datePublished;
            this.url = url;
            this.desc = desc;
        }

        /**
         * Second constructor for this class that takes everything that the first contructor takes including the id
         * @param title Title of this article
         * @param datePublished Date this article was published
         * @param url The url of this article
         * @param desc The description of this article
         * @param id The id of this article
         */
        public Article(String title, String datePublished, String url, String desc, long id) {
            this.title = title;
            this.datePublished = datePublished;
            this.url = url;
            this.desc = desc;
            setID(id);
        }

        /**
         * Getter for the title of this article
         * @return Returns the title of this article
         */
        public String getTitle() {
            return title;
        }

        /**
         * Getter for the date of this article
         * @return Returns the date this article was published
         */
        public String getDatePublished() {
            return datePublished;
        }

        /**
         * Getter for the url of this article
         * @return Returns the url of this article
         */
        public String getUrl() {
            return url;
        }

        /**
         * Getter for the description of this article
         * @return Returns the description of this article
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Getter for the id of this article
         * @return Returns the id of this article
         */
        public long getID() {
            return this.id;
        }

        /**
         * Setter for the id of this article
         */
        public void setID(long id) {
            this.id = id;
        }
    }
}
