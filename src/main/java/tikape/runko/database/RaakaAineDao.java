package tikape.runko.database;

import java.sql.*;
import java.util.*;
import tikape.runko.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    public RaakaAine findByName(String name) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM RaakaAine WHERE nimi=?");
        stmt.setString(1, name);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            rs.close();
            stmt.close();

            con.close();

            return null;
        }
        RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));

        rs.close();
        stmt.close();

        con.close();

        return r;
    }

    public List<RaakaAine> findNameLike(String name) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM RaakaAine WHERE nimi LIKE ?");
        stmt.setString(1, "%" + name + "%");

        List<RaakaAine> aineet = new ArrayList<>();

        ResultSet rs = stmt.executeQuery();
        /*if (!rs.next()) {
            rs.close();
            stmt.close();

            con.close();

            return null;
        }*/

        while (rs.next()) {
            RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
            aineet.add(r);
        }

        rs.close();
        stmt.close();

        con.close();

        return aineet;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM RaakaAine WHERE id=?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            rs.close();
            stmt.close();

            con.close();

            return null;
        }
        RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));

        rs.close();
        stmt.close();

        con.close();
        return r;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM RaakaAine");
        List<RaakaAine> aineet = new ArrayList();

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            RaakaAine r = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
            aineet.add(r);
        }
        rs.close();
        stmt.close();

        con.close();

        return aineet;
    }

    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        RaakaAine rkaine = findByName(object.nimi);

        if (rkaine == null) {
            return save(object);
        }
        return rkaine;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        con.close();
    }

    private RaakaAine save(RaakaAine aine) throws SQLException {
        Connection con = database.getConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO RaakaAine"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, aine.nimi);

        stmt.executeUpdate();
        stmt.close();

        stmt = con.prepareStatement("SELECT * FROM RaakaAine WHERE nimi=?");
        stmt.setString(1, aine.nimi);

        ResultSet rs = stmt.executeQuery();
        rs.next();

        aine.id = rs.getInt("id");

        stmt.close();
        rs.close();

        con.close();

        return aine;
    }

    private RaakaAine update(RaakaAine aine) throws SQLException {
        Connection con = database.getConnection();

        PreparedStatement stmt = con.prepareStatement("UPDATE RaakaAine SET nimi=? WHERE id=?");
        stmt.setString(1, aine.nimi);
        stmt.setInt(2, aine.id);

        stmt.executeUpdate();

        stmt.close();

        con.close();

        return aine;
    }
}
