package com.example.daltascallapp.UserModels;

public class UserClass {
    private String name,profile,city,uniqueID;

    public UserClass() {
    }

    public UserClass(String name, String profile, String city, String uniqueID) {
        this.name = name;
        this.profile = profile;
        this.city = city;
        this.uniqueID = uniqueID;
    }

    public UserClass(String name, String city, String uniqueID) {
        this.name = name;

        this.city = city;
        this.uniqueID = uniqueID;
    }


    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
