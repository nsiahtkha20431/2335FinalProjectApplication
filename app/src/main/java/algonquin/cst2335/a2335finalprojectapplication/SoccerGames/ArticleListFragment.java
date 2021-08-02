package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

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

import algonquin.cst2335.a2335finalprojectapplication.R;
import algonquin.cst2335.a2335finalprojectapplication.SoccerGames.SoccerGames;


public class ArticleListFragment extends Fragment {
    ArticleAdapter adapter; //declaring an ArticleAdapter object, but not initializing yet
    ArrayList<String> articleTitlesList = new ArrayList<>(); //making an array to hold the titles of each article
    String stringURL = "https://www.goal.com/en/feeds/news?fmt=rss";

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
//        articleTitlesList.add(getString(R.string.soccer_list_article_title)); //adding temporary article titles for the array
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));
//        articleTitlesList.add(getString(R.string.soccer_list_article_title));

        adapter = new ArticleAdapter(articleTitlesList, getContext()); //initializing the ArticleAdapter object and passing it the values of the array and the context (like that it comes from here)

        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)); //set the layout of the contents, i.e. list of repeating views in the recycler view
        articlesRecyclerView.setAdapter(adapter); //this line tells the adapter object that the array we have created is the collection of objects to display

        ratingAlertDialog(); //calling this function to get the AlertDialog running as soon as the app is opened

        return articlesListLayout;
    }

    public void fetchAndParseRSS () {
        InputStream is = fetchRSS(this.stringURL);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(is, null);

            articleTitlesList.clear();

            String tag = null;
            String title = null;
            String link  = null;
            String description = null;

            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                String name = xpp.getName();

                switch (xpp.getEventType()) {
                    case XmlPullParser.TEXT:
                        tag = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("title")) {
                            // in your details fragment
                            // if title == chosenArticle
                            // then set the description, link and tag
                            title = tag;
                            articleTitlesList.add(title);
                        } else if (name.equals("description")) {
                            description = tag;
                            articleTitlesList.add(description);
                        }
                        else if(name.equals("link")){
                            link = tag;
                            articleTitlesList.add(link);
                        }


                }
            }

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

    //not currently being called in the code
    public void notifyArticleDeleted(String chosenArticle, int chosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Danger!")
                .setMessage("Do you want to delete the article " + articleTitlesList.get(chosenPosition))
                .setNegativeButton("Cancel", (dialog, cl) -> { })
                .setPositiveButton("Delete", (dialog, cl) -> {
                    String removedArticle = articleTitlesList.get(chosenPosition);
                    articleTitlesList.remove(chosenPosition);
                    adapter.notifyItemRemoved(chosenPosition);
                }).create().show();
    }

    //ViewHolder for this RecyclerView - this represents one element of the list and how it will look like
    private class ArticleViewHolder extends RecyclerView.ViewHolder{

        public ImageView articleThumb; //declaring a variable to hold the thumbnail image for each article in the list
        public TextView articleTitle; //declaring a variable to hold the article title for each article in the list

        public ArticleViewHolder(View itemView) {
            super(itemView); //getting the super's View

            articleThumb = itemView.findViewById(R.id.articleThumb); //initializing
            articleTitle = itemView.findViewById(R.id.articleTitle); //initializing

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                Toast.makeText(getContext(), getString(R.string.soccer_list_u_clicked_on) + " " + articleTitlesList.get(position), Toast.LENGTH_SHORT).show();

                SoccerGames parentActivity = (SoccerGames)getContext();
                parentActivity.userClickedMessage(articleTitlesList.get(position), position);
            });
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    //adapts a collection of objects for display
    //tells the list how to build the items
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
        ArrayList<String> array; //declaring an array to use for an ArticleAdapter object
        Context context; //declaring a context variable to get the context

        public ArticleAdapter(ArrayList array, Context context) { //constructor for this class
            this.array = array; //setting the articleTitlesList as the array for this instance of ArticleAdapter
            this.context = context; //setting 'this' as the context for this instance of ArticleAdapter
        }


        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//          LayoutInflater inflater = getLayoutInflater();
            View loadedRow = LayoutInflater.from(context).inflate(R.layout.article_row_layout, parent, false); //taking the loaded row and inflating it (takes layout xml files and converts into a View object)

            return new ArticleViewHolder(loadedRow); //returning the loaded row, now an ArticleViewHolder object
        }

        @Override
        public void onBindViewHolder(ArticleViewHolder viewHolder, int position) {
            viewHolder.articleTitle.setText(array.get(position).toString()); //how do i set this from that link of just xml code?
//          viewHolder.articleThumb ----> this is an image (?)
        }

        @Override
        public int getItemCount() {
            return array.size(); //returns the number of rows in the RecyclerView
        }
    }

    //this class is similar to the ChatMessage class in the RecyclerView lab (week 5) - used to store and get the information for each article to be displayed in the RecyclerView
    //not a ViewHolder or an Adapter
//    private class ArticleInfo {
//        String title;
//        String datePublished;
//
//        public ArticleInfo (String title, String datePublished) {
//            this.title = title;
//            this.datePublished = datePublished;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDatePublished() {
//            return datePublished;
//        }
//    }
}
