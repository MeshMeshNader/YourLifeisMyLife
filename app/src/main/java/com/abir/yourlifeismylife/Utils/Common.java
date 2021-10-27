package com.abir.yourlifeismylife.Utils;

import com.abir.yourlifeismylife.DataModels.UserDataModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {


    public static final String PUBLIC_LOCATION = "PublicLocation";
    public static final String USER_UID_SAVED_KEY = "SaveUid";
    public static final String USERS_INFORMATION = "Users";
    public static final String USER_CIRCLE_MEMBERS = "CircleMembers";
    public static UserDataModel loggedUser;
    public static UserDataModel trackingUser;


    public static Date convertTimeStampToDate(long time) {
        return new Date(new Timestamp(time).getTime());
    }

    public static String getDateFormatted(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
    }
}
