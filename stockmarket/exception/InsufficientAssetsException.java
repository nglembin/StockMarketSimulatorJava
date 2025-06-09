package com.stockmarket.exception;

// Wyjątek rzucany, gdy próbujemy sprzedać więcej aktywów niż posiadamy
public class InsufficientAssetsException extends Exception {
    public InsufficientAssetsException(String message) {
        super(message);
    }
}
