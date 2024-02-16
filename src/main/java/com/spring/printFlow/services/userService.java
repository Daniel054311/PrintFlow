package com.spring.printFlow.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.printFlow.models.User;
import com.spring.printFlow.repository.UserRepository;

@Service
public class userService {
    @Autowired
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(userService.class);

    // create instance of the user repository
    public userService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // fetch all services
    public List<User> getAllUsers() {
        LOGGER.info("services : fetch all users..");
        return userRepository.findAll();
    }

    // save new users
    public User saveUser(User user) {
        LOGGER.info("services : saving all users..");
        return userRepository.save(user);
    }

    // delete users
    public void deleteById(String _id) {

        LOGGER.info("services : delete user by _id..");
        userRepository.deleteById(_id);
    }

    public User updateById(User updatedUser) {

        LOGGER.info("User updated successfully");
        // Save the updated user
        return userRepository.save(updatedUser);

    }

    // Find user by _id / returns users when the _id is provided
    public Optional<User> findById(String _id) {
        return userRepository.findById(_id);
    }

    // Find user by _id / returns users when the usermail is provided
    public User getUserByUsermail(String usermail) {
        return userRepository.findByUsermail(usermail);
    }

}
