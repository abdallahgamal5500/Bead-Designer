package com.e.handmade_patterns.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.e.handmade_patterns.R;
import com.e.handmade_patterns.fragments.FragmentChoose;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Home extends AppCompatActivity implements Communicator {

    public static final int DELETE_COLOR = Color.WHITE;
    public static Context context;
    public static int PEN_COLOR = Color.parseColor("#FFCB41"), CURRENT_COLOR = Color.parseColor("#FFCB41");
    public static final int CLICK_COLOR = Color.parseColor("#80FFFFFF");
    public static final int COLOR_ACCENT = Color.parseColor("#FFCB41");
    public static boolean [] TOOLS_STATE = new boolean[3];
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this line to disable darkmood
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_home);
        showFragmentHere(FragmentChoose.getInstance());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.home_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void showFragmentHere (Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}