import Models.User;
import Services.DatabaseManager;
import Services.SwoshTableCreator;
import Services.UserService;
import Views.AdminView;
import Views.UserView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseManager.getConnection();

        SwoshTableCreator.createTables(connection);

        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;
        while (loggedInUser == null) {
            System.out.println("Ange personnummer: ");
            int socialSecurityNumber = Integer.parseInt(scanner.nextLine());
            System.out.println("Ange lösenord: ");
            String password = scanner.nextLine();
            loggedInUser = UserService.logInUser(connection, socialSecurityNumber, password);

            if (loggedInUser == null) {
                System.out.println("Ogitligt personnummer eller lösenord.");
            }
        }

        if (loggedInUser.getSocial_security_number() == 1) {
            AdminView.showMenu(connection, scanner);
        }
        else if (loggedInUser.getSocial_security_number() > 1) {
            UserView.userMenu(connection, scanner, loggedInUser);
        }
        scanner.close();
        connection.close();
    }
}