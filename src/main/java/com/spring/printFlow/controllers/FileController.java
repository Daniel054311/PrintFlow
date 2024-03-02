package com.spring.printFlow.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@SuppressWarnings("null")
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    @GetMapping("/fetch")
    public ResponseEntity<FileSystemResource> fetchFile(@RequestParam String filename) {

        LOGGER.info("Controller: Fetch files users...");
        try {
            // Provide the path to the directory where files are stored
            String directoryPath = "src/main/resources/static/uploads/";

            // Create a FileSystemResource from the file
            FileSystemResource resource = new FileSystemResource(new File(directoryPath + filename));

            // Check if the file exists
            if (resource.exists()) {
                return ResponseEntity
                        .ok()
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_OCTET_STREAM))
                        .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                        .body(resource);

            } else {
                LOGGER.info("Error File not found:  " + filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception error) {
            error.printStackTrace();
            LOGGER.error("Error fetching files: {}", error.getMessage(), error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/fetch-preview")
    public ResponseEntity<FileSystemResource> fetchFileForPreview(@RequestParam String filename) {
        LOGGER.info("Controller: Fetching file for preview...");

        try {
            // Provide the path to the directory where files are stored
            String directoryPath = "src/main/resources/static/uploads/";

            // Create a FileSystemResource from the file
            FileSystemResource resource = new FileSystemResource(new File(directoryPath + filename));

            // Check if the file exists
            if (resource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("filename", filename);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                LOGGER.info("Error: File not found - {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") List<MultipartFile> files) {
        try {

            List<String> successfulUploads = new ArrayList<>();

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

                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Files.copy(file.getInputStream(), Paths.get(uploadDir, fileName),
                            StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("Received file upload request. File name: {}", fileName);

                    // Add the successfully uploaded file name to the list
                    successfulUploads.add(fileName);
                }
            }

            if (!successfulUploads.isEmpty()) {
                // Return a success response with the list of uploaded file names
                return ResponseEntity.ok().body("Files sent successfully: " + String.join(", ", successfulUploads));
            } else {
                // Return a response indicating no valid files were uploaded
                return ResponseEntity.badRequest().body("No valid files were uploaded");
            }
        } catch (Exception error) {

            error.printStackTrace();
            LOGGER.error("Error uploading files: {}", error.getMessage(), error);

            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading files");
        }
    }

    @GetMapping("/raw")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) throws IOException {

        Path filePath = Paths.get(UPLOAD_DIR, filename);

        FileInputStream fileInputStream = new FileInputStream(filePath.toFile());
        InputStreamResource resource = new InputStreamResource(fileInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
