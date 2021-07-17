package algonquin.cst2335.a2335finalprojectapplication.OCTranspo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import algonquin.cst2335.a2335finalprojectapplication.R;

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