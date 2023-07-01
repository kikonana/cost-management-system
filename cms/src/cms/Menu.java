
package cms;

import java.sql.*;
import java.util.Scanner;

public class Menu {

    private Connection conn;
    private int userId;

    public Menu(Connection conn, int userId) {
        this.conn = conn;
        this.userId = userId;
    }

    public void viewMenu(Scanner scanner) {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("\nSilahkan pilih opsi:");
            System.out.println("1. Income Setting");
            System.out.println("2. Budget Setting"); // we will add this later
            System.out.println("3. Expense Tracking"); // we will add this later
            System.out.println("4. Report View"); // we will add this later
            System.out.println("5. Logout");
            System.out.print("Pilih opsi (1-5): ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Income income = new Income(conn, userId);
                    income.incomeSetting(scanner);
                    break;
                case 2:
                    Budget budget = new Budget(conn, userId);
                    budget.budgetSetting(scanner);
                    break;
                case 3:
                    Expense expense = new Expense(conn, userId);
                    expense.expenseSetting(scanner);
                    break;
                case 4:
                    ViewReport viewReport = new ViewReport(conn, userId);
                    viewReport.viewReport(scanner);
                    break;

                case 5:
                    continueLoop = false;
                    System.out.println("Anda berhasil logout.");
                    break;
                default:
                    System.out.println("Opsi tidak valid. Harap masukkan angka antara 1 dan 5.");
                    break;
            }
        }
    }
}
