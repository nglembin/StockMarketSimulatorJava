package com.stockmarket.exception;

// Wyjątek, gdy nie mamy wystarczająco kasy na zakup aktywa
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message); // przekazujemy wiadomość do klasy nadrzędnej (Exception)
    }
}
