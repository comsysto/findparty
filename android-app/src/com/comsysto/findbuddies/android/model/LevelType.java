package com.comsysto.findbuddies.android.model;

import android.content.Context;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 29.11.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public enum LevelType {

    BEGINNER(R.string.BEGINNER),
    AMATEUR(R.string.AMATEUR),
    PROFESSIONAL(R.string.PROFESSIONAL),
    EVERYBODY(R.string.EVERYBODY);

    private int displayStringId;

    LevelType(int displayStringId) {

        this.displayStringId = displayStringId;
    }


    public static String getDisplayString(Context context, String enumString) {
        LevelType levelType = LevelType.valueOf(enumString.toUpperCase());
        if (levelType != null) {
            return levelType.getDisplayString(context);
        }
        else
            return enumString;
    }

    public String getDisplayString(Context context) {
        return context.getString(this.displayStringId);
    };


    public static ArrayList getAllLevelsDisplayString(Context context){
        ArrayList<String> arrayList = new ArrayList<String>();
        for (LevelType levelType : LevelType.values()) {
            arrayList.add(levelType.getDisplayString(context));
        }
        return arrayList;
    }

}
