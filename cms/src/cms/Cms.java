package cms;

import java.sql.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import koneksi.koneksi;

public class Cms {

    public static void main(String[] args) {
        koneksi dbConn = new koneksi();
        Connection conn = dbConn.connect();

        Scanner scanner = new Scanner(System.in);

        User user = new User(conn);

        System.out.println("Untuk melanjutkan silahkan login atau register");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Pilih opsi (1 atau 2): ");

        try {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (user.login(scanner)) {
                        Menu menu = new Menu(conn, user.getUserId());
                        menu.viewMenu(scanner);
                    }
                    break;
                case 2:
                    user.register(scanner);
                    if (user.login(scanner)) {
                        Menu menu = new Menu(conn, user.getUserId());
                        menu.viewMenu(scanner);
                    }
                    break;
                default:
                    System.out.println("Opsi tidak valid. Harap masukkan 1 atau 2.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Input tidak valid. Harap masukkan angka.");
        } finally {
            scanner.close();
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat menutup koneksi.");
                e.printStackTrace();
            }
        }
    }

}
