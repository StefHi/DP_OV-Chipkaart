package P1;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        int id;
        String voorletters;
        String tussenvoegsel;
        String achternaam;
        Date geboortedatum;

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();

            String query = "SELECT * FROM reiziger";
            ResultSet rs = st.executeQuery(query);

            System.out.println("Alle reizigers:");

            while (rs.next()) {
                id = rs.getInt("reiziger_id");
                voorletters = rs.getString("voorletters");
                tussenvoegsel = rs.getString("tussenvoegsel");
                achternaam = rs.getString("achternaam");
                geboortedatum = rs.getDate("geboortedatum");


                System.out.printf("\t#%d: %s. %s%s (%s)%n", id, voorletters, (tussenvoegsel != null) ?
                        (tussenvoegsel + " ") : "", achternaam, geboortedatum);
            }

            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
