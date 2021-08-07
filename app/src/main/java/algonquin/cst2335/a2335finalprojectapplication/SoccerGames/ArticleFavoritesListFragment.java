package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

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

public class ArticleFavoritesListFragment extends Fragment {
    private SoccerGames parent;
    FavArticleAdapter adapter;
    ArrayList<ArticleListFragment.Article> favArticlesList = new ArrayList<>();


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

    //ViewHolder for this RecyclerView - this represents one element of the list and how it will look like
    private class FavArticleViewHolder extends RecyclerView.ViewHolder{

        public ImageView articleThumb; //declaring a variable to hold the thumbnail image for each article in the list
        public TextView articleTitle; //declaring a variable to hold the article title for each article in the list
        int position;

        public FavArticleViewHolder(View itemView) {
            super(itemView); //getting the super's View

            itemView.setOnClickListener(click -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete the article " + articleTitle.getText())
                        .setTitle("Delete article from Favorites")
                        .setNegativeButton("No", (dialog, cl) -> { })
                        .setPositiveButton("Yes", (dialog, cl) -> {

                            position = getAbsoluteAdapterPosition();

                            ArticleListFragment.Article removedArticle = favArticlesList.get(position);

                            favArticlesList.remove(position);
                            adapter.notifyItemRemoved(position);
                            String id = Long.toString(removedArticle.getID());
                            Long ID = removedArticle.getID();
                            db.delete(FinalOpenHelper.SOCCER_TABLE_NAME, "_id=?", new String[] {Long.toString(removedArticle.getID())});

                            Snackbar.make(articleTitle, "You deleted article #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        favArticlesList.add(position, removedArticle);
                                        adapter.notifyItemInserted(position);

                                        ContentValues newRow = new ContentValues();
                                        newRow.put("_id", removedArticle.getID());
                                        newRow.put(FinalOpenHelper.TITLE_COLUMN, removedArticle.getTitle());
                                        newRow.put(FinalOpenHelper.DATE_COLUMN, removedArticle.getDatePublished());
                                        newRow.put(FinalOpenHelper.URL_COLUMN, removedArticle.getUrl());
                                        newRow.put(FinalOpenHelper.DESC_COLUMN, removedArticle.getDesc());
                                        db.insert(FinalOpenHelper.SOCCER_TABLE_NAME, FinalOpenHelper.TITLE_COLUMN, newRow);


//                                        db.execSQL("Insert into " + FinalOpenHelper.SOCCER_TABLE_NAME + " values(" + removedArticle.getID() +
//                                                "," + removedArticle.getTitle() +
//                                                "," + removedArticle.getDatePublished() +
//                                                "," + removedArticle.getUrl() +
////                                                "," + removedArticle.getDesc() + ");");
//                                        db.execSQL("INSERT INTO "  + FinalOpenHelper.SOCCER_TABLE_NAME + " VALUES(" +
//                                                        removedArticle.getID() + "," +
//                                                        "'" + removedArticle.getTitle() + "'," +
//                                                        "'" + removedArticle.getDatePublished() + "'," +
//                                                        "'" + removedArticle.getUrl() + "'," +
//                                                        "'" +removedArticle.getDesc() + "');");
                                    })
                                    .show();
                        })
                        .create().show();
            });

            articleThumb = itemView.findViewById(R.id.articleThumb); //initializing
            articleTitle = itemView.findViewById(R.id.articleTitle); //initializing
        }

        public void setPosition(int p) {
            this.position = p;
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    //adapts a collection of objects for display
    //tells the list how to build the items
    private class FavArticleAdapter extends RecyclerView.Adapter<FavArticleViewHolder> {
        ArrayList<ArticleListFragment.Article> array; //declaring an array to use for an ArticleAdapter object
        Context context; //declaring a context variable to get the context

        public FavArticleAdapter(ArrayList array, Context context) { //constructor for this class
            this.array = array; //setting the favArticlesList as the array for this instance of ArticleAdapter
            this.context = context; //setting 'this' as the context for this instance of ArticleAdapter
        }


        @Override
        public FavArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.article_row_layout, parent, false); //taking the loaded row and inflating it (takes layout xml files and converts into a View object)

            return new FavArticleViewHolder(loadedRow); //returning the loaded row
        }

        @Override
        public void onBindViewHolder(FavArticleViewHolder holder, int position) {
            holder.articleTitle.setText(array.get(position).getTitle());
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return array.size(); //returns the number of rows in the RecyclerView
        }
    }

}
