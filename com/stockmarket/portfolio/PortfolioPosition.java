package com.stockmarket.portfolio;

import com.stockmarket.model.Asset;

// Rekord – przechowuje aktywo i ilość sztuk w portfelu
public record PortfolioPosition(Asset asset, int quantity) {}