package com.e.handmade_patterns.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.e.handmade_patterns.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Help {
    private Context context;
    private Activity activity;
    private AppCompatActivity appCompatActivity;
    private Bitmap bitmap;

    public Help(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.appCompatActivity = (AppCompatActivity) activity;
    }

    public void saveAndTakeScreenShot() {
        bitmap = ScreenshotUtil.getInstance().takeScreenshotForScreen(activity);
        requestPermissionAndSave();
    }

    private void requestPermissionAndSave() {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (bitmap != null) {
                            FileUtil.getInstance().storeBitmap(bitmap,context);
                            Toast.makeText(context,"The screenshot is saved successfully to your Gallery", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed during saving", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Toast.makeText(context, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public boolean showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appCompatActivity.getSupportFragmentManager().popBackStack();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        return true;
    }

    public boolean showReloadDialog(final Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Do you want to clear the screen?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appCompatActivity.getSupportFragmentManager().popBackStack();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout, fragment)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(context, "The screen is cleared successfully", Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        return true;
    }
}
