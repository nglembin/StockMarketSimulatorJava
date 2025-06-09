package com.stockmarket.model;

// Klasa reprezentująca obligacje – dziedziczy po Asset
public class Bond extends Asset {
    // Oprocentowanie np. 0.01 = 1%
    private double interestRate;

    // Konstruktor – ustawia dane i sprawdza poprawność oprocentowania
    public Bond(String symbol, String name, double currentPrice, double interestRate) {
        super(symbol, name, currentPrice);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Oprocentowanie nie może być ujemne");
        }
        this.interestRate = interestRate;
    }

    // Nadpisana metoda – cena obligacji rośnie o stały procent (oprocentowanie)
    @Override
    public void updatePrice() {
        currentPrice *= (1.0 + interestRate);
    }
}