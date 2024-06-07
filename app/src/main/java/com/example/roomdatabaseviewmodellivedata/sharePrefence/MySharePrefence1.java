package com.example.roomdatabaseviewmodellivedata.sharePrefence;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharePrefence1 {
    private static final String MY_SHARE = "MY_SHARE";
    private static final String MY_SHARE1 = "MY_SHARE1";

    private Context context;

    public MySharePrefence1(Context context) {
        this.context = context;
    }
// lưu trạng thái favorite
    public void putBooleanValue(String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

   // lưu user favorite
    public void putStringValue(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public void removeStringValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
    public String getStringValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE1,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}
