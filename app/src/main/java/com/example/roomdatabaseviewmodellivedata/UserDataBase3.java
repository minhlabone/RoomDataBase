package com.example.roomdatabaseviewmodellivedata;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1)


public abstract class UserDataBase3 extends RoomDatabase {
//    static Migration migration_1_2 = new Migration(1,2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
//               supportSQLiteDatabase.execSQL("ALTER TABLE user_table ALTER COLUMN user_image String" );
//        }
//    };
    public abstract UserDAO2 getUserDAO2();

    private static UserDataBase3 dbInstance;

    public static synchronized UserDataBase3 getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(),UserDataBase3.class,"contex_table")
                    .fallbackToDestructiveMigration().build();
        }
        return dbInstance;
    }
}
