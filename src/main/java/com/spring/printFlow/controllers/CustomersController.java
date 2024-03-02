package com.spring.printFlow.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.printFlow.models.ActionTypes;
import com.spring.printFlow.models.Customers;
import com.spring.printFlow.models.File;
import com.spring.printFlow.services.customerServices;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {
   @Autowired
   private static final Logger LOGGER = LoggerFactory.getLogger(customerServices.class);

   private final customerServices customerServices;

   public CustomersController(customerServices customerServices) {

      this.customerServices = customerServices;

   }

   @PostMapping("/save")
   public ResponseEntity<?> createUser(
         @RequestParam("file") List<MultipartFile> files,
         @RequestParam("tel") String tel,
         @RequestParam("name") String name,
         @RequestParam("activitiy") String activitiy,
         @RequestParam("copies") Long copies,
         @RequestParam("color") String color) {
      // Log info
      LOGGER.info("Controller: Saving users...");
      try {
         List<String> successfulUploads = new ArrayList<>();
         String refId = Random();

         for (MultipartFile file : files) {
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

                     // Return an error response
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Error uploading files");
                  }
               }

               String fileName = file.getOriginalFilename();
               Files.copy(file.getInputStream(), Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);

               File fileData = new File(fileName, "Null", refId, "PNG", 10, activitiy, (float) 90, copies, color);

               customerServices.saveFile(fileData);

               // Add the successfully uploaded file name to the list
               successfulUploads.add(fileName);
            }
         }

         if (!successfulUploads.isEmpty()) {

            // Create a new User with the profile name
            Customers customers = new Customers(name, tel, refId);
            // Send user data to userService for saving
            customerServices.saveCustomer(customers);
            // Return a success response with the list of uploaded file names
            return ResponseEntity.ok().body("Files uploaded successfully: ");
         } else {
            // Return a response indicating no valid files were uploaded
            return ResponseEntity.badRequest().body("No valid files were uploaded");
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

   private String Random() {
      return RandomStringUtils.randomAlphanumeric(20);
   }

   @GetMapping("/fetch")
   public ResponseEntity<?> getAllCustomers() {

      try {
         List<Customers> customers = customerServices.getAllCustomers();
         LOGGER.info("Controller: Fetch all customers...");
         return ResponseEntity.ok().body(customers);
      } catch (Exception error) {
         LOGGER.error("Error fetching customers: {}", error.getMessage(), error);
         // Return an error response with a message
         error.printStackTrace();
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body("Something happened  fetching customers: ");

      }
   }

   @GetMapping("findById")
   public ResponseEntity<?> findById(@RequestParam(name = "_id") String _id) {
      LOGGER.info("Controller: search  customers...");

      try {

         // Find the customer by _id
         Customers customer = customerServices.findById(_id).orElse(null);

         if (customer != null) {
            return ResponseEntity.ok(customer);
         } else {
            LOGGER.warn("customer not found with id: {}", _id);
            // If customer is not found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("customer not found with that id");
         }

      }

      catch (Exception error) {
         error.printStackTrace();
         LOGGER.error("Error fetching  customer", error);
         // 501
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch customer");
      }
   }

   @GetMapping("find/Files/ByRefId")
   public ResponseEntity<?> findByCusRefId(@RequestParam(name = "refId") String refId) {
      LOGGER.info("Controller: search  customers...");

      try {

         // Find the customer by _id
         List<File> file = customerServices.findByCusRefId(refId);

         if (file != null) {
            return ResponseEntity.ok(file);
         } else {
            LOGGER.warn("customer not found with id: {}", refId);
            // If customer is not found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("customer not found with that id");
         }

      }

      catch (Exception error) {
         error.printStackTrace();
         LOGGER.error("Error fetching  customer", error);
         // 501
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch customer");
      }
   }

   @GetMapping("/action-types")
   public ResponseEntity<?> getAllActionTypes() {
      try {
         List<ActionTypes> types = customerServices.getAllActionTypes();
         LOGGER.info("Controller: Fetch all customers...");
         return ResponseEntity.ok().body(types);
      } catch (Exception error) {
         LOGGER.error("Error fetching types: {}", error.getMessage(), error);
         // Return an error response with a message
         error.printStackTrace();
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body("Something happened  fetching types: ");

      }
   }

}
