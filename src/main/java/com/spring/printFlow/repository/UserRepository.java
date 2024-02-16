package com.spring.printFlow.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.printFlow.models.User;

public interface UserRepository extends MongoRepository<User, String> {

    // Optional<User> findByUsermail(String usermail);

    // find username
    User findUserByUsername(String username);

    // Define a method to find a user by username
    User findByUsermail(String usermail);

    // Define a method to update a user by username
    // void updateByUsername(String username, User updatedUser);

    // //save user
    // public abstract User saveUser(User user);

    // //delete user by _id
    // public abstract void deleteById(String _id);

    // //find user by _id
    // public abstract void findById(String _id);

}
