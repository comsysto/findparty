package org.comsysto.findparty;

import com.comsysto.findparty.LevelType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void createPartytest() {
        Party party2 = new Party();
        party2.setId("4711");
        party2.setSize(3);
        party2.setCategory("BIKING");
        party2.setLevel(LevelType.BEGINNER.name());
        Point location = new Point();
        location.setLon(11.53144);
        location.setLat(48.1567);
        party2.setLocation(location);
        party2.setStartDate(new Date());

        party2.setName("Jubigrat");

        String partyId = resttemplate.postForObject(PARTY_SERVICE_URL, party2, String.class);
    }

    @Test
    public void createPartyImageTest() throws IOException {
        Party party2 = new Party();
        party2.setId("4711");
        party2.setSize(3);
        party2.setCategory("BIKING");
        party2.setLevel(LevelType.BEGINNER.name());
        Point location = new Point();
        location.setLon(11.53144);
        location.setLat(48.1567);
        party2.setLocation(location);
        party2.setStartDate(new Date());
        party2.setName("Testparty 2");

        String partyId = resttemplate.postForObject(PARTY_SERVICE_URL, party2, String.class);

        URL resource = this.getClass().getResource("/jubi.jpg");
        InputStream inStream = resource.openStream();

        ByteArrayOutputStream result = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int read = 0;
        while((read = inStream.read(buffer)) != -1){
            result.write(buffer);
        }

        resttemplate.postForObject(PARTY_SERVICE_URL + "/" + partyId, result.toByteArray(), String.class);
    }


    @Test
    public void searchParties() throws IOException {
        Party party2 = new Party();
        party2.setSize(3);
        party2.setCategory("BIKING");
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
        vars.put("maxdistance", "15");
        ObjectMapper mapper = new ObjectMapper();
        Set<Party> parties = resttemplate.getForObject(PARTY_SERVICE_URL + "/search/{lon}/{lat}/{maxdistance}", HashSet.class, vars);
        assertNotNull(parties);
        assertNotNull(partyId);

        resttemplate.delete(PARTY_SERVICE_URL + "/{partyId}", partyId);
    }
    
    @Test
    public void createFindUpdateParty() throws IOException {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(2012, 11, 21, 14, 30, 03);

        Point location = new Point();
        location.setLon(11.53110);
        location.setLat(48.1562);
        
        Party party = new Party();

        party.setSize(2);
        party.setCategory("MUSIC");
        party.setLevel(LevelType.AMATEUR.name());
        party.setLocation(location);
        party.setStartDate(cal.getTime());
        party.setName("Awesome party for test!");
        party.setOwner("Robert");
        
        //create Party on server
        String partyId = resttemplate.postForObject(PARTY_SERVICE_URL, party, String.class);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        URL resource = this.getClass().getResource("/jubi.jpg");
        InputStream inStream = resource.openStream();

        byte[] buffer = new byte[4096];
        int read = 0;
        while((read = inStream.read(buffer)) != -1){
            result.write(buffer);
        }

        assertNotNull(partyId);
        
        //try load party from server
        Party storedParty = resttemplate.getForObject(PARTY_SERVICE_URL+"/"+partyId, Party.class);
        assertNotNull(storedParty);

        Date storedDate = storedParty.getStartDate();
        Calendar storedCal = GregorianCalendar.getInstance();
        storedCal.setTime(storedDate);
        
        assertNotNull(storedParty.getId());
        assertEquals("Robert", storedParty.getOwner());
        assertEquals("MUSIC", storedParty.getCategory());
        assertEquals(LevelType.AMATEUR.name(), storedParty.getLevel());
        assertEquals(new Double(11.53110d), storedParty.getLocation().getLon());
        assertEquals(new Double(48.1562d), storedParty.getLocation().getLat());
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
    public void createAndJoinWithDifferentUser() throws IOException {
        Party party = new Party();
        party.setName("join this party");
        party.setOwner("Robert");
        party.setSize(8);
        party.setStartDate(new Date());
        
        Point location = new Point();
        location.setLon(11.53154);
        location.setLat(48.1576);
        party.setLocation(location);
        party.setCategory("SWIMMING");
        party.setLevel(LevelType.BEGINNER.name());
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        URL resource = this.getClass().getResource("/jubi.jpg");
        InputStream inStream = resource.openStream();

        byte[] buffer = new byte[4096];
        int read = 0;
        while((read = inStream.read(buffer)) != -1){
            result.write(buffer);
        }

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
        updatedParty = resttemplate.getForObject(PARTY_SERVICE_URL+"/"+partyId, Party.class);
        assertNotNull(updatedParty);
        assertNotNull(updatedParty.getCandidates());
        assertEquals(0, updatedParty.getCandidates().size());
        
    }


    @Test
    public void dateFormat() {
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        String format1 = sdf.format(date);
        System.out.println(format1);

    }

}
