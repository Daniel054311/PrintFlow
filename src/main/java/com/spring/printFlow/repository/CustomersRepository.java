package com.spring.printFlow.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.printFlow.models.Customers;

public interface CustomersRepository extends MongoRepository <Customers,String>{
   

}
