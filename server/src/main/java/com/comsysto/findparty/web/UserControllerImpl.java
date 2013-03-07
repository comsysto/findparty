package com.comsysto.findparty.web;

import com.comsysto.findparty.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rpelger
 * Date: 21.11.12
 * Time: 18:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserControllerImpl implements  UserController {

    @Autowired
    public PartyService partyService;

    public static final Logger LOGGER = Logger.getLogger(PartyControllerImpl.class);

    @Override
    @RequestMapping(value="/users", method = RequestMethod.POST)
    public @ResponseBody User createUser(@RequestBody User user) {
        User createdUser = partyService.createUser(user.getUsername(), user.getPassword());
        return createdUser;
    }

    @Override
    @RequestMapping(value="/users/{username}", method = RequestMethod.GET)
    public @ResponseBody User getUser(@PathVariable("username") String username) {
        return partyService.getUser(username);
    }

    @Override
    @RequestMapping(value="/users/login", method = RequestMethod.POST)
    public @ResponseBody boolean getUser(@RequestBody User user) {
        return validateUser(user);
    }

    private boolean validateUser(User user) {
        String username = user.getUsername();
        User foundUser = partyService.getUser(username);
        if(foundUser != null && isPasswordValid(user, foundUser)){
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(User user, User foundUser) {
        return user.getPassword().equals(foundUser.getPassword());
    }

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody List<User> findAll() {
        return partyService.getAllUsers();
    }


}
