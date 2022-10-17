package P2345.persistancy;

import P2345.domain.OVChipkaart;
import P2345.domain.Product;
import P2345.domain.Reiziger;
import P2345.interfaces.OVChipkaartDAO;
import P2345.interfaces.ReizigerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
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

        String q2 = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)";
        PreparedStatement pst2 = conn.prepareStatement(q2);
        List< Product> products = ovChipkaart.getProducts();
        if (!products.isEmpty()) {
            for (Product product : products) {
                pst2.setInt(1, ovChipkaart.getKaartnummer());
                pst2.setInt(2, product.getNummer());
                pst2.executeUpdate();
            }
        }

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

        String q2 = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        PreparedStatement pst2 = conn.prepareStatement(q2);
        pst2.setInt(1, ovChipkaart.getKaartnummer());
        pst2.executeUpdate();

        String q3 = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)";
        PreparedStatement pst3 = conn.prepareStatement(q3);
        List<Product> products = ovChipkaart.getProducts();
        if (!products.isEmpty()) {
            for (Product product : products) {
                pst3.setInt(1, ovChipkaart.getKaartnummer());
                pst3.setInt(2, product.getNummer());
                pst3.executeUpdate();
            }
        }

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, ovChipkaart.getKaartnummer());

        String q2 = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        PreparedStatement pst2 = conn.prepareStatement(q2);
        pst2.setInt(1, ovChipkaart.getKaartnummer());
        pst2.executeUpdate();

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

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        String q = "SELECT * FROM ov_chipkaart";
        PreparedStatement pst = conn.prepareStatement(q);
        ResultSet rs = pst.executeQuery();
        List<OVChipkaart> ovChipkaarts = new ArrayList<>();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rdao.findById(rs.getInt("reiziger_id"))
            );
            ovChipkaarts.add(ovChipkaart);
        }
        return ovChipkaarts;
    }
}
