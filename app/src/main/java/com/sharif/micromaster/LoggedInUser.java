package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logged_in_user")
public class LoggedInUser {
    @PrimaryKey
    @NonNull
    private int userID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LoggedInUser(int userID) {
        this.userID = userID;
    }
}
