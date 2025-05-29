package com.stockmarket.model;

// Abstrakcyjna klasa bazowa dla wszystkich aktywów: akcje, obligacje itp.
public abstract class Asset {
    // Symbol aktywa, np. "BTC" – musi być unikalny
    protected String symbol;

    // Pełna nazwa aktywa, np. "Bitcoin"
    protected String name;

    // Aktualna cena aktywa (może się zmieniać)
    protected double currentPrice;

    // Konstruktor z walidacją danych wejściowych
    public Asset(String symbol, String name, double currentPrice) {
        if (symbol == null || name == null) {
            throw new IllegalArgumentException("Symbol i nazwa nie mogą być null");
        }
        if (currentPrice < 0) {
            throw new IllegalArgumentException("Cena nie może być ujemna");
        }

        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    // Getter symbolu
    public String getSymbol() {
        return symbol;
    }

    // Getter nazwy
    public String getName() {
        return name;
    }

    // Getter aktualnej ceny
    public double getCurrentPrice() {
        return currentPrice;
    }

    // Abstrakcyjna metoda – każda klasa dziedzicząca musi ją zaimplementować
    public abstract void updatePrice();

    // Equals – porównujemy aktywa po symbolu (symbol = identyfikator)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Asset)) return false;
        Asset other = (Asset) obj;
        return symbol.equals(other.symbol);
    }

    // HashCode – również na podstawie symbolu
    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}