package com.essam.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.essam.chatapp.home.activity.HomeActivity;
import com.essam.chatapp.login.LoginActivity;
import com.essam.chatapp.utils.firebase.FirebaseHelper;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseHelper.isUserLoggedIn()){
                    Log.i(TAG, "isUserLoggedIn: YES");
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                }
                else{
                    Log.i(TAG, "isUserLoggedIn: No");
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        },3000);
    }

}