package com.manacher.hammer.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;

import com.manacher.hammer.Activities.SplashActivity;
import com.manacher.hammer.R;

import java.util.ArrayList;
import java.util.List;

public class Util {


    public static void vibrate(Activity activity){
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    public static List<Drawable> getBottleList(Activity activity){
        List<Drawable> list = new ArrayList<>();

        list.add(activity.getDrawable(R.drawable.b1));
        list.add(activity.getDrawable(R.drawable.b2));
        list.add(activity.getDrawable(R.drawable.b3));
        list.add(activity.getDrawable(R.drawable.b4));
        list.add(activity.getDrawable(R.drawable.b5));
        list.add(activity.getDrawable(R.drawable.b6));
        list.add(activity.getDrawable(R.drawable.b7));
        list.add(activity.getDrawable(R.drawable.b8));
        list.add(activity.getDrawable(R.drawable.b9));
        list.add(activity.getDrawable(R.drawable.b10));
        list.add(activity.getDrawable(R.drawable.b11));

        return list;
    }

    public static List<Integer> getPlayersNumberList(){
        List<Integer> list = new ArrayList<>();


        for(int i = 2; i <= 10; i++){

            list.add(i);

        }

        return list;
    }

    public static void setWidthHeight(View layout, int width, int height){
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                /*width*/ width,
                /*height*/ height
        );

        layout.setLayoutParams(param);
    }

    public static String pureName(String name){
        name = name.toLowerCase();

        String str = String.valueOf(name.charAt(0));

        str = str.toUpperCase();

        return str + name.substring(1);

    }
}
