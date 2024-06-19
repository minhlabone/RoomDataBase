package com.example.roomdatabaseviewmodellivedata.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roomdatabaseviewmodellivedata.reponsitory.Reponsitory4;
import com.example.roomdatabaseviewmodellivedata.model.User;

import java.util.List;

public class ViewModel5 extends AndroidViewModel {
    private Reponsitory4 reponsitory4;
    private LiveData<List<User>> allUser;


    public ViewModel5(@NonNull Application application) {
        super(application);
        // lấy ra reponitory đ truy cập vào table
        this.reponsitory4 = new Reponsitory4(application);
    }

    public LiveData<List<User>> getAllUser() {
        allUser = reponsitory4.getAllUser();
        return allUser;
    }
    public LiveData<List<User>> getSearchUser(String key){
        allUser = reponsitory4.getSearchUser(key);
        return allUser;
    }

    public void addNewUser(User user) {
        reponsitory4.insertUser(user);
    }

    public void deleteUser(User user) {
        reponsitory4.deleteUser(user);
    }

    public void updateUser(User user) {
        reponsitory4.updateUser(user);
    }
}
