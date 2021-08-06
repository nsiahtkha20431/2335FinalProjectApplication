package algonquin.cst2335.a2335finalprojectapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;


public class ArticleDetailsFragment extends Fragment {
    String chosenArticle;
    int chosenPosition;
    Button delete;

    public ArticleDetailsFragment(String article, int position) {
        chosenArticle = article;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articlesDetailsLayout = inflater.inflate(R.layout.soccer_details_layout, container, false);

        delete = articlesDetailsLayout.findViewById(R.id.deleteFromFavButton);

        TextView titleView = articlesDetailsLayout.findViewById(R.id.titleView);
        TextView urlView = articlesDetailsLayout.findViewById(R.id.urlView);
        TextView descriptionView = articlesDetailsLayout.findViewById(R.id.descriptionView);
        Button addToFavButton = articlesDetailsLayout.findViewById(R.id.addToFavButton);
        Button deleteFromFavButton = articlesDetailsLayout.findViewById(R.id.deleteFromFavButton);
        Button backButton = articlesDetailsLayout.findViewById(R.id.backButton);

        titleView.setText("Article is: " + chosenArticle);
        urlView.setText("URL is: URL");
        descriptionView.setText("Description: No description yet");

        backButton.setOnClickListener(clicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        addToFavButton.setOnClickListener(clicked -> {
            //implement this later
        });

        deleteFromFavButton.setOnClickListener(clicked -> {
//            SoccerGames parentActivity = (SoccerGames)getContext();
//            parentActivity.notifyArticleDeleted(chosenArticle, chosenPosition);

            Snackbar.make(delete, "You deleted " + chosenArticle, Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", click -> { }).show();

        });

        return articlesDetailsLayout;
    }
}
