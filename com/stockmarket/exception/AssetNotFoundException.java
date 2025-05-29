package com.stockmarket.exception;

// Wyjątek rzucany, gdy aktywo nie istnieje w portfelu lub na rynku
public class AssetNotFoundException extends Exception {
    public AssetNotFoundException(String message) {
        super(message);
    }
}