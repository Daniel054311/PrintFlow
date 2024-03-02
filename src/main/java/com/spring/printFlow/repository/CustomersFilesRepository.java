package com.spring.printFlow.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.printFlow.models.File;

public interface CustomersFilesRepository extends MongoRepository<File, String> {
   List<File> findByCusRefId(String cusRefId);
}
