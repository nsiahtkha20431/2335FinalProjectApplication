package algonquin.cst2335.a2335finalprojectapplication.OCTranspo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.MainActivity;
import algonquin.cst2335.a2335finalprojectapplication.R;

public class StopListFragment extends Fragment {

    private ArrayList<ArrayList<String>> busStops = new ArrayList<>();
    private StopListFragment.MyAdapter adptr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View stopListLayout = inflater.inflate(R.layout.stop_list_layout, container,false);

        RecyclerView stopList = stopListLayout.findViewById(R.id.stopRecycler);
        Cursor results = MainActivity.db.rawQuery("Select * from " + FinalOpenHelper.OCTRANSPO_TABLE_NAME + ";", null);

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

        stopList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adptr = new StopListFragment.MyAdapter(busStops, getContext());
        stopList.setAdapter(adptr);
        results.close();

        return stopListLayout;
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
        private int position;

        public MyViewHolder(View view) {
            super(view);
            stopNumber = view.findViewById(R.id.busStopNumber);
            stopDescription = view.findViewById(R.id.busStopDescription);
            position = getAdapterPosition();

            view.setOnClickListener( clk -> {
                int stopNo = Integer.parseInt(stopNumber.getText().toString());
                OCTranspoActivity parent = (OCTranspoActivity) getContext();
                parent.stopSelected(stopNo);
            });
        }

    }

}
