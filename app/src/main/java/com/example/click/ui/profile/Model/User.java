package com.example.click.ui.profile.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String gender;
    private String country;
    private String bio;

    public User(String id, String username, String imageURL, String status, String gender, String country, String bio) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.gender = gender;
        this.country = country;
        this.bio = bio;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
