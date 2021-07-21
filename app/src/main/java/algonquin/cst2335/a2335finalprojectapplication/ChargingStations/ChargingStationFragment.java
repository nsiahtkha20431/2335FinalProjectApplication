package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingStationFragment extends Fragment {

    ChargingStationAdapter myAdapter;
    ArrayList<String> stationList = new ArrayList<>(); //something to hold values just until i can connect to the database


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chargingList = inflater.inflate(R.layout.charging_recycler_page, container, false);
        RecyclerView stationsRecyclerView = chargingList.findViewById(R.id.chargingRecyclerView);


        //adding temp values to my recycler
        stationList.add("Station 1");
        stationList.add("Station 2");
        stationList.add("Station 3");
        stationList.add("Station 4");
        stationList.add("Station 5");


        myAdapter = new ChargingStationAdapter(stationList, getContext());

        stationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        stationsRecyclerView.setAdapter(myAdapter);

//        stationsRecyclerView.setAdapter(myAdapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
//        stationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return chargingList;
    }

    private class StationsListHolder extends RecyclerView.ViewHolder {
        public TextView stationName;
        public ImageView plugImage;

        public StationsListHolder(View itemView) {
            //stores the itemView in a public member that can be used to access the context from any viewHolder instance
            super(itemView);

            stationName = itemView.findViewById(R.id.stationName);
            plugImage = itemView.findViewById(R.id.plugImage);
            }
        }

    private class ChargingStationAdapter extends RecyclerView.Adapter<StationsListHolder> {
        Context context;
        ArrayList<String> myArray; //any array

        public ChargingStationAdapter(ArrayList myArray, Context context) {
            this.myArray = myArray;
            this.context = context;
        }

        @Override
        public StationsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myChargingRows = inflater.inflate(R.layout.charging_recycler_row_layout, parent, false);
           // View myChargingRows = LayoutInflater.from(context).inflate(R.layout.charging_recycler_row_layout, parent,false);
            return new StationsListHolder(myChargingRows);
        }

        @Override
        public void onBindViewHolder(StationsListHolder holder, int position) {
            holder.stationName.setText(myArray.get(position));
        }

        @Override
        public int getItemCount() {
            return myArray.size();
        }
    }

    }





