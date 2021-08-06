package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;


public class OCTranspoActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);
        prefs = getSharedPreferences("OCT_Data", Context.MODE_PRIVATE);
        StopListFragment listFrag = new StopListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.listFrag, listFrag, "list").commit();
    }

    public void addStop(String stop, String description) {
        ContentValues newStop = new ContentValues();
        newStop.put(FinalOpenHelper.OCT_COL_NO, stop);
        newStop.put(FinalOpenHelper.OCT_COL_DESC, description);
        MainActivity.db.insert(FinalOpenHelper.OCTRANSPO_TABLE_NAME, FinalOpenHelper.OCT_COL_NO, newStop);
        Snackbar.make(findViewById(R.id.listFrag), "Bus Stop #" + stop + " has been added.", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", click -> {
                    deleteStop(Integer.parseInt(stop), description);
                }).show();
    }

    public void stopSelected(int stopNo) {
        StopDetailsFragment newFrag = new StopDetailsFragment(stopNo);
        getSupportFragmentManager().beginTransaction().replace(R.id.listFrag, newFrag, "detail").addToBackStack(null).commit();
    }

    public void deleteStop(int stopNo, String description) {
        String where = FinalOpenHelper.OCT_COL_NO + " = " + stopNo;
        MainActivity.db.delete(FinalOpenHelper.OCTRANSPO_TABLE_NAME, where, null);
        prefs.edit().putString("deleted_stop", String.valueOf(stopNo)).apply();
        prefs.edit().putString("deleted_stop_desc", description).apply();
        Snackbar.make(findViewById(R.id.listFrag), "Bus Stop #" + stopNo + " has been deleted.", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", click -> {
                    undoDelete();
                }).show();
        getSupportFragmentManager().findFragmentByTag("list").onResume();
    }

    public void undoDelete() {
        String stop = prefs.getString("deleted_stop", "");
        String stopDesc = prefs.getString("deleted_stop_desc", "");
        addStop(stop, stopDesc);
        getSupportFragmentManager().findFragmentByTag("list").onResume();
    }

    public void routeSelected(int route) {
    }
}