package com.comsysto.findparty.web;

import com.comsysto.findparty.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/users")
public class UserControllerImpl implements  UserController {

    @Autowired
    public PartyService partyService;

    public static final Logger LOGGER = Logger.getLogger(PartyControllerImpl.class);

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody User createUser(@RequestBody User user) {
        User createdUser = partyService.createUser(user.getUsername(), user.getPassword());
        return createdUser;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody User getUser(@RequestParam("username") String username) {
        return partyService.getUser(username);
    }

    @Override
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public @ResponseBody boolean getUser(@RequestBody User user) {
        return validateUser(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody User user, @PathVariable String userId) {

        if(!user.getId().equals(userId))
            throw new IllegalArgumentException("Id mismatch of requested party(id="+userId+") and provided user(id="+user.getId()+") does not match");

        partyService.update(user);
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
}
