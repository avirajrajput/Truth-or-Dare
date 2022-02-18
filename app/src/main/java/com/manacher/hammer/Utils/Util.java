package com.manacher.hammer.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.manacher.hammer.R;
import com.manacher.hammer.models.MessageModel;
import com.manacher.hammer.models.User;
import org.webrtc.DataChannel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
    public static User USER = new User();
    public static User OTHER;
    public static List<MessageModel> messages;

    public static String WOMEN_DP = "https://firebasestorage.googleapis.com/v0/b/truth-or-dare-application.appspot.com/o/default_dp%2Ficons8-female-profile-96.png?alt=media&token=1ea3cf36-31ce-48c2-8a2f-21daf2b7cd2e";
    public static String MEN_DP = "https://firebasestorage.googleapis.com/v0/b/truth-or-dare-application.appspot.com/o/default_dp%2Ficons8-male-user-96.png?alt=media&token=67babd65-3e51-444e-a53b-ff0743e88fac";

    @SuppressLint("UseCompatLoadingForDrawables")
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

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getRandom(){
        return String.valueOf(new Random().nextInt(1000));
    }

    public static DataChannel.Buffer serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
            return new DataChannel.Buffer(ByteBuffer.wrap(yourBytes), false);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static Object deserialize(DataChannel.Buffer dataBuffer)
            throws IOException, ClassNotFoundException {

        ByteBuffer buffer = dataBuffer.data;
        byte[] bytes;

        if (buffer.hasArray()) {
            bytes = buffer.array();

        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static char[] c = new char[]{'K', 'M', 'B', 'T'};
    public static String coolFormat(double n, int iteration) {
        if(n < 1000){
            return String.valueOf(n);
        }

        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;
        return d < 1000 ? (d > 99.9 || isRound || d > 9.99 ? (int) d * 10 / 10 : d + "") + "" + c[iteration] : coolFormat(d, iteration + 1);
    }


    public static void sendInvitation(Activity activity) {

        try {

            String invitationLink = "https://play.google.com/store/apps/details?id=com.manacher.hammer";
            Intent shareIntent =
                    new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey I am using this amazing app: "+activity.getString(R.string.app_name));
            String shareMessage =
                    "Hey I am using this amazing app: \n\nInstall "+activity.getString(R.string.app_name)+" app from store."
                            + invitationLink;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));


        } catch(Exception e) {
            //e.toString();

        }
    }
}
