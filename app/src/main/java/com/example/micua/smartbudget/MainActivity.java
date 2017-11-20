package com.example.micua.smartbudget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micua.smartbudget.RegisterUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private Button register, signIn;
    private LinearLayout topBanner;
    private TextView separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBanner = (LinearLayout) findViewById(R.id.ll_top_main);
        register = (Button) findViewById(R.id.btn_register_main);
        signIn = (Button) findViewById(R.id.btn_sign_in_main);
        separator = (TextView) findViewById(R.id.tv_separator_main);

        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        Animation growInSize = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_in_x_size);
        growInSize.reset();
        topBanner.startAnimation(slideUp);
        separator.clearAnimation();
        separator.startAnimation(growInSize);
        fadeIn();

        getSupportActionBar().hide();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });

    }

    private void fadeIn() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(3000);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(fadeIn);
        register.setAnimation(set);
        signIn.setAnimation(set);
    }

}
