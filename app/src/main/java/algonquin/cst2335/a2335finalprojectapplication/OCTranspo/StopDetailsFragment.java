package algonquin.cst2335.a2335finalprojectapplication.OCTranspo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.a2335finalprojectapplication.FinalOpenHelper;
import algonquin.cst2335.a2335finalprojectapplication.MainActivity;
import algonquin.cst2335.a2335finalprojectapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class StopDetailsFragment extends Fragment {
    private int stopNo;

    public StopDetailsFragment(int stopNo) {
        this.stopNo = stopNo;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        OCTranspoActivity parent = (OCTranspoActivity) getContext();
        SharedPreferences prefs = getActivity().getSharedPreferences("OCT_Data", Context.MODE_PRIVATE);
        View detailsView;
        detailsView = inflater.inflate(R.layout.add_stop_layout, container, false);
        EditText stopNumber = detailsView.findViewById(R.id.addStopNumber);
        String savedStop = prefs.getString("saved_stop", "");
        stopNumber.setText(savedStop);
        if (stopNo == 5555) {
            Button addStop = detailsView.findViewById(R.id.addStopButton);
            addStop.setOnClickListener( clk -> {
                if (stopNumber.getText().toString().length() > 0) {
                    String stop = stopNumber.getText().toString();
                    prefs.edit().putString("saved_stop", stop).apply();
                    int stopNo = Integer.parseInt(stop);
                    try {
                        URL url = new URL("https://api.octranspo1.com/v2.0/GetRouteSummaryForStop?appID=223eb5c3&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                                + URLEncoder.encode(stop, "UTF-8"));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        Executor newThread = Executors.newSingleThreadExecutor();
                        newThread.execute( () -> {
                            try {
                                InputStream in = new BufferedInputStream(conn.getInputStream());
                                String text = (new BufferedReader(
                                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                                        .lines().collect(Collectors.joining("\n"));
                                JSONObject stopData = new JSONObject(text);
                                JSONObject mainData = stopData.getJSONObject("GetRouteSummaryForStopResult");
                                String error = mainData.getString("Error");
                                if (error.equals("")) {
                                    String stopNo1 = mainData.getString("StopNo");
                                    String stopDescription = mainData.getString("StopDescription");
                                    parent.addStop(stopNo1, stopDescription);
                                    getActivity().runOnUiThread( () -> {
                                        getParentFragmentManager().popBackStackImmediate();
                                    });
                                } else {
                                    String errorText;
                                    switch(error) {
                                        case "1":
                                        case "2":
                                            errorText = "API Error: Please try again.";
                                            break;
                                        case "10":
                                            errorText = "Error: Invalid stop number.";
                                            break;
                                        default:
                                            errorText = "Error: Please try again.";
                                            break;
                                    }
                                    getActivity().runOnUiThread( () -> {
                                        Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Attention")
                    .setMessage("Please enter a stop number to add to your list.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            Button closeFrag = detailsView.findViewById(R.id.addStopButtonClose);
            closeFrag.setOnClickListener( clk -> {
                getParentFragmentManager().popBackStackImmediate();
            });
        } else {
            //get api result for stop no

            //inflate stop detail layout
            detailsView = inflater.inflate(R.layout.stop_details_layout, container, false);

            Button back = detailsView.findViewById(R.id.backButton);
            back.setOnClickListener( clk -> {
                getParentFragmentManager().popBackStackImmediate();
            });
            TextView description = detailsView.findViewById(R.id.description);
            String stopDesc = description.getText().toString();
            Button delete = detailsView.findViewById(R.id.deleteButton);
            delete.setOnClickListener( clk -> {
                parent.deleteStop(stopNo, stopDesc);
                getParentFragmentManager().popBackStackImmediate();
            });
            //populate detail fields
        }
        return detailsView;
    }
}
