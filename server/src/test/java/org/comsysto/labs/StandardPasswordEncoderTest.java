package org.comsysto.labs;

import org.junit.Test;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by IntelliJ IDEA.
 * com.comsysto.findparty.User: tim.hoheisel
 * Date: 29.03.12
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class StandardPasswordEncoderTest {

    @Test
    public void encodeTest() {
        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        String joe = encoder.encode("scott");
    }
}