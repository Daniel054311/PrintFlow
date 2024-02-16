package com.spring.printFlow.controllers;

import java.util.Arrays;

import org.mindrot.jbcrypt.BCrypt;

public class ValidationController {
    // hash password helper
    // Hash the password using BCrypt
    public static String hashPassword(String plainTextPassword) {
        // The higher the workload factor, the more time is needed to hash the password
        int workload = 12;
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(workload));
    }

    public static boolean isImageFile(String filename) {
        // Add logic to check if the file extension corresponds to common image types
        String[] allowedImageExtensions = { "jpg", "jpeg", "png", "gif" ,"pdf" ,"msword" ,"text/plain"};
        String fileExtension = getFileExtension(filename);

        return Arrays.asList(allowedImageExtensions).contains(fileExtension.toLowerCase());
    }

    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    public static boolean checkPassword(String enteredPassword, String hashedPassword) {
        return BCrypt.checkpw(enteredPassword, hashedPassword);
    }

}
