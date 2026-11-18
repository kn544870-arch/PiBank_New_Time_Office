package com.pibank.demo.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return "₽" + formatter.format(amount);
    }

    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String formatDateShort(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            return cardNumber;
        }
        String cleaned = cardNumber.replace(" ", "");
        return cleaned.substring(0, 4) + " **** **** " + cleaned.substring(12);
    }

    public static String getCardTypeDisplayName(String cardType) {
        switch (cardType) {
            case "debit":
                return "Дебетовая карта";
            case "credit":
                return "Кредитная карта";
            case "kids":
                return "Детская карта";
            default:
                return "Карта";
        }
    }

    public static String getTransactionTypeDisplayName(String type) {
        switch (type) {
            case "deposit":
                return "Пополнение";
            case "withdrawal":
                return "Снятие";
            case "transfer":
                return "Перевод";
            case "payment":
                return "Оплата";
            default:
                return "Операция";
        }
    }
}
