package com.barathsandy.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.barathsandy.food.rider.RiderHomeActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences MainActivity;
    SharedPreferences user_session;
    String currentUserType;
    boolean isLoggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);




        //User session
        user_session = this.getSharedPreferences("user_details", MODE_PRIVATE);


        //Get the Session Values
        currentUserType = user_session.getString("cu_type", "");
        isLoggedIn = user_session.getBoolean("isLoggedIn", false);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        MainActivity = getSharedPreferences("onBordingScreen",MODE_PRIVATE);
        boolean isFirstTime = MainActivity.getBoolean("firstTime",true);



        if (isFirstTime){

          SharedPreferences.Editor editor = MainActivity.edit();
          editor.putBoolean("firstTime",false);
          editor.commit();

          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
          startActivity(intent);
          finish();


           }
        else {
            //Check whether user available or not

            if(isLoggedIn){

                if (currentUserType.equals("Rider")) {
                    startActivity(new Intent(SplashActivity.this, RiderHomeActivity.class));

                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }


            }else{
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();

            }


                }


            }
        },8000);
    }
}