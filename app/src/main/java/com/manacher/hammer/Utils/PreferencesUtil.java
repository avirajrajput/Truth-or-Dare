package com.manacher.hammer.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class PreferencesUtil {
    SharedPreferences  mPrefs;
    String name = "HAMMER";

    public PreferencesUtil(Activity activity){
        this.mPrefs = activity.getSharedPreferences(name,
                MODE_PRIVATE);

    }

    public boolean save(Object obj, String key){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        prefsEditor.putString(key, json);
        return prefsEditor.commit();
    }

    public Object load(String key){
        Gson gson = new Gson();
        String json = mPrefs.getString(key, "");
        return gson.fromJson(json, Object.class);
    }

    public boolean saveBoolean(boolean bool, String key){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, bool);
        return editor.commit();
    }

    public boolean saveInt(Integer integer, String key){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(key, integer);
        return editor.commit();
    }

    public int loadInt(String key){
        return mPrefs.getInt(key, 1);
    }
}
