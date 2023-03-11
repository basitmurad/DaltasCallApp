package com.example.daltascallapp.UserModels;

public class UserClass {
    private String name,profile,uniqueID;

    public UserClass() {
    }

    public UserClass(String name, String profile, String uniqueID) {
        this.name = name;
        this.profile = profile;
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

}
