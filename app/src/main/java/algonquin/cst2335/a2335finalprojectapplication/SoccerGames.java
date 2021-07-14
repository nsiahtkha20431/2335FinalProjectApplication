package algonquin.cst2335.a2335finalprojectapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SoccerGames extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout); //layout to list the article titles

        RecyclerView articlesRecyclerView = findViewById(R.id.myrecycler);
    }
}
