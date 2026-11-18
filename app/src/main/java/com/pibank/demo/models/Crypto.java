package com.pibank.demo.models;

public class Crypto {
    private String cryptoId;
    private String userId;
    private String cryptoName;
    private String symbol;
    private double amount;
    private double priceUSD;
    private long lastUpdated;

    public Crypto() {
    }

    public Crypto(String cryptoId, String userId, String cryptoName, String symbol, double amount, double priceUSD) {
        this.cryptoId = cryptoId;
        this.userId = userId;
        this.cryptoName = cryptoName;
        this.symbol = symbol;
        this.amount = amount;
        this.priceUSD = priceUSD;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(double priceUSD) {
        this.priceUSD = priceUSD;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getTotalValueUSD() {
        return amount * priceUSD;
    }
}
