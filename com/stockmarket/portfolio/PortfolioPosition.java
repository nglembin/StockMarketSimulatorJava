package com.stockmarket.portfolio;

import com.stockmarket.model.Asset;

// Klasa przechowująca aktywo i jego ilość
public class PortfolioPosition {
    private final Asset asset;
    private final int quantity;

    public PortfolioPosition(Asset asset, int quantity) {
        this.asset = asset;
        this.quantity = quantity;
    }

    public Asset getAsset() {
        return asset;
    }

    public int getQuantity() {
        return quantity;
    }
}
