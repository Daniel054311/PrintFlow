package com.spring.printFlow.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.printFlow.models.User;
import com.spring.printFlow.services.userService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(userService.class);

    private final userService userService;

    public UserController(userService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            LOGGER.info("Controller: Fetch all users...");
            return ResponseEntity.ok().body(users);
        } catch (Exception error) {
            LOGGER.error("Error fetching users: {}", error.getMessage(), error);
            // Return an error response with a message
            error.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something happened  fetching users: ");

        }
    }

    // save user
    @PostMapping("/save")
    public ResponseEntity<?> createUser(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tel") String tel,
            @RequestParam("username") String username,
            @RequestParam("location") String location,
            @RequestParam("password") String password,
            @RequestParam("usermail") String usermail) {
        // Log info
        LOGGER.info("Controller: Saving users...");
        try {

            // Check if any of the required parameters are empty
            if (StringUtils.isAnyBlank(username, location, password, usermail, tel)) {
                return ResponseEntity.badRequest().body("Invalid input. Please provide all required fields.");
            }

            // Check if the user with the given usermail exists
            User existingUserOptional = userService.getUserByUsermail(usermail);
            LOGGER.info("Controller: Auth  checking duplicate  usermail: " + existingUserOptional);

            if (existingUserOptional != null) {
                return ResponseEntity.badRequest().body("Usermail already exist , Please , use different mail");
            }

            // hash the password
            String plainTextPassword = password;
            String hashedPassword = ValidationController.hashPassword(plainTextPassword);

            // Check if the file is not empty and is an image file
            if (!file.isEmpty() && ValidationController.isImageFile(file.getOriginalFilename())) {
                // Save the file to a folder
                // Specify the upload directory within resources/static
                String uploadDir = "src/main/resources/static/uploads";

                // Create the directory if it doesn't exist
                Path uploadPath = Paths.get(uploadDir);
                if (Files.notExists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                    } catch (IOException error) {
                        // Handle the exception (e.g., log it)
                        LOGGER.error("Error uploading files: {}", error.getMessage(), error);
                        error.printStackTrace();

                    }
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info("Received file upload request. File name: {}", fileName);
                // Create a new User with the profile name
                User user = new User(username, location, fileName, tel, hashedPassword, usermail);
                // Send user data to userService for saving
                userService.saveUser(user);
                // Return a success response with the saved user
                // return ResponseEntity.ok(savedUser);
                return ResponseEntity.ok().body("User account created successfully");

            } else {
                // Return a response indicating that the file is empty or is not an image file
                return ResponseEntity.badRequest().body("Invalid file data");
            }

        } catch (IOException | DataAccessException error) {
            // Handle or log the exception
            error.printStackTrace();
            LOGGER.error("Error saving user: {}", error.getMessage(), error);

            // Return an error response with a message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something happened saving user, Please try again");
        }
    }

    // Delete user
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "_id") String _id) {
        LOGGER.info("Controller: delete users...");

        try {
            // Fetch the user by _id before deleting
            Optional<User> optionalUser = userService.findById(_id);

            if (optionalUser.isPresent()) {
                // User found, retrieve the user
                // User deletedUser = optionalUser.get();
                String deletedUserProfile = optionalUser.get().getProfile();

                // Now delete the user
                userService.deleteById(_id);

                // delete profile picture
                String uploadDir = "src/main/resources/static/uploads"; // Adjust the upload directory as needed
                Path filePath = Paths.get(uploadDir).resolve(deletedUserProfile);

                // Check if the file exists before attempting to delete
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    LOGGER.info("File deleted successfully user profile: {}", deletedUserProfile);

                } else {
                    LOGGER.error("Error File not found:user profile: {}", deletedUserProfile);
                }

                // Return a response with the deleted user information
                return ResponseEntity.ok().body("User account deleted successfully");
            } else {
                // User not found with the given _id
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with _id: " + _id);
            }
        } catch (Exception error) {
            LOGGER.error("Error deleting user: {}", error.getMessage(), error);

            // For demonstration purposes, let's print the stack trace to the console
            error.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something happened deleting user, Please try again");
        }

    }

    // update user with _id
    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam("_id") String _id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("tel") String tel,
            @RequestParam("username") String username,
            @RequestParam("location") String location,
            @RequestParam("password") String password,
            @RequestParam("usermail") String usermail) {
        try {

            String hashedPassword = "";

            // Check if any of the required parameters are empty
            if (StringUtils.isAnyBlank(username, location, usermail)) {
                return ResponseEntity.badRequest().body("Invalid input. Please provide all required fields.");
            }

            // Check if the user with the given _id exists
            Optional<User> existingUserOptional = userService.findById(_id);

            // hash password

            if (!password.isEmpty() && existingUserOptional.isPresent()) {
                String plainTextPassword = password;
                hashedPassword = ValidationController.hashPassword(plainTextPassword);
            }

            if (password.isEmpty() && existingUserOptional.isPresent()) {
                hashedPassword = existingUserOptional.get().getPassword();
            }

            // Check if the file is not empty and is an image file
            if (!file.isEmpty() && ValidationController.isImageFile(file.getOriginalFilename())) {

                // Save the file to a folder
                // Specify the upload directory within resources/static
                String uploadDir = "src/main/resources/static/uploads";

                // Create the directory if it doesn't exist
                Path uploadPath = Paths.get(uploadDir);
                if (Files.notExists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                    } catch (IOException error) {
                        // Handle the exception (e.g., log it)
                        LOGGER.error("Error uploading files: {}", error.getMessage(), error);
                        error.printStackTrace();

                    }
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);

                // check if it exist
                if (existingUserOptional.isPresent()) {

                    User existingUser = existingUserOptional.get();

                    // get profile pic file
                    String oldUserProfile = existingUserOptional.get().getProfile();

                    Path filePath = Paths.get(uploadDir).resolve(oldUserProfile);

                    // Check if the file exists before attempting to delete
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        LOGGER.info("File deleted successfully user old profile: {}", oldUserProfile);

                    } else {
                        LOGGER.error("Error File not found:user old profile: {}", oldUserProfile);
                    }

                    // Update fields of the existing user with the new data
                    // update setters
                    existingUser.setUsername(username);
                    existingUser.setLocation(location);
                    existingUser.setProfile(fileName);
                    existingUser.setTel(tel);
                    existingUser.setPassword(hashedPassword);
                    existingUser.setUsermail(usermail);
                    existingUser.setUpdatedAt(new Date()); // Set the updatedAt field to the current date

                    // Save the updated user
                    userService.updateById(existingUser);

                    LOGGER.info("User updated successfully with id: {}", _id);
                    // 200 -299
                    return ResponseEntity.ok().body("User updated successfully");
                } else {
                    // Handle the case where the user with the given _id is not found
                    // 404
                    LOGGER.warn("User not found with id: {}", _id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with that id");
                }

            } else if (file.isEmpty()) {

                if (existingUserOptional.isPresent()) {
                    User existingUser = existingUserOptional.get();

                    // get profile pic file
                    String oldUserProfile = existingUserOptional.get().getProfile();

                    // Update fields of the existing user with the new data
                    // update setters
                    existingUser.setUsername(username);
                    existingUser.setLocation(location);
                    existingUser.setProfile(oldUserProfile);
                    existingUser.setTel(tel);
                    existingUser.setPassword(hashedPassword);
                    existingUser.setUsermail(usermail);
                    existingUser.setUpdatedAt(new Date()); // Set the updatedAt field to the current date

                    // Save the updated user
                    userService.updateById(existingUser);

                    LOGGER.info("User updated successfully with id: {}", _id);
                    // 200 -299
                    return ResponseEntity.ok().body("User updated successfully");
                } else {
                    // Handle the case where the user with the given _id is not found
                    // 404
                    LOGGER.warn("User not found with id: {}", _id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with that id");
                }

            } else {

                // Return a response indicating that the file is empty or is not an image file
                return ResponseEntity.badRequest().body("Invalid file data / format");

            }

        } catch (IOException | DataAccessException error) {
            // Handle or log the exception
            error.printStackTrace();
            LOGGER.error("Error saving user: {}", error.getMessage(), error);

            // Return an error response with a message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something happened saving user, Please try again");
        }
    }

    // fetch user with _id
    @GetMapping("search")
    public ResponseEntity<?> findById(@RequestParam(name = "_id") String _id) {
        LOGGER.info("Controller: search  users...");

        try {

            // Find the user by _id
            User user = userService.findById(_id).orElse(null);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                LOGGER.warn("User not found with id: {}", _id);
                // If user is not found, return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with that id");
            }

        }

        catch (Exception error) {
            error.printStackTrace();
            LOGGER.error("Error fetching  user", error);
            // 501
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch user");
        }
    }

    
}
