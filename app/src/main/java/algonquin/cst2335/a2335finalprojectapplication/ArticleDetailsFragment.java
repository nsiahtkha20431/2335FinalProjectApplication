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
    String linkString = null;
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
        TextView dateView = articlesDetailsLayout.findViewById(R.id.dateView);
        TextView urlView = articlesDetailsLayout.findViewById(R.id.urlView);
        TextView descriptionView = articlesDetailsLayout.findViewById(R.id.descriptionView);
        Button addToFavButton = articlesDetailsLayout.findViewById(R.id.addToFavButton);
        Button deleteFromFavButton = articlesDetailsLayout.findViewById(R.id.deleteFromFavButton);
        Button backButton = articlesDetailsLayout.findViewById(R.id.backButton);


//        titleView.setText(getString(R.string.article_is) + " " + chosenArticle);
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
        });

        deleteFromFavButton.setOnClickListener(clicked -> {
//            SoccerGames parentActivity = (SoccerGames)getContext();
//            parentActivity.notifyArticleDeleted(chosenArticle, chosenPosition);

            Snackbar.make(delete, "You deleted " + chosenArticle, Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", click -> { }).show();

        });

//        Executor newThread = Executors.newSingleThreadExecutor();
//        newThread.execute( () -> {
//
//            try {
//
//                URL url = new URL(stringURL);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(false);
//                XmlPullParser xpp = factory.newPullParser();
//                xpp.setInput(in,"UTF-8");
//
//                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
//                    String whatIsIt = String.valueOf(xpp.next());
//                    switch (xpp.getEventType()) {
//
//                        case XmlPullParser.START_TAG:
//
//                            if (xpp.getEventType() == XmlPullParser.TEXT) {
//
//                                String contents = xpp.getName();
//
////                                if (contents.equals("pubDate")) {
////                                    titleString = xpp.getText();
////                                }
//                            }
//
//                            break;
//                        case XmlPullParser.END_TAG:
//                            break;
//
//                        case XmlPullParser.TEXT:
//                            String hello = null;
//                            //logic to get the text out of the tag
//                            break;
//                    }
//                }
//                getActivity().runOnUiThread( () -> {
//                   TextView title = getActivity().findViewById(R.id.titleView);
//                   title.setText(titleString);
//                });
//
//            } catch (IOException | XmlPullParserException e) {
//                e.printStackTrace();
//            }
//        });

        return articlesDetailsLayout;
    }

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

//    private String readTitle(XmlPullParser xpp) throws IOException, XmlPullParserException {
//        xpp.require(XmlPullParser.START_TAG, null, "title");
//        String title = readText(xpp);
//        xpp.require(XmlPullParser.END_TAG, null, "title");
//        return title;
//    }
//
//    private String readText(XmlPullParser xpp) throws IOException, XmlPullParserException {
//        String result = "";
//        if (xpp.next() == XmlPullParser.TEXT) {
//            result = xpp.getText();
//            xpp.nextTag();
//        }
//        return result;
//    }
}
