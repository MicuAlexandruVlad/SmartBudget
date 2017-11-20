package com.example.micua.smartbudget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private ScrollView scrollView;
    private RelativeLayout themeRL, currentLocationRL;
    private LinearLayout deviceNickname;
    private CheckBox themeCheck, currentLocationCB;
    public static final String ANDROID_NAME_KEY = "android name", THEME_CHECK_KEY = "theme check", THEME_COUNT = "theme count",
                               SCROLL_X = "scroll x", SCROLL_Y = "scroll y";
    private Intent starterIntent;
    private boolean isChecked, isRestarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isChecked = loadBool(THEME_CHECK_KEY);
        if (isChecked) {
            setTheme(R.style.AppThemeSunset);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        starterIntent = getIntent();
        isRestarted = starterIntent.getBooleanExtra("restarted", false);
        if (loadString(ANDROID_NAME_KEY) == null)
            saveString("", ANDROID_NAME_KEY);


        if (isChecked) {
            String title = "Settings";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        } else {
            String title = "Settings";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        }


        deviceNickname = (LinearLayout) findViewById(R.id.ll_device_nickname);
        scrollView = (ScrollView) findViewById(R.id.scrollviewParent);
        themeRL = (RelativeLayout) findViewById(R.id.rl_theme);
        currentLocationRL = (RelativeLayout) findViewById(R.id.rl_settings_use_current_location);
        themeCheck = (CheckBox) findViewById(R.id.cb_settings_theme);
        currentLocationCB = (CheckBox) findViewById(R.id.cb_settings_location);

        //TODO: fix scrollview scrolling to saved position if activity is started normally
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(loadInt(SCROLL_X), loadInt(SCROLL_Y));
            }
        });

        themeCheck.setChecked(isChecked);

        themeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBool(!themeCheck.isChecked(), THEME_CHECK_KEY);
                themeCheck.setChecked(!themeCheck.isChecked());
            }
        });

        themeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveBool(isChecked, THEME_CHECK_KEY);
                saveInt(scrollView.getScrollX(), SCROLL_X);
                saveInt(scrollView.getScrollY(), SCROLL_Y);
                restartActivity(starterIntent);
            }
        });


        currentLocationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocationCB.setChecked(!currentLocationCB.isChecked());
            }
        });

        deviceNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomNameDialog nameDialog = new CustomNameDialog(Settings.this);
                nameDialog.create();
                nameDialog.show();
                if (loadString(ANDROID_NAME_KEY) != null) {
                    nameDialog.setNameText(loadString(ANDROID_NAME_KEY));
                    System.out.println(loadString(ANDROID_NAME_KEY));
                }

                nameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!nameDialog.getNameText().equalsIgnoreCase("")) {
                            Toast.makeText(Settings.this, nameDialog.getNameText(), Toast.LENGTH_SHORT).show();
                            saveString(nameDialog.getNameText(), ANDROID_NAME_KEY);
                            System.out.println(loadString(ANDROID_NAME_KEY));
                        }
                    }
                });
            }
        });
    }

    private void saveInt(int value, String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int loadInt(String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    private void saveBool(boolean bool, String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, bool);
        editor.apply();
    }

    private boolean loadBool(String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    private void saveString(String name, String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, name);
        editor.apply();
    }

    private String loadString(String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    private void restartActivity (Intent intent) {
        finish();
        startActivity(intent);

    }
}
