package com.comsysto.findparty.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * This Configuration enables "." in URL paths. So u can successfully use a URL like
 * 'http://localhost:8080/services/users/some.guy'
 * Without this configuration the above URL would be cut to:
 * 'http://localhost:8080/services/users/some'
 *
 * User: rpelger
 * Date: 06.06.13
 */
@Configuration
public class Config extends WebMvcConfigurationSupport {

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
        handlerMapping.setUseSuffixPatternMatch(false);
        handlerMapping.setUseTrailingSlashMatch(false);
        return handlerMapping;

    }
}
