package algonquin.cst2335.a2335finalprojectapplication;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
        View detailsView;
        if (stopNo == 0000) {
            detailsView = inflater.inflate(R.layout.add_stop_layout, container, false);
            EditText stopNumber = detailsView.findViewById(R.id.addStopNumber);
            Button addStop = detailsView.findViewById(R.id.addStopButton);
            addStop.setOnClickListener( clk -> {
                if (stopNumber.getText() != null) {
                    String stop = stopNumber.getText().toString();
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
                                    ContentValues newRow = new ContentValues();
                                    newRow.put(FinalOpenHelper.OCT_COL_NO, stopNo1);
                                    newRow.put(FinalOpenHelper.OCT_COL_DESC, stopDescription);
                                    MainActivity.db.insert(FinalOpenHelper.OCTRANSPO_TABLE_NAME, null, newRow);
                                } else {
                                    // display error
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Alert : please enter a stop number
                }
            });
            Button closeFrag = detailsView.findViewById(R.id.addStopButtonClose);
            closeFrag.setOnClickListener( clk -> {
                getParentFragmentManager().beginTransaction().remove(this).commit();
            });
        } else {
            //get api result for stop no
            //inflate stop detail layout
            detailsView = inflater.inflate(R.layout.stop_details_layout, container, false);
            //populate detail fields
        }
        return detailsView;
    }


}
