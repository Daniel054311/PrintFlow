package com.spring.printFlow.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import org.springframework.data.annotation.Id;

@Document(collection = "users")
public class User {

    // streucture model for databese
    @Id
    private String _id;
    private String username;
    private String location;
    private String profile;
    private String tel;
    private String password;
    private String usermail;
    private String role;
    private Date createdAt;
    private Date updatedAt;
    private Date lastVisit;

    // default constructor
    public User() {

    }

    // constructor
    // Constructor with required fields
    public User(String username, String location, String profile, String tel, String password, String usermail) {
        this.username = username;
        this.location = location;
        this.profile = profile;
        this.tel = tel;
        this.password = password;
        this.usermail = usermail;
        this.role = "ADMIN";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.lastVisit = new Date();
    }

    // Getters and setters...
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreated_at(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

}


