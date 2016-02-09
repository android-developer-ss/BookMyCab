package com.svs.myprojects.bookmyauto.mapspage;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.svs.myprojects.bookmyauto.BuildConfig;
import com.svs.myprojects.bookmyauto.Constants;

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

/**
 * Created by snehalsutar on 1/27/16.
 */
public class GeoCodingAsyncTask extends AsyncTask<String, Void, String> {

    String LOG_TAG = "SVS_me";
    AddressToCoorInterface mAddressToCoorInterface;
    CoordinateToAddressInterface mCoordinateToAddressInterface;
    String mInterfaceType;

    public GeoCodingAsyncTask(String type, Object object) {
        mInterfaceType = type;
        if (type.equals(Constants.ADDRESS_TO_COORDINATES)) {
            this.mAddressToCoorInterface = (AddressToCoorInterface) object;
        } else {
            this.mCoordinateToAddressInterface = (CoordinateToAddressInterface) object;
        }
    }

    @Override
    protected String doInBackground(String[] params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL url = null;
        String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
        String ADDRESS = "address";
        String LATLNG = "latlng";
        String APPID_PARAM = "key";
        Uri builtUri;

        try {
//            https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KE
//            https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
            if (mInterfaceType.equals(Constants.ADDRESS_TO_COORDINATES)) {
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(ADDRESS, params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_GOOGLE_MAP_API_KEY)
                        .build();
            } else { //Constants.COORDINATES_TO_ADDRESS
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(LATLNG, params[0])
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
            String result;
            if (mInterfaceType.equals(Constants.ADDRESS_TO_COORDINATES)) {
                result = getCoordinates(jsonData);
            } else {
                result = getAddress(jsonData);
            }
            return result;


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAddress(String jsonData) throws JSONException {

        String RESULTS = "results";
        String FORMATTED_ADDRESS = "formatted_address";

        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray jsonArrayResults = jsonObject.getJSONArray(RESULTS);
        if (jsonArrayResults.length() <= 0) {
            return "Coordinates not found";
        }
        JSONObject jsonObjectResults = jsonArrayResults.getJSONObject(0);

//        JSONObject jsonObjectGeometry = jsonObjectResults.getJSONObject(FORMATTED_ADDRESS);
        String address = jsonObjectResults.getString(FORMATTED_ADDRESS);

        return address;
    }

    private String getCoordinates(String jsonData) throws JSONException {

        String RESULTS = "results";
        String GEOMETRY = "geometry";
        String G_LOCATION = "location";
        String LAT = "lat";
        String LNG = "lng";

        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray jsonArrayResults = jsonObject.getJSONArray(RESULTS);
        if (jsonArrayResults.length() <= 0) {
            return "Coordinates not found";
        }

        JSONObject jsonObjectResults = jsonArrayResults.getJSONObject(0);

        JSONObject jsonObjectGeometry = jsonObjectResults.getJSONObject(GEOMETRY);

        JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject(G_LOCATION);

        String latitude = jsonObjectLocation.getString(LAT);
        String longitude = jsonObjectLocation.getString(LNG);
        String final_lng_lat = latitude + "," + longitude;

        return final_lng_lat;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        double lat, lng;
        if (mInterfaceType.equals(Constants.ADDRESS_TO_COORDINATES)) {
            String latlngString[] = string.split(",");
            if (latlngString.length>1) {
                lat = Double.parseDouble(latlngString[0]);
                lng = Double.parseDouble(latlngString[1]);
                LatLng latLng = new LatLng(lat, lng);
                this.mAddressToCoorInterface.setCoordinateText(latLng);
            }
        } else {
            this.mCoordinateToAddressInterface.setTextAddress(string);
        }
    }
}
