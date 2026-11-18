package com.pibank.demo.models;

public class Transaction {
    private String transactionId;
    private String userId;
    private String cardId;
    private String type;
    private double amount;
    private String description;
    private long timestamp;

    public static final String TYPE_DEPOSIT = "deposit";
    public static final String TYPE_WITHDRAWAL = "withdrawal";
    public static final String TYPE_TRANSFER = "transfer";
    public static final String TYPE_PAYMENT = "payment";

    public Transaction() {
    }

    public Transaction(String transactionId, String userId, String cardId, String type, double amount, String description) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.cardId = cardId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
