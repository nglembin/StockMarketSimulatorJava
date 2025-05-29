package com.stockmarket.model;

import com.stockmarket.market.Tradable;

// Klasa reprezentująca akcje (np. BTC, ETH) – dziedziczy po Asset i implementuje Tradable
public class Stock extends Asset implements Tradable {

    // Konstruktor – przekazuje dane do klasy bazowej
    public Stock(String symbol, String name, double currentPrice) {
        super(symbol, name, currentPrice);
    }

    // Nadpisana metoda – losowa zmiana ceny o około +/- 5%
    @Override
    public void updatePrice() {
        double changePercent = (Math.random() - 0.5) * 0.1; // przedział -5% do +5%
        currentPrice *= (1.0 + changePercent);
        if (currentPrice < 0) {
            currentPrice = 0; // zabezpieczenie przed ujemną ceną
        }
    }
}
