package com.example.roomdatabaseviewmodellivedata.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomdatabaseviewmodellivedata.model.User;

import java.util.List;

@Dao
public interface UserDAO2 {
    @Insert
    void insertUser(User user);
    @Update
    void updateUser(User user);
    @Delete
    void deleteUser(User user);
    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUser();

    @Query("SELECT * FROM user_table WHERE user_name LIKE '%' || :name || '%'")
    LiveData<List<User>> getSearchUser(String name);
}
