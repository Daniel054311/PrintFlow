package com.spring.printFlow.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "action_type")

public class ActionTypes {
   @Id
   private String _id;
   private String name;

   public ActionTypes(String name) {
      this.name = name;
   }

   public String getname() {
      return name;
   }

   public void setname(String name) {
      this.name = name;
   }
}
