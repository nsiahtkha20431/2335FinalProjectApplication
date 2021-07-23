package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingDetailsFragment extends Fragment {
    String chosenLocation;
    int chosenPosition;

    public ChargingDetailsFragment (String location, int position) {
        chosenLocation = location;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.charging_fragment_details, container, false);
        TextView locationName = detailsView.findViewById(R.id.locationName);
        TextView longitude = detailsView.findViewById(R.id.longitudeText);
        TextView latitude = detailsView.findViewById(R.id.latitudeText);
        TextView phoneNumber = detailsView.findViewById(R.id.phoneText);
        Button load = detailsView.findViewById(R.id.directionsButton);
        Button favourites = detailsView.findViewById(R.id.favouritesButton);
        Button back = detailsView.findViewById(R.id.stationBackButton);

        locationName.setText("Location Name: " + chosenLocation);
        longitude.setText("Not yet initialized --- will be done as part of module 2");
        latitude.setText("Not yet initialized --- will be done as part of module 2");
        phoneNumber.setText("Not yet initialized --- will be done as part of module 2");

        back.setOnClickListener(click -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return detailsView;
    }
}
