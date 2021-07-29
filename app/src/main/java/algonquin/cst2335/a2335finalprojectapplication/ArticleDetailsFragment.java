package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import algonquin.cst2335.a2335finalprojectapplication.R;

import static android.content.ContentValues.TAG;

public class ArticleDetailsFragment extends Fragment {
    String chosenArticle;
    int chosenPosition;

    String stringURL = "https://www.goal.com/en/feeds/news?fmt=rss&mode=xml";

    String titleString = null;
    String link = null;
    String description = null;
    String pubDate = null;
    ImageView thumbnail = null;


    public ArticleDetailsFragment(String article, int position) {
        chosenArticle = article;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articlesDetailsLayout = inflater.inflate(R.layout.soccer_details_layout, container, false);

        delete = articlesDetailsLayout.findViewById(R.id.deleteFromFavButton);

        TextView titleView = articlesDetailsLayout.findViewById(R.id.titleView);
        TextView urlView = articlesDetailsLayout.findViewById(R.id.urlView);
        TextView descriptionView = articlesDetailsLayout.findViewById(R.id.descriptionView);
        Button addToFavButton = articlesDetailsLayout.findViewById(R.id.addToFavButton);
        Button deleteFromFavButton = articlesDetailsLayout.findViewById(R.id.deleteFromFavButton);
        Button backButton = articlesDetailsLayout.findViewById(R.id.backButton);

        titleView.setText("Article is: " + chosenArticle);
        urlView.setText("URL is: URL");
        descriptionView.setText("Description: No description yet");

        backButton.setOnClickListener(clicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        addToFavButton.setOnClickListener(clicked -> {
        });

        deleteFromFavButton.setOnClickListener(clicked -> {
//            SoccerGames parentActivity = (SoccerGames)getContext();
//            parentActivity.notifyArticleDeleted(chosenArticle, chosenPosition);

            Snackbar.make(delete, "You deleted " + chosenArticle, Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", click -> { }).show();

        });

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute( () -> {

            try {

                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in,"UTF-8");

                //code is not currently reaching this point
                boolean startedItem = false;
                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {

                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:

                            if (xpp.getName().equals("channel")) {
                                startedItem = true;
                                titleString = xpp.getAttributeValue(null, "title");
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            if (xpp.getName().equals("item")) {
                                startedItem = false;
                            }


                            break;
                        case XmlPullParser.TEXT:
                            if(startedItem) {
                                Log.d(TAG, xpp.getText());
                            }
                            break;
                    }
                }

                getActivity().runOnUiThread( () -> {
                   TextView title = getActivity().findViewById(R.id.titleView);
                   title.setText(titleString);
                });

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        });

        return articlesDetailsLayout;
    }
}
