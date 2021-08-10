package algonquin.cst2335.a2335finalprojectapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.a2335finalprojectapplication.ArticleFavoritesDetailsFragment;
import algonquin.cst2335.a2335finalprojectapplication.R;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.db;

public class ArticleFavoritesDetailsFragment extends Fragment {

    /** The parent fragment of this class which is the list of favorites */
    ArticleFavoritesListFragment parentFragment;

    /** An instance of the Article object article to manipulate in this class */
    ArticleListFragment.Article currentArticle;

    /** The Article-type article that was selected */
    ArticleListFragment.Article chosenFavArticle;

    /** The position of the chosen article */
    int chosenPosition;

    /** The url to be displayed in the Details fragment */;
    String linkString = null;

    /**
     * The second constructor for this class that takes the article when it is an Article object and the position
     * @param article The Article object
     * @param position The position
     */
    public ArticleFavoritesDetailsFragment(ArticleListFragment.Article article, int position) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articleFavDetailsLayout = inflater.inflate(R.layout.soccer_fav_details_layout, container, false);

        TextView titleView = articleFavDetailsLayout.findViewById(R.id.favTitleView);
        TextView dateView = articleFavDetailsLayout.findViewById(R.id.dateView);
        TextView urlView = articleFavDetailsLayout.findViewById(R.id.urlView);
        TextView descriptionView = articleFavDetailsLayout.findViewById(R.id.descriptionView);
        Button deleteFromFavButton = articleFavDetailsLayout.findViewById(R.id.deleteFromFavButton);
        Button backButton = articleFavDetailsLayout.findViewById(R.id.backButton);
        Button browserButton = articleFavDetailsLayout.findViewById(R.id.brosweButton);

        Cursor results = db.rawQuery("Select * from " + FinalOpenHelper.SOCCER_TABLE_NAME + ";", null);

        int _idCol = results.getColumnIndex("_id");
        int titleCol = results.getColumnIndex(FinalOpenHelper.TITLE_COLUMN);
        int dateCol = results.getColumnIndex(FinalOpenHelper.DATE_COLUMN);
        int urlCol = results.getColumnIndex(FinalOpenHelper.URL_COLUMN);
        int descCol = results.getColumnIndex(FinalOpenHelper.DESC_COLUMN);

        parentFragment = (ArticleFavoritesListFragment) getParentFragment();

//        favArticlesList.clear();

        while (results.moveToNext()) {
            String title = chosenFavArticle.getTitle();
            String aTitle = results.getString(titleCol);

            if (title.equals(aTitle)) {
                long id = results.getInt(_idCol);
                String aDate = results.getString(dateCol);
                String aURL = results.getString(urlCol);
                String aDesc = results.getString(descCol);
                linkString = aURL;

                titleView.setText(getString(R.string.article_is) + " " + aTitle);
                dateView.setText(getString(R.string.date_is) + " " + aDate);
                urlView.setText(getString(R.string.url_is) + " " + aURL);
                descriptionView.setText(getString(R.string.description_is) + " " + aDesc);

                currentArticle = new ArticleListFragment.Article(aTitle, aDate, aURL, aDesc, id);
            }
        }



        backButton.setOnClickListener(clicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        deleteFromFavButton.setOnClickListener(clicked -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.delete_title)
                    .setNegativeButton(R.string.delete_no_option, (dialog, cl) -> { })
                    .setPositiveButton(R.string.delete_yes_option, (dialog, cl) -> {

                        ArticleListFragment.Article removedArticle = this.currentArticle;

                        this.parentFragment.favArticlesList.remove(chosenPosition);
                        this.parentFragment.notifyItemDeleted(chosenPosition);

                        db.delete(FinalOpenHelper.SOCCER_TABLE_NAME, "_id=?", new String[] {Long.toString(removedArticle.getID())});

                        Snackbar.make(titleView, getString(R.string.u_deleted), Snackbar.LENGTH_LONG)
                                .setAction(R.string.soccer_details_snackbar_undo, clk -> {
                                    this.parentFragment.favArticlesList.add(chosenPosition, removedArticle);
                                    this.parentFragment.notifyItemInserted(chosenPosition);

                                    ContentValues newRow = new ContentValues();
                                    newRow.put("_id", removedArticle.getID());
                                    newRow.put(FinalOpenHelper.TITLE_COLUMN, removedArticle.getTitle());
                                    newRow.put(FinalOpenHelper.DATE_COLUMN, removedArticle.getDatePublished());
                                    newRow.put(FinalOpenHelper.URL_COLUMN, removedArticle.getUrl());
                                    newRow.put(FinalOpenHelper.DESC_COLUMN, removedArticle.getDesc());
                                    db.insert(FinalOpenHelper.SOCCER_TABLE_NAME, FinalOpenHelper.TITLE_COLUMN, newRow);
                                })
                                .show();
                    })
                    .create().show();
        });

        browserButton.setOnClickListener( (clicked) -> {
            Uri openInBrowser = Uri.parse(linkString);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, openInBrowser);
            startActivity(browserIntent);
        });

        return articleFavDetailsLayout;
    }

}
