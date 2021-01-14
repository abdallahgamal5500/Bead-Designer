package com.example.beaddesigner.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beaddesigner.R;
import com.example.beaddesigner.ui.Home;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class FragmentPalette extends Fragment implements View.OnClickListener{

    private View view;
    private ColorPickerView colorPickerView;
    private AlphaSlideBar alphaSlideBar;
    private BrightnessSlideBar brightnessSlideBar;
    private AlphaTileView alphaTileView;
    private TextView textView;
    private ImageView imagesArray [] = new ImageView[6];
    private Button button;
    private ColorEnvelope colorEnvelope;

    public FragmentPalette() {
    }

    public static FragmentPalette getInstance() {
        FragmentPalette fragmentPalette = null;
        if (fragmentPalette == null)
            fragmentPalette = new FragmentPalette();
        return fragmentPalette;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_palette, container, false);
        colorPickerView = view.findViewById(R.id.palette_color_picker_view);
        alphaSlideBar = view.findViewById(R.id.palette_alpha_slide_bar);
        brightnessSlideBar = view.findViewById(R.id.palette_brightness_slide);
        alphaTileView = view.findViewById(R.id.palette_alpha_tile_view);
        textView = view.findViewById(R.id.palette_textview);
        button = view.findViewById(R.id.palette_btn);

        button.setOnClickListener(this);

        for (int i=1;i<=6;i++) {
            int id = getResources().getIdentifier("palette_recent"+i, "id", getContext().getPackageName());
            CardView temp = view.findViewById(id);
            temp.setOnClickListener(FragmentPalette.this);
            int imgId = getResources().getIdentifier("palette_recent"+i+"_img", "id", getContext().getPackageName());
            ImageView temp2 = view.findViewById(imgId);
            imagesArray[i-1] = temp2;
            imagesArray[i-1].setOnClickListener(FragmentPalette.this);

            if (Home.SAVED_RECENT_COLORS[i-1] == Color.WHITE)
                temp.setVisibility(View.INVISIBLE);
            else {
                temp.setCardBackgroundColor(Home.SAVED_RECENT_COLORS[i-1]);
                temp.setVisibility(View.VISIBLE);
            }
        }

        for (int i=1;i<=6;i++) {
            int id = getResources().getIdentifier("palette_recent"+i+"_img", "id", getContext().getPackageName());
            ImageView temp = view.findViewById(id);
            temp.setOnClickListener(FragmentPalette.this);
        }

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                for (int i=0;i<6;i++)
                    imagesArray[i].setVisibility(View.INVISIBLE);

                colorEnvelope = envelope;
                // attach alphaSlideBar
                colorPickerView.attachAlphaSlider(alphaSlideBar);

                // attach brightnessSlideBar
                colorPickerView.attachBrightnessSlider(brightnessSlideBar);

                textView.setTextColor(envelope.getColor());
                textView.setText("#"+envelope.getHexCode());
                alphaTileView.setPaintColor(envelope.getColor());

                Home.PEN_COLOR = colorEnvelope.getColor();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.palette_btn:
                int i;
                boolean choseRecentColor = false;
                Home.CURRENT_COLOR = Home.PEN_COLOR;
                Home.TOOLS_STATE[2] = true;
                for (i = 0; i < 6; i++) {
                    if (Home.SAVED_RECENT_COLORS[i] == Home.PEN_COLOR)
                        choseRecentColor = true;
                    if (Home.SAVED_RECENT_COLORS[i] == Color.WHITE)
                        break;
                }
                if (!choseRecentColor) {
                    if (i==6) {
                        for (int j=0;j<5;j++) {
                            Home.SAVED_RECENT_COLORS[j] = Home.SAVED_RECENT_COLORS[j+1];
                        }
                        Home.SAVED_RECENT_COLORS[5] = Home.PEN_COLOR;
                    } else
                        Home.SAVED_RECENT_COLORS[i] = Home.PEN_COLOR;
                }
                getFragmentManager().popBackStack();
                break;
            case R.id.palette_recent1:
                handleCardClicking(0);
                break;
            case R.id.palette_recent2:
                handleCardClicking(1);
                break;
            case R.id.palette_recent3:
                handleCardClicking(2);
                break;
            case R.id.palette_recent4:
                handleCardClicking(3);
                break;
            case R.id.palette_recent5:
                handleCardClicking(4);
                break;
            case R.id.palette_recent6:
                handleCardClicking(5);
                break;
        }
    }

    private void handleCardClicking (int index) {
        for (int i=0;i<6;i++)
            imagesArray[i].setVisibility(View.INVISIBLE);
        Home.PEN_COLOR = Home.SAVED_RECENT_COLORS[index];
        textView.setTextColor(Home.PEN_COLOR);
        textView.setText("#"+Integer.toHexString(Home.PEN_COLOR));
        alphaTileView.setPaintColor(Home.PEN_COLOR);
        imagesArray[index].setVisibility(View.VISIBLE);
    }
}