package algonquin.cst2335.a2335finalprojectapplication.OCTranspo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class StopDetailsFragment extends Fragment {
    private int stopNo;

    public StopDetailsFragment(int stopNo) {
        this.stopNo = stopNo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View detailsView;
        if (stopNo == 0000) {
            detailsView = inflater.inflate(R.layout.add_stop_layout, container, false);
        } else {
            //get api result for stop no
            //inflate stop detail layout
            detailsView = inflater.inflate(R.layout.stop_details_layout, container, false);
            //populate detail fields
        }
        return detailsView;
    }
}
