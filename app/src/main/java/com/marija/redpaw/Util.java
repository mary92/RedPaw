package com.marija.redpaw;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by demouser on 8/6/15.
 */
public class Util {
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String bytes = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return bytes;
    }
}
