package com.comsysto.findparty.service;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.stereotype.Service;

/**
 * User: rpelger
 * Date: 07.03.13
 */
@Service
public class MongoService {

    private MongoTemplate mongoTemplate;

    @Autowired
    public MongoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

        this.mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("username", Order.ASCENDING).unique());
        this.mongoTemplate.indexOps(Party.class).ensureIndex(new GeospatialIndex("location"));
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

}
