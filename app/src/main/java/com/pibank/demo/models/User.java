package com.pibank.demo.models;

public class User {
    private String userId;
    private String email;
    private String name;
    private double balance;
    private boolean isAdmin;
    private long createdAt;

    public User() {
    }

    public User(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.balance = 0.0;
        this.isAdmin = false;
        this.createdAt = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
