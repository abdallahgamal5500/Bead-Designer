package com.e.handmade_patterns.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.e.handmade_patterns.R;
import com.e.handmade_patterns.ui.Home;
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
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();

        button.setOnClickListener(this);

        for (int i=1;i<=6;i++) {
            int id = getResources().getIdentifier("palette_recent" + i, "id", getContext().getPackageName());
            CardView temp = view.findViewById(id);
            temp.setOnClickListener(FragmentPalette.this);
            int imgId = getResources().getIdentifier("palette_recent" + i + "_img", "id", getContext().getPackageName());
            ImageView temp2 = view.findViewById(imgId);
            imagesArray[i - 1] = temp2;
            imagesArray[i - 1].setOnClickListener(FragmentPalette.this);

            if (Integer.parseInt(preferences.getString("" + (i-1), Color.WHITE + "")) == Color.WHITE) {
                temp.setVisibility(View.INVISIBLE);
            } else {
                temp.setCardBackgroundColor(Integer.parseInt(preferences.getString(""+(i-1),Color.WHITE+"")));
                temp.setVisibility(View.VISIBLE);
            }
            /*
            if (Home.SAVED_RECENT_COLORS[i-1] == Color.WHITE)
                temp.setVisibility(View.INVISIBLE);
            else {
                temp.setCardBackgroundColor(Home.SAVED_RECENT_COLORS[i-1]);
                temp.setVisibility(View.VISIBLE);
            }
             */
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

                if (colorEnvelope.getColor() != -1)
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
                /*
                for (i = 0; i < 6; i++) {
                    if (Home.SAVED_RECENT_COLORS[i] == Home.PEN_COLOR)
                        choseRecentColor = true;
                    if (Home.SAVED_RECENT_COLORS[i] == Color.WHITE)
                        break;
                }
                 */
                for (i = 0; i < 6; i++) {
                    if (Integer.parseInt(preferences.getString(""+i,Color.WHITE+"")) == Home.PEN_COLOR)
                        choseRecentColor = true;
                    if (Integer.parseInt(preferences.getString(""+i,Color.WHITE+""))  == Color.WHITE)
                        break;
                }
                /*
                if (!choseRecentColor) {
                    if (i==6) {
                        for (int j=0;j<5;j++) {
                            Home.SAVED_RECENT_COLORS[j] = Home.SAVED_RECENT_COLORS[j+1];
                            editor.putString(""+j,preferences.getString(""+(j+1),""+Color.WHITE));
                        }
                        Home.SAVED_RECENT_COLORS[5] = Home.PEN_COLOR;
                        editor.putString("5",Home.PEN_COLOR+"");
                    } else {
                        Home.SAVED_RECENT_COLORS[i] = Home.PEN_COLOR;
                        editor.putString(""+i,""+Home.PEN_COLOR);
                    }
                }
                */
                if (!choseRecentColor) {
                    if (i==6) {
                        for (int j=0;j<5;j++) {
                            editor.putString(""+j,preferences.getString(""+(j+1),""+Color.WHITE));
                        }
                        editor.putString("5",Home.PEN_COLOR+"");
                    } else {
                        editor.putString(""+i,""+Home.PEN_COLOR);
                    }
                }
                editor.commit();
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
        //Home.PEN_COLOR = Home.SAVED_RECENT_COLORS[index];
        Home.PEN_COLOR = Integer.parseInt(preferences.getString(""+index,Color.WHITE+""));
        textView.setTextColor(Home.PEN_COLOR);
        textView.setText("#"+Integer.toHexString(Home.PEN_COLOR));
        alphaTileView.setPaintColor(Home.PEN_COLOR);
        imagesArray[index].setVisibility(View.VISIBLE);
    }
}