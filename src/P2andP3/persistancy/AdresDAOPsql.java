package P2andP3.persistancy;

import P2andP3.domain.Adres;
import P2andP3.domain.Reiziger;
import P2andP3.interfaces.AdresDAO;
import P2andP3.interfaces.ReizigerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        String q = "INSERT INTO adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, adres.getId());
        pst.setString(2, adres.getPostcode());
        pst.setString(3, adres.getHuisnummer());
        pst.setString(4, adres.getStraat());
        pst.setString(5, adres.getWoonplaats());
        pst.setInt(6, adres.getReiziger().getId());

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String q = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE " +
                "adres_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, adres.getPostcode());
        pst.setString(2, adres.getHuisnummer());
        pst.setString(3, adres.getStraat());
        pst.setString(4, adres.getWoonplaats());
        pst.setInt(5, adres.getReiziger().getId());
        pst.setInt(6, adres.getId());

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        String q = "DELETE FROM adres WHERE adres_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, adres.getId());

        return pst.executeUpdate() > 0;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String q = "SELECT * FROM adres WHERE reiziger_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, reiziger.getId());
        ResultSet rs = pst.executeQuery();
        Adres adres = null;
        if (rs.next()) {
            adres = new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    reiziger
            );
        }
        return adres;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String q = "SELECT * FROM adres";
        PreparedStatement pst = conn.prepareStatement(q);
        ResultSet rs = pst.executeQuery();
        List<Adres> adresList = new ArrayList<>();
        while (rs.next()) {
            Adres adres = new Adres(
                rs.getInt("adres_id"),
                rs.getString("postcode"),
                rs.getString("huisnummer"),
                rs.getString("straat"),
                rs.getString("woonplaats"),
                rdao.findById((rs.getInt("reiziger_id")))
            );
            adresList.add(adres);
        }
        return adresList;
    }
}
