/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.mvc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PC
 */
public class DbConnection {
    private static DbConnection dbConnection;
    private Connection connection;
    
    private DbConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/pos_mvc", "root", "1234");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static DbConnection getInstance(){
    if(dbConnection == null){
        dbConnection = new DbConnection();
    }
        return dbConnection;
    }

    public Connection getConnection() {
       return connection;
    }
}
