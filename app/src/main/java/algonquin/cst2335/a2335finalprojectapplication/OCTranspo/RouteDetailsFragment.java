package algonquin.cst2335.a2335finalprojectapplication.OCTranspo;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import algonquin.cst2335.a2335finalprojectapplication.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RouteDetailsFragment extends Fragment {

    private int stop;
    private int route;
    private String routeUrl;

    public RouteDetailsFragment(int stop, int route) {
        this.stop = stop;
        this.route = route;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        OCTranspoActivity parent = (OCTranspoActivity) getContext();
        try {
            routeUrl = "https://api.octranspo1.com/v2.0/GetNextTripsForStop?appID="
                    + parent.getAppId() +"&apiKey=" + parent.getApiKey() + "&stopNo=" +
                    URLEncoder.encode(String.valueOf(stop), "UTF-8") +
                    "&routeNo=" + URLEncoder.encode(String.valueOf(route), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Create view
        View routeDetailsView = inflater.inflate(R.layout.route_details, container, false);
        TextView routeNo = routeDetailsView.findViewById(R.id.routeNumber);
        TextView stopNo = routeDetailsView.findViewById(R.id.stopNumber);
        TextView destView = routeDetailsView.findViewById(R.id.destText);
        TextView busLongitude = routeDetailsView.findViewById(R.id.busLongitude);
        TextView busLatitude = routeDetailsView.findViewById(R.id.busLatitude);
        TextView busSpeed = routeDetailsView.findViewById(R.id.busSpeed);
        TextView arrivalTime = routeDetailsView.findViewById(R.id.arrivalTime);
        TextView startTime = routeDetailsView.findViewById(R.id.startTime);
        TextView tripArrival1 = routeDetailsView.findViewById(R.id.arriving1);
        TextView tripArrival2 = routeDetailsView.findViewById(R.id.arriving2);
        TextView tripArrival3 = routeDetailsView.findViewById(R.id.arriving3);
        TextView tripStart1 = routeDetailsView.findViewById(R.id.startTime1);
        TextView tripStart2 = routeDetailsView.findViewById(R.id.startTime2);
        TextView tripStart3 = routeDetailsView.findViewById(R.id.startTime3);
        TextView tripAge1 = routeDetailsView.findViewById(R.id.age1);
        TextView tripAge2 = routeDetailsView.findViewById(R.id.age2);
        TextView tripAge3 = routeDetailsView.findViewById(R.id.age3);
        TextView tripLast1 = routeDetailsView.findViewById(R.id.last1);
        TextView tripLast2 = routeDetailsView.findViewById(R.id.last2);
        TextView tripLast3 = routeDetailsView.findViewById(R.id.last3);

        // Pull data from server
        // Fill view with data
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute( () -> {
            try {
                URL url = new URL(routeUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String data = (new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));
                JSONObject dataObject = new JSONObject(data);
                JSONObject stopData = dataObject.getJSONObject("GetNextTripsForStopResult");
                String stopNumber = stopData.getString("StopNo");
                String stopDesc = stopData.getString("StopLabel");
                String error = stopData.getString("Error");
                JSONObject routeObject = stopData.getJSONObject("Route");
                JSONObject routeData = routeObject.getJSONObject("RouteDirection");
                String routeNumber = routeData.getString("RouteNo");
                String routeDest = routeData.getString("RouteLabel");
                JSONObject tripsObject = routeData.getJSONObject("Trips");
                JSONArray trips = tripsObject.getJSONArray("Trip");
                int length = trips.length();
                JSONObject currentTrip = trips.getJSONObject(0);
                String longitude = currentTrip.getString("Longitude");
                String latitude = currentTrip.getString("Latitude");
                String speed = currentTrip.getString("GPSSpeed");
                String tripStart = currentTrip.getString("TripStartTime");
                String tripArrive = currentTrip.getString("AdjustedScheduleTime");
                ArrayList<ArrayList<String>> tripList = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    currentTrip = trips.getJSONObject(i);
                    tripList.add(new ArrayList<>());
                    tripList.get(i).add(currentTrip.getString("AdjustedScheduleTime"));
                    tripList.get(i).add(currentTrip.getString("TripStartTime"));
                    tripList.get(i).add(currentTrip.getString("AdjustmentAge"));
                    tripList.get(i).add(currentTrip.getString("LastTripOfSchedule"));
                }

                parent.runOnUiThread( () -> {

                    routeNo.setText(routeNumber);
                    stopNo.setText(stopNumber);
                    destView.setText(routeDest);
                    busLongitude.setText(longitude);
                    busLatitude.setText(latitude);
                    busSpeed.setText(speed);
                    arrivalTime.setText(tripArrive);
                    startTime.setText(tripStart);
                    tripArrival1.setText(tripList.get(0).get(0));
                    tripStart1.setText(tripList.get(0).get(1));
                    tripAge1.setText(tripList.get(0).get(2));
                    tripLast1.setText(tripList.get(0).get(3));
                    if (length > 1) {
                        tripArrival2.setText(tripList.get(1).get(0));
                        tripStart2.setText(tripList.get(1).get(1));
                        tripAge2.setText(tripList.get(1).get(2));
                        tripLast2.setText(tripList.get(1).get(3));
                    }
                    if (length > 2) {
                        tripArrival3.setText(tripList.get(2).get(0));
                        tripStart3.setText(tripList.get(2).get(1));
                        tripAge3.setText(tripList.get(2).get(2));
                        tripLast3.setText(tripList.get(2).get(3));
                    }
                });

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });

        Button rfrsh = routeDetailsView.findViewById(R.id.refreshButton);
        rfrsh.setOnClickListener( clk -> {
            getParentFragmentManager().popBackStackImmediate();
            parent.routeSelected(stop, route);
        });

        Button back = routeDetailsView.findViewById(R.id.rtBackButton);
        back.setOnClickListener( clk -> {
            getParentFragmentManager().popBackStackImmediate();
        });
        // Return View
        return routeDetailsView;
    }
}
