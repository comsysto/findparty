package com.comsysto.findparty.security;

import com.comsysto.findparty.User;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * User: rpelger
 * Date: 06.03.13
 */
public class MongoUserDetailsService implements UserDetailsService{


    public MongoOperations mongoOperations;

    public MongoUserDetailsService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoOperations.findOne(query, User.class);

        return new SimpleUserDetails(user);
    }

}
