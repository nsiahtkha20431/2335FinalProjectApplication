package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ArticleFavoritesListFragment extends Fragment {
    private SoccerGames parent;
    FavArticleAdapter adapter;
    ArrayList<String> favArticlesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (SoccerGames) getContext(); //getting a reference to the main activity
        View articlesFavListLayout = inflater.inflate(R.layout.fav_article_recycler_layout, container, false); //creating a View of the RecyclerView
        RecyclerView articlesRecyclerView = articlesFavListLayout.findViewById(R.id.myFavRecycler); //setting the RecyclerView to the Recycler part of that layout

        adapter = new FavArticleAdapter(favArticlesList, getContext()); //creating Adapter object and passing the ArrayList and Context to it
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        articlesRecyclerView.setAdapter(adapter); //setting the adapter of this RecyclerView

        return articlesFavListLayout; //returning the RecyclerView layout
    }

    //ViewHolder for this RecyclerView - this represents one element of the list and how it will look like
    private class FavArticleViewHolder extends RecyclerView.ViewHolder{

        public ImageView articleThumb; //declaring a variable to hold the thumbnail image for each article in the list
        public TextView articleTitle; //declaring a variable to hold the article title for each article in the list

        public FavArticleViewHolder(View itemView) {
            super(itemView); //getting the super's View

            articleThumb = itemView.findViewById(R.id.articleThumb); //initializing
            articleTitle = itemView.findViewById(R.id.articleTitle); //initializing
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    //adapts a collection of objects for display
    //tells the list how to build the items
    private class FavArticleAdapter extends RecyclerView.Adapter<FavArticleViewHolder> {
        ArrayList<String> array; //declaring an array to use for an ArticleAdapter object
        Context context; //declaring a context variable to get the context

        public FavArticleAdapter(ArrayList array, Context context) { //constructor for this class
            this.array = array; //setting the favArticlesList as the array for this instance of ArticleAdapter
            this.context = context; //setting 'this' as the context for this instance of ArticleAdapter
        }


        @Override
        public FavArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//          LayoutInflater inflater = getLayoutInflater();
            View loadedRow = LayoutInflater.from(context).inflate(R.layout.article_row_layout, parent, false); //taking the loaded row and inflating it (takes layout xml files and converts into a View object)

            return new FavArticleViewHolder(loadedRow); //returning the loaded row, now an ArticleViewHolder object
        }

        @Override
        public void onBindViewHolder(FavArticleViewHolder viewHolder, int position) {
            viewHolder.articleTitle.setText(array.get(position).toString()); //how do i set this from that link of just xml code?
//          viewHolder.articleThumb ----> this is an image (?)
        }

        @Override
        public int getItemCount() {
            return array.size(); //returns the number of rows in the RecyclerView
        }
    }

}
