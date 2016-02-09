package com.svs.myprojects.bookmyauto.mapspage;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.svs.myprojects.bookmyauto.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by snehalsutar on 1/28/16.
 */
public class GetDirectionsInAsyncTask extends AsyncTask<String, Void, ArrayList<LatLng>> {

    String LOG_TAG = "SVS_mee";
    DirectionsInterface mDirectionsInterface;
    String mDistance, mDuration, mDirections="";

    public GetDirectionsInAsyncTask(DirectionsInterface directionsInterface) {
        this.mDirectionsInterface = directionsInterface;
    }

    @Override
    protected ArrayList<LatLng> doInBackground(String... params) {
// These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection;
        BufferedReader reader;
        URL url;
        String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
        String AVOID = "avoid";
        String ORIGIN = "origin";
        String DESTINATION = "destination";
        String MODE = "mode";
        String APPID_PARAM = "key";
        Uri builtUri;

        try {
            if(params[3]==null) {
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(ORIGIN, params[0]) //origin lat lng
                        .appendQueryParameter(DESTINATION, params[1]) // destination lat lng
                        .appendQueryParameter(MODE, params[2]) // transit mode
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_GOOGLE_MAP_API_KEY)
                        .build();
            }else{
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(ORIGIN, params[0]) //origin lat lng
                        .appendQueryParameter(DESTINATION, params[1]) // destination lat lng
                        .appendQueryParameter(MODE, params[2]) // transit mode
                        .appendQueryParameter(AVOID, params[3]) // avoid tolls,highways,ferries
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_GOOGLE_MAP_API_KEY)
                        .build();
            }


            Log.i(LOG_TAG, builtUri.toString());
            url = new URL(builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            String jsonData = buffer.toString();

            ArrayList<LatLng> latLngList = getDirections(jsonData);

            return latLngList;


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<LatLng> getDirections(String jsonData) throws JSONException {
        String ROUTES = "routes";
        String LEGS = "legs";
        String STEPS = "steps";
        String TEXT = "text";
        String DISTANCE = "distance";
        String DURATION = "duration";
        String POLYLINE = "polyline";
        String POINTS = "points";
        String HTML_DIRECTIONS = "html_instructions";

        ArrayList<LatLng> arrayListResult = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray jsonArrayResults = jsonObject.getJSONArray(ROUTES);
        if (jsonArrayResults.length() <= 0) {
            return null;
        }

        JSONObject jsonObjectRoutes = jsonArrayResults.getJSONObject(0);

        JSONArray jsonArrayLegs = jsonObjectRoutes.getJSONArray(LEGS);
        JSONObject jsonObjectLegs = jsonArrayLegs.getJSONObject(0);

        JSONArray jsonArraySteps = jsonObjectLegs.getJSONArray(STEPS);

        for (int i = 0; i < jsonArraySteps.length(); i++) {
            JSONObject jsonObjectStep = jsonArraySteps.getJSONObject(i);
            JSONObject jsonObjectPolyline = jsonObjectStep.getJSONObject(POLYLINE);
            String points = jsonObjectPolyline.getString(POINTS);
            arrayListResult.addAll(PolyUtil.decode(points));
            //Get Directions in text format
            mDirections = mDirections +jsonObjectStep.getString(HTML_DIRECTIONS) +"\n";
        }
        JSONObject jsonObjectDuration = jsonObjectLegs.getJSONObject(DURATION);
        mDuration = jsonObjectDuration.getString(TEXT);
        JSONObject jsonObjectDistance = jsonObjectLegs.getJSONObject(DISTANCE);
        mDistance = jsonObjectDistance.getString(TEXT);
        return arrayListResult;
    }


    @Override
    protected void onPostExecute(ArrayList<LatLng> latLngArrayList) {
        super.onPostExecute(latLngArrayList);
        this.mDirectionsInterface.plotPoints(latLngArrayList, mDistance, mDuration,mDirections);
    }
}
