package Views;

import Models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static Services.TransactionService.sendMoneyAsUser;

public class UserView {
    public static void userMenu(Connection connection, Scanner scanner, User loggedInUser) throws SQLException {
        boolean menu = true;

        while (menu) {
            System.out.println("1. Skicka pengar till angivet mottagarkonto.");
            System.out.println("10. Logga ut!");

            int menuChoice = Integer.parseInt(scanner.nextLine());

            switch (menuChoice) {
                case 1:
                    sendMoneyAsUser(connection, scanner, loggedInUser);
                    break;

                case 10:
                    menu = false;
            }

        }
    }
}
