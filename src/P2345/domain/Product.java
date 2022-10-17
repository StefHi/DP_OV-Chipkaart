package P2345.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<Integer> ovChipkaartnummers;

    public Product(int nummer, String naam, String beschrijving, double prijs) {
        this.nummer = nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        ovChipkaartnummers = new ArrayList<>();
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<Integer> getOvChipkaartnummers() {
        return ovChipkaartnummers;
    }

    public void setOvChipkaartnummers(List<Integer> ovChipkaartnummers) {
        this.ovChipkaartnummers = ovChipkaartnummers;
    }

    public boolean addKaartnummer (Integer kaartnummer) {
        if (!ovChipkaartnummers.contains(kaartnummer)) {
            return ovChipkaartnummers.add(kaartnummer);
        }
        return false;
    }

    public boolean removekaartnummer(Integer kaartnummer) {
        if (ovChipkaartnummers.contains(kaartnummer)) {
            return ovChipkaartnummers.remove(kaartnummer);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Product #%d: %s %s €%s", nummer, naam, beschrijving, prijs);
    }
}
