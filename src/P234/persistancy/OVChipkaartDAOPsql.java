package P234.persistancy;

import P234.domain.OVChipkaart;
import P234.domain.Reiziger;
import P234.interfaces.OVChipkaartDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, ovChipkaart.getKaartnummer());
        pst.setDate(2, ovChipkaart.getGeldig_tot());
        pst.setInt(3, ovChipkaart.getKlasse());
        pst.setDouble(4, ovChipkaart.getSaldo());
        pst.setInt(5, ovChipkaart.getReiziger().getId());

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String q = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? " +
                "WHERE kaart_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setDate(1, ovChipkaart.getGeldig_tot());
        pst.setInt(2, ovChipkaart.getKlasse());
        pst.setDouble(3, ovChipkaart.getSaldo());
        pst.setInt(4, ovChipkaart.getReiziger().getId());
        pst.setInt(5, ovChipkaart.getKaartnummer());

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, ovChipkaart.getKaartnummer());

        return pst.executeUpdate() > 0;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, reiziger.getId());
        ResultSet rs = pst.executeQuery();
        List<OVChipkaart> ovChipkaarts = new ArrayList<>();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    reiziger
            );
            ovChipkaarts.add(ovChipkaart);
        }
        return ovChipkaarts;
    }
}
