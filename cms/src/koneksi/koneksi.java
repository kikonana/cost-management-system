/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;

import java.sql.*;

/**
 *
 * @author Neuron-NB167
 */
public class koneksi {
    public Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/cms_db";
            String user = "root";
            String password = "";

            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database");
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while connecting MySQL database");
            ex.printStackTrace();
        }
        return conn;
    }
}
