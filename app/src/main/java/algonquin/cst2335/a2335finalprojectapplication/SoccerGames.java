package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SoccerGames extends AppCompatActivity {
    ArticleAdapter adapter;
    ArrayList<String> articleTitlesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_recycler_layout); //layout to list the article titles
        RecyclerView articlesRecyclerView = findViewById(R.id.myrecycler);

        articleTitlesList.add("Article Title 1");
        adapter = new ArticleAdapter(articleTitlesList, this);

        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        articlesRecyclerView.setAdapter(adapter);
    }

    //ViewHolder for this RecyclerView
    private class ArticleViewHolder extends RecyclerView.ViewHolder{

        public ImageView articleThumb;
        public TextView articleTitle;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            articleThumb = itemView.findViewById(R.id.articleThumb);
            articleTitle = itemView.findViewById(R.id.articleTitle);
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
        ArrayList<String> array;
        Context context;

        public ArticleAdapter(ArrayList array, Context context) {
            this.array = array;
            this.context = context;
        }

        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//          LayoutInflater inflater = getLayoutInflater();
            View loadedRow = LayoutInflater.from(context).inflate(R.layout.article_row_layout, parent, false);

            return new ArticleViewHolder(loadedRow);
        }

        @Override
        public void onBindViewHolder(ArticleViewHolder viewHolder, int position) {
            viewHolder.articleTitle.setText(array.get(position).toString()); //how do i set this from that link of just xml code?
//          viewHolder.articleThumb ----> this is an image (?)
        }

        @Override
        public int getItemCount() {
            return array.size();
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
