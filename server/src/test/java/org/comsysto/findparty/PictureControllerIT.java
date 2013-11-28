package org.comsysto.findparty;

import com.comsysto.findparty.LevelType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;
import org.codehaus.jackson.map.ObjectMapper;
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
public class PictureControllerIT {

    private static final String PICTURE_SERVICE = "http://localhost:8080/services/pictures";
//    private static final String PICTURE_SERVICE = "http://snuggle.eu01.aws.af.cm/services/pictures";
    @Autowired
    private RestTemplate resttemplate;

    @Test
    public void createPictureTest() throws IOException {
        URL resource = this.getClass().getResource("/jubi.jpg");
        InputStream inStream = resource.openStream();

        ByteArrayOutputStream result = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int read = 0;
        while((read = inStream.read(buffer)) != -1){
            result.write(buffer);
        }

        resttemplate.postForObject(PICTURE_SERVICE + "/" + "jubi@test.de", result.toByteArray(), String.class);
    }

}
