package algonquin.cst2335.a2335finalprojectapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Contains information on how to load details in to the fragment and recycler view
 */
public class ChargingDetailsFragment extends Fragment {
    String chosenLocation;
    int chosenPosition;

    /**
     * Constructor for the class that extends fragment
     * Gets the location and position
     * @param location
     * @param position
     */
    public ChargingDetailsFragment (String location, int position) {
        chosenLocation = location;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.charging_fragment_details, container, false);
        Button back = detailsView.findViewById(R.id.stationBackButton);


        back.setOnClickListener(click -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return detailsView;
    }
}
