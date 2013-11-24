package com.comsysto.findbuddies.android.service.async.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findparty.Picture;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class GetUserPictureAsync extends AsyncTask<Void, Void, Bitmap> {

    private final GetUserPictureCallback callback;
    private final String userName;

    public GetUserPictureAsync(GetUserPictureCallback callback, String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("You have to pass a username.");
        }

        this.callback = callback;
        this.userName = userName;

    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Picture userPicture = PartyManagerApplication.getInstance().getUserPicture(userName);
        if(userPicture != null){
            return BitmapFactory.decodeByteArray(userPicture.getContent(), 0, userPicture.getContent().length);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap picture) {
        if (picture != null) {
            callback.successOnGetUserPicture(picture, userName);
        } else {
            callback.errorOnGetUserPicture();
        }

    }
}
