package com.spring.printFlow.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.printFlow.models.User;

public interface UserRepository extends MongoRepository<User, String> {

  
    User findUserByUsername(String username);

    // Define a method to find a user by username
    User findByUsermail(String usermail);


   
}
