package com.e.handmade_patterns.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {

    private static FileUtil mInstance;

    private FileUtil() {
    }

    public static FileUtil getInstance() {
        if (mInstance == null) {
            synchronized (FileUtil.class) {
                if (mInstance == null) {
                    mInstance = new FileUtil();
                }
            }
        }
        return mInstance;
    }

    public void storeBitmap(Bitmap bitmap, Context context) {
        boolean saved;
        OutputStream fos = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, String.format("%d",System.currentTimeMillis()));
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "Bead Designer");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "Bead Designer";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, String.format("%d",System.currentTimeMillis()) + ".png");
            try {
                fos = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));
        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        if (bitmap != null)
            Toast.makeText(context, "Hello kaream", Toast.LENGTH_LONG).show();
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/"+String.format("%d.JPEG",System.currentTimeMillis());
        File imageFile = new File(filePath);
        imageFile.getParentFile().mkdirs();
        try {
            OutputStream fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // this line to refresh the gallery
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));
    }
}