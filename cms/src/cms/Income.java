
package cms;

import java.sql.*;
import java.util.Scanner;

public class Income {
    private Connection conn;
    private int userId;

    public Income(Connection conn, int userId) {
        this.conn = conn;
        this.userId = userId;
    }

    public void incomeSetting(Scanner scanner) {
        System.out.println("\nIncome Setting");
        System.out.println("1. Show income");
        System.out.println("2. Add income");
        System.out.println("3. Back");
        System.out.print("Pilih opsi (1-3): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                showIncome(scanner);
                break;
            case 2:
                addIncome(scanner);
                break;
            case 3:
                return;  // return back to the caller
            default:
                System.out.println("Opsi tidak valid. Harap masukkan 1, 2 atau 3.");
                break;
        }
    }

    public void showIncome(Scanner scanner) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM income WHERE userid = " + this.userId);

            System.out.printf("%-30s%-30s%-30s%-30s\n", "ID", "Date", "Description", "Amount");

            while (rs.next()) {
                Date date_inc = rs.getDate("date_inc");
                String description = rs.getString("description");
                String id_inc = rs.getString("id_inc");
                double amount = rs.getDouble("amount");
                System.out.printf("%-30s%-30s%-30s%-30.2f\n", id_inc, date_inc, description, amount);
            }
            rs.close();
            stmt.close();

            System.out.println("\n1. Kembali");
            System.out.println("2. Hapus Data");
            System.out.print("Pilih opsi (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline
            if (choice == 1) {
                incomeSetting(scanner);
            } else if (choice == 2) {
                deleteIncome(scanner);
            } else {
                System.out.println("Opsi tidak valid. Harap masukkan angka 1 atau 2.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIncome(Scanner scanner) {
        System.out.print("Masukkan ID income yang ingin dihapus: ");
        int incomeId = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM income WHERE id_inc = ?");
            stmt.setInt(1, incomeId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Data income berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus data income. ID income tidak valid atau data tidak ditemukan.");
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIncome(Scanner scanner) {
        System.out.print("Masukkan sumber pendapatan Anda (contoh: gaji): ");
        String description = scanner.next();
        System.out.print("Masukkan jumlah pendapatan Anda: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 

        Date date_inc = new Date(new java.util.Date().getTime());

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO income (date_inc, description, amount, userid) VALUES ('" + date_inc + "', '" + description + "', '" + amount + "', " + this.userId + ")");

            stmt.close();
            System.out.println("Income Setting berhasil disimpan.");
            incomeSetting(scanner);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
