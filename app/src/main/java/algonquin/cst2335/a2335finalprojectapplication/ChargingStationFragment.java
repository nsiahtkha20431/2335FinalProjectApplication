package algonquin.cst2335.a2335finalprojectapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class ChargingStationFragment extends Fragment {

    /**
     * Declaring my adapter object to be used later on in the code as well as
     * declaring the ArrayList that will be populated parsing the XML
     */
    ChargingStationAdapter myAdapter;
    ArrayList<ChargingStation> stationList = new ArrayList<>(); //something to hold values
    String stringURL;
    String latitude;
    String longitude;

    /**
     * Constructor for getting the latitude and longitude
     * @param latitude
     * @param longitude
     */

    public ChargingStationFragment(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chargingList = inflater.inflate(R.layout.charging_recycler_page, container, false);
        RecyclerView chargingRecyclerView = chargingList.findViewById(R.id.chargingRecyclerView);
        myAdapter = new ChargingStationAdapter(stationList, getContext());


        chargingRecyclerView.setHasFixedSize(true);
        chargingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chargingRecyclerView.setAdapter(myAdapter);

        /**display an alert that tells the user that their information is being loaded
         *
         */
        AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
                .setTitle("Getting Charging Stations")
                .setMessage("We're looking for your charging stations. Hang tight!")
                .setView(new ProgressBar(this.getActivity()))
                .show();

        /**
         * Recieving data from the first page into this fragment
         */
        if (getArguments() != null) {
            String latitudeInfo = getArguments().getString("latitude");
            String longitudeInfo = getArguments().getString("longitude");
        }

        /**
         * this is the part of the code that will parse through the xml and get the values that we're looking for
         */
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {

            try {
                /**
                 * we already have the string variable initialized, but we're giving it a value now
                 * the URL encoder removes any spaces in the string
                 * UTF8 means convert it to a string using 8 but characters instead of 16 or 32 bit ones
                 */
                stringURL = "https://api.openchargemap.io/v3/poi/?output=xml&key=21ccddce-bf3f-438c-ac5a-cedb5a641515&latitude="
                        + URLEncoder.encode(latitude, "UTF-8")
                        + "&longitude="
                        + URLEncoder.encode(longitude, "UTF-8")
                        + "&maxresults=10";

                /**
                 * Creating a connection to the xml
                 */
                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");

                //stationList.clear();
                String locationTitle = "";
                String searchedLat = "";
                String searchedLong = "";
                String contactPhone = "";

                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("LocationTitle")) {
                                xpp.next();
                                locationTitle = xpp.getText();
                                //stationList.add(locationTitle);
                            } else if (xpp.getName().equals("Latitude")) {
                                xpp.next();
                                searchedLat = xpp.getText();
                            } else if (xpp.getName().equals("Longitude")) {
                                xpp.next();
                                searchedLong = xpp.getText();
                            } else if (xpp.getName().equals("ContactTelephone1")) {
                                xpp.next();
                                contactPhone = xpp.getText();
                                ChargingStation station = new ChargingStation(locationTitle, searchedLat, searchedLong, contactPhone);
                                stationList.add(station);
//                                Intent intent = new Intent(getActivity().getBaseContext(), ChargingThirdPage.class);
//                                intent.putExtra("StationName",locationTitle);
//                                intent.putExtra("LatitudeHere",searchedLat);
//                                intent.putExtra("LongitudeHere",searchedLong);
//                                intent.putExtra("PhoneNumberHere",contactPhone);
                            }
                            break;
                    }
                }
                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));
            } catch (UnsupportedEncodingException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            /**
             * make the activity run async and also hide the dialog when all the information is loaded into the list
             */
            getActivity().runOnUiThread(() -> {
                myAdapter.notifyDataSetChanged();
                dialog.hide();
            });
        });

        return chargingList;
    }

    private class StationsListHolder extends RecyclerView.ViewHolder {
        public TextView stationName; //declaring the variable that holds the name of the station
        public ImageView plugImage; // declaring the variable that holds the image of the station


        public StationsListHolder(View itemView) {
            /**stores the itemView in a public member that can be used to access the context from any viewHolder instance
             */
            super(itemView);

            stationName = itemView.findViewById(R.id.stationName); //initializing the variables
            plugImage = itemView.findViewById(R.id.plugImage);//initializing the variables

            /**
             * onClickListener that will display a toast when something is clicked on
             */
            itemView.setOnClickListener(click -> {
                        int position = getAbsoluteAdapterPosition();
                        ChargingSecondPage parentActivity = (ChargingSecondPage) getContext();
                        //Toast.makeText(getContext(), "You clicked on" + stationList.get(position), Toast.LENGTH_SHORT).show(); //shows us a toast with what was clicked
                        parentActivity.userClickedMessage(stationList.get(position), position);
                    }
            );
        }
    }

    /**
     * This is the adapter for the recycler view
     * The adapter is the middle man for what the user sees and how the application gets and sets the data
     * It will also tell us how to build the list
     * The context variable gets us the context
     */
    private class ChargingStationAdapter extends RecyclerView.Adapter<StationsListHolder> {
        Context context;
        ArrayList<ChargingStation> myArray; //any array

        /**
         * just a simple method to set "this" as the array and context
         *
         * @param myArray
         * @param context
         */
        public ChargingStationAdapter(ArrayList<ChargingStation> myArray, Context context) {
            this.myArray = myArray;
            this.context = context; //setting "this" as the context for this instance of ChargingStationAdapter
        }


        @Override
        /**
         * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
         * an item.
         */
        public StationsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myChargingRows = LayoutInflater.from(context).inflate(R.layout.charging_recycler_row_layout, parent, false);
            return new StationsListHolder(myChargingRows);
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
         * position.
         */
        @Override
        public void onBindViewHolder(StationsListHolder holder, int position) {
            holder.stationName.setText(myArray.get(position).getLocationTitle());
        }

        @Override
        /**
         * This method just returns the size of the array (the number of rows that are in the RecyclerView)
         */
        public int getItemCount() {
            return myArray.size(); //returns the size of the array aka the number of rows in the RecyclerView
        }
    }

    /**
     * This class will be used strictly to get all the information that will be passed onto the third page
     * There are getters that will take information from here and give it to the next page
     */
    class ChargingStation {
        String locationTitle;
        String longitude;
        String latitude;
        String contactPhone;

        /**
         * Constructor for the Charging Station class
         * @param locationTitle
         * @param latitude
         * @param longitude
         * @param contactPhone
         */
        public ChargingStation(String locationTitle, String latitude, String longitude, String contactPhone) {
            this.locationTitle = locationTitle;
            this.longitude = longitude;
            this.latitude = latitude;
            this.contactPhone = contactPhone;
        }

        /**
         * Getters for Locaiton, Longitude, Latitude, and Phone Number
         * @return
         */
        String getLocationTitle() {
            return this.locationTitle;
        }

        String getLongitude() {
            return this.longitude;
        }

        String getLatitude() {
            return this.latitude;
        }

        String getContactPhone() {
            return this.contactPhone;
        }

    }

}

