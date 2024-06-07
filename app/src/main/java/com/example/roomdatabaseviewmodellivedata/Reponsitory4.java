package com.example.roomdatabaseviewmodellivedata;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reponsitory4 {

    private  UserDAO2 userDAO2;
    private UserDataBase3 userDataBase3;
//    private LiveData<List<User>> userList;
    ExecutorService executor;
    Handler handler;

    public Reponsitory4(Application application) {
        userDataBase3 = UserDataBase3.getInstance(application);
        userDAO2 = userDataBase3.getUserDAO2();
//        userList = userDAO2.getAllUser();
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public LiveData<List<User>> getAllUser() {
        return userDAO2.getAllUser();
    }
    public LiveData<List<User>> getSearchUser(String key){
        return userDAO2.getSearchUser(key);
    }
    public void insertUser(User user) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDAO2.insertUser(user);
            }
        });
    }
     public void updateUser(User user){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDAO2.updateUser(user);
            }
        });
     }
     public void deleteUser(User user){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDAO2.deleteUser(user);
            }
        });
     }
}
