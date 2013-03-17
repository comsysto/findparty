package com.comsysto.findbuddies.android.model;

import com.comsysto.dalli.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public enum CategoryType {

    SNUGGLING(R.drawable.snuggling), BIKING(R.drawable.biking), JOGGING(R.drawable.jogging),CLUBBING(R.drawable.clubbing), MUSIC(R.drawable.music), HIKING(R.drawable.hiking), SWIMMING(R.drawable.androidmarker);


    int drawableId;

    CategoryType(int drawableId) {
        this.drawableId = drawableId;
    }

    public static List<String> names() {
        ArrayList list = new ArrayList();

        for (CategoryType type : values()) {
            list.add(type.name());
        }
        return list;
    }

    public int getDrawableId() {
        return drawableId;
    }


}
