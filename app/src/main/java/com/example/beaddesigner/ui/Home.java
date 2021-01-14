package com.example.beaddesigner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.example.beaddesigner.R;
import com.example.beaddesigner.fragments.FragmentChoose;

public class Home extends AppCompatActivity implements Communicator {

    public static final int DELETE_COLOR = Color.WHITE;
    public static Context context;
    public static int PEN_COLOR = Color.parseColor("#FFCB41"), CURRENT_COLOR = Color.parseColor("#FFCB41");
    public static final int CLICK_COLOR = Color.parseColor("#80FFFFFF");
    public static final int COLOR_ACCENT = Color.parseColor("#FFCB41");
    public static boolean [] TOOLS_STATE = new boolean[3];
    public static int [] SAVED_RECENT_COLORS = {Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showFragmentHere(FragmentChoose.getInstance());
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