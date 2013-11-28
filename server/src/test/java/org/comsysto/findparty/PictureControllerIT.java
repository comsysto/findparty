package org.comsysto.findparty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: tim.hoheisel
 * Date: 06.09.12
 * Time: 10:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/comsysto/findparty/spring-test.xml")
public class PictureControllerIT {

    private static final String PICTURE_SERVICE = "http://snuggle.eu01.aws.af.cm/services/pictures";

    @Autowired
    private RestTemplate restTemplate;

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

        ResponseEntity<String> url = restTemplate.postForEntity(PICTURE_SERVICE + "/" + "jubi@test.de", result.toByteArray(), String.class);
        assertThat(url.getStatusCode(), is(HttpStatus.OK));
        assertThat(url.getBody(), containsString(PICTURE_SERVICE));
    }

}
