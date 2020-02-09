package com.f10company.barcodewidgetf10;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ArrayListSaveByJson extends AppCompatActivity {
    void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = context.getSharedPreferences("sFile", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("sFile", context.MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    /*현재 디스플레이에 띄어져있는 화면 관련 입출력*/
    int getNowPosition(Context context, String key)
    {
        SharedPreferences prefs = context.getSharedPreferences("sFile", context.MODE_PRIVATE);
        return prefs.getInt(key,0);
    }

    void setNowPosition(Context context, String key,int position)
    {
        SharedPreferences prefs = context.getSharedPreferences("sFile", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key,position);
        editor.commit();
    }
}
