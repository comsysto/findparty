package org.comsysto.findparty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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


    @Test
    public void getPictureTest() {

        String url = "http://snuggle.eu01.aws.af.cm/services/pictures/52975fcde4b0a67d99b8b7d8?sz=200";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.IMAGE_JPEG));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Byte[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, Byte[].class, new HashMap<String, String>());

        HttpStatus statusCode = exchange.getStatusCode();
        Byte[] body = exchange.getBody();
        assertThat(statusCode.value(), is(200));
        assertThat(body, notNullValue());
    }

}
