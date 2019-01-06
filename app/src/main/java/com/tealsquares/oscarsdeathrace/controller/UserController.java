package com.tealsquares.oscarsdeathrace.controller;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tealsquares.oscarsdeathrace.entities.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private final String REGULAR_USER = "regular";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    //private ChildEventListener mChildEventListener;

    public UserController() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
    }


    public void storeUser(FirebaseUser firebaseUser) {
        User user = getUserModel(firebaseUser);
        Map<String, Object> userValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        // Below line will update /users/<user-id> path
        childUpdates.put(user.getUserId(), userValues);
        mUsersDatabaseReference.updateChildren(childUpdates);
    }

    private User getUserModel(FirebaseUser firebaseUser) {
        User user = new User();
        user.setName(firebaseUser.getDisplayName());
        user.setUserId(firebaseUser.getUid());
        user.setEmailAddress(firebaseUser.getEmail());
        user.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
        user.setCreationTimestamp(firebaseUser.getMetadata().getCreationTimestamp());
        user.setLastLoginTimestamp(firebaseUser.getMetadata().getLastSignInTimestamp());
        user.setType(REGULAR_USER);
        return user;
    }

}
