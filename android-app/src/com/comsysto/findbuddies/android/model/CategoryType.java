package com.comsysto.findbuddies.android.model;

import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

/**
 * User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:57
 */
public enum CategoryType {

    SNUGGLING(R.drawable.snuggling, R.string.CATEGORY_SNUGGLING),
    BIKING(R.drawable.biking, R.string.CATEGORY_BIKING),
    JOGGING(R.drawable.jogging, R.string.CATEGORY_JOGGING),
    CLUBBING(R.drawable.clubbing, R.string.CATEGORY_CLUBBING),
    HIKING(R.drawable.hiking, R.string.CATEGORY_HIKING),
    SWIMMING(R.drawable.swimming, R.string.CATEGORY_SWIMMING),
    SOCCER(R.drawable.soccer, R.string.CATEGORY_SOCCER),
    DANCING(R.drawable.dancing, R.string.CATEGORY_DANCING);

    private String displayName;
    private int drawableId;

    CategoryType(int drawableId, int nameId) {
        this.drawableId = drawableId;
        this.displayName = PartyManagerApplication.getInstance().getString(nameId);
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CategoryType getForDisplayName(String name) {
        for(CategoryType type : CategoryType.values()) {
            if(type.getDisplayName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("no CategoryType for displayName: " +name);
    }


}
