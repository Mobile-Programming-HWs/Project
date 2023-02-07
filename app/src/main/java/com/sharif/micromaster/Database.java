package com.sharif.micromaster;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {User.class, Course.class, Homework.class, LoggedInUser.class, TA.class}, version = 6, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract UserDao UserDao();

    public abstract CourseDao CourseDao();

    public abstract LoggedInUserDao LoggedInUserDao();

    public abstract HomeworkDao HomeworkDao();

    public abstract TADao TADao();

    private static volatile Database INSTANCE;

    static Database getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    Database.class, "database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
