package com.tealsquares.oscarsdeathrace.entities;


import java.util.HashMap;
import java.util.Map;

//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
public class User {

    String userId;
    String name;
    String emailAddress;
    String photoUrl;
    long lastLoginTimestamp;
    long creationTimestamp;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(long lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("name", name);
        userMap.put("emailAddress", emailAddress);
        userMap.put("photoUrl", photoUrl);
        userMap.put("lastLoginTimestamp", lastLoginTimestamp);
        userMap.put("creationTimestamp", creationTimestamp);
        userMap.put("type", type);
        return userMap;
    }


}
