package algonquin.cst2335.a2335finalprojectapplication.ChargingStations;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

import algonquin.cst2335.a2335finalprojectapplication.R;

public class ChargingStationFragment extends Fragment {

    /**
     * Declaring my adapter object to be used later on in the code as well as
     * declaring the ArrayList that will be populated parsing the XML
     */
    ChargingStationAdapter myAdapter;
    ArrayList<String> stationList = new ArrayList<>(); //something to hold values
    String stringURL;
    String locationTitle;

    String latitude = "45.4215";
    String longitude = "-75.6972";
    String contactPhone;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chargingList = inflater.inflate(R.layout.charging_recycler_page, container, false);
        RecyclerView chargingRecyclerView = chargingList.findViewById(R.id.chargingRecyclerView);
        myAdapter = new ChargingStationAdapter(stationList, getContext());
        chargingRecyclerView.setHasFixedSize(true);
        chargingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chargingRecyclerView.setAdapter(myAdapter);



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
                        + "&maxresults=5";

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
                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if(xpp.getName().equals("LocationTitle")){
                                xpp.next();
                                locationTitle = xpp.getText();
                                stationList.add(locationTitle);
                            } else if(xpp.getName().equals("Latitude")){
                                xpp.next();
                                latitude = xpp.getText();
                                //stationList.add(latitude);
                            } else if(xpp.getName().equals("Longitude")){
                                xpp.next();
                                longitude = xpp.getText();
                                //stationList.add(longitude);
                            }else if(xpp.getName().equals("ContactTelephone1")){
                                xpp.next();
                                contactPhone = xpp.getText();
                                //stationList.add(contactPhone);
                            }
                            break;

                    }

                }
                int i=0;

                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8))).lines().collect(Collectors.joining("\n"));


            } catch (UnsupportedEncodingException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(( ) -> {
                myAdapter.notifyDataSetChanged();
            });
        });

        return chargingList;
    }

    private class StationsListHolder extends RecyclerView.ViewHolder {
        public TextView stationName; //declaring the variable that holds the name of the station
        public ImageView plugImage; // declaring the variable that holds the image of the station


        public StationsListHolder(View itemView) {
            /**stores the itemView in a public member that can be used to access the context from any viewHolder instance
             *
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
                        Toast.makeText(getContext(),"You clicked on" + stationList.get(position), Toast.LENGTH_SHORT).show(); //shows us a toast with what was clicked
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
        ArrayList<String> myArray; //any array

        /**
         * just a simple method to set "this" as the array and context
         * @param myArray
         * @param context
         */
        public ChargingStationAdapter(ArrayList myArray, Context context) {
            this.myArray = myArray;
            this.context = context; //setting "this" as the context for this instance of ChargingStationAdapter
        }


        @Override
        public StationsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
           View myChargingRows = LayoutInflater.from(context).inflate(R.layout.charging_recycler_row_layout, parent,false);
            return new StationsListHolder(myChargingRows);
        }

        @Override
        public void onBindViewHolder(StationsListHolder holder, int position) {
            holder.stationName.setText(myArray.get(position));
        }

        @Override
        /**
         * This method just returns the size of the array (the number of rows that are in the RecyclerView)
         */
        public int getItemCount() {
            return myArray.size(); //returns the size of the array aka the number of rows in the RecyclerView
        }
    }

    }





