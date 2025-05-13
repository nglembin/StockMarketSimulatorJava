/*


Nikodem Glembin, s32943 | Etap 2 - Symulacja z dziedziczeniem, polimorfizmem i updatePrice()


*/

Etap 2 – Symulacja z dziedziczeniem, polimorfizmem i updatePrice()
import java.util.*;

public class SymulacjaEtap2 {

    // abstrakcyjna klasa bazowa dla wszystkich aktywów (akcje, obligacje)
    abstract static class Asset {
        protected String symbol;        // unikalny symbol np. "BTC"
        protected String name;          // pełna nazwa np. "Bitcoin"
        protected double currentPrice;  // aktualna cena

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

        public String getSymbol() {
            return symbol;
        }

        public String getName() {
            return name;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }

        // każda klasa pochodna musi zaimplementować tą metodę
        public abstract void updatePrice();

        // porównujemy aktywa po symbolu, bo symbol się nie zmienia
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Asset)) return false;
            Asset other = (Asset) obj;
            return symbol.equals(other.symbol);
        }

        @Override
        public int hashCode() {
            return symbol.hashCode();
        }
    }

    // klasa reprezentująca akcje (np. BTC, ETH) – dziedziczy po Asset
    static class Stock extends Asset {
        public Stock(String symbol, String name, double currentPrice) {
            super(symbol, name, currentPrice);
        }

        // losowa zmiana ceny o około +/- 5%
        @Override
        public void updatePrice() {
            double changePercent = (Math.random() - 0.5) * 0.1;
            currentPrice *= (1.0 + changePercent);
            if (currentPrice < 0) {
                currentPrice = 0;
            }
        }
    }

    // klasa reprezentująca obligacje (np. skarbowe) – dziedziczy po Asset
    static class Bond extends Asset {
        private double interestRate; // np. 0.01 = 1%

        public Bond(String symbol, String name, double currentPrice, double interestRate) {
            super(symbol, name, currentPrice);
            if (interestRate < 0) {
                throw new IllegalArgumentException("Oprocentowanie nie może być ujemne");
            }
            this.interestRate = interestRate;
        }

        // cena obligacji rośnie powoli – symulacja odsetek
        @Override
        public void updatePrice() {
            currentPrice *= (1.0 + interestRate);
        }
    }

    // prosta struktura: aktywo + ilość – nowość w Etapie 2
    public record PortfolioPosition(Asset asset, int quantity) {}

    // portfel trzyma kasę i aktywa różnego typu (Stock, Bond itd.)
    static class Portfolio {
        private double cash;
        private Map<String, PortfolioPosition> positions;

        public Portfolio(double initialCash) {
            if (initialCash < 0) {
                throw new IllegalArgumentException("Gotówka nie może być ujemna");
            }
            this.cash = initialCash;
            this.positions = new HashMap<>();
        }

        // dodajemy aktywo (akcję lub obligację) do portfela
        public void addAsset(Asset asset, int quantity) {
            if (asset == null || quantity <= 0) {
                throw new IllegalArgumentException("Niepoprawne dane wejściowe");
            }
            String symbol = asset.getSymbol();
            if (positions.containsKey(symbol)) {
                PortfolioPosition existing = positions.get(symbol);
                int newQty = existing.quantity() + quantity;
                positions.put(symbol, new PortfolioPosition(asset, newQty));
            } else {
                positions.put(symbol, new PortfolioPosition(asset, quantity));
            }
        }

        // liczy całkowitą wartość aktywów wg bieżących cen
        public double calculateAssetsValue() {
            double total = 0.0;
            for (PortfolioPosition pos : positions.values()) {
                total += pos.asset().getCurrentPrice() * pos.quantity();
            }
            return total;
        }

        public double calculateTotalValue() {
            return cash + calculateAssetsValue();
        }

        public double getCash() {
            return cash;
        }

        public Map<String, PortfolioPosition> getPositions() {
            return Collections.unmodifiableMap(positions);
        }
    }

    // metoda main – symulacja giełdy w 10 krokach
    public static void main(String[] args) {
        // lista aktywów dostępnych na rynku
        List<Asset> market = new ArrayList<>();
        market.add(new Stock("BTC", "Bitcoin", 91000.0));
        market.add(new Stock("ETH", "Ethereum", 1698.0));
        market.add(new Stock("PMS", "ProfesorMiotkSzef", 9999.99));
        market.add(new Bond("OBL1", "Obligacja Skarbowa 10Y", 1000.0, 0.01));

        // tworzymy portfel z 100k gotówki
        Portfolio portfel = new Portfolio(100000.0);
        portfel.addAsset(market.get(0), 2); // BTC
        portfel.addAsset(market.get(1), 10); // ETH
        portfel.addAsset(market.get(2), 1); // PMS
        portfel.addAsset(market.get(3), 5); // Obligacje

        // symulacja zmian cen przez 10 kroków
        for (int i = 1; i <= 10; i++) {
            System.out.println("\n=== Krok symulacji: " + i + " ===");
            for (Asset a : market) {
                a.updatePrice();
                System.out.printf("%s (%s): %.2f PLN\n", a.getSymbol(), a.getName(), a.getCurrentPrice());
            }
            System.out.printf("Wartość portfela: %.2f PLN\n", portfel.calculateTotalValue());
        }

        // finalny raport portfela
        System.out.println("\n--- Finalny stan portfela ---");
        System.out.printf("Gotówka: %.2f PLN\n", portfel.getCash());
        for (PortfolioPosition pos : portfel.getPositions().values()) {
            Asset a = pos.asset();
            int q = pos.quantity();
            double val = a.getCurrentPrice() * q;
            System.out.printf("- %s (%s): %d szt. @ %.2f PLN = %.2f PLN\n",
                    a.getSymbol(), a.getName(), q, a.getCurrentPrice(), val);
        }
        System.out.printf("Wartość aktywów: %.2f PLN\n", portfel.calculateAssetsValue());
        System.out.printf("Wartość całkowita portfela: %.2f PLN\n", portfel.calculateTotalValue());
    }
}