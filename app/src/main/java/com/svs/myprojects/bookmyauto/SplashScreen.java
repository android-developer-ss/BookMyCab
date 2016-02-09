package com.svs.myprojects.bookmyauto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.svs.myprojects.bookmyauto.mapspage.GoogleMapsActivity;

public class SplashScreen extends AppCompatActivity {

    private static final long GAME_LENGTH_MILLISECONDS = 1000;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mp = MediaPlayer.create(SplashScreen.this, R.raw.mustang);
        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId("ca-app-pub-1525758968346984/5769718551");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //Call the next Activity
                callMainActivity();
            }
        });

        createTimer(GAME_LENGTH_MILLISECONDS);
        mCountDownTimer.start();

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

    public void callMainActivity() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
//        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
//                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        //Check from Shared Preferences if SIGN IN is saved or not.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String firstTime = sharedPref.getString(Constants.APP_UNIQUE_NAME_FOR_SP, "blah blah");
        if (firstTime.equals(Constants.YES)) {
            //If found YES in shared preferences then directly call the Maps Activity.
            Intent intent = new Intent(SplashScreen.this, GoogleMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            //If found NO in shared preferences then ask user to SIGN IN / REGISTER
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void createTimer(final long milliseconds) {
        // Create the game timer, which counts down to the end of the level
        // and shows the "retry" button.
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mp.start();
        final TextView textView = ((TextView) findViewById(R.id.timer));

        mCountDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                textView.setText(" " + ((millisUnitFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                mp.stop();
                showInterstitial();
            }
        };
    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
        callMainActivity();
    }

}
