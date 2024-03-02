package com.spring.printFlow.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
public class Customers {
   @Id
   private String _id;
   private String name;
   private String tel;
   private String refId;
   private String fileUrl;
   private Date createdAt;

   public Customers() {

   }

   public Customers(String name, String tel, String refId) {
      this.name = name;
      this.tel = tel;
      this.refId = refId;
      this.fileUrl = "1707729273805_Screenshot 2023-11-19 171047.png";
      this.createdAt = new Date();
   }

   public String get_id() {
      return _id;
   }

   public void set_id(String _id) {
      this._id = _id;
   }

   public String getrefId() {
      return refId;
   }

   public void setfefId(String refId) {
      this.refId = refId;
   }

   public String getname() {
      return name;
   }

   public void setname(String name) {
      this.name = name;
   }

   public String getTel() {
      return tel;
   }

   public void setTel(String tel) {
      this.tel = tel;
   }

   public Date getCreatedAt() {
      return createdAt;
   }

   public void setCreated_at(Date createdAt) {
      this.createdAt = createdAt;
   }

   public String getFileUrl() {
      return fileUrl;
   }

   public String setFileUrl(String fileUrl) {
      return this.fileUrl = fileUrl;
   }

}