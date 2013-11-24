package com.comsysto.findbuddies.android.service.async.picture;

import android.graphics.Bitmap;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public interface GetUserPictureCallback {

    void successOnGetUserPicture(Bitmap bitmap, String userName);
    void errorOnGetUserPicture();
}
