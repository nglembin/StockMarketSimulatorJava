package com.stockmarket.market;

// interfejs definiujący, że coś może być handlowane (np. akcje)
// wymusza implementację metod: getSymbol() i getCurrentPrice()
public interface Tradable {
    String getSymbol();          // unikalny symbol np. "BTC"
    double getCurrentPrice();    // aktualna cena rynkowa
}