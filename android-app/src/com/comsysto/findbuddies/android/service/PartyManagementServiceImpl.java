package com.comsysto.findbuddies.android.service;

import android.util.Log;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.service.interceptor.ClientAuthenticationRequestInterceptor;
import com.comsysto.findbuddies.android.service.interceptor.NoCacheClientRequestInterceptor;
import com.comsysto.findbuddies.android.service.util.UrlBuilder;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;
import com.comsysto.findparty.web.PartyService;
import com.comsysto.findparty.web.PictureService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Implementation using {@link RestTemplate} from Spring to communicate with our
 * RestService to obtain Tasks.
 *
 * @author stefandjurasic
 * 
 */
public class PartyManagementServiceImpl {

    private static final String TAG = Constants.LOG_SERVICE_PREFIX + PartyManagementServiceImpl.class.getSimpleName();
    private RestTemplate restTemplate;
    private UrlBuilder urlBuilder;

    private final static String PICTURES_SERVICE_PATH = "/services/pictures";
    private final static String PARTY_SERVICE_PATH = "/services/parties";

    public PartyManagementServiceImpl(String host) {
        HttpComponentsClientHttpRequestFactory requestFactory = createHttpRequestFactory();

        this.restTemplate = new RestTemplate(true, requestFactory);
        NoCacheClientRequestInterceptor noCacheInterceptor = new NoCacheClientRequestInterceptor();
        ClientAuthenticationRequestInterceptor authInterceptor = new ClientAuthenticationRequestInterceptor();

        this.restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor[]{noCacheInterceptor, authInterceptor}));
        this.urlBuilder = new UrlBuilder(host);
    }

    private HttpComponentsClientHttpRequestFactory createHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(10000);

        return requestFactory;
    }



    public String createParty(Party party) {
        String url = urlBuilder.createFrom(PARTY_SERVICE_PATH);
        try {
            return restTemplate.postForEntity(url, party, String.class).getBody();
        } catch (Exception e) {
            Log.i("MY_PARTIES", "didn't create party: " + party, e);
            return null;
        }
    }

    public List<Party> searchParties(Double lon, Double lat, Double maxDistance) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, "search", String.valueOf(lon), String.valueOf(lat), String.valueOf(maxDistance));

        try {
            Log.d(TAG, "calling url: " + url);
            Party[] parties = restTemplate.getForObject(url, Party[].class);
            Log.d(TAG, "received "+ parties.length+ "parties: " + Arrays.asList(parties));
            return Arrays.asList(parties);
        } catch(Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean update(Party party) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, party.getId());
        try {
            restTemplate.put(url, party);
            return true;
        } catch (Exception e) {
            Log.i("MY_PARTIES", "Fehler beim updaten der Party: " +party);
            return false;
        }
    }

    public boolean deleteParty(String partyId) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, partyId);
        try {
            restTemplate.delete(url);
            return true;
        } catch(Exception e) {
            Log.i("MY_PARTIES", "Fehler beim Löschen der Party mit ID="+partyId);
            return false;
        }
    }

    public String createPartyImage(String username, byte[] content) {
        String url = urlBuilder.createUri(PICTURES_SERVICE_PATH, username);
        try {
            return restTemplate.postForObject(url, content, String.class);
        } catch(Exception e) {
            Log.i("MY_PARTIES", "Fehler beim Löschen des Bildes mit ID="+username);
        }
        return null;
    }

    public List<Party> getAllParties(String username) {
        ArrayList<Party> partyArrayList = new ArrayList<Party>();
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user", username);
            params.put("cache", String.valueOf(System.currentTimeMillis()));
            String url = urlBuilder.createUri(params, PARTY_SERVICE_PATH);
            Party[] response = restTemplate.getForObject(url, Party[].class);
            Collections.addAll(partyArrayList, response);
            return partyArrayList;
        } catch (Exception e) {
            Log.e(TAG, "unhandled exception : " + e.getMessage(), e);
            return null;
        }
    }

    public byte[] getPicture(String pictureUrl) {
        try {
            return restTemplate.getForObject(pictureUrl, byte[].class);
        } catch (Exception e) {
            Log.i("MY_PARTIES", "Fehler beim laden des Bildes mit url : " + pictureUrl, e);
            return null;
        }
    }
}
