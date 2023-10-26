package Views;

import Models.Accounts;
import Services.AccountService;
import Services.TransactionService;
import Services.UserService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    public static void showMenu(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Tjena Admin!");
        boolean showMenu = true;

        while (showMenu) {
        System.out.println("1. Lägg till användare");
        System.out.println("2. Ta bort användare");
        System.out.println("3. Uppdatera användaruppgifter");
        System.out.println("4. Ta bort konto från användare");
        System.out.println("5. Lägg till konto hos användare");
        System.out.println("6. Skicka pengar mellan en användares konto till en annan användares konto.");
        System.out.println("7. Visa transaktionshistorik för konto");
        System.out.println("8. Visa en summering av en användare, samt dennes konton och belopp.");
        System.out.println("10. Logga ut");

        int menuChoice = Integer.parseInt(scanner.nextLine());

            switch (menuChoice) {
                case 1:
                    UserService.addNewUser(connection, scanner);
                    break;

                case 2:
                    UserService.deleteUser(connection, scanner);
                    break;

                case 3:
                    UserService.updateUserInfo(connection, scanner);
                    break;

                case 4:
                    AccountService.deleteAccountBasedOnUserId(connection, scanner);
                    break;

                case 5:
                    AccountService.addAccount(connection, scanner);
                    break;

                case 6:
                    TransactionService.sendMoneyAsAdmin(connection, scanner);
                    break;

                case 7:
                    TransactionService.getTransactionHistory(connection, scanner);
                    break;

                case 8:
                    UserService.printUserSummary(connection, scanner);
                    break;

                case 10:
                    showMenu = false;
            }
        }
    }
}
