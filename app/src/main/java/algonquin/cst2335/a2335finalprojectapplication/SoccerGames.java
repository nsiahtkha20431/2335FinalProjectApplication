package algonquin.cst2335.a2335finalprojectapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SoccerGames extends AppCompatActivity {
    ArticleListFragment articleFragment = new ArticleListFragment();
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout); //setting the view

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, new ArticleListFragment()).commit();

        SoccerGames.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SoccerGames.context;
    }

    public void userClickedMessage(String article, int position) {
        ArticleDetailsFragment adFragment = new ArticleDetailsFragment(article, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, adFragment).commit();
    }

    //not currently being called in the code
    public void notifyArticleDeleted(String chosenArticle, int chosenPosition) {
        articleFragment.notifyArticleDeleted(chosenArticle, chosenPosition);
    }
}
