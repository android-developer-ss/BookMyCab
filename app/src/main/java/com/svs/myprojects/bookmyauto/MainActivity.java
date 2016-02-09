package com.svs.myprojects.bookmyauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.svs.myprojects.bookmyauto.login.RegisterSignInActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //662129107466-dsk3b29mnghqaahq33vr2jnc93fm2vjh.apps.googleusercontent.com
    }

    public void call_register_function(View view){
        Intent intent = new Intent(MainActivity.this, RegisterSignInActivity.class);
        intent.putExtra(Constants.REGISTER_SIGN_IN_TYPE, Constants.REGISTER_TYPE);
        startActivity(intent);
    }
    public void call_sign_in_function(View view){
        Intent intent = new Intent(MainActivity.this, RegisterSignInActivity.class);
        intent.putExtra(Constants.REGISTER_SIGN_IN_TYPE, Constants.SIGN_IN_TYPE);
        startActivity(intent);
    }
}
