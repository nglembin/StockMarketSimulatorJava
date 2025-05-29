package com.stockmarket.market;

import com.stockmarket.model.Asset;

import java.util.*;

// klasa Market – przechowuje dostępne aktywa na rynku i aktualizuje ich ceny
public class Market {
    // mapa: symbol → obiekt Asset (np. "BTC" → Stock)
    private Map<String, Asset> assetMap;

    // konstruktor – tworzy mapę aktywów na podstawie listy
    public Market(List<Asset> assets) {
        this.assetMap = new HashMap<>();
        for (Asset a : assets) {
            assetMap.put(a.getSymbol(), a); // dodajemy aktywa do mapy po symbolu
        }
    }

    // zwraca Optional z aktywem, jeśli istnieje
    public Optional<Asset> getAsset(String symbol) {
        return Optional.ofNullable(assetMap.get(symbol));
    }

    // aktualizuje ceny wszystkich aktywów (np. wywoływane co krok symulacji)
    public void updatePrices() {
        for (Asset a : assetMap.values()) {
            a.updatePrice(); // każda klasa (Stock/Bond) ma własny sposób
        }
    }

    // zwraca widok mapy aktywów (np. do wypisania na ekranie)
    public Map<String, Asset> getAllAssets() {
        return Collections.unmodifiableMap(assetMap);
    }
}