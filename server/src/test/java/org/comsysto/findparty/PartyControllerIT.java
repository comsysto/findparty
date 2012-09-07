package org.comsysto.findparty;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private static final String PARTY_SERVICE_URL = "http://localhost:8080/services/parties";
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

        String partyId = resttemplate.postForObject(PARTY_SERVICE_URL, party2, String.class);


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
    
    @Test
    public void createFindUpdateParty() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(2012, 11, 21, 14, 30, 03);

        Point location = new Point();
        location.setLon(12.2292d);
        location.setLat(-14.222d);
        
        Party party = new Party();

        party.setSize(2);
        party.setCategory(CategoryType.MUSIC.name());
        party.setLevel(LevelType.EXPERIENCED.name());
        party.setLocation(location);
        party.setStartDate(cal.getTime());
        party.setName("Awesome party for test!");
        party.setOwner("Robert");
        
        //create Party on server
        String partyId = resttemplate.postForObject(PARTY_SERVICE_URL, party, String.class);
        
        assertNotNull(partyId);
        
        //try load party from server
        Party storedParty = resttemplate.getForObject(PARTY_SERVICE_URL+"/"+partyId, Party.class);
        assertNotNull(storedParty);

        Date storedDate = storedParty.getStartDate();
        Calendar storedCal = GregorianCalendar.getInstance();
        storedCal.setTime(storedDate);
        
        assertNotNull(storedParty.getId());
        assertEquals("Robert", storedParty.getOwner());
        assertEquals(CategoryType.MUSIC.name(), storedParty.getCategory());
        assertEquals(LevelType.EXPERIENCED.name(), storedParty.getLevel());
        assertEquals(new Double(12.2292d), storedParty.getLocation().getLon());
        assertEquals(new Double(-14.222d), storedParty.getLocation().getLat());
        assertEquals("Awesome party for test!", storedParty.getName());
        
        assertEquals(cal.get(Calendar.YEAR), storedCal.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), storedCal.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), storedCal.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.HOUR), storedCal.get(Calendar.HOUR));
        assertEquals(cal.get(Calendar.MINUTE), storedCal.get(Calendar.MINUTE));
        assertEquals(cal.get(Calendar.SECOND), storedCal.get(Calendar.SECOND));

        //update name
        storedParty.setName("ChangedName");
        resttemplate.put(PARTY_SERVICE_URL+"/"+storedParty.getId(), storedParty);
        
        Party updatedParty = resttemplate.getForObject(PARTY_SERVICE_URL+"/"+storedParty.getId(), Party.class);
        assertNotNull(updatedParty);
        assertEquals("ChangedName", updatedParty.getName());

    }
    
    @Test
    public void createAndJoinWithDifferentUser() {
        Party party = new Party();
        party.setName("join this party");
        party.setOwner("Robert");
        party.setSize(8);
        party.setStartDate(new Date());
        
        Point location = new Point();
        location.setLon(12d);
        location.setLat(44d);
        party.setLocation(location);
        party.setCategory(CategoryType.SWIMMING.name());
        party.setLevel(LevelType.BEGINNER.name());
        
        String partyId = resttemplate.postForObject(PARTY_SERVICE_URL, party, String.class);
        
        //join user
        String username = "Joined User";
        resttemplate.put(PARTY_SERVICE_URL+"/"+partyId+"/subscriptions", username);
        
        Party updatedParty = resttemplate.getForObject(PARTY_SERVICE_URL+"/"+partyId, Party.class);
        assertNotNull(updatedParty);
        assertNotNull(updatedParty.getCandidates());
        assertEquals(1, updatedParty.getCandidates().size());
        assertEquals("Joined User", updatedParty.getCandidates().get(0));
        
        //cancel user
        resttemplate.put(PARTY_SERVICE_URL+"/"+partyId+"/subscriptions?action=cancel", username);
        assertNotNull(updatedParty);
        assertNotNull(updatedParty.getCandidates());
        assertEquals(0, updatedParty.getCandidates().size());
        
    }


    @Test
    public void testList() {
        List<String> list = new ArrayList<String>();
        list.add("test");
        list.add("bla");
    }

}
