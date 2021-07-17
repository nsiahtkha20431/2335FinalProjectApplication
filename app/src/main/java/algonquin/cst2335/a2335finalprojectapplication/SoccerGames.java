package algonquin.cst2335.a2335finalprojectapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SoccerGames extends AppCompatActivity {
    ArticleAdapter adapter = new ArticleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list_layout); //layout to list the article titles
        RecyclerView articlesRecyclerView = findViewById(R.id.myrecycler);
        articlesRecyclerView.setAdapter(new ArticleAdapter());
    }

    private class ArticleViewHolder extends RecyclerView.ViewHolder{

        ImageView articleThumb;
        TextView articleTitle;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            articleThumb = itemView.findViewById(R.id.articleThumb);
            articleTitle = itemView.findViewById(R.id.articleTitle);
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.row_layout, parent, false);

            return null;
        }

        @Override
        public void onBindViewHolder(ArticleViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    //this class is similar to the ChatMessage class in the RecyclerView lab (week 5) - used to store and get the information for each article to be displayed in the RecyclerView
    //not a ViewHolder or and Adapter
    private class ArticleInfo {
        String title;
        String datePublished;

        public ArticleInfo (String title, String datePublished) {
            this.title = title;
            this.datePublished = datePublished;
        }

        public String getTitle() {
            return title;
        }

        public String getDatePublished() {
            return datePublished;
        }
    }
}
