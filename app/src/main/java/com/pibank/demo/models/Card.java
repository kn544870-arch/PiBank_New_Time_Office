package com.pibank.demo.models;

public class Card {
    private String cardId;
    private String userId;
    private String cardNumber;
    private String cardType;
    private String cardHolder;
    private String expiryDate;
    private String cvv;
    private double balance;
    private boolean isBlocked;
    private long createdAt;

    public static final String TYPE_DEBIT = "debit";
    public static final String TYPE_CREDIT = "credit";
    public static final String TYPE_KIDS = "kids";

    public Card() {
    }

    public Card(String cardId, String userId, String cardType, String cardHolder) {
        this.cardId = cardId;
        this.userId = userId;
        this.cardNumber = generateCardNumber();
        this.cardType = cardType;
        this.cardHolder = cardHolder;
        this.expiryDate = generateExpiryDate();
        this.cvv = generateCVV();
        this.balance = 0.0;
        this.isBlocked = false;
        this.createdAt = System.currentTimeMillis();
    }

    private String generateCardNumber() {
        StringBuilder number = new StringBuilder("4276");
        for (int i = 0; i < 12; i++) {
            number.append((int) (Math.random() * 10));
        }
        return formatCardNumber(number.toString());
    }

    private String formatCardNumber(String number) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(number.charAt(i));
        }
        return formatted.toString();
    }

    private String generateExpiryDate() {
        int month = (int) (Math.random() * 12) + 1;
        int year = 26 + (int) (Math.random() * 5);
        return String.format("%02d/%02d", month, year);
    }

    private String generateCVV() {
        return String.format("%03d", (int) (Math.random() * 900) + 100);
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
