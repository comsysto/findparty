package com.comsysto.dalli.android.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import org.springframework.http.ResponseEntity;
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
public class PartyManagementServiceImpl implements PartyService {

    private RestTemplate restTemplate;
    private UrlBuilder urlBuilder;

    private final static String PARTY_SERVICE_PATH = "/services/parties";
    private final static String SUBSCRIPTIONS = "subscriptions";

    public PartyManagementServiceImpl(String host) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);

        this.restTemplate = new RestTemplate(true, requestFactory);
        
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        
        
        
        this.urlBuilder = new UrlBuilder(host);
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
        ResponseEntity<String> postForEntity;
        try {
            return restTemplate.postForEntity(url, party, String.class).getBody();
        } catch (Exception e) {
            Log.e("createpartyPostEntity", "didn't create", e);
        }
        try {
            return restTemplate.postForObject(url, party, String.class);
        }
         catch (Exception e) {
            Log.e("createpartyPostLocation", "didn't create", e);
        }

        return null;
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

        return restTemplate.getForObject(url, Party.class);
    }

    @Override
    public void update(Party party) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, party.getId());

        restTemplate.put(url, party);
    }

    @Override
    public void delete(String partyId) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, partyId);
        
        restTemplate.delete(url);
    }

    
    @Override
    public List<Party> getAllParties(String username) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", username);
        
        String url = urlBuilder.createUri(params, PARTY_SERVICE_PATH);
        
        Party[] response = restTemplate.getForObject(url, Party[].class);
        return Arrays.asList(response);
    }

    @Override
    public String echo(String arg0) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, "echo", arg0);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        
        return forEntity.getBody();
    }

}
