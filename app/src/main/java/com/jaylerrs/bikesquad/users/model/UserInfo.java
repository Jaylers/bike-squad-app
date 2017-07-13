package com.jaylerrs.bikesquad.users.model;

/**
 * Created by jaylerr on 13-Jul-17.
 */

public class UserInfo {
    String email;
    String username;
    String birthDate;
    String weight;
    String height;
    Boolean privacy;

    public UserInfo() {
    }

    public UserInfo(String email, String username, String birthDate, String weight, String height, Boolean privacy) {
        this.email = email;
        this.username = username;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.privacy = privacy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
