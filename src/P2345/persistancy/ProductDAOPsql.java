package P2345.persistancy;

import P2345.domain.OVChipkaart;
import P2345.domain.Product;
import P2345.interfaces.OVChipkaartDAO;
import P2345.interfaces.ProductDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {

    private Connection conn;
    private OVChipkaartDAO odao;

    public ProductDAOPsql (Connection conn, OVChipkaartDAO odao) {
        this.conn = conn;
        this.odao = odao;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String q = "INSERT INTO product(product_nummer, naam, beschrijving, prijs) VALUES(?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, product.getNummer());
        pst.setString(2, product.getNaam());
        pst.setString(3, product.getBeschrijving());
        pst.setDouble(4, product.getPrijs());

        String q2 = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)";
        PreparedStatement pst2 = conn.prepareStatement(q2);
        List<Integer> ovChipkaartnummers = product.getOvChipkaartnummers();
        if (!ovChipkaartnummers.isEmpty()) {
            for (Integer kaartNummer : ovChipkaartnummers) {
                pst2.setInt(1, kaartNummer);
                pst2.setInt(2, product.getNummer());
                pst2.executeUpdate();
            }
        }

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String q = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, product.getNaam());
        pst.setString(2, product.getBeschrijving());
        pst.setDouble(3, product.getPrijs());
        pst.setInt(4, product.getNummer());

        String q2 = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        PreparedStatement pst2 = conn.prepareStatement(q2);
        pst2.setInt(1, product.getNummer());
        pst2.executeUpdate();

        String q3 = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)";
        PreparedStatement pst3 = conn.prepareStatement(q3);
        List<Integer> ovChipkaartnummers = product.getOvChipkaartnummers();
        if (!ovChipkaartnummers.isEmpty()) {
            for (Integer kaartNummer : ovChipkaartnummers) {
                pst3.setInt(1, kaartNummer);
                pst3.setInt(2, product.getNummer());
                pst3.executeUpdate();
            }
        }

        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        String q = "DELETE FROM product WHERE product_nummer = ?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, product.getNummer());

        String q2 = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        PreparedStatement pst2 = conn.prepareStatement(q2);
        pst2.setInt(1, product.getNummer());
        pst2.executeUpdate();

        return pst.executeUpdate() > 0;
    }

    private List<Product> getProducts(ResultSet rs) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("product_nummer"),
                    rs.getString("naam"),
                    rs.getString("bescrhijving"),
                    rs.getDouble("prijs")
            );
            products.add(product);
        }
        return products;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String q = """
                   SELECT *
                   FROM product
                   INNER JOIN ov_chipkaart_product
                   ON ov_chipkaart_product.product_nummer = product.product_nummer
                   WHERE ov_chipkaart_product.kaart_nummer = ?
                   """;
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setInt(1, ovChipkaart.getKaartnummer());
        ResultSet rs = pst.executeQuery();
        return getProducts(rs);
    }

    @Override
    public List<Product> findall() throws SQLException {
        String q = "SELECT * FROM product";
        PreparedStatement pst = conn.prepareStatement(q);
        ResultSet rs = pst.executeQuery();
        return getProducts(rs);
    }
}
