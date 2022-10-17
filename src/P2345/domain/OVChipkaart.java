package P2345.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldig_tot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    private List<Product> products;

    public OVChipkaart(int kaartnummer, Date geldig_tot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartnummer = kaartnummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.products = new ArrayList<>();
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean addProduct (Product product) {
        if (!products.contains(product)) {
            products.add(product);
            return product.addKaartnummer(kaartnummer);
        }
        return false;
    }

    public boolean removeProduct(Product product) {
        if (products.contains(product)) {
            return products.remove(product) && product.removekaartnummer(kaartnummer);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OVChipkaart that = (OVChipkaart) o;
        return kaartnummer == that.kaartnummer && klasse == that.klasse && Double.compare(that.saldo,
                                                                                          saldo) == 0 && Objects.equals(
                geldig_tot, that.geldig_tot) && Objects.equals(reiziger, that.reiziger);
    }

    @Override
    public String toString() {
        StringBuilder s;
        s = new StringBuilder(String.format("#%d: geldig tot %s, klasse %d, saldo €%s", kaartnummer, geldig_tot,
                                            klasse, saldo));
        if (!products.isEmpty()) {
            for (Product product : products) {
                s.append("\n").append(product.toString());
            }
        }

        return s.toString();
    }
}
