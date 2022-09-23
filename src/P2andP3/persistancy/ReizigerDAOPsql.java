package P2andP3.persistancy;

import P2andP3.domain.Reiziger;
import P2andP3.interfaces.ReizigerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String q = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                "VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, reiziger.getId());
        pst.setString(2, reiziger.getVoorletters());
        pst.setString(3, reiziger.getTussenvoegsel());
        pst.setString(4, reiziger.getAchternaam());
        pst.setDate(5, reiziger.getGeboortedatum());

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String q = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum= ? WHERE " +
                "reiziger_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, reiziger.getVoorletters());
        pst.setString(2, reiziger.getTussenvoegsel());
        pst.setString(3, reiziger.getAchternaam());
        pst.setDate(4, reiziger.getGeboortedatum());
        pst.setInt(5, reiziger.getId());

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, reiziger.getId());

        return pst.executeUpdate() > 0;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        Reiziger reiziger = null;
        if (rs.next()) {
            reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboortedatum")
            );
        }

        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        String q = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, datum);
        return getReizigers(pst);
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String q = "SELECT * FROM reiziger";
        PreparedStatement pst = conn.prepareStatement(q);
        return getReizigers(pst);
    }

    private List<Reiziger> getReizigers(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();
        List<Reiziger> reizigerList = new ArrayList<>();
        while (rs.next()) {
            Reiziger reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboortedatum")
            );
            reizigerList.add(reiziger);
        }

        return reizigerList;
    }
}
