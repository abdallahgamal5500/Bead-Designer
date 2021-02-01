package com.e.handmade_patterns.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.e.handmade_patterns.R;
import com.e.handmade_patterns.helper.Help;
import com.e.handmade_patterns.ui.Communicator;
import com.e.handmade_patterns.ui.Home;
import com.e.handmade_patterns.ui.IOnBackPressed;


public class FragmentRaw1 extends Fragment implements View.OnClickListener, IOnBackPressed {

    private Communicator communicator;
    private ImageView tools_pen,tools_eraser,tools_palette,save_btn,toolbar_reload;
    private Help help;
    private View view;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LinearLayout raw1_tools_layout;

    public FragmentRaw1() {
    }

    public static FragmentRaw1 getInstance() {
        FragmentRaw1 fragmentRaw1 = null;
        if (fragmentRaw1 == null)
            fragmentRaw1 = new FragmentRaw1();
        return fragmentRaw1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_raw1, container, false);

        tools_pen = view.findViewById(R.id.tools_pen);
        tools_eraser = view.findViewById(R.id.tools_eraser);
        tools_palette = view.findViewById(R.id.tools_palette);
        save_btn = view.findViewById(R.id.save_btn);
        progressBar = view.findViewById(R.id.raw1_progress);
        toolbar_reload = view.findViewById(R.id.toolbar_reload);
        raw1_tools_layout = view.findViewById(R.id.raw1_tools_layout);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (Home.TOOLS_STATE[0]) {
            tools_pen.setBackgroundColor(Home.CLICK_COLOR);
            tools_eraser.setBackgroundColor(Home.COLOR_ACCENT);
        } else
            tools_pen.setBackgroundColor(Home.CLICK_COLOR);

        if (Home.TOOLS_STATE[1]) {
            tools_eraser.setBackgroundColor(Home.CLICK_COLOR);
            tools_pen.setBackgroundColor(Home.COLOR_ACCENT);
        }

        tools_palette.setOnClickListener(this);
        tools_eraser.setOnClickListener(this);
        tools_pen.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        toolbar_reload.setOnClickListener(this);

        for (int i=1;i<=900;i++) {
            int id = getResources().getIdentifier("r1_view"+i, "id", getContext().getPackageName());
            View temp = view.findViewById(id);
            temp.setBackgroundColor(Integer.parseInt(preferences.getString(""+id, Color.WHITE+"")));
            temp.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        communicator = (Communicator) context;
        help = new Help(context,getActivity());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.tools_palette && v.getId() != R.id.tools_eraser && v.getId() != R.id.tools_pen && v.getId() != R.id.save_btn && v.getId() != R.id.toolbar_reload && v.getId() != R.id.raw1_tools_layout) {
            View temp = v.findViewById(v.getId());
            temp.setBackgroundColor(Home.CURRENT_COLOR);
            editor.putString(v.getId()+"", Home.CURRENT_COLOR+"");
            editor.commit();
            if (Home.TOOLS_STATE[2]) {
                Home.TOOLS_STATE[0] = true;
                Home.TOOLS_STATE[1] = false;
                tools_pen.setBackgroundColor(Home.CLICK_COLOR);
                tools_eraser.setBackgroundColor(Home.COLOR_ACCENT);
                Home.TOOLS_STATE[2] = false;
            }
        } else {
            switch (v.getId()) {
                case R.id.tools_palette:
                    communicator.showFragment(FragmentPalette.getInstance());
                    break;
                case R.id.tools_eraser:
                    tools_pen.setBackgroundColor(Home.COLOR_ACCENT);
                    tools_eraser.setBackgroundColor(Home.CLICK_COLOR);
                    Home.CURRENT_COLOR = Home.DELETE_COLOR;
                    Home.TOOLS_STATE[0] = false;
                    Home.TOOLS_STATE[1] = true;
                    break;
                case R.id.tools_pen:
                    tools_pen.setBackgroundColor(Home.CLICK_COLOR);
                    tools_eraser.setBackgroundColor(Home.COLOR_ACCENT);
                    Home.CURRENT_COLOR = Home.PEN_COLOR;
                    Home.TOOLS_STATE[0] = true;
                    Home.TOOLS_STATE[1] = false;
                    break;
                case R.id.save_btn:
                    showProgress(View.VISIBLE);
                    raw1_tools_layout.setVisibility(View.GONE);
                    help.saveAndTakeScreenShot();
                    showProgress(View.INVISIBLE);
                    raw1_tools_layout.setVisibility(View.VISIBLE);
                    break;
                case R.id.toolbar_reload:
                    for (int i=1;i<=900;i++) {
                        int id = getResources().getIdentifier("r1_view"+i, "id", getContext().getPackageName());
                        editor.remove(""+id);
                    }
                    editor.commit();
                    help.showReloadDialog(FragmentRaw1.getInstance());
                    break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return help.showAlertDialog();
    }

    private void showProgress(int state) {
        progressBar.setVisibility(state);
    }
}