package com.barathsandy.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.barathsandy.food.rider.RiderRegisterActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView,powerd;
    private String welcome_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome_note = getString(R.string.welcom_note);

        textView = findViewById(R.id.welcome);
        powerd = findViewById(R.id.powerd);





        textView.startAnimation((Animation) AnimationUtils.
                loadAnimation(MainActivity.this,R.anim.animation_logo));
        powerd.startAnimation((Animation) AnimationUtils.
                loadAnimation(MainActivity.this,R.anim.translate));



    }

    public void LoginBtn(View view) {
        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        finish();

    }

    public void RegisterBtn(View view) {
        startActivity(new Intent(MainActivity.this, RiderRegisterActivity.class));
        finish();
    }
}