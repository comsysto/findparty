package com.comsysto.findparty.service;

import com.comsysto.findparty.Party;
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

    public static final String SERVER_URL = "http://snuggle.eu01.aws.af.cm/services/pictures/";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Picture getPicture(String id) {
        Picture picture = findPicture(id);
        if (picture != null) {
            return picture;
        }
        throw new ResourceNotFoundException("The picture with the id " + id + " could not be found.");
    }

    private Picture findPicture(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Picture.class);
    }

    @Override
    public void removePicture(String pictureId) {
        Picture picture = findPicture(pictureId);
        mongoTemplate.remove(picture);
    }

    @Override
    public String createPartyImage(String username, byte[] content) {
        Picture picture = createPicture(username, content);
        mongoTemplate.insert(picture);
        return SERVER_URL + picture.getId();
    }

    private Picture createPicture(String username, byte[] content) {
        Picture picture = new Picture();
        picture.setContent(content);
        picture.setUsername(username);
        mongoTemplate.insert(picture);
        return picture;
    }

}
