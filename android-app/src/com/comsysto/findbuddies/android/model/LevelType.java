package com.comsysto.findbuddies.android.model;

import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

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

    private String displayName;

    LevelType(int displayStringId) {
        this.displayName = PartyManagerApplication.getInstance().getString(displayStringId);
    }

    public String getDisplayName() {
        return this.displayName;
    };



    public static LevelType getForDisplayName(String name) {
        for(LevelType type : values()) {
            if(type.getDisplayName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No LevelType for displayName: " + name);
    }

}
