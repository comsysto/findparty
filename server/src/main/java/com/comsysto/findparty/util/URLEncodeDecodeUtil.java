package org.comsysto.labs.util;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * USER: tim.hoheisel
 * Date: 28.03.12
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class URLEncodeDecodeUtil {

    private static final Logger LOG = Logger.getLogger(URLEncodeDecodeUtil.class);

    public static final String UTF_8 = "UTF-8";

    public static String decodeString(String value) {
        return decodeUTF8(value);
    }

    private static String decodeUTF8(String value) {
        try {
            return URLDecoder.decode(value, UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOG.error("This type of encoding ist not supported.", e);
            new IllegalArgumentException("This type of encoding ist not supported.", e);
        }
        return null;
    }
}
