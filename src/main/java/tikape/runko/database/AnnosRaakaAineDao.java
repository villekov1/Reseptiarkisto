package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tikape.runko.Annos;
import tikape.runko.AnnosRaakaAine;
import tikape.runko.RaakaAine;

public class AnnosRaakaAineDao{
    private Database database;
    
    public AnnosRaakaAineDao(Database database){
        this.database = database;
    }
    
    public AnnosRaakaAine findOne(int annos_id, int raaka_aine_id) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_Id = ? AND raaka_aine_id = ?");
        stmt.setInt(1, annos_id);
        stmt.setInt(2, raaka_aine_id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("annos_id"), rs.getInt("raaka_aine_id"), rs.getInt("jarjestys"), rs.getString("maara"));
        
        stmt.close();
        rs.close();

        conn.close();

        return a;
    }
    
    public List<AnnosRaakaAine> findAll() throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine");
        
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("annos_id"), rs.getInt("raaka_aine_id"), rs.getInt("jarjestys"), rs.getString("maara"));
            annosRaakaAineet.add(a);
        }
        rs.close();
        stmt.close();
        conn.close();
        
        return annosRaakaAineet;
    }
    
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine annosRaakaAine) throws SQLException{
		// AnnosRaakaAine-olion annos.id ja raakaAine.id ei ole koskaan null:
		// AnnosRaakaAine-taulussa ei voi olla riviä ilman vastaavia annoksia ja raaka-aineita.
		// Kun tullaan tähän metodiin, on parametrina annetulla oliolla tiedossa annos.id ja raakaAine.id
		// Jos tietokannassa ei ole vastaavaa annosRaakaAinetta, findOne palauttaa null
		
        if (findOne(annosRaakaAine.annosId,annosRaakaAine.raakaAineId) == null) {
            return save(annosRaakaAine);
        } else {
            return update(annosRaakaAine);
        }
    }
    
    public void delete(int annos_id, int raaka_aine_id) throws SQLException{
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ? AND raaka_aine_id = ?");

        stmt.setInt(1, annos_id);
        stmt.setInt(2, raaka_aine_id);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
    
    private AnnosRaakaAine save(AnnosRaakaAine annosRaakaAine) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine"
                + " (annos_id, raaka_aine_id, jarjestys, maara)"
                + " VALUES (?, ?, ?, ?)");
        stmt.setInt(1, annosRaakaAine.getAnnosId());
        stmt.setInt(2, annosRaakaAine.getRaakaAineId());
        stmt.setInt(3, annosRaakaAine.getJarjestys());
        stmt.setString(4, annosRaakaAine.getMaara());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE annos_id = ? AND raaka_aine_id = ? AND jarjestys = ? AND maara = ?");
        stmt.setInt(1, annosRaakaAine.getAnnosId());
        stmt.setInt(2, annosRaakaAine.getRaakaAineId());
        stmt.setInt(3, annosRaakaAine.getJarjestys());
        stmt.setString(4, annosRaakaAine.getMaara());

        ResultSet rs = stmt.executeQuery();
        rs.next(); // vain 1 tulos

        AnnosRaakaAine a = new AnnosRaakaAine(rs.getInt("annos_id"), rs.getInt("raaka_aine_id"), rs.getInt("jarjestys"), rs.getString("maara"));

        stmt.close();
        rs.close();
        conn.close();

        return a;
    }
    
    private AnnosRaakaAine update(AnnosRaakaAine annosRaakaAine) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE AnnosRaakaAine SET "
                + "jarjestys = ?, maara = ? WHERE annos_id = ? AND raaka_aine_id = ?");
        stmt.setInt(1, annosRaakaAine.getJarjestys());      
        stmt.setString(2, annosRaakaAine.getMaara());
        stmt.setInt(4, annosRaakaAine.getAnnosId());
        stmt.setInt(5, annosRaakaAine.getRaakaAineId());
        
        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return annosRaakaAine;
    }
    
    public HashMap<Annos, List<RaakaAine>> etsiRaakaAineet () throws SQLException{
        AnnosDao annosDao = new AnnosDao(database);
        HashMap<Annos, List<RaakaAine>> raakaAineMap = new HashMap<>();
        
        Connection conn = database.getConnection();       
        List<Annos> annokset = annosDao.findAll();     
        
        int i=0;
        
        while(i<annokset.size()){
            
            PreparedStatement stmt = conn.prepareStatement("SELECT RaakaAine.id, RaakaAine.nimi FROM Annos, RaakaAine, AnnosRaakaAine WHERE Annos.nimi = ? AND AnnosRaakaAine.annos_id = Annos.id AND AnnosRaakaAine.raaka_aine_id = RaakaAine.id");
            stmt.setString(1, annokset.get(i).getNimi());

            ResultSet rs = stmt.executeQuery();

            List<RaakaAine> raakaAineet = new ArrayList<>();

            while (rs.next()) {  
                RaakaAine raakaAine = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
                raakaAineet.add(raakaAine);
            }

            raakaAineMap.put(annokset.get(i), raakaAineet);

            rs.close();
            stmt.close();

            i++;
        }
        
        
        
        conn.close();
        
        return raakaAineMap;
    }
}
