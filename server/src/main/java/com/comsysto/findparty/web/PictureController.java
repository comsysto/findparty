package com.comsysto.findparty.web;

import com.comsysto.findparty.Picture;
import com.comsysto.findparty.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 30.03.13
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/pictures")
public class PictureController implements PictureService {

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * TODO: This call not working for e.g. .com usernames. When fixed you can use this instead of getPicture
     * @param username
     * @return

    @RequestMapping("/{username}")
    public @ResponseBody Picture getUserPicture(@PathVariable String username) {
        return findPictureFromUser(username);
    }
     */

    @RequestMapping(method = RequestMethod.GET)
    @Override
    public @ResponseBody Picture getUserPicture(@RequestParam("username") String username) {
        return findPictureFromUser(username);
    }

    private Picture findPictureFromUser(String username) {
        User user = mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), User.class);
        if (user != null) {
            return user.getPicture();
        }
        return null;
    }

}
