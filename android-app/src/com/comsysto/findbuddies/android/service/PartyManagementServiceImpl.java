package com.comsysto.findbuddies.android.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.util.Log;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.account.AccountAuthenticator;
import com.comsysto.findbuddies.android.service.interceptor.ClientAuthenticationRequestInterceptor;
import com.comsysto.findbuddies.android.service.interceptor.NoCacheClientRequestInterceptor;
import com.comsysto.findbuddies.android.service.util.UrlBuilder;
import com.comsysto.findparty.Category;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;
import com.comsysto.findparty.web.CategoryService;
import com.comsysto.findparty.web.PartyService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Implementation using {@link RestTemplate} from Spring to communicate with our
 * RestService to obtain Tasks.
 *
 * @author stefandjurasic
 * 
 */
public class PartyManagementServiceImpl implements PartyService, CategoryService {

    private static final String TAG = Constants.LOG_SERVICE_PREFIX + PartyManagementServiceImpl.class.getSimpleName();
    private RestTemplate restTemplate;
    private UrlBuilder urlBuilder;

    private final static String USER_SERVICE_PATH = "/services/users";
    private final static String PARTY_SERVICE_PATH = "/services/parties";
    private final static String CATEGORY_SERVICE_PATH = "/services/category";

    private final static String SUBSCRIPTIONS = "subscriptions";

    public PartyManagementServiceImpl(String host, PartyManagerApplication application) {
        HttpComponentsClientHttpRequestFactory requestFactory = createHttpRequestFactory();

        this.restTemplate = new RestTemplate(true, requestFactory);
        NoCacheClientRequestInterceptor noCacheInterceptor = new NoCacheClientRequestInterceptor();
        ClientAuthenticationRequestInterceptor authInterceptor = new ClientAuthenticationRequestInterceptor(application);

        this.restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(noCacheInterceptor, authInterceptor));
        this.urlBuilder = new UrlBuilder(host);
    }

    private User getUser(PartyManagerApplication application) {
        AccountManager accountManager = AccountManager.get(application);
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        if(accounts.length>0) {
            Account account = accounts[0];
            User user = new User();
            user.setUsername(account.name);
            user.setPassword(accountManager.getPassword(account));
            return user;
        }
        return null;
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

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Basic cm9iOm51bGw=\n");
//        ResponseEntity<Party[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Party[].class);


        try {
            Log.d(TAG, "calling url: " + url);
            Party[] parties = restTemplate.getForObject(url, Party[].class);
            Log.d(TAG, "received parties: " + parties);
            return Arrays.asList(parties);
        } catch(Exception e) {
            Log.d(TAG, "Exception: " +e.getMessage(), e);
        }
        return null;
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
        ArrayList<Party> partyArrayList = new ArrayList<Party>();
        Collections.addAll(partyArrayList, response);
        return partyArrayList;
    }

    @Override
    public String echo(String arg0) {
        String url = urlBuilder.createUri(PARTY_SERVICE_PATH, "echo", arg0);
        Log.d(TAG, "echoing server: " + url);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        
        return forEntity.getBody();
    }

    @Override
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        ResponseEntity<User> entity = restTemplate.postForEntity(urlBuilder.createFrom(USER_SERVICE_PATH), user, User.class);

        Log.d(TAG, "create user response: " + entity.getStatusCode().value() + "-" + entity.getStatusCode().name() + " -> Body: " + entity.getBody());

        return entity.getBody();
    }

    @Override
    public User getUser(String username) {
        return restTemplate.getForObject(urlBuilder.createUri(USER_SERVICE_PATH+"?username=" + username), User.class);
    }

    @Override
    public List<User> getAllUsers() {
        return Arrays.asList(restTemplate.getForObject(urlBuilder.createUri(USER_SERVICE_PATH), User[].class));
    }

    @Override
    public Boolean login(User user) {
        return restTemplate.postForObject(urlBuilder.createUri(USER_SERVICE_PATH, "login"), user, Boolean.class);
    }

    @Override
    public void update(User user) {
        String url = urlBuilder.createUri(USER_SERVICE_PATH, user.getId());
        try {
            restTemplate.put(url, user);
        } catch (Exception e) {
            Log.i("MY_PARTIES", "Fehler beim updaten des Users: " + user);
        }
    }

    @Override
    public Set<Category> getAllCategories() {
        String url = urlBuilder.createUri(CATEGORY_SERVICE_PATH, "getall");
        ResponseEntity<Set> forEntity = restTemplate.getForEntity(url, Set.class);

        return forEntity.getBody();
    }
}
