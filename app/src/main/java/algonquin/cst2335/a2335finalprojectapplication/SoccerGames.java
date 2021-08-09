package algonquin.cst2335.a2335finalprojectapplication;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;

import algonquin.cst2335.a2335finalprojectapplication.ChargingStationsMain;
import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.MovieInfoActivity;
import algonquin.cst2335.a2335finalprojectapplication.OCTranspoActivity;
import algonquin.cst2335.a2335finalprojectapplication.R;

/**
 * This is the main activity class for the Soccer Articles API
 * @author Nishat Khan
 * @version 1.0
 */
public class SoccerGames extends AppCompatActivity {

    /**  */
    ArticleListFragment articleFragment = new ArticleListFragment();

    /**  */
    ArticleFavoritesListFragment favFrag = new ArticleFavoritesListFragment();

    /**  */
    private static Context context;

    /**
     * onCreate function for this class to open up the main layout and establish a connection with the database
     * @param savedInstanceState The previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout); //setting the view

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, articleFragment).commit();

        SoccerGames.context = getApplicationContext();

        FinalOpenHelper opener = new FinalOpenHelper(this);
        SQLiteDatabase db = opener.getWritableDatabase();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.soccerdrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.soccernavview);
        navigationView.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item); //call the function for the other toolbar
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    /**
     * This function creates the icon options in the Toolbar
     * @param menu The Menu object that the function needs to inflate
     * @return Returns the menu item with inflated icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function defines what should happen each time one of the icons in the Toolbar is selected
     * @param item The MenuItem object that was clicked
     * @return Returns true depending on the MenuItem that was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Button charging = findViewById(R.id.open_charging_activity);
        Button octranspo = findViewById(R.id.open_OCT_activity);
        Button movie = findViewById(R.id.open_movie_activity);

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
            case R.id.open_charging_activity:
                charging.setOnClickListener( (click) -> {
                    Intent chargingActivity = new Intent(SoccerGames.this, ChargingStationsMain.class);
                    startActivity(chargingActivity);
                });
                break;
            case R.id.open_OCT_activity:
                octranspo.setOnClickListener( (click) -> {
                    Intent ocTranspoActivity = new Intent(SoccerGames.this, OCTranspoActivity.class);
                    startActivity(ocTranspoActivity);
                });
                break;
            case R.id.open_movie_activity:
                movie.setOnClickListener( (click) -> {
                    Intent movieActivity = new Intent(SoccerGames.this, MovieInfoActivity.class);
                    startActivity(movieActivity);
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * This function is simply used to get the context of this app for the SharedPreferences
     * @return Returns the context of this app
     */
    public static Context getAppContext() {
        return SoccerGames.context;
    }

    /**
     * This function opens up the details page (ArticleDetailsFragment) if one of the articles in the main RecyclerView (ArticleListFragment) was clicked
     * @param article The String name of the article that was clicked
     * @param position The position of the article in the RecyclerView
     */
    public void userClickedTitle(String article, int position) {
        ArticleDetailsFragment adFragment = new ArticleDetailsFragment(article, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, adFragment).commit();
    }

    /**
     * This function opens up the details page (ArticleDetailsFragment) if one of the articles in the main RecyclerView (ArticleListFragment) was clicked
     * @param article The String name of the article that was clicked
     * @param position The position of the article in the RecyclerView
     */
    public void userClickedFavTitle(ArticleListFragment.Article article, int position) {
        ArticleFavoritesDetailsFragment adFragment = new ArticleFavoritesDetailsFragment(article, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, adFragment).commit();
    }
}
