package algonquin.cst2335.a2335finalprojectapplication.SoccerGames;

import android.content.ClipData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.R;

public class SoccerGames extends AppCompatActivity {
    ArticleListFragment articleFragment = new ArticleListFragment();
    ArticleFavoritesListFragment favFrag = new ArticleFavoritesListFragment();
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout); //setting the view

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, articleFragment).commit();

        SoccerGames.context = getApplicationContext();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        FinalOpenHelper opener = new FinalOpenHelper(this);
        SQLiteDatabase db = opener.getWritableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_icon:
                AlertDialog.Builder builder = new AlertDialog.Builder(SoccerGames.this);
                TextView helpDialogText = new TextView(this);
                helpDialogText.setText(getString(R.string.help_dialog));
                builder.setTitle(getString(R.string.help_dialog_title))
                        .setView(helpDialogText)
                        .setPositiveButton(getString(R.string.help_dialog_got_it), (dialog, cl) -> { })
                        .create().show();
                break;

            case R.id.fav_icon:

                if(item.isChecked()) {
                    item.setChecked(false);
                    getSupportFragmentManager().beginTransaction().remove(favFrag).commit();
                } else {
                    item.setChecked(true);
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, favFrag).commit();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Context getAppContext() {
        return SoccerGames.context;
    }

    public void userClickedTitle(String article, int position) {
        ArticleDetailsFragment adFragment = new ArticleDetailsFragment(article, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, adFragment).commit();
    }

    public void userClickedFavTitle(ArticleListFragment.Article article, int position) {
        ArticleDetailsFragment adFragment = new ArticleDetailsFragment(article, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, adFragment).commit();
    }

    //not currently being called in the code
//    public void notifyArticleDeleted(String chosenArticle, int chosenPosition) {
//        articleFragment.notifyArticleDeleted(chosenArticle, chosenPosition);
//    }


}
