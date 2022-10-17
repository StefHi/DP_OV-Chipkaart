package P2345.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private List<OVChipkaart> ovChipkaarts;
    private Adres adres;

    public Reiziger(int id, String voorletters, String tussenvoegsels, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsels;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.ovChipkaarts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getNaam() {
        return voorletters + " " + ((tussenvoegsel != null) ? (tussenvoegsel + " ") : "") + achternaam;
    }

    public List<OVChipkaart> getOvChipkaarts() {
        return ovChipkaarts;
    }

    public void setOvChipkaarts(List<OVChipkaart> ovChipkaarts) {
        this.ovChipkaarts = ovChipkaarts;
    }

    public boolean addOvChipkaart (OVChipkaart ovChipkaart) {
        if (!ovChipkaarts.contains(ovChipkaart)) {
            return ovChipkaarts.add(ovChipkaart);
        }
        return false;
    }

    public boolean removeOvChipkaart (OVChipkaart ovChipkaart) {
        if (ovChipkaarts.contains(ovChipkaart)) {
            return ovChipkaarts.remove(ovChipkaart);
        }
        return false;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    @Override
    public String toString() {
        StringBuilder s;
        s = new StringBuilder(String.format("Reiziger: #%d: %s (%s)", id, getNaam(), geboortedatum));
        if (adres != null) {
            s.append("\n").append(adres.toString());
        }
        if (!ovChipkaarts.isEmpty()) {
            for (OVChipkaart ovChipkaart : ovChipkaarts) {
                s.append("\n").append(ovChipkaart.toString());
            }
        }
        return s.toString();
    }
}
