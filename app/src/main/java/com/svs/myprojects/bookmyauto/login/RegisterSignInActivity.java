package com.svs.myprojects.bookmyauto.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.svs.myprojects.bookmyauto.Constants;
import com.svs.myprojects.bookmyauto.R;
import com.svs.myprojects.bookmyauto.mapspage.GoogleMapsActivity;


public class RegisterSignInActivity extends AppCompatActivity implements RegisterSignInInterface {

    EditText mEditTextUserEmail, mEditTextUserPassword, mEditTextMobile;
    String mRegisterOrSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterOrSignIn = getIntent().getExtras().getString(Constants.REGISTER_SIGN_IN_TYPE);
        if (mRegisterOrSignIn.equals(Constants.REGISTER_TYPE)) {
            setContentView(R.layout.login_register_user);
        } else {
            setContentView(R.layout.login_sign_in_user);
        }
        getHandlers();
    }


    public void getHandlers() {
        mEditTextUserEmail = (EditText) findViewById(R.id.user_email_id);
        mEditTextUserPassword = (EditText) findViewById(R.id.user_password);
        mEditTextMobile = (EditText) findViewById(R.id.user_mobile_number);
    }

    /**********************************************************************************************/
    public void next_button_function(View view) {
        if (mRegisterOrSignIn.equals(Constants.REGISTER_TYPE)) {// From REGISTER Screen
            RegisterAsync registerAsync = new RegisterAsync(RegisterSignInActivity.this, this);
            registerAsync.execute(mEditTextUserEmail.getText().toString(),
                    mEditTextUserPassword.getText().toString(),
                    mEditTextMobile.getText().toString());
        } else { // From SIGN IN Screen
            //validate login
            LoginAsync loginAsync = new LoginAsync(RegisterSignInActivity.this, this);
            loginAsync.execute(mEditTextUserEmail.getText().toString(), mEditTextUserPassword.getText().toString());
        }
    }

    @Override
    public void setSignInResult(String result) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.snackbar_location), "Result: " + result, Snackbar.LENGTH_LONG);
        snackbar.show();

        if (result.contains("success")) {
            saveInSharedPreferences();
            Intent intent = new Intent(RegisterSignInActivity.this, GoogleMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void setRegistrationResult(String result) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.snackbar_location), "Result: " + result, Snackbar.LENGTH_LONG);
        snackbar.show();

        if (result.contains("successfully registered")) {
            saveInSharedPreferences();
            Intent intent = new Intent(RegisterSignInActivity.this, GoogleMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void saveInSharedPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.APP_UNIQUE_NAME_FOR_SP, Constants.YES);
        editor.commit();
    }

    /***********************************************************************************************
     * MEnu bar options
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_sign_in_menu, menu);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
//                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**********************************************************************************************/

}
