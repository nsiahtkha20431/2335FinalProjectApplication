package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.db;

/**
 * This class is the main Details fragment that is opened any time an article from the main RecyclerView is clicked. It displays the detials of each article and allows users to add the article to their Favorites list
 * @author Nishat Khan
 * @version 1.0
 */
public class ArticleDetailsFragment extends Fragment {

    /** The String-type article title that was selected */
    String chosenArticle;

    /** The Article-type favorite article that was selected */
    ArticleListFragment.Article chosenFavArticle;

    /** The position of the chosen article */
    int chosenPosition;

    /** The String url of the RSS feed to be parsed */
    String stringURL = "https://www.goal.com/en/feeds/news?fmt=rss&mode=xml";

    /** The url to be displayed in the Details fragment */;
    String linkString = null;

    /** The description to be displayed in the Details fragment */
    String description = null;

    /** The date to be displayed in the Detials fragment */
    String pubDate = null;

    /** The image thumbnail to be displayed in the Details fragment */
    ImageView thumbnail = null;

    /**
     * The first constructor for this class that takes the article title as a String and the position
     * @param article The String article title
     * @param position The position of the article
     */
    public ArticleDetailsFragment(String article, int position) {
        chosenArticle = article;
        chosenPosition = position;
    }

    /**
     * The second constructor for this class that takes the article when it is an Article object and the position
     * @param article The Article object
     * @param position The position
     */
    public ArticleDetailsFragment(ArticleListFragment.Article article, int position) {
        chosenFavArticle = article;
        chosenPosition = position;
    }

    /**
     * The onCreateView for this fragment. Inflates the View, sets all the TextViews and Buttons, creates a new thread to parse all the information from the RSS feed and displays it, tells the app what to do when the buttons are clicked
     * @param inflater The layout that we want to inflate in this fragment
     * @param container The parent view that this fragment is attached to
     * @param savedInstanceState The previous saved state
     * @return Returns the Fragment view with the details displayed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articlesDetailsLayout = inflater.inflate(R.layout.soccer_details_layout, container, false);

        TextView titleView = articlesDetailsLayout.findViewById(R.id.titleView);
        TextView dateView = articlesDetailsLayout.findViewById(R.id.dateView);
        TextView urlView = articlesDetailsLayout.findViewById(R.id.urlView);
        TextView descriptionView = articlesDetailsLayout.findViewById(R.id.descriptionView);
        Button addToFavButton = articlesDetailsLayout.findViewById(R.id.addToFavButton);
        Button backButton = articlesDetailsLayout.findViewById(R.id.backButton);
        Button browserButton = articlesDetailsLayout.findViewById(R.id.broswerButton);


        descriptionView.setText(getString(R.string.description_is));
        urlView.setText(getString(R.string.url_is));

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            fetchAndParseRSS();
            getActivity().runOnUiThread( () -> {
                titleView.setText(getString(R.string.article_is) + " " + chosenArticle);
                descriptionView.setText(getString(R.string.description_is) + " " + description);
                dateView.setText(getString(R.string.date_is) + " " + pubDate);
                urlView.setText(getString(R.string.url_is) + " " + linkString);
            });
        });

        backButton.setOnClickListener(clicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        addToFavButton.setOnClickListener(clicked -> {
            ArticleListFragment.Article article;
            article = new ArticleListFragment.Article(chosenArticle, pubDate, linkString, description);
            ContentValues newRow = new ContentValues();
            newRow.put(FinalOpenHelper.TITLE_COLUMN, article.getTitle());
            newRow.put(FinalOpenHelper.DATE_COLUMN, article.getDatePublished());
            newRow.put(FinalOpenHelper.URL_COLUMN, article.getUrl());
            newRow.put(FinalOpenHelper.DESC_COLUMN, article.getDesc());
            long newID = db.insert(FinalOpenHelper.SOCCER_TABLE_NAME, FinalOpenHelper.TITLE_COLUMN, newRow);
            article.setID(newID);
        });

        browserButton.setOnClickListener( (clicked) -> {
            Uri openInBrowser = Uri.parse(linkString);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, openInBrowser);
//            browserIntent.setPackage("com.google.android.apps.maps");
            startActivity(browserIntent);
        });

        return articlesDetailsLayout;
    }

    /**
     * This function creates the InputStream object from the results of the fetchRSS method, the XmlPullParser object and parses the RSS feed
     */
    public void fetchAndParseRSS () {
        InputStream is = fetchRSS(this.stringURL);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(is, null);

            String tag = null;
            String title = null;
            String link  = null;
            String description = null;

            boolean titleTagSeenAndEqualsChosenArticle = false;
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (xpp.getEventType()) {
                    case XmlPullParser.TEXT:
                        tag = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals("title")) {
                            title = tag;
                            if (chosenArticle.equals(title)) {
                                titleTagSeenAndEqualsChosenArticle = true;
                            }
                        } else if (tagName.equals("description") && titleTagSeenAndEqualsChosenArticle && description == null) {
                            description = tag;
                        }
                        else if(tagName.equals("link") && titleTagSeenAndEqualsChosenArticle && link == null){
                            link = tag;
                        }
                        else if (tagName.equals("pubDate") && titleTagSeenAndEqualsChosenArticle && pubDate == null) {
                            pubDate = tag;
                        }
                }
            }
            this.linkString = link;
            this.description = description;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function gets the Input Stream
     * @param stringURL The url of the RSS feed we want to parse
     * @return Returns the InputStream to parse
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
}
