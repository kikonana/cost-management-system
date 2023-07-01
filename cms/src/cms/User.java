package cms;

import java.sql.*;
import java.util.Scanner;

public class User {
    private int userId;
    private String username;
    private String password;
    private Connection conn;

    public User(Connection conn) {
        this.conn = conn;
    }

    public boolean login(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.next();

        System.out.print("Masukkan password: ");
        String password = scanner.next();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'");

            if (rs.next()) {
                System.out.println("Login berhasil.");
                this.userId = rs.getInt("userid"); // Replace "userid" with the actual column name in your database
                rs.close();
                stmt.close();
                return true;
            } else {
                System.out.println("Login gagal. Username atau password salah.");
                rs.close();
                stmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat melakukan login.");
            e.printStackTrace();
            return false;
        }
    }

    public void register(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.next();

        System.out.print("Masukkan password: ");
        String password = scanner.next();

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO user (username, password) VALUES ('" + username + "', '" + password + "')");
            stmt.close();
            System.out.println("Pendaftaran berhasil! Silahkan login.");
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat melakukan pendaftaran.");
            e.printStackTrace();
        }
    }

    public int getUserId() {
        return this.userId;
    }
}
