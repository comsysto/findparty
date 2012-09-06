package com.comsysto.findparty.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class AndroidRestTemplateTest {

    
    @Test
    public void testRestTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // TODO: NOT WOKRING, check again when spring-android-rest is released
        // requestFactory.setReadTimeout(100);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
//        List<MediaType> mediaTypes = new ArrayList<MediaType>();
//        mediaTypes.add(new MediaType("text", "javascript"));
//        converter.setSupportedMediaTypes(mediaTypes);
//        restTemplate.getMessageConverters().add(converter);
        
        String response = restTemplate.getForObject("http://localhost:8080/services/echo/hello", String.class);
        assertEquals("hello", response);
    }
    
}
