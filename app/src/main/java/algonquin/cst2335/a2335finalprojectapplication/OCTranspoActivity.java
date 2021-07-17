package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class OCTranspoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);
        StopListFragment listFrag = new StopListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.detailsFrag, listFrag).commit();
    }

    public void stopSelected(int stopNo) {
        StopDetailsFragment newFrag = new StopDetailsFragment(stopNo);
        getSupportFragmentManager().beginTransaction().add(R.id.detailsFrag, newFrag).commit();
    }
}