package com.spring.printFlow.dataBaseServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PostConstruct;

@Service
public class MongoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoService.class);

    @Autowired
    private final MongoDatabaseFactory mongoDatabaseFactory;

    public MongoService(MongoDatabaseFactory mongoDatabaseFactory) {
        this.mongoDatabaseFactory = mongoDatabaseFactory;
    }

    @PostConstruct
    public void checkMongoConnection() {
        LOGGER.debug("Checking MongoDB connection...");
        try {
            MongoDatabase mongoDatabase = mongoDatabaseFactory.getMongoDatabase();
            LOGGER.info("Connected to MongoDB: {}", mongoDatabase.getName());
        } catch (Exception e) {
            LOGGER.error("Error connecting to MongoDB", e);
        }
    }

}
