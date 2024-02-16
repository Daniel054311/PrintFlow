package com.spring.printFlow.controllers;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.printFlow.models.User;
import com.spring.printFlow.services.userService;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final userService userService;

    public AuthController(userService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestParam("password") String password,
            @RequestParam("usermail") String usermail) {

        try {

            // Check if any of the required parameters are empty
            if (StringUtils.isAnyBlank(password, usermail)) {
                return ResponseEntity.badRequest().body("Invalid input. Please provide all required fields.");
            }

            // Check if the user with the given usermail exists
            User existingUserOptional = userService.getUserByUsermail(usermail);
            LOGGER.info("Controller: Auth  logginin user: " + existingUserOptional);

            if (existingUserOptional != null) {

                // get hashed password
                String hashedPassword = existingUserOptional.getPassword();

                LOGGER.info("Controller: Auth  logginin hashed pass: " + hashedPassword + " pure password " + password);

                // check password match // true or false
                boolean isPasswordCorrect = ValidationController.checkPassword(password, hashedPassword);

                if (isPasswordCorrect) {
                    existingUserOptional.setLastVisit(new Date()); // Set the updatedAt field to
                    // the current date

                    return ResponseEntity.ok().body("Accout authenticated");

                } else {

                    return ResponseEntity.badRequest().body("Password is incorrect");

                }

            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");

            }

        } catch (Exception error) {
            // Handle or log the exception
            error.printStackTrace();
            LOGGER.error("Error logging in user: {}", error.getMessage(), error);

            // Return an error response with a message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something happened logging in user, Please try again");
        }

    }

    @GetMapping("logout")
    public ResponseEntity<String> logout() {
        try {

            return ResponseEntity.ok().body("Accout logged out successfully..");

        } catch (Exception error) {

            error.printStackTrace();
            LOGGER.error("Error logging out user: {}", error.getMessage(), error);

            // Return an error response with a message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something happened logging in user, Please try again");

        }

    }

}