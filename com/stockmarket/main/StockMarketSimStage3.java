package com.stockmarket.main;

import com.stockmarket.exception.AssetNotFoundException;
import com.stockmarket.exception.InsufficientAssetsException;
import com.stockmarket.exception.InsufficientFundsException;
import com.stockmarket.market.Market;
import com.stockmarket.model.Asset;
import com.stockmarket.model.Bond;
import com.stockmarket.model.Stock;
import com.stockmarket.portfolio.Portfolio;
import com.stockmarket.portfolio.PortfolioPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockMarketSimStage3 {
    public static void main(String[] args) {
        // Tworzymy aktywa
        List<Asset> aktywa = new ArrayList<>();
        aktywa.add(new Stock("BTC", "Bitcoin", 91000.0));
        aktywa.add(new Stock("ETH", "Ethereum", 1698.0));
        aktywa.add(new Stock("PMS", "ProfesorMiotkSzef", 9999.99));
        aktywa.add(new Bond("OBL1", "Obligacja Skarbowa 10Y", 1000.0, 0.01));

        // Tworzymy rynek
        Market rynek = new Market(aktywa);

        // Tworzymy portfel z 100k gotówki
        Portfolio portfel = new Portfolio(100000.0);

        // Zakupy – testujemy buy()
        try {
            portfel.buy("BTC", 2, rynek);
            portfel.buy("ETH", 10, rynek);
            portfel.buy("PMS", 1, rynek);
            portfel.buy("OBL1", 5, rynek);
        } catch (InsufficientFundsException | AssetNotFoundException e) {
            System.out.println("Błąd podczas kupna: " + e.getMessage());
        }

        // Sprzedaż – testujemy sell()
        try {
            portfel.sell("ETH", 5, rynek); // sprzedajemy połowę ETH
        } catch (InsufficientAssetsException | AssetNotFoundException e) {
            System.out.println("Błąd podczas sprzedaży: " + e.getMessage());
        }

        // Symulacja 10 kroków zmian cen
        for (int i = 1; i <= 10; i++) {
            System.out.println("\n=== Krok symulacji: " + i + " ===");
            rynek.updatePrices();
            for (Asset a : rynek.getAllAssets().values()) {
                System.out.printf("%s (%s): %.2f PLN\n",
                        a.getSymbol(), a.getName(), a.getCurrentPrice());
            }
            System.out.printf("Wartość portfela: %.2f PLN\n", portfel.calculateTotalValue());
        }

        // Finalny stan portfela
        System.out.println("\n--- Finalny stan portfela ---");
        System.out.printf("Gotówka: %.2f PLN\n", portfel.getCash());
        for (Map.Entry<String, PortfolioPosition> entry : portfel.getPositions().entrySet()) {
            String symbol = entry.getKey();
            PortfolioPosition pos = entry.getValue();
            double val = pos.getAsset().getCurrentPrice() * pos.getQuantity();
            System.out.printf("- %s (%s): %d szt. @ %.2f PLN = %.2f PLN\n",
                    symbol, pos.getAsset().getName(), pos.getQuantity(), pos.getAsset().getCurrentPrice(), val);
        }
        System.out.printf("Wartość aktywów: %.2f PLN\n", portfel.calculateAssetsValue());
        System.out.printf("Całkowita wartość portfela: %.2f PLN\n", portfel.calculateTotalValue());
    }
}