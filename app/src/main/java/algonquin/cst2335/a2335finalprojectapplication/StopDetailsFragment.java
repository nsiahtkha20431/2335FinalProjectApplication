package algonquin.cst2335.a2335finalprojectapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Displays either the Add Stop layout or Stop Details layout depending on the value of stopNo.
 * Add Stop allows users to search for and add new bus stops to the database to be displayed in
 * StopListFragment.
 * Stop Details displays the routes that access the stop number in a RecyclerView. The routes can
 * be selected to display trip details.
 *
 * Written for CST2335 Mobile Graphical Interface Programming Final Project
 * Algonquin College
 * August 8th, 2021
 *
 * @author Emma McArthur
 */

public class StopDetailsFragment extends Fragment {

    /**
     * Stop Number to display
     */
    private int stopNo;

    /**
     * Stores parent activity context
     */
    private OCTranspoActivity parent;

    /**
     * No argument constructor sets stopNo to -1, triggering the Add Stop Layout
     */
    public StopDetailsFragment() { this.stopNo = -1; }

    /**
     * Constructor sets the stopNo, triggering the Stop Details Layout.
     * @param stopNo stop number to be displayed
     */
    public StopDetailsFragment(int stopNo) {
        this.stopNo = stopNo;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parent = (OCTranspoActivity) getContext();
        SharedPreferences prefs = getActivity().getSharedPreferences("OCT_Data", Context.MODE_PRIVATE);
        View detailsView;
        String stopUrl = "https://api.octranspo1.com/v2.0/GetRouteSummaryForStop?appID=" + parent.getAppId()
                + "&apiKey=" + parent.getApiKey() + "&stopNo=";

        if (stopNo == -1) { //if "Add new" selected, inflate Add Stop Layout
            detailsView = inflater.inflate(R.layout.add_stop_layout, container, false);
            EditText stopNumber = detailsView.findViewById(R.id.addStopNumber);
            String savedStop = prefs.getString("saved_stop", "");
            stopNumber.setText(savedStop);

            // Add Stop Click Listener
            Button addStop = detailsView.findViewById(R.id.addStopButton);
            addStop.setOnClickListener(clk -> {
                // If stop number was entered continue to search, else show error alert
                if (stopNumber.getText().toString().length() > 0) {
                    // Save stop number entered to Shared Preferences
                    String stop = stopNumber.getText().toString();
                    prefs.edit().putString("saved_stop", stop).apply();
                    // Pull stop data from server
                    try {
                        URL url = new URL(stopUrl + URLEncoder.encode(stop, "UTF-8"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        Executor newThread = Executors.newSingleThreadExecutor();
                        newThread.execute(() -> {
                            try {
                                InputStream in = new BufferedInputStream(conn.getInputStream());
                                String text = (new BufferedReader(
                                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                                        .lines().collect(Collectors.joining("\n"));
                                JSONObject stopData = new JSONObject(text);
                                JSONObject mainData = stopData.getJSONObject("GetRouteSummaryForStopResult");
                                String error = mainData.getString("Error");
                                // If no error code add stop to database, else display error
                                if (error.equals("")) {
                                    String stopNo1 = mainData.getString("StopNo");
                                    String stopDescription = mainData.getString("StopDescription");
                                    parent.addStop(stopNo1, stopDescription);
                                    getActivity().runOnUiThread(() -> getParentFragmentManager().popBackStackImmediate());
                                } else {
                                    String errorText;
                                    switch (error) {
                                        case "1":
                                        case "2":
                                            errorText = getResources().getString(R.string.oct_api_error);
                                            break;
                                        case "10":
                                            errorText = getResources().getString(R.string.oct_stop_error);
                                            break;
                                        default:
                                            errorText = getResources().getString(R.string.oct_default_error);
                                            break;
                                    }
                                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show());
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Alert if Stop Number was left empty
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getResources().getString(R.string.oct_alert_title))
                            .setMessage(getResources().getString(R.string.oct_alert_message))
                            .setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            // Close Add Stop fragment
            Button closeFrag = detailsView.findViewById(R.id.addStopButtonClose);
            closeFrag.setOnClickListener(clk -> getParentFragmentManager().popBackStackImmediate());

        } else { // Otherwise inflate the Stop Details layout for the selected stop
            //inflate stop detail layout
            detailsView = inflater.inflate(R.layout.stop_details_layout, container, false);
            // Loading dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getResources().getString(R.string.oct_loading_title))
                    .setMessage(getResources().getString(R.string.oct_stop_loading_message))
                    .setView(new ProgressBar(getContext()));
            AlertDialog loadingDialog = builder.create();
            loadingDialog.show();
            TextView stopText = detailsView.findViewById(R.id.stopNo);
            TextView descText = detailsView.findViewById(R.id.description);
            //get api result for stop no
            try {
                URL url = new URL(stopUrl + URLEncoder.encode(String.valueOf(stopNo), "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute(() -> {
                    try {
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        String text = (new BufferedReader(
                                new InputStreamReader(in, StandardCharsets.UTF_8)))
                                .lines().collect(Collectors.joining("\n"));
                        // Populate array with route data
                        JSONObject stopData = new JSONObject(text).getJSONObject("GetRouteSummaryForStopResult");
                        String stopDesc = stopData.getString("StopDescription");
                        String stop = stopData.getString("StopNo");
                        JSONArray routeArray = stopData.getJSONObject("Routes").optJSONArray("Route");
                        JSONObject routeObject;
                        ArrayList<ArrayList<String>> routes = new ArrayList<>();
                        if (routeArray == null) { //Only one route
                            routeObject = stopData.getJSONObject("Routes").optJSONObject("Route");
                            if (routeObject != null) {
                                routes.add(new ArrayList<>());
                                routes.get(0).add(routeObject.getString("RouteNo"));
                                routes.get(0).add(routeObject.getString("RouteHeading"));
                            }
                        } else {
                            for (int i = 0; i < routeArray.length(); i++) {
                                routeObject = routeArray.getJSONObject(i);
                                routes.add(new ArrayList<>());
                                routes.get(i).add(routeObject.getString("RouteNo"));
                                routes.get(i).add(routeObject.getString("RouteHeading"));
                            }

                        }
                        getActivity().runOnUiThread(() -> {
                            // Set up recycler view with route array
                            RecyclerView routeList = detailsView.findViewById(R.id.routeRecycler);
                            routeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            StopDetailsAdapter adptr = new StopDetailsAdapter(routes, getContext());
                            routeList.setAdapter(adptr);
                            // Add data to fields
                            stopText.setText(stop);
                            descText.setText(stopDesc);
                            // Back button
                            Button back = detailsView.findViewById(R.id.backButton);
                            back.setOnClickListener(clk -> getParentFragmentManager().popBackStackImmediate());

                            // Delete button
                            Button delete = detailsView.findViewById(R.id.deleteButton);
                            delete.setOnClickListener(clk -> {
                                parent.deleteStop(stopNo, stopDesc);
                                getParentFragmentManager().popBackStackImmediate();
                            });
                            loadingDialog.hide();
                        });

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return detailsView;
    }

    /**
     * Custom RecyclerView Adapter object for StopDetailsFragment. Loads custom ViewHolders
     * (RouteViewHolders) from array of route data to display.
     */
    protected class StopDetailsAdapter extends RecyclerView.Adapter<RouteViewHolder> {

        /**
         * Holds route data to be displayed
         */
        private ArrayList<ArrayList<String>> routeData;

        /**
         * Holds parent activity context
         */
        private Context ctxt;

        /**
         * Creates new StopDetailsAdapter with initialized values
         * @param routeData
         * @param context
         */
        public StopDetailsAdapter(ArrayList<ArrayList<String>> routeData, Context context) {
            this.routeData = routeData;
            this.ctxt = context;
        }

        @Override
        public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctxt).inflate(R.layout.oct_list_item, parent, false);
            return new RouteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RouteViewHolder holder, int position) {
            if (position < routeData.size()) {
                holder.routeNumber.setText(routeData.get(position).get(0));
                holder.routeDescription.setText(routeData.get(position).get(1));
            }
        }

        @Override
        public int getItemCount() {
            return routeData.size();
        }
    }

    /**
     * Custom ViewHolder for StopDetailsFragment. Holds route numbers and descriptions.
     * When clicked calls routeSelected() method from OCTranspoActivity.
     */
    protected class RouteViewHolder extends RecyclerView.ViewHolder {
        /**
         * TextView that displays route number
         */
        public TextView routeNumber;

        /**
         * TextView that displays route description
         */
        public TextView routeDescription;

        /**
         * Creates new RouteViewHolder with initialized values
         * @param view
         */
        public RouteViewHolder(View view) {
            super(view);
            routeNumber = view.findViewById(R.id.listNumber);
            routeDescription = view.findViewById(R.id.listDescription);

            view.setOnClickListener( clk -> {
                int routeNo = Integer.parseInt(routeNumber.getText().toString());
                OCTranspoActivity parent = (OCTranspoActivity) getContext();
                parent.routeSelected(stopNo, routeNo);
            });
        }

    }

}
