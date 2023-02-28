package com.example.daltascallapp.UserModels;

public class UserClass {
    private String uniqeID,name,profile,city;

    public UserClass() {
    }

    public UserClass(String uniqeID, String name, String profile, String city) {
        this.uniqeID = uniqeID;
        this.name = name;
        this.profile = profile;
        this.city = city;
    }

    public String getUniqeID() {
        return uniqeID;
    }

    public void setUniqeID(String uniqeID) {
        this.uniqeID = uniqeID;
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
