package com.spring.printFlow.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
public class File {
   @Id
   private String _id;
   private String name;
   private String cusRefId;
   private String type;
   private String status;
   private Long pages;
   private String activitiy;
   private Float price;
   private long copies;
   private String color;
   private Date createdAt;
   private Date operatedAt;

   public File() {

   }

   public File(String name, String status, String cusRefId,
         String type, long pages, String activitiy, Float price,
         Long copies, String color) {
      this.name = name;
      this.cusRefId = cusRefId;
      this.type = type;
      this.status = status;
      this.pages = pages;
      this.activitiy = activitiy;
      this.price = price;
      this.copies = copies;
      this.color = color;
      this.createdAt = new Date();
      this.operatedAt = new Date();
   }

   public String get_id() {
      return _id;
   }

   public void set_id(String _id) {
      this._id = _id;
   }

   public String getcusRefId() {
      return cusRefId;
   }

   public void setfefId(String cusRefId) {
      this.cusRefId = cusRefId;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getname() {
      return name;
   }

   public void setname(String name) {
      this.name = name;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public Date getCreatedAt() {
      return createdAt;
   }

   public void setOperatedAt(Date operatedAt) {
      this.operatedAt = operatedAt;
   }

   public Date getOperatedAt() {
      return operatedAt;
   }

   public void setCreated_at(Date createdAt) {
      this.createdAt = createdAt;
   }

   public Long getpages() {
      return pages;
   }

   public void setpages(Long pages) {
      this.pages = pages;
   }

   public String getActivity() {
      return activitiy;
   }

   public void setActivity(String activitiy) {
      this.activitiy = activitiy;
   }

   public Float getPrice() {
      return price;
   }

   public Float setPrice(Float price) {
      return this.price = price;
   }

   public Long getCopies() {
      return copies;
   }

   public Long setCopies(Long copies) {
      return copies;
   }

   public String getColor() {
      return color;
   }

   public String setColor(String color) {
      return color;
   }

}