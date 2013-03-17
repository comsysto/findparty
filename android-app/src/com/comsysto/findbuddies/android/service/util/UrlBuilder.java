package com.comsysto.findbuddies.android.service.util;

import java.util.Map;

public class UrlBuilder {

    private String host;

    public UrlBuilder(String host) {
        this.host = "http://" + host;
    }

    public String createFrom(String path) {
        if (path.startsWith("/"))
            return host + path;
        else
            return host + "/" + path;
    }

    public String createUri(String... pathElements) {
        String path = "";
        for (String element : pathElements) {
            path += element + "/";
        }

        // cut trailing "/"
        return createFrom(path.substring(0, path.length() - 1));
    }

    public String createUri(String path, Map<String, String> params) {
        String uriPart = createFrom(path);
        String paramsPart = build(params);

        return createFrom(uriPart + paramsPart);
    }

    public String createUri(Map<String, String> params, String... pathElements) {
        String queryParams = build(params);
        String url = createUri(pathElements);
        return url + queryParams;
    }

    private String build(Map<String, String> requestParams) {
        String params = "?";
        for (String key : requestParams.keySet()) {
            params += key + "=" + requestParams.get(key);
            params += "&";
        }

        // cut trailing "&"
        return params.substring(0, params.length() - 1);
    }

}
