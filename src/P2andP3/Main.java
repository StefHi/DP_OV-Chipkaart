package P2andP3;

import P2andP3.domain.Adres;
import P2andP3.domain.Reiziger;
import P2andP3.interfaces.AdresDAO;
import P2andP3.interfaces.ReizigerDAO;
import P2andP3.persistancy.AdresDAOPsql;
import P2andP3.persistancy.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = getConnection();
            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
            AdresDAO adresDAO = new AdresDAOPsql(connection, reizigerDAO);
            testReizigerDAO(reizigerDAO);
            testAdresDAO(adresDAO, reizigerDAO);
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        return DriverManager.getConnection(url, username, password);
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update reiziger
        sietske.setAchternaam("Stads");
        System.out.print("[Test] Eerst was de achternaam van de reiziger " + rdao.findById(77).getAchternaam() + ", ");
        rdao.update(sietske);
        System.out.println("na ReizigerDAO.update() " + rdao.findById(77).getAchternaam() + "\n");

        // Delete reiziger
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adresList = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adresList) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuwe reiziger met adres aan em persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(sietske);
        System.out.print("[Test] Eerst " + adresList.size() + " reizigers, na ReizigerDAO.save() ");
        Adres adrSietske = new Adres(6, "1234AB", "1", "eenstraat", "utrecht", rdao.findById(77));
        adao.save(adrSietske);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen\n");

        // Update adres
        adrSietske.setHuisnummer("2");
        System.out.print("[Test] Eerst was het huisnummer van de reiziger " + adao.findByReiziger(sietske).getHuisnummer() +
                                 ", ");
        adao.update(adrSietske);
        System.out.println("na AdresDAO.update() " + adao.findByReiziger(sietske).getHuisnummer() + "\n");

        // Delete adres
        System.out.print("[Test] Eerst " + adresList.size() + " reizigers, na ReizigerDAO.delete() ");
        adao.delete(adrSietske);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen");
        rdao.delete(sietske);
    }
}
