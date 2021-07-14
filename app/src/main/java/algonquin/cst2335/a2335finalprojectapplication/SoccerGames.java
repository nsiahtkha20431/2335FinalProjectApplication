package algonquin.cst2335.a2335finalprojectapplication;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class SoccerGames extends AppCompatActivity {
    ArticleAdapter adapter = new ArticleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout); //layout to list the article titles
        RecyclerView articlesRecyclerView = findViewById(R.id.myrecycler);
        articlesRecyclerView.setAdapter(new ArticleAdapter());
    }

    private class ArticleAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    //this class is similar to the ChatMessage class in the RecyclerView lab (week 5) - used to store and get the information for each article to be displayed in the RecyclerView
    private class ArticleInfo {
        String title;
        String datePublished;

        public ArticleInfo(String title, String datePublished) {
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
