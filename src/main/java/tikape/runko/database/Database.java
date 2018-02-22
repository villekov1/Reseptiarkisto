package tikape.runko.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    public Database() throws SQLException {
        
    }

    public static Connection getConnection() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            try {
                return DriverManager.getConnection(dbUrl);
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            return DriverManager.getConnection("jdbc:sqlite:reseptit.db");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        

    }
}
