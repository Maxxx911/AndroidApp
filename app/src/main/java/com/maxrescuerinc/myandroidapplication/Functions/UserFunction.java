package com.maxrescuerinc.myandroidapplication.Functions;

import android.content.Context;
import android.content.SharedPreferences;

import com.maxrescuerinc.myandroidapplication.Models.User;
import com.orm.SugarRecord;

import java.util.Objects;


public class UserFunction {

    private final String APP_PREFERENCES = "current_user_setting";

    public static Long getIdCurrentUser(SharedPreferences mSetting){

        String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
        if(mSetting.contains(APP_PREFERENCES_CURRENT_USER_ID))
        {
            return mSetting.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
        }else {
            return (long) -1;
        }
    }

    public static void UpdateUser(Long userId, String name, String lastName, String phoneNumber, String rssLink){
        User user = SugarRecord.findById(User.class, userId);
        if(user!=null){
            if(name!= null){
                user.Name = name;
            }
            if(lastName!=null){
                user.LastName = lastName;
            }
            if(phoneNumber!=null){
                user.PhoneNumber = phoneNumber;
            }
            if(rssLink!=null){
                user.RssLink = rssLink;
            }
            user.save();
        }
    }

    public static void UpdateUserRssLink(Long userId, String rssLink){
        User user = SugarRecord.findById(User.class, userId);
        if(user!=null){
            if(rssLink!=null){
                user.RssLink = rssLink;
                user.save();
            }
        }
    }

}
