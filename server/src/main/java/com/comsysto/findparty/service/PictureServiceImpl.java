package com.comsysto.findparty.service;

import com.comsysto.findparty.Picture;
import com.comsysto.findparty.exceptions.ResourceNotFoundException;
import com.comsysto.findparty.web.PictureService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class PictureServiceImpl implements PictureService {

    public static final Logger LOGGER = Logger.getLogger(PictureService.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Picture getPicture(String id) {
        Picture picture = mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Picture.class);
        if (picture != null) {
            return picture;
        }
        throw new ResourceNotFoundException("The picture with the id " + id + " could not be found.");
    }

}
