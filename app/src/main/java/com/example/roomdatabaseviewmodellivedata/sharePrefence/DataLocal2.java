package com.example.roomdatabaseviewmodellivedata.sharePrefence;

import android.content.Context;

import com.example.roomdatabaseviewmodellivedata.model.User;
import com.google.gson.Gson;

public class DataLocal2 {
    private static final String FAVORITE ="FAVORITE" ;
    private static final String FAVORITE1 ="FAVORITE1" ;

    public static DataLocal2 instance;
    private MySharePrefence1 mySharePrefence1;

    public static void init(Context context){
        instance = new DataLocal2();
        instance.mySharePrefence1 = new MySharePrefence1(context);
    }
    public static  DataLocal2 getInstance(){
        if(instance == null){
            instance = new DataLocal2();
        }
        return instance;
    }
    public static void setSaveFavorite(boolean check,int pos){
        DataLocal2.getInstance().mySharePrefence1.putBooleanValue(FAVORITE +pos,check);
    }
    public static boolean getFavorite(int pos){
        return DataLocal2.getInstance().mySharePrefence1.getBooleanValue(FAVORITE+pos);
    }
    //object
    public static  void setObjectFavorite(User user,int pos,boolean check){
        Gson gson = new Gson();
        String strJsonUser = gson.toJson(user);
         if(check == true){
             DataLocal2.getInstance().mySharePrefence1.putStringValue(FAVORITE1+pos,strJsonUser);
         }else {
             DataLocal2.getInstance().mySharePrefence1.removeStringValue(FAVORITE1+pos);
         }
    }
    public static  User getUser(int pos){
        String strJson = DataLocal2.getInstance().mySharePrefence1.getStringValue(FAVORITE1+pos);
        Gson gson = new Gson();
        User user = gson.fromJson(strJson, User.class);
        return  user;
    }
}
