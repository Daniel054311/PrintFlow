package com.spring.printFlow.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.printFlow.models.ActionTypes;
import com.spring.printFlow.models.Customers;
import com.spring.printFlow.models.File;
import com.spring.printFlow.repository.ActionTypesRepository;
import com.spring.printFlow.repository.CustomersFilesRepository;
import com.spring.printFlow.repository.CustomersRepository;

@SuppressWarnings("null")
@Service
public class customerServices {

   @Autowired

   private final CustomersRepository CustomersRepository;
   private final CustomersFilesRepository CustomersFilesRepository;
   private final ActionTypesRepository ActionTypesRepository;

   private static final Logger LOGGER = LoggerFactory.getLogger(customerServices.class);

   // create instance of the user repository
   public customerServices(
         CustomersRepository CustomersRepository,
         CustomersFilesRepository CustomersFilesRepository,
         ActionTypesRepository ActionTypesRepository) {
      this.CustomersRepository = CustomersRepository;
      this.CustomersFilesRepository = CustomersFilesRepository;
      this.ActionTypesRepository = ActionTypesRepository;
   }

   public File saveFile(File file) {
      return CustomersFilesRepository.save(file);
   }

   // fetch all services
   public List<Customers> getAllCustomers() {
      LOGGER.info("services : fetch all users..");
      return CustomersRepository.findAll();
   }

   public Optional<Customers> findById(String _id) {
      return CustomersRepository.findById(_id);
   }

   /**
    * 
    * @param refId
    * @return
    */
   public List<File> findByCusRefId(String refId) {
      return CustomersFilesRepository.findByCusRefId(refId);
   }

   // fetch all ActionTypes
   /***
    * get all ActionTypes
    * 
    * @return
    */
   public List<ActionTypes> getAllActionTypes() {
      LOGGER.info("services : fetch all action types..");
      return ActionTypesRepository.findAll();
   }

   /**
    * save customer files and credentials
    * 
    * @param request
    */

   public Customers saveCustomer(Customers customer) {
      LOGGER.info("customer services : saving customers..");
      return CustomersRepository.save(customer);
   }

}
