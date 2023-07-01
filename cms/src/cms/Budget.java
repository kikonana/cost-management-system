package cms;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Budget {

    private Connection conn;
    private int userId;

    public Budget(Connection conn, int userId) {
        this.conn = conn;
        this.userId = userId;
    }

    public void budgetSetting(Scanner scanner) {
        System.out.println("\nBudget Setting");
        System.out.println("1. Show Category");
        System.out.println("2. Set Budget");
        System.out.println("3. Back");
        System.out.print("Pilih opsi (1-3): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                showBudget(scanner, "home");
                break;
            case 2:
                setBudget(scanner);
                break;
            case 3:
                return;
            default:
                System.out.println("Opsi tidak valid. Harap masukkan 1, 2 atau 3.");
                break;
        }
    }

    public void showBudget(Scanner scanner, String from) {
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

        System.out.println("\nSilahkan pilih kategori budget");
        System.out.print("Input: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear newline
        if (choice > 0 && choice <= budgetMap.size()) {
            int selectedBudgetId = budgetMap.get(choice);
            if (from.equals("set")) {
                setDetailBudget(scanner, selectedBudgetId);
            } else if (from.equals("delete")) {
                deleteBudget(selectedBudgetId, scanner);
            } else {
                detailBudget(selectedBudgetId, scanner);
            }
        }
    }

    public void deleteBudget(int budgetId, Scanner scanner) {
        try {
            // First delete the budget details from the budget_category table
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM budget_category WHERE id_budget = ?");
            stmt.setInt(1, budgetId);
            stmt.executeUpdate();

            // Then delete the budget itself from the budget table
            stmt = conn.prepareStatement("DELETE FROM budget WHERE id_budget = ?");
            stmt.setInt(1, budgetId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Budget dan detail budget berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus budget. ID budget tidak valid atau budget tidak ditemukan.");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println("Gagal menghapus budget dan detail budget.");
            e.printStackTrace();
            showBudget(scanner, "home");
        }
    }

    public void detailBudget(int budgetId, Scanner scanner) {
        Map<Integer, Integer> detailBudgetMap = new HashMap<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM budget_category WHERE id_budget = " + budgetId);

            System.out.println("-----------------------------------------------------------");
            System.out.printf("| %-5s | %-10s | %-30s |\n", "ID", "Amount", "Description");
            System.out.println("-----------------------------------------------------------");

            int i = 1;
            while (rs.next()) {
                int id = rs.getInt("id_budcat");
                detailBudgetMap.put(i, id);
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                System.out.printf("| %-5d | %-10.2f | %-30s |\n", id, amount, description);
                i++;
            }
            System.out.println("-----------------------------------------------------------");
            System.out.println(i + ". Delete detail budget");
            System.out.println((i + 1) + ". Delete kategori & detail budget");
            System.out.println((i + 2) + ". Back");
            rs.close();
            stmt.close();
            rs.close();
            stmt.close();

            System.out.print("\nInput: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            if (choice > 0 && choice <= detailBudgetMap.size()) {
                deleteDetailBudget(detailBudgetMap.get(choice));
            } else if (choice == i) {
                System.out.println("Enter the ID of detail budget to delete:");
                int detailBudgetId = scanner.nextInt();
                scanner.nextLine(); // Clear newline
                deleteDetailBudget(detailBudgetId);
            } else if (choice == i + 1) {
                deleteBudget(budgetId, scanner);
            } else if (choice == i + 2) {
                showBudget(scanner, "home");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDetailBudget(int detailBudgetId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM budget_category WHERE id_budcat = ?");
            stmt.setInt(1, detailBudgetId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Detail budget berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus detail budget. ID detail budget tidak valid atau detail budget tidak ditemukan.");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBudget(Scanner scanner) {
        System.out.println("\nSet Budget");
        System.out.print("Masukkan nama budget: ");
        String nama_budget = scanner.next();

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO budget(userid, nama_budget) VALUES (?, ?)");
            stmt.setInt(1, this.userId);
            stmt.setString(2, nama_budget);
            stmt.executeUpdate();

            System.out.println("Berhasil menambahkan budget baru: " + nama_budget);
            System.out.println("Silahkan tambahkan detail budget:");

            stmt.close();
            showBudget(scanner, "set");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDetailBudget(Scanner scanner, int budgetId) {
        System.out.println("\nSet Detail Budget");
        System.out.print("Masukkan amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Masukkan description: ");
        String description = scanner.next();

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO budget_category(id_budget, amount, description) VALUES (?, ?, ?)");
            stmt.setInt(1, budgetId);
            stmt.setDouble(2, amount);
            stmt.setString(3, description);
            stmt.executeUpdate();

            System.out.println("Berhasil menambahkan detail budget baru");

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
