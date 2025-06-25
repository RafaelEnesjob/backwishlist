package com.backwishlist.bdd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


@Component
public class DatabaseCleaner {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void cleanDatabase() {
        mongoTemplate.getDb().drop();
    }
}