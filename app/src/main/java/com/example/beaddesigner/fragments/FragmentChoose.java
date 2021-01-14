package com.example.beaddesigner.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.beaddesigner.R;
import com.example.beaddesigner.ui.Communicator;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class FragmentChoose extends Fragment {

    private View view;
    private Button btn;
    private MaterialSpinner spinner;
    private String options [] = {"Brick","Peyote","Raw 1 bead","Square"};
    private AppCompatActivity appCompatActivity = new AppCompatActivity();
    private Communicator communicator;
    private ProgressBar progressBar;

    public FragmentChoose() {
    }

    public static FragmentChoose getInstance() {
        FragmentChoose fragmentChoose = null;
        if (fragmentChoose == null)
            fragmentChoose = new FragmentChoose();
        return fragmentChoose;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_choose, container, false);
        spinner = view.findViewById(R.id.choose_spinner1);
        btn = view.findViewById(R.id.choose_btn);
        progressBar = view.findViewById(R.id.choose_progress);
        spinner.setItems(options);
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                showProgress(View.VISIBLE);
                String value = spinner.getText().toString();
                if (value.equals(options[0])) {
                    updateButtonState();
                    communicator.showFragment(FragmentBrick.getInstance());
                } else if (value.equals(options[1])) {
                    updateButtonState();
                    communicator.showFragment(FragmentPeyote.getInstance());
                } else if (value.equals(options[2])) {
                    updateButtonState();
                    communicator.showFragment(FragmentRaw1.getInstance());
                } else if (value.equals(options[3])) {
                    updateButtonState();
                    communicator.showFragment(FragmentSquare.getInstance());
                } else {
                    spinner.setHintColor(Color.RED);
                    showProgress(View.INVISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        communicator = (Communicator) context;
    }
    // this function to avoid crashing during multi clicking on GO button
    private void updateButtonState () {
        btn.setClickable(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appCompatActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn.setClickable(true);
                        showProgress(View.INVISIBLE);
                    }
                });
            }
        });
        thread.start();
    }

    private void showProgress(int state) {
        progressBar.setVisibility(state);
    }
}