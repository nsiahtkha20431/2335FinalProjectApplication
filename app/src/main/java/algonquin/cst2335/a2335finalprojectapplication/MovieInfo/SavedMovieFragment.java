package algonquin.cst2335.a2335finalprojectapplication.MovieInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.a2335finalprojectapplication.R;

public class SavedMovieFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View SavedLayout = inflater.inflate(R.layout.saved_movies_layout, container, false);


        return SavedLayout;
    }
}
