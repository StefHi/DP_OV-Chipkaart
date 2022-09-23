package P2;

import P2.domain.Reiziger;
import P2.interfaces.ReizigerDAO;
import P2.persistancy.ReizigerDAOPsql;

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
            testReizigerDAO(reizigerDAO);
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
}
