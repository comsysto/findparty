package com.comsysto.dalli.android.service;

import java.util.*;

import android.util.Log;
import com.comsysto.findparty.Category;
import com.comsysto.findparty.web.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.comsysto.dalli.android.service.util.UrlBuilder;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.web.PartyService;

/**
 * Implementation using {@link RestTemplate} from Spring to communicate with our
 * RestService to obtain Tasks.
 *
 * @author stefandjurasic
 * 
 */
public class PartyManagementServiceImpl implements PartyService, CategoryService {

    private RestTemplate restTemplate;
    private UrlBuilder urlBuilder;

    private final static String PARTY_SERVICE_PATH = "/services/parties";
    private final static String CATEGORY_SERVICE_PATH = "/services/category";

    private final static String SUBSCRIPTIONS = "subscriptions";

    public PartyManagementServiceImpl(String host) {
        HttpComponentsClientHttpRequestFactory requestFactory = createHttpRequestFactory();

        this.restTemplate = new RestTemplate(true, requestFactory);
        this.restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new NoCacheClientRequestInterceptor()));
        this.urlBuilder = new UrlBuilder(host);
    }

    private HttpComponentsClientHttpRequestFactory createHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2000);
        return requestFactory;
    }

    @Override
    public void cancelParty(String partyId, String username) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "cancel");
        String url = urlBuilder.createUri(params, PARTY_SERVICE_PATH, partyId, SUBSCRIPTIONS);

        restTemplate.put(url, username);
    }

    @Override
    public String createParty(Party party) {
        String url = urlBuilder.createFrom(PARTY_SERVICE_PATH);
        try {
            return restTemplate.postForEntity(url, party, String.class).getBody();
        } catch (Exception e) {
            Log.i("MY_PARTIES", "didn't create party: " + party, e);
            return null;
        }
    }

    @Override
    public void joinParty(String partyId, String username) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, partyId, SUBSCRIPTIONS);

        restTemplate.put(url, username);
    }

    @Override
    public List<Party> searchParties(Double lon, Double lat, Double maxDistance) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, "search", String.valueOf(lon), String.valueOf(lat), String.valueOf(maxDistance));

        
        Party[] response = restTemplate.getForObject(url, Party[].class);

        return Arrays.asList(response);
    }

    @Override
    public Party showDetails(String partyId) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, partyId);
        try {
            return restTemplate.getForObject(url, Party.class);
        } catch (Exception e) {
            Log.i("MY_PARTIES", "Fehler beim Löschen der Party mit ID="+partyId);
            return null;
        }
    }

    @Override
    public void update(Party party) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, party.getId());
        try {
            restTemplate.put(url, party);
        } catch (Exception e) {
            Log.i("MY_PARTIES", "Fehler beim updaten der Party: " +party);
        }
    }

    @Override
    public void delete(String partyId) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, partyId);
        try {
            restTemplate.delete(url);
        } catch(Exception e) {
            Log.i("MY_PARTIES", "Fehler beim Löschen der Party mit ID="+partyId);
        }
    }

    
    @Override
    public List<Party> getAllParties(String username) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", username);
        
        String url = urlBuilder.createUri(params, PARTY_SERVICE_PATH);

        Party[] response = restTemplate.getForObject(url, Party[].class);
        //Party[] response = new RestTemplate(true, createHttpRequestFactory()).getForObject(url, Party[].class);
        return Arrays.asList(response);
    }

    @Override
    public String echo(String arg0) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, "echo", arg0);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        
        return forEntity.getBody();
    }

    @Override
    public Set<Category> getAllCategories() {
        String url = urlBuilder.createUri(CATEGORY_SERVICE_PATH, "getall");
        ResponseEntity<Set> forEntity = restTemplate.getForEntity(url, Set.class);

        return forEntity.getBody();
    }
}
