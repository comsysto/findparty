package com.comsysto.findparty.web;

import com.comsysto.findparty.User;

/**
 * Created with IntelliJ IDEA.
 * User: rpelger
 * Date: 21.11.12
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public interface UserController {

    User createUser(User user);

    User getUser(String username);
}
