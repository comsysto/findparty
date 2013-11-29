package com.comsysto.findbuddies.android.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import com.comsysto.findbuddies.android.account.AccountService;
import com.comsysto.findbuddies.android.activity.StartActivity;
import com.comsysto.findbuddies.android.service.PartyManagementServiceImpl;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link PartyManagerApplication} holds relevant stuff for the whole app .
 * 
 * Currently:
 * <ul>
 * 	<li>caches the Tasks</li>
 * 	<li>holds the currently selectedParty (e.g. when editing a Task)</li>
 *  <li>delegates update,get,create,delete calls to the {@link com.comsysto.findbuddies.android.service.PartyManagementServiceImpl}</li>
 *  <li>whether online/offline selects a corresponding {@link com.comsysto.findbuddies.android.service.PartyManagementServiceImpl}</li>
 * 
 * @author stefandjurasic
 *
 */
public class PartyManagerApplication extends Application {

    private static final String CLOUD_HOST =  "snuggle.eu01.aws.af.cm";
    private static final String LOCAL_EMULATOR = "10.0.2.2:8080";
    private static final String LOCAL_STEFAN = "192.168.178.52:8080";
    private static final String LOCAL_ROB = "192.168.178.65:8080";

    private static final String API_KEY = "U251Z2dsZU1lIGlzIGF3ZXNvbWUh";


    private static final String TAG = Constants.LOG_APP_PREFIX + PartyManagerApplication.class.getSimpleName();

    private Party selectedParty;

	private PartyManagementServiceImpl partyService;

    private AccountService accountService;


    private static PartyManagerApplication instance;

    public static PartyManagerApplication getInstance() {
        return instance;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    @Override
	public void onCreate() {
        accountService = new AccountService(this.getApplicationContext());
        partyService = new PartyManagementServiceImpl(CLOUD_HOST);
        PartyManagerApplication.instance = this;
    }

	public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()
				&& activeNetworkInfo.isConnected()) {
            Log.d(TAG, "device connected successfully to network");
			return true;
		} else {
            Log.d(TAG, "device not connected to network");
			return false;
		}
	}


    public String getUsername() {
        return accountService.getUsername();
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public Party getSelectedParty() {
        return selectedParty;
    }

    public void setSelectedParty(Party selectedParty) {
        this.selectedParty = selectedParty;
    }

    public String uploadPicture(Bitmap resizedBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return this.partyService.createPartyImage(getUsername(), stream.toByteArray());
    }

    public Bitmap loadPicture(String pictureUrl) {
        byte[] picture = partyService.getPicture(pictureUrl);
        if (picture != null) {
            return BitmapFactory.decodeByteArray(picture, 0, picture.length);
        }
        return null;
    }

    public void deleteUserPicture() {
        //TODO
    }

    public void goToStart(Activity activity) {
        Intent intent = new Intent(activity, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public String createParty(Party toBeUpdatedParty) {
        return partyService.createParty(toBeUpdatedParty);
    }

    public void update(Party toBeUpdatedParty) {
        partyService.update(toBeUpdatedParty);
    }

    public void deleteParty(String id) {
        partyService.deleteParty(id);
    }


    public List<Party> searchParties(double longitude, double latitude, Double searchDistance) {
        return partyService.searchParties(longitude, latitude, searchDistance);
    }

    public List<Party> getAllParties(String userName) {
        return partyService.getAllParties(userName);
    }

    public String getUserImageUrl() {
        return getAccountService().getUserImageUrl();
    }

    public float getDeviceDensity() {
        return getResources().getDisplayMetrics().density;
    }


    public boolean isGooglePicture(String pictureUrl) {
        if (pictureUrl == null) {
            return false;
        }
        return pictureUrl.contains("googleusercontent");
    }

    public boolean isLocaleGerman() {
        Locale current = getResources().getConfiguration().locale;
        if(current == Locale.GERMAN){
            return true;
        }else{
            return false;
        }
    }

    public SimpleDateFormat getSimpleDateFormat(){
        if(isLocaleGerman()){
            return new SimpleDateFormat("dd.MM.yyyy");
        }else{
            return new SimpleDateFormat();
        }
    }

}
