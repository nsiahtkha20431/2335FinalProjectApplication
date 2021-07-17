package algonquin.cst2335.a2335finalprojectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class OCTranspoActivity extends AppCompatActivity {

    private ArrayList<ArrayList<String>> busStops = new ArrayList<>();
    private MyAdapter adptr;
    private FinalOpenHelper opener;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);
        RecyclerView stopList = findViewById(R.id.stopRecycler);


        opener = new FinalOpenHelper(this);
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + FinalOpenHelper.OCTRANSPO_TABLE_NAME + ";", null);

        int ctr = 0;
        while (results.moveToNext()) {
            busStops.add(new ArrayList<>());
            busStops.get(ctr).add(String.valueOf(results.getInt(results.getColumnIndex(FinalOpenHelper.OCT_COL_NO))));
            busStops.get(ctr).add(results.getString(results.getColumnIndex(FinalOpenHelper.OCT_COL_DESC)));
            ctr++;
        }
        busStops.add(new ArrayList<>());
        busStops.get(ctr).add("0000");
        busStops.get(ctr).add("Add new...");

        stopList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adptr = new MyAdapter(busStops, this);
        stopList.setAdapter(adptr);
        results.close();
    }

    protected class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<ArrayList<String>> busData;
        private Context ctxt;

        public MyAdapter(ArrayList<ArrayList<String>> busStops, Context context) {
            busData = busStops;
            ctxt = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctxt).inflate(R.layout.stop_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (position < busData.size()) {
                holder.stopNumber.setText(busData.get(position).get(0));
                holder.stopDescription.setText(busData.get(position).get(1));
            }
        }

        @Override
        public int getItemCount() {
            return busData.size();
        }
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView stopNumber;
        public TextView stopDescription;

        public MyViewHolder(View view) {
            super(view);
            stopNumber = view.findViewById(R.id.busStopNumber);
            stopDescription = view.findViewById(R.id.busStopDescription);
        }

    }
}