package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class SoccerGames extends AppCompatActivity {
    ArticleListFragment articleFragment = new ArticleListFragment();
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout); //setting the view

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, new ArticleListFragment()).commit();

        SoccerGames.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SoccerGames.context;
    }

    public void userClickedMessage(String article, int position) {
        ArticleDetailsFragment adFragment = new ArticleDetailsFragment(article, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, adFragment).commit();
    }

    //not currently being called in the code
    public void notifyArticleDeleted(String chosenArticle, int chosenPosition) {
        articleFragment.notifyArticleDeleted(chosenArticle, chosenPosition);
    }
}
