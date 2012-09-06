package com.comsysto.findparty.util;

import com.comsysto.findparty.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class DomainObjectCreator {

    public static final String PREFIX = System.getProperty("user.dir") + File.separator + "../meta" + File.separator + "generated"; 

    public void createObjects() {
        try {
            createParties();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonGenerationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void createParties() throws IOException, JsonGenerationException {
        Party party1 = new Party();
        party1.setSize(3);
        party1.setCategory(CategoryType.BIKING.name());
        party1.setLevel(LevelType.BEGINNER.name());
        Point location = new Point();
        location.setLon(13.222);
        location.setLat(59.232);
        party1.setLocation(location);
        party1.setStartDate(new Date());
        party1.setName("Testparty");
        User dave = new User();
        dave.setUsername("dave");

        User scott = new User();
        scott.setUsername("scott");

        writeObject(party1, "party");

        writeObject(dave, "dave");

        writeObject(scott, "scott");
    }

    private void writeObject(Object object, String name) throws IOException {
        FileOutputStream partyOut = getOutputStream(name + ".json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(partyOut, object);

        partyOut.close();
        ;
    }

    private FileOutputStream getOutputStream(String prefix) throws FileNotFoundException {
        File file = new File(PREFIX + File.separator  + prefix);
        return new FileOutputStream(file);
    }


}
