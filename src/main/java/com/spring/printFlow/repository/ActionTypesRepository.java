package com.spring.printFlow.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.printFlow.models.ActionTypes;
public interface ActionTypesRepository extends MongoRepository<ActionTypes, String>  {
   
}
