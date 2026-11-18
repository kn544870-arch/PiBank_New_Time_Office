package com.pibank.demo.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private final FirebaseAuth auth;
    private final DatabaseReference database;

    private FirebaseHelper() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public DatabaseReference getUsersRef() {
        return database.child("users");
    }

    public DatabaseReference getCardsRef() {
        return database.child("cards");
    }

    public DatabaseReference getTransactionsRef() {
        return database.child("transactions");
    }

    public DatabaseReference getNewsRef() {
        return database.child("news");
    }

    public DatabaseReference getCryptoRef() {
        return database.child("crypto");
    }

    public String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }
}
