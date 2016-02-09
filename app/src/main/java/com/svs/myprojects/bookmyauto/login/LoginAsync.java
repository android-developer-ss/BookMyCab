package com.svs.myprojects.bookmyauto.login;

/**
 * Created by snehalsutar on 2/1/16.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginAsync extends AsyncTask<String, Void, String> {

    Context mContext;
    RegisterSignInInterface mRegisterSignInInterface;
    private String LOG_TAG = "SVS";
    private Dialog loadingDialog;

    /***********************************************************************************************
     * Constructor to initialize Context and Calling Activity which implements the callback fr result
     * */

    public LoginAsync(Context context, RegisterSignInInterface registerSignInInterface) {
        mContext = context;
        this.mRegisterSignInInterface = registerSignInInterface;
    }

    /***********************************************************************************************
     * Start  the Loading Dialog to show validation is in Progress.
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = ProgressDialog.show(mContext, "Please wait", "Loading...");
    }

    /***********************************************************************************************
     * Do in Background the actual calling process.
     * */
    @Override
    protected String doInBackground(String... params) {
//        String BASE_URL = "http://rjtmobile.com/ansari/dbConnect.php?";
//
//        HashMap<String,String> data = new HashMap<>();
//        data.put("username",params[0]);
//        data.put("password",params[1]);
//
//        RegisterUserClass ruc = new RegisterUserClass();
//
//        String result = ruc.sendPostRequest(BASE_URL,data);
//        return result;
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = "";
        try {

            final String REGISTRATION_BASE_URL = "http://rjtmobile.com/ansari/dbConnect.php?";
            final String USERNAME = "username";
            final String PASSWORD = "password";


            Uri builtUri = Uri.parse(REGISTRATION_BASE_URL).buildUpon()
                    .appendQueryParameter(USERNAME, params[0]) //username
                    .appendQueryParameter(PASSWORD, params[1]) //username
                    .build();

            URL url = new URL(builtUri.toString());

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
            result = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movies data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return result;
    }

    /***********************************************************************************************
     * Send result to the callback method in calling Activity.
     * */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        loadingDialog.dismiss();
        this.mRegisterSignInInterface.setSignInResult(result);
    }


}
