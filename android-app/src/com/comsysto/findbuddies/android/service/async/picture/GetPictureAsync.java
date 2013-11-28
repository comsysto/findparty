package com.comsysto.findbuddies.android.service.async.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class GetPictureAsync extends AsyncTask<Void, Void, Bitmap> {

    private final GetPictureCallback callback;
    private final String pictureUrl;
    private final static int DEFAULT_PICTURE_DENSITIY = 100;

    public GetPictureAsync(GetPictureCallback callback, String pictureUrl) {
        if (pictureUrl == null) {
            throw new IllegalArgumentException("You have to pass a pictureUrl.");
        }

        this.callback = callback;
        this.pictureUrl = pictureUrl;

    }

    private String addSize(String pictureUrl) {
        String sizeParameter = "?sz=";
        if (pictureUrl.contains("?sz=")) {
            sizeParameter = "&sz=" +
                    (int)(DEFAULT_PICTURE_DENSITIY * PartyManagerApplication.getInstance().getDeviceDensity());
        }
        return pictureUrl + sizeParameter;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            return getPicture(pictureUrl);
        } catch (IOException e) {
            Log.e(GetPictureAsync.class.getName(), "error getting picture", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap picture) {
        if (picture != null) {
            callback.successOnGetPicture(picture, pictureUrl);
        } else {
            callback.errorOnGetPicture();
        }
    }


    private Bitmap getPicture(String pictureUrl) throws IOException {
        String pictureUrlWithSize = addSize(pictureUrl);
        if (PartyManagerApplication.getInstance().isGooglePicture(pictureUrl)) {
            URL url = new URL(pictureUrlWithSize);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        return PartyManagerApplication.getInstance().loadPicture(pictureUrlWithSize);
    }

}
