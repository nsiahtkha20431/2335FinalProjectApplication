package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class MovieDetailsFragment extends Fragment {

    TextView title;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout =inflater.inflate(R.layout.movie_details_layout, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        title = detailsLayout.findViewById(R.id.title_message);
        title.setText(prefs.getString("TitleName", ""));

        return detailsLayout;
    }
}
