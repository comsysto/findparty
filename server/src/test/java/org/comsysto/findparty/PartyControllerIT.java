package org.comsysto.findparty;

import com.comsysto.findparty.CategoryType;
import com.comsysto.findparty.LevelType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 06.09.12
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/comsysto/findparty/spring-test.xml")
public class PartyControllerIT {

    @Autowired
    private RestTemplate resttemplate;

    @Test
    public void createPartytest(){
       // resttemplate.getForObject("localhost:8080")
    }

    @Test
    public void searchParties(){
        Party party2 = new Party();
        party2.setSize(3);
        party2.setCategory(CategoryType.BIKING.name());
        party2.setLevel(LevelType.BEGINNER.name());
        Point location = new Point();
        location.setLon(11.53144);
        location.setLat(48.1567);
        party2.setLocation(location);
        party2.setStartDate(new Date());
        party2.setName("Testparty 2");

        resttemplate.put("http://localhost:8080/services/party/", party2);


        Map<String, String> vars = new HashMap<String, String>();
        vars.put("lat", "48.125870");
        vars.put("lon", "11.550380");
        vars.put("maxdistance", "15000000");
        String result = resttemplate.getForObject("http://localhost:8080/services/party/search/{lon}/{lat}/{maxdistance}", String.class, vars);
    }



}
