package cms;

import java.sql.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Expense {

    private Connection conn;
    private int userId;

    public Expense(Connection conn, int userId) {
        this.conn = conn;
        this.userId = userId;
    }

    public void expenseSetting(Scanner scanner) {
        System.out.println("\nExpense Setting");
        System.out.println("1. Show expenses");
        System.out.println("2. Add expense");
        System.out.println("3. Back");
        System.out.print("Pilih opsi (1 atau 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear newline
        switch (choice) {
            case 1:
                showExpenses(scanner);
                break;
            case 2:
                selectBudgetForExpense(scanner);
                break;
            case 3:
                return;
            default:
                System.out.println("Opsi tidak valid. Harap masukkan 1, 2, atau 3.");
                break;
        }
    }

    public void showExpenses(Scanner scanner) {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * "
                    + "FROM expense, budget "
                    + "WHERE expense.id_budget = budget.id_budget AND budget.userid = " + this.userId;
            ResultSet rs = stmt.executeQuery(query);

            // Menampilkan header tabel
            System.out.printf("%-30s%-30s%-30s%-30s\n", "ID", "Date", "Description", "Amount", "Budget");

            while (rs.next()) {
                int expenseId = rs.getInt("id_exp");
                Date date_exp = rs.getDate("date_exp");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                String budget = rs.getString("nama_budget");
                System.out.printf("%-30s%-30s%-30s%-30.2f%-30s\n", expenseId, date_exp, description, amount, budget);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat mengambil data pengeluaran. Silakan coba lagi.");
            e.printStackTrace();
            return;
        }

        // Menampilkan opsi untuk kembali atau menghapus
        System.out.println("\n1. Kembali");
        System.out.println("2. Hapus Data");
        System.out.print("Pilih opsi (1-2): ");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline
        } catch (InputMismatchException e) {
            System.out.println("Masukan tidak valid. Harap masukkan angka 1 atau 2.");
            return;
        }

        switch (choice) {
            case 1:
                expenseSetting(scanner);
                break;
            case 2:
                deleteExpense(scanner);
                break;
            default:
                System.out.println("Opsi tidak valid. Harap masukkan angka 1 atau 2.");
                break;
        }
    }

    public void deleteExpense(Scanner scanner) {
        System.out.print("Masukkan ID expense yang ingin dihapus: ");
        int expenseId = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM expense WHERE id_exp = ?");
            stmt.setInt(1, expenseId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Data expense berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus data expense. ID expense tidak valid atau data tidak ditemukan.");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectBudgetForExpense(Scanner scanner) {
    Map<Integer, Integer> budgetMap = new HashMap<>();
    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id_budget, nama_budget FROM budget WHERE userid = " + this.userId);

        int i = 1;
        while (rs.next()) {
            budgetMap.put(i, rs.getInt("id_budget"));
            System.out.println(i + ". " + rs.getString("nama_budget"));
            i++;
        }

        System.out.println(i + ". Back");
        rs.close();
        stmt.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("\nSilahkan pilih kategori budget untuk pengeluaran");
    System.out.print("Input: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Clear newline

    if (choice > 0 && choice <= budgetMap.size()) {
        addExpense(scanner, budgetMap.get(choice)); // Send id_budget to addExpense
    } else {
        expenseSetting(scanner);
    }
}


    public void addExpense(Scanner scanner, int budgetId) {
        System.out.print("Masukkan deskripsi pengeluaran Anda (contoh: belanja bulanan): ");
        String description = scanner.next();
        System.out.print("Masukkan jumlah pengeluaran Anda: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear newline

        // Mendapatkan tanggal sekarang
        Date date_exp = new Date(new java.util.Date().getTime());

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO expense (date_exp, description, amount, id_budget) VALUES (?, ?, ?, ?)");
            stmt.setDate(1, date_exp);
            stmt.setString(2, description);
            stmt.setDouble(3, amount);
            stmt.setInt(4, budgetId);

            stmt.executeUpdate();

            stmt.close();

            System.out.println("Pengaturan pengeluaran berhasil disimpan.");

            expenseSetting(scanner);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
