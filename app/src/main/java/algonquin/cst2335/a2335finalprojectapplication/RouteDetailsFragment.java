package algonquin.cst2335.a2335finalprojectapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Displays trip details for the selected route from the OCTranspo Server.
 * Written for CST2335 Mobile Graphical Interface Programming Final Project
 * Algonquin College
 * August 8th, 2021
 *
 * @author Emma McArthur
 */


public class RouteDetailsFragment extends Fragment {

    /**
     * Stop number to display trip data for.
     */
    private String stop;

    /**
     * Route number to display trip data for.
     */
    private String route;

    /**
     * Direction ID for stops that are serviced by both directions of a route.
     */
    private String direction;

    /**
     * Holds the completed url to access server data for route and stop numbers.
     */
    private String routeUrl;

    /**
     * Creates new fragment with stop and route initialized.
     * @param stop bus stop number to show trip data for
     * @param route bus route number to show trip data for
     */
    public RouteDetailsFragment(String stop, String route, String direction) {
        this.stop = stop;
        this.route = route;
        this.direction = direction;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.oct_loading_title))
                .setMessage(getResources().getString(R.string.oct_route_loading_message))
                .setView(new ProgressBar(getContext()));
        AlertDialog loadingDialog = builder.create();
        loadingDialog.show();
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
                JSONObject routeData = routeObject.optJSONObject("RouteDirection");
                if (routeData == null) {
                    JSONArray routeDataArray = routeObject.getJSONArray("RouteDirection");
                    routeData = (JSONObject) routeDataArray.get(Integer.parseInt(direction));
                }
                String routeNumber = routeData.getString("RouteNo");
                if (!routeNumber.equals("")) {
                    String routeDest = routeData.getString("RouteLabel");
                    JSONObject tripsObject = routeData.getJSONObject("Trips");
                    JSONArray trips = tripsObject.optJSONArray("Trip");
                    int length;
                    JSONObject currentTrip;
                    if (trips == null) {
                        currentTrip = tripsObject.optJSONObject("Trip");
                        length = 1;
                    } else {
                        length = trips.length();
                        currentTrip = trips.getJSONObject(0);
                    }
                    String longitude = currentTrip.getString("Longitude");
                    String latitude = currentTrip.getString("Latitude");
                    String speed = currentTrip.getString("GPSSpeed");
                    String tripStart = currentTrip.getString("TripStartTime");
                    String tripArrive = currentTrip.getString("AdjustedScheduleTime") + " min";
                    ArrayList<ArrayList<String>> tripList = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        if (i > 0) {
                            currentTrip = trips.getJSONObject(i);
                        }
                        String age;
                        if (Double.parseDouble(currentTrip.getString("AdjustmentAge")) < 1) {
                            age = "< 1 min";
                        } else if (Double.parseDouble(currentTrip.getString("AdjustmentAge")) < 5) {
                            age = "< 5 min";
                        } else if (Double.parseDouble(currentTrip.getString("AdjustmentAge")) < 10) {
                            age = "< 10 min";
                        } else {
                            age = "> 10 min";
                        }
                        String lastTrip;
                        if (currentTrip.getString("LastTripOfSchedule").equals("true")) {
                            lastTrip = getResources().getString(R.string.yes);
                        } else {
                            lastTrip = getResources().getString(R.string.no);
                        }
                        tripList.add(new ArrayList<>());
                        tripList.get(i).add(currentTrip.getString("AdjustedScheduleTime") + " min");
                        tripList.get(i).add(currentTrip.getString("TripStartTime"));
                        tripList.get(i).add(age);
                        tripList.get(i).add(lastTrip);
                    }

                    parent.runOnUiThread(() -> {

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
                        loadingDialog.hide();
                    });
                } else {
                    parent.runOnUiThread(() -> {
                        destView.setText(getResources().getString(R.string.no_trips));
                        loadingDialog.hide();
                    });
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });

        Button rfrsh = routeDetailsView.findViewById(R.id.refreshButton);
        rfrsh.setOnClickListener( clk -> {
            getParentFragmentManager().popBackStack();
            parent.routeSelected(stop, route, direction);
        });

        Button back = routeDetailsView.findViewById(R.id.rtBackButton);
        back.setOnClickListener( clk -> {
            getParentFragmentManager().popBackStackImmediate();
        });
        // Return View
        return routeDetailsView;
    }
}
