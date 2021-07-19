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
    ArticleAdapter adapter; //declaring an ArticleAdapter object, but no initializing yet
    ArrayList<String> articleTitlesList = new ArrayList<>(); //making an array to hold the titles of each article

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_recycler_layout); //setting the view as the RecyclerView layout
        RecyclerView articlesRecyclerView = findViewById(R.id.myrecycler); //creating an instance of RecyclerView and attaching it to the XML tag

        articleTitlesList.add("Article Title 1"); //adding temporary article titles for the array -- will get these properly from a DB later
        articleTitlesList.add("Article Title 2");
        articleTitlesList.add("Article Title 3");
        articleTitlesList.add("Article Title 4");
        articleTitlesList.add("Article Title 5");
        adapter = new ArticleAdapter(articleTitlesList, this); //initializing the ArticleAdapter object and passing it the values of the array and the context (like that it comes from here)

        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //set the layout of the contents, i.e. list of repeating views in the recycler view
        articlesRecyclerView.setAdapter(adapter); //this line tells the adapter object that the array we have created is the collection of objects to display

        ratingAlertDialog(); //calling this function to get the AlertDialog running as soon as the app is opened

//        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        prefs.getString("VariableName", "");
    }

    public void ratingAlertDialog() { //function for the rating AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SoccerGames.this); //making an AlertDialog object called builder

        LinearLayout linearLayout = new LinearLayout(this); //creating a LinearLayout to be implemented into the AlertDialog
        final RatingBar rating = new RatingBar(this); //creating a RatingBar

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( //defining the parameters for the LinearLayout
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rating.setLayoutParams(lp); //setting the parameters for the LinearLayout
        rating.setNumStars(5); //setting the number of stars for the RatingBar
        rating.setStepSize(1); //setting the number of stars you can increment by

        linearLayout.addView(rating); //add RatingBar to linearLayout

        builder.setTitle("Please rate our app!"); //setting title of the AlertDialog
        builder.setView(linearLayout); //adding the LinearLayout view to the AlertDialog

        float value = rating.getRating(); //saving the value of the RatingBar to the 'value' variable
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE); //creating a SharedPreferences object
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("numStars", value); //put value
        editor.commit();
        float ratingInt = prefs.getFloat("numStars", 0);
        rating.setRating(ratingInt);//save
        builder.create().show();
    }

    //ViewHolder for this RecyclerView - this represents one element of the list and how it will look like
    private class ArticleViewHolder extends RecyclerView.ViewHolder{

        public ImageView articleThumb; //declaring a variable to hold the thumbnail image for each article in the list
        public TextView articleTitle; //declaring a variable to hold the article title for each article in the list

        public ArticleViewHolder(View itemView) {
            super(itemView); //getting the super's View

            articleThumb = itemView.findViewById(R.id.articleThumb); //initializing
            articleTitle = itemView.findViewById(R.id.articleTitle); //initializing

            itemView.setOnClickListener(click -> {
                Toast.makeText(getApplicationContext(), "You clicked on " + articleTitlesList.get(0), Toast.LENGTH_SHORT).show(); //how to make this show the actual title no matter what they click? right now it will only ever show the first title
            });
        }
    }

    //Adapter for this RecyclerView - Adapter is like the middle man for what the user sees and how the application gets and displays data
    //adapts a collection of objects for display
    //tells the list how to build the items
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
        ArrayList<String> array; //declaring an array to use for an ArticleAdapter object
        Context context; //declaring a context variable to get the context

        public ArticleAdapter(ArrayList array, Context context) { //constructor for this class
            this.array = array; //setting the articleTitlesList as the array for this instance of ArticleAdapter
            this.context = context; //setting 'this' as the context for this instance of ArticleAdapter
        }

        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//          LayoutInflater inflater = getLayoutInflater();
            View loadedRow = LayoutInflater.from(context).inflate(R.layout.article_row_layout, parent, false); //taking the loaded row and inflating it (takes layout xml files and converts into a View object)

            return new ArticleViewHolder(loadedRow); //returning the loaded row, now an ArticleViewHolder object
        }

        @Override
        public void onBindViewHolder(ArticleViewHolder viewHolder, int position) {
            viewHolder.articleTitle.setText(array.get(position).toString()); //how do i set this from that link of just xml code?
//          viewHolder.articleThumb ----> this is an image (?)
        }

        @Override
        public int getItemCount() {
            return array.size();
        } //returns the number of rows in our RecyclerView
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
