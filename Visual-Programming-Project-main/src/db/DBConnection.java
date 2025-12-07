package db;

import java.sql.Connection;
import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.*;


public class DBConnection {
   

    private static final String URL = "jdbc:mysql://srv1816.hstgr.io:3306/u889193302_events_system";
    private static final String USER = "u889193302_azam";
    private static final String PASSWORD = "1234Azam";

   
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

       public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✅ Connected to MySQL!");
            } else {
                System.out.println("❌ Connection failed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
        }
    }
}