package com.example.plant_app.util;

import android.content.Context;
import android.widget.Toast;

public class MsgUtil {

    public static void toast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
