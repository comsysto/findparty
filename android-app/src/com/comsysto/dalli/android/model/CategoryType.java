package com.comsysto.dalli.android.model;

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

    SNUGGLING, BIKING, JOGGING,CLUBBING, MUSIC, HIKING, SWIMMING;

    public static List<String> names() {
        ArrayList list = new ArrayList();

        for (CategoryType type : values()) {
            list.add(type.name());
        }
        return list;
    }


}
