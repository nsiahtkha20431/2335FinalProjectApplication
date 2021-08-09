package algonquin.cst2335.a2335finalprojectapplication;

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

/**
 * Displays stop numbers and descriptions stored in SQLite Database in a RecyclerView.
 *
 * Written for CST2335 Mobile Graphical Interface Programming Final Project
 * Algonquin College
 * August 8th, 2021
 *
 * @author Emma McArthur
 */

public class StopListFragment extends Fragment {

    /**
     * ArrayList of Bus Stop numbers and descriptions to be displayed in the RecyclerView.
     */
    private ArrayList<ArrayList<String>> busStops;

    /**
     * Custom RecyclerView Adapter object to load stops.
     */
    private StopListAdapter adptr;

    /**
     * RecyclerView to display stops from database.
     */
    private RecyclerView stopList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View stopListLayout = inflater.inflate(R.layout.stop_list_layout, container,false);
        stopList = stopListLayout.findViewById(R.id.stopRecycler);
        initializeArray();
        stopList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adptr = new StopListAdapter(busStops, getContext());
        stopList.setAdapter(adptr);
        return stopListLayout;
    }

    /**
     * Adds stop data from database to busStops array to be loaded in the RecyclerView.
     */
    private void initializeArray() {
        busStops = new ArrayList<>();
        Cursor results = MainActivity.db.rawQuery("Select * from " + FinalOpenHelper.OCTRANSPO_TABLE_NAME + ";", null);

        int ctr = 0;
        while (results.moveToNext()) {
            busStops.add(new ArrayList<>());
            busStops.get(ctr).add(String.valueOf(results.getInt(results.getColumnIndex(FinalOpenHelper.OCT_COL_NO))));
            busStops.get(ctr).add(results.getString(results.getColumnIndex(FinalOpenHelper.OCT_COL_DESC)));
            ctr++;
        }

        results.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeArray();
        updateList();
    }

    /**
     * Reloads stop data from database into RecyclerView.
     */
    private void updateList() {
        initializeArray();
        adptr = new StopListAdapter(busStops, getContext());
        stopList.setAdapter(adptr);
    }

    /**
     * Custom RecyclerView Adapter for StopListFragment. Loads custom ViewHolders (StopViewHolder) from
     * array of bus stop data to display.
     */
    protected class StopListAdapter extends RecyclerView.Adapter<StopViewHolder> {

        /**
         * Holds bus stop data to be displayed in RecyclerView
         */
        private ArrayList<ArrayList<String>> busData;

        /**
         * Holds context from parent activity for LayoutInflater
         */
        private Context ctxt;

        /**
         * Creates new StopListAdapter with initialized values
         * @param busStops Bus Stop Data to be displayed
         * @param context parent activity context
         */
        public StopListAdapter(ArrayList<ArrayList<String>> busStops, Context context) {
            busData = busStops;
            ctxt = context;
        }

        @Override
        public StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctxt).inflate(R.layout.oct_list_item, parent, false);
            return new StopViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StopViewHolder holder, int position) {
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

    /**
     * Custom ViewHolder for StopListFragment. Holds stop number and description from stored stops in
     * SQLiteDatabase. When clicked calls stopSelected() method from OCTranspoActivity.
     */
    protected class StopViewHolder extends RecyclerView.ViewHolder {

        /**
         * TextView that displays bus stop number
         */
        public TextView stopNumber;

        /**
         * TextView that displays bus stop description
         */
        public TextView stopDescription;

        /**
         * Creates new StopViewHolder with initialized values
         * @param view
         */
        public StopViewHolder(View view) {
            super(view);
            stopNumber = view.findViewById(R.id.listNumber);
            stopDescription = view.findViewById(R.id.listDescription);

            view.setOnClickListener( clk -> {
                int stopNo = Integer.parseInt(stopNumber.getText().toString());
                OCTranspoActivity parent = (OCTranspoActivity) getContext();
                parent.stopSelected(stopNo);
            });
        }

    }

}
