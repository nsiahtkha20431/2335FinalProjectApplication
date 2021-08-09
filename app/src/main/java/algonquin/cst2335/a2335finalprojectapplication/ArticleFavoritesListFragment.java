package algonquin.cst2335.a2335finalprojectapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

import static algonquin.cst2335.a2335finalprojectapplication.MainActivity.db;

/**
 * This class is to display the articles that were added to the Favorites by the user. It is the Favorites RecyclerView list.
 * @author Nishat Khan
 * @version 1.0
 */
public class ArticleFavoritesListFragment extends Fragment {

    /** The parent object of this class */
    private SoccerGames parent;

    /** The Adapter for the RecyclerView in this class */
    FavArticleAdapter adapter;

    /** The ArrayList to hold the article titles in an array to display */
    ArrayList<ArticleListFragment.Article> favArticlesList = new ArrayList<>();

    /**
     * The onCreateView for this class. It inflates the View and creates the RecyclerView object, creates the Cursor object to get the database results, sets the columns in the FavoriteAtricles table, and adds the articles to the array
     * @param inflater The layout that we want to inflate in this fragment
     * @param container The parent view that this fragment is attached to
     * @param savedInstanceState The previous saved state
     * @return Returns the Fragment view with the RecyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (SoccerGames) getContext(); //getting a reference to the main activity
        View articlesFavListLayout = inflater.inflate(R.layout.fav_article_recycler_layout, container, false); //creating a View of the RecyclerView
        RecyclerView articlesRecyclerView = articlesFavListLayout.findViewById(R.id.myFavRecycler); //setting the RecyclerView to the Recycler part of that layout

        Cursor results = db.rawQuery("Select * from " + FinalOpenHelper.SOCCER_TABLE_NAME + ";", null);

        int _idCol = results.getColumnIndex("_id");
        int titleCol = results.getColumnIndex(FinalOpenHelper.TITLE_COLUMN);
        int dateCol = results.getColumnIndex(FinalOpenHelper.DATE_COLUMN);
        int urlCol = results.getColumnIndex(FinalOpenHelper.URL_COLUMN);
        int descCol = results.getColumnIndex(FinalOpenHelper.DESC_COLUMN);

        favArticlesList.clear();

        while (results.moveToNext()) {
            long id = results.getInt(_idCol);
            String aTitle = results.getString(titleCol);
            String aDate = results.getString(dateCol);
            String aURL = results.getString(urlCol);
            String aDesc = results.getString(descCol);

            favArticlesList.add(new ArticleListFragment.Article(aTitle, aDate, aURL, aDesc, id));
        }

        adapter = new FavArticleAdapter(favArticlesList, getContext()); //creating Adapter object and passing the ArrayList and Context to it
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        articlesRecyclerView.setAdapter(adapter); //setting the adapter of this RecyclerView

        return articlesFavListLayout; //returning the RecyclerView layout
    }

    public void notifyItemDeleted(int position) {
        this.adapter.notifyItemRemoved(position);
    }

    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    public void userClickedFavTitle(ArticleListFragment.Article article, int position) {
        ArticleFavoritesDetailsFragment adFragment = new ArticleFavoritesDetailsFragment(article, position);
        getChildFragmentManager().beginTransaction().add(R.id.fragmentRoom, adFragment).commit();
    }

    /**
     * The ViewHolder for this RecyclerView. This represents one element of the list and how it will look
     */
    private class FavArticleViewHolder extends RecyclerView.ViewHolder{

        /** This holds the ImageView of the thumbnail image for the article */
        public ImageView articleThumb; //declaring a variable to hold the thumbnail image for each article in the list

        /** This holds the TextView object for the article title */
        public TextView articleTitle; //declaring a variable to hold the article title for each article in the list

        /** This holds the position of the article in the list */
        int position;

        /**
         * This is the constructor for this class that creates the row views from the information stored in the database. Includes an AlertDialog to ask the user if they want to delete the article from their Favorites when clicked and also a Snackbar to notify them it was deleted with an option to undo.
         * @param itemView The View of the row to display
         */
        public FavArticleViewHolder(View itemView) {
            super(itemView); //getting the super's View

            itemView.setOnClickListener(click -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle(R.string.delete_title)
//                        .setNegativeButton(R.string.delete_no_option, (dialog, cl) -> { })
//                        .setPositiveButton(R.string.delete_yes_option, (dialog, cl) -> {
//
//                            position = getAbsoluteAdapterPosition();
//
//                            ArticleListFragment.Article removedArticle = favArticlesList.get(position);
//
//                            favArticlesList.remove(position);
//                            adapter.notifyItemRemoved(position);
//                            db.delete(FinalOpenHelper.SOCCER_TABLE_NAME, "_id=?", new String[] {Long.toString(removedArticle.getID())});
//
//                            Snackbar.make(articleTitle, R.string.u_deleted + position, Snackbar.LENGTH_LONG)
//                                    .setAction(R.string.soccer_details_snackbar_undo, clk -> {
//                                        favArticlesList.add(position, removedArticle);
//                                        adapter.notifyItemInserted(position);
//
//                                        ContentValues newRow = new ContentValues();
//                                        newRow.put("_id", removedArticle.getID());
//                                        newRow.put(FinalOpenHelper.TITLE_COLUMN, removedArticle.getTitle());
//                                        newRow.put(FinalOpenHelper.DATE_COLUMN, removedArticle.getDatePublished());
//                                        newRow.put(FinalOpenHelper.URL_COLUMN, removedArticle.getUrl());
//                                        newRow.put(FinalOpenHelper.DESC_COLUMN, removedArticle.getDesc());
//                                        db.insert(FinalOpenHelper.SOCCER_TABLE_NAME, FinalOpenHelper.TITLE_COLUMN, newRow);
//                                    })
//                                    .show();
//                        })
//                        .create().show();

                int position = getAbsoluteAdapterPosition();

                userClickedFavTitle(favArticlesList.get(position), position);
            });

            articleThumb = itemView.findViewById(R.id.articleThumb); //initializing
            articleTitle = itemView.findViewById(R.id.articleTitle); //initializing
        }

        /**
         * This function is used to set the position of the article in the onBindViewHolder
         * @param p the int position of the article in the list
         */
        public void setPosition(int p) {
            this.position = p;
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    //adapts a collection of objects for display
    //tells the list how to build the items

    /**
     * The Adapter class for this RecyclerView. Adapts a collection of objects to display and tells the list how to build the items
     */
    private class FavArticleAdapter extends RecyclerView.Adapter<FavArticleViewHolder> {

        /** The ArrayList to use for a FavArticleAdapter object */
        ArrayList<ArticleListFragment.Article> array; //declaring an array to use for an ArticleAdapter object

        /** The context variable to use to get the context */
        Context context; //declaring a context variable to get the context

        /**
         * The constructor for this class. Creates an adapter object.
         * @param array The array of the article titles to display
         * @param context The context of this adapter object
         */
        public FavArticleAdapter(ArrayList array, Context context) { //constructor for this class
            this.array = array; //setting the favArticlesList as the array for this instance of ArticleAdapter
            this.context = context; //setting 'this' as the context for this instance of ArticleAdapter
        }

        /**
         * The onCreateViewHolder for this Adapter
         * @param parent The parent View
         * @param viewType The int view type
         * @return Returns The ViewHolder of this loaded row
         */
        @Override
        public FavArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.article_row_layout, parent, false); //taking the loaded row and inflating it (takes layout xml files and converts into a View object)

            return new FavArticleViewHolder(loadedRow); //returning the loaded row
        }

        /**
         * The function to bind the view
         * @param holder The ViewHolder
         * @param position The position for this row
         */
        @Override
        public void onBindViewHolder(FavArticleViewHolder holder, int position) {
            holder.articleTitle.setText(array.get(position).getTitle());
            holder.setPosition(position);
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

}
