import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

// tu zaczyna się cały projekt w jednym pliku więc nie musisz nic importować z innych plików

// klasa reprezentująca jedną akcję np. CDR albo PKO
class Akcja {
    // symbol akcji np. CDR (musi być unikalny)
    private String symbol;

    // pełna nazwa akcji np. CD Projekt
    private String nazwa;

    // cena 1 sztuki na starcie symulacji
    private double cenaPoczatkowa;

    // konstruktor czyli sposób tworzenia nowej akcji z danymi
    public Akcja(String symbol, String nazwa, double cenaPoczatkowa) {
        this.symbol = symbol;
        this.nazwa = nazwa;
        this.cenaPoczatkowa = cenaPoczatkowa;
    }

    // getter do pobrania symbolu
    public String pobierzSymbol() {
        return symbol;
    }

    // getter do pobrania pełnej nazwy
    public String pobierzNazwe() {
        return nazwa;
    }

    // getter do pobrania ceny początkowej
    public double pobierzCene() {
        return cenaPoczatkowa;
    }

    // equals i hashCode są potrzebne żeby akcje mogły być użyte jako klucze w mapie
    // porównujemy akcje po symbolu bo symbol jest unikalny
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Akcja)) return false;
        Akcja inna = (Akcja) o;
        return symbol.equals(inna.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}

// klasa która trzyma wszystkie akcje i kasę użytkownika czyli cały jego portfel
class Portfel {
    // ile mamy hajsu w portfelu (na razie tego nie ruszamy ale warto mieć)
    private double gotowka;

    // mapa akcji i ile ich mamy np. CDR -> 10 sztuk
    private Map<Akcja, Integer> akcjeWPortfelu;

    // konstruktor portfela – ustawia startową gotówkę i tworzy pustą mapę na akcje
    public Portfel(double gotowkaStartowa) {
        this.gotowka = gotowkaStartowa;
        this.akcjeWPortfelu = new HashMap<>();
    }

    // metoda dodająca akcję do portfela
    // jeśli mamy już tą akcję to zwiększamy ilość
    // jeśli nie mamy to dodajemy nową pozycję
    public void dodajAkcje(Akcja akcja, int ilosc) {
        if (akcjeWPortfelu.containsKey(akcja)) {
            int obecnaIlosc = akcjeWPortfelu.get(akcja);
            akcjeWPortfelu.put(akcja, obecnaIlosc + ilosc);
        } else {
            akcjeWPortfelu.put(akcja, ilosc);
        }
        // ważne – na tym etapie nie zmniejszamy gotówki więc luzik
    }

    // zwraca ile mamy gotówki
    public double pobierzGotowke() {
        return gotowka;
    }

    // zwraca mapę z naszymi akcjami (ale tylko do odczytu)
    public Map<Akcja, Integer> pobierzAkcje() {
        return Collections.unmodifiableMap(akcjeWPortfelu);
    }

    // oblicza łączną wartość posiadanych akcji (cena * ilość)
    public double obliczWartoscAkcji() {
        double suma = 0.0;
        for (Map.Entry<Akcja, Integer> wpis : akcjeWPortfelu.entrySet()) {
            double cena = wpis.getKey().pobierzCene();
            int ilosc = wpis.getValue();
            suma += cena * ilosc;
        }
        return suma;
    }

    // zwraca łączną wartość portfela (czyli wartość akcji + gotówka)
    public double obliczCalkowitaWartosc() {
        return obliczWartoscAkcji() + gotowka;
    }
}

// klasa główna z metodą main – czyli miejsce gdzie wszystko się odpala
public class SymulacjaEtap1 {
    public static void main(String[] args) {
        // akcje, musi byc CDR i PKO czy nie?
        Akcja btc = new Akcja("BTC", "Bitcoin", 91000.0); // hodling to the moon!
        Akcja eth = new Akcja("ETH", "Ethereum", 1698.0); // ETH gleba 
        Akcja pms = new Akcja("PMS", "ProfesorMiotkSzef", 9999.99); // :D

        // robimy nowy portfel z gotówką na start
        Portfel mojPortfel = new Portfel(100000.0); // mamy 100k żeby poszaleć

        // dodajemy akcje do portfela
        mojPortfel.dodajAkcje(btc, 2); // 2 bitcoiny (grubo)
        mojPortfel.dodajAkcje(eth, 10); // 10 ethereum
        mojPortfel.dodajAkcje(pms, 1); // 1 PMS bo to limitowana edycja szefa

        // liczymy ile są warte nasze akcje
        double wartoscAkcji = mojPortfel.obliczWartoscAkcji();

        // liczymy całkowitą wartość portfela (czyli akcje + gotówka)
        double wartoscCalkowita = mojPortfel.obliczCalkowitaWartosc();

        // wypisujemy na konsolę wszystko co mamy w portfelu
        System.out.println("--- STAN PORTFELA ---");
        System.out.println("Gotówa: " + mojPortfel.pobierzGotowke() + " PLN");

        // wypisujemy każdą akcję z ilością i wartością
        System.out.println("Posiadane akcje:");
        for (Map.Entry<Akcja, Integer> wpis : mojPortfel.pobierzAkcje().entrySet()) {
            Akcja a = wpis.getKey();      // bierzemy akcję
            int ilosc = wpis.getValue();  // i ile jej mamy
            double cena = a.pobierzCene();
            double wartosc = cena * ilosc;

            // pokazujemy każdą akcję w ładnym formacie
            System.out.println("- " + a.pobierzSymbol() + " (" + a.pobierzNazwe() + "): " +
                    ilosc + " szt. @ " + cena + " PLN/szt. = " + wartosc + " PLN");
        }

        // na koniec podsumowanie
        System.out.println("Wartość akcji: " + wartoscAkcji + " PLN");
        System.out.println("Wartość calkowita portfela: " + wartoscCalkowita + " PLN");
    }
}