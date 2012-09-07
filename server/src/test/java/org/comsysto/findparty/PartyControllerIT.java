package org.comsysto.findparty;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.comsysto.findparty.CategoryType;
import com.comsysto.findparty.LevelType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;

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
    public void searchParties() throws IOException {
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

        String partyId = resttemplate.postForObject("http://localhost:8080/services/parties", party2, String.class);


        Map<String, String> vars = new HashMap<String, String>();
        vars.put("lat", "48.125870");
        vars.put("lon", "11.550380");
        //vars.put("maxdistance", "15000000");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashSet<Party>> typeRef
                = new TypeReference<
                HashSet<Party>>() {};
        Set<Party> parties = mapper.readValue(resttemplate.getForObject("http://localhost:8080/services/parties/location/{lon}/{lat}", String.class, vars), typeRef);
        assertNotNull(parties);
        assertNotNull(partyId);
    }



}
