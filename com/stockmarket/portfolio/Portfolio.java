package com.stockmarket.portfolio;

import com.stockmarket.exception.AssetNotFoundException;
import com.stockmarket.exception.InsufficientAssetsException;
import com.stockmarket.exception.InsufficientFundsException;
import com.stockmarket.market.Market;
import com.stockmarket.market.Tradable;
import com.stockmarket.model.Asset;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// klasa reprezentująca portfel użytkownika – trzyma aktywa i gotówkę
public class Portfolio {
    private double cash; // aktualna gotówka
    private Map<String, PortfolioPosition> positions; // symbol → pozycja

    // konstruktor – ustawia początkową gotówkę i pustą mapę
    public Portfolio(double initialCash) {
        if (initialCash < 0) {
            throw new IllegalArgumentException("Gotówka nie może być ujemna");
        }
        this.cash = initialCash;
        this.positions = new HashMap<>();
    }

    // kupno aktywa z rynku
    public void buy(String symbol, int quantity, Market market)
            throws InsufficientFundsException, AssetNotFoundException {

        // pobieramy aktywo z rynku
        Asset asset = market.getAsset(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Aktywo nie istnieje na rynku: " + symbol));

        // Sprawdzamy, czy aktywo można handlować
        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo nie jest handlowalne: " + symbol);
        }

        // Rzutowanie ręczne (bo nie mamy Java 16+)
        Tradable tradable = (Tradable) asset;
        double cost = tradable.getCurrentPrice() * quantity;


        // sprawdzamy czy mamy wystarczająco gotówki
        if (cash < cost) {
            throw new InsufficientFundsException("Brak wystarczającej gotówki: " + cost + " PLN");
        }

        // odejmujemy gotówkę i dodajemy pozycję
        cash -= cost;
        addOrUpdatePosition(asset, quantity);
    }

    // sprzedaż aktywa
    public void sell(String symbol, int quantity, Market market)
            throws AssetNotFoundException, InsufficientAssetsException {

        // sprawdzamy czy posiadamy daną pozycję
        PortfolioPosition pos = positions.get(symbol);
        if (pos == null) {
            throw new AssetNotFoundException("Nie posiadasz aktywa: " + symbol);
        }

        // sprawdzamy czy mamy wystarczającą ilość sztuk
        if (quantity > pos.getQuantity()) {
            throw new InsufficientAssetsException("Próbujesz sprzedać więcej niż masz: " + quantity);
        }

        // pobieramy aktualne dane rynkowe
        Asset asset = market.getAsset(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Aktywo nie istnieje na rynku: " + symbol));

        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo nie jest handlowalne: " + symbol);
        }

        Tradable tradable = (Tradable) asset;
        double income = tradable.getCurrentPrice() * quantity;


        // zwiększamy gotówkę i zmniejszamy ilość pozycji
        cash += income;
        removeOrUpdatePosition(symbol, pos, quantity);
    }

    // pomocnicza metoda do dodawania/aktualizacji pozycji w portfelu
    private void addOrUpdatePosition(Asset asset, int quantity) {
        String symbol = asset.getSymbol();
        if (positions.containsKey(symbol)) {
            PortfolioPosition existing = positions.get(symbol);
            int newQty = existing.getQuantity() + quantity;
            positions.put(symbol, new PortfolioPosition(asset, newQty));
        } else {
            positions.put(symbol, new PortfolioPosition(asset, quantity));
        }
    }

    // pomocnicza metoda do zmniejszania lub usuwania pozycji
    private void removeOrUpdatePosition(String symbol, PortfolioPosition pos, int quantityToRemove) {
        int newQty = pos.getQuantity() - quantityToRemove;
        if (newQty > 0) {
            positions.put(symbol, new PortfolioPosition(pos.getAsset(), newQty));
        } else {
            positions.remove(symbol);
        }
    }

    // zwraca aktualną wartość aktywów (ilość * cena bieżąca)
    public double calculateAssetsValue() {
        double total = 0.0;
        for (PortfolioPosition pos : positions.values()) {
            total += pos.getAsset().getCurrentPrice() * pos.getQuantity();
        }
        return total;
    }

    // zwraca łączną wartość portfela (aktywa + gotówka)
    public double calculateTotalValue() {
        return cash + calculateAssetsValue();
    }

    // getter gotówki
    public double getCash() {
        return cash;
    }

    // getter pozycji (niemodyfikowalna mapa)
    public Map<String, PortfolioPosition> getPositions() {
        return Collections.unmodifiableMap(positions);
    }
}