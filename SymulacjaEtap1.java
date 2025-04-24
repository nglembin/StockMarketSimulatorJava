import java.util.HashMap; // używany mapy akcji w portfolio
import java.util.Map; // używany do interfejsu map i entry
import java.util.Objects; // do equals i hashcode

// klasa główna – czyli miejsce gdzie wszystko się odpala
public class SymulacjaEtap1 {
    public static void main(String[] args) {
        // akcje, musi byc CDR i PKO czy nie?
        Stock btc = new Stock("BTC", "Bitcoin", 91000.0); // hodling to the moon!
        Stock eth = new Stock("ETH", "Ethereum", 1698.0); // ETH 
        Stock pms = new Stock("PMS", "ProfesorMiotkSzef", 9999.99); // :D

        // robimy nowy portfel z gotówką na start
        Portfolio portfolio = new Portfolio(100000.0); // mamy 100k żeby poszaleć

        // dodajemy akcje do portfela
        portfolio.addStock(btc, 2); // to the moon!
        portfolio.addStock(eth, 10); // eth
        portfolio.addStock(pms, 1); // 1 bo to edycja limitowana

        // liczymy ile są warte nasze akcje
        double stockValue = portfolio.calculateStockValue();

        // liczymy całkowitą wartość portfela (czyli akcje + gotówka)
        double totalValue = portfolio.calculateTotalValue();

        // wypisujemy stan portfela
        System.out.println("--- Stan Portfela ---");
        System.out.println("Gotówa: " + portfolio.getCash() + " PLN");
        System.out.println("Posiadanie akcje:");
        for (Map.Entry<Stock, Integer> entry : portfolio.getStocksInPortfolio().entrySet()) {
            Stock s = entry.getKey();
            int quantity = entry.getValue();
            double value = s.getInitialPrice() * quantity;

            System.out.println("- " + s.getSymbol() + " (" + s.getName() + "): " +
                    quantity + " units @ " + s.getInitialPrice() + " PLN = " + value + " PLN");
        }

        // podsumowanie
        System.out.println("Wartość akcji: " + stockValue + " PLN");
        System.out.println("Całkowita wartość portfela łącznie z gotówą: " + totalValue + " PLN");
    }
}

// klasa reprezentująca jedną akcję np. CDR albo PKO
class Stock {
    // symbol akcji np. CDR (musi być unikalny)
    private String symbol;

    // pełna nazwa akcji np. CD Projekt
    private String name;

    // cena 1 sztuki na starcie symulacji
    private double initialPrice;

    // konstruktor czyli sposób tworzenia nowej akcji z danymi
    public Stock(String symbol, String name, double initialPrice) {
        this.symbol = symbol;
        this.name = name;
        this.initialPrice = initialPrice;
    }

    // getter do pobrania symbolu
    public String getSymbol() {
        return symbol;
    }

    // getter do pobrania pełnej nazwy
    public String getName() {
        return name;
    }

    // getter do pobrania ceny początkowej
    public double getInitialPrice() {
        return initialPrice;
    }

    // equals i hashCode są potrzebne żeby akcje mogły być użyte jako klucze w mapie
    // porównujemy akcje po symbolu bo symbol jest unikalny
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Stock)) return false;
        Stock stock = (Stock) obj;
        return Objects.equals(symbol, stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}

// klasa która trzyma wszystkie akcje i kasę użytkownika czyli cały jego portfel
class Portfolio {
    // ile mamy kasy w portfelu (na razie tego nie ruszamy ale warto mieć)
    private double cash;

    // mapa akcji i ile ich mamy np. CDR -> 10 sztuk tak jak w treści było napisane
    private Map<Stock, Integer> stocksInPortfolio;

    // konstruktor portfela – ustawia startową gotówkę i tworzy pustą mapę na akcje
    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.stocksInPortfolio = new HashMap<>();
    }
    
    // metoda dodająca akcję do portfela
    // jeśli mamy już tą akcję to zwiększamy ilość
    // jeśli nie mamy to dodajemy nową pozycję
    public void addStock(Stock stock, int quantity) {
        stocksInPortfolio.put(stock, stocksInPortfolio.getOrDefault(stock, 0) + quantity);
    }

    // zwraca ile mamy gotówki
    public double getCash() {
        return cash;
    }

    // zwraca mapę z naszymi akcjami (ale tylko do odczytu)
    public Map<Stock, Integer> getStocksInPortfolio() {
        return Map.copyOf(stocksInPortfolio);
    }

    // oblicza łączną wartość posiadanych akcji (cena * ilość)
    public double calculateStockValue() {
        double total = 0.0;
        for (Map.Entry<Stock, Integer> entry : stocksInPortfolio.entrySet()) {
            Stock stock = entry.getKey();
            int quantity = entry.getValue();
            total += stock.getInitialPrice() * quantity;
        }
        return total;
    }

    // zwraca łączną wartość portfela (czyli wartość akcji + gotówka)
    public double calculateTotalValue() {
        return cash + calculateStockValue();
    }
}
