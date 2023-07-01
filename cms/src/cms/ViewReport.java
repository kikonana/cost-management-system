package cms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ViewReport {
    private Connection conn;
    private int userId;

    public ViewReport(Connection conn, int userId) {
        this.conn = conn;
        this.userId = userId;
    }

    public void viewReport(Scanner scanner) {
        System.out.println("\nReport View");

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT b.nama_budget, bc.description, bc.amount, e.description, e.amount, (bc.amount - SUM(e.amount)) as sisa_budget "
                    + "FROM budget b "
                    + "INNER JOIN budget_category bc ON b.id_budget = bc.id_budget "
                    + "INNER JOIN expense e ON e.id_budget = b.id_budget "
                    + "WHERE b.userid = " + this.userId
                    + " GROUP BY bc.id_budget, e.id_exp";
            ResultSet rs = stmt.executeQuery(query);

            // Menampilkan header tabel
            System.out.printf("%-30s%-30s%-30s%-30s%-30s%-30s\n", "Budget", "Budget Category", "Budget Amount", "Expense Description", "Expense Amount", "Remaining Budget");

            while (rs.next()) {
                String budget = rs.getString(1);
                String budgetCategory = rs.getString(2);
                double budgetAmount = rs.getDouble(3);
                String expenseDescription = rs.getString(4);
                double expenseAmount = rs.getDouble(5);
                double remainingBudget = rs.getDouble(6);

                System.out.printf("%-30s%-30s%-30.2f%-30s%-30.2f%-30.2f\n", budget, budgetCategory, budgetAmount, expenseDescription, expenseAmount, remainingBudget);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Menampilkan opsi untuk kembali
        System.out.println("\n1. Kembali");
        System.out.print("Pilih opsi (1): ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            viewMenu(scanner);
        }
    }

    private void viewMenu(Scanner scanner) {
        Menu menu = new Menu(conn, userId);
        menu.viewMenu(scanner);
    }
}
