package Services;

import Models.Accounts;
import Models.User;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class AccountService {

    public static Accounts getAccountById(Connection connection, int accountId) throws SQLException {
        List<Accounts> accountsList = Accounts.getAllAccounts(connection);
        for (Accounts accounts : accountsList) {
            if (accounts.getAccount_id() == accountId) {
                return accounts;
            }
        }
        return null;
    }

    public static void addAccount(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("För vilket användar-ID?");
        List<User> userList = User.getAllUsers(connection);
        for (User user : userList) {
            System.out.println("ID " + user.getUser_id() + ". " + user.getName() + ", personnummer: " + user.getSocial_security_number());
        }

        int userId = Integer.parseInt(scanner.nextLine());

        User user = UserService.getUserById(connection, userId);

        if (user == null) {
            System.out.println("Användaren med det angivna ID:t finns inte.");
            return;
        }

        System.out.println("Kontonamn:");
        String accountName = scanner.nextLine();

        System.out.println("Balans:");
        float balance;
        try {
            balance = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt belopp-format. Avbryter...");
            return;
        }

        int accountNumber = generateUniqueAccountNumber(connection);

        String query = "INSERT INTO accounts (account_number, balance, user_id, account_name) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, accountNumber);
        preparedStatement.setFloat(2, balance);
        preparedStatement.setInt(3, userId);
        preparedStatement.setString(4, accountName);
        preparedStatement.executeUpdate();
        System.out.println("Kontot \"" + accountName + "\" har lagts till.");
    }

    public static int generateUniqueAccountNumber(Connection connection) throws SQLException {
        int randomAccountNumber;

        do {
            randomAccountNumber = (int) Math.floor(Math.random() * 999999999);
        } while (!isAccountNumberUnique(connection, randomAccountNumber));

        return randomAccountNumber;
    }

    private static boolean isAccountNumberUnique(Connection connection, int accountNumber) throws SQLException {
        String query = "SELECT account_number FROM accounts WHERE account_number = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, accountNumber);
        ResultSet resultSet = preparedStatement.executeQuery();

        return !resultSet.next();
    }

    public static void deleteAccountBasedOnUserId(Connection connection, Scanner scanner) throws SQLException {
        String showUsersWithAccountsQuery = "SELECT users.user_id, users.name, users.social_security_number FROM users " +
                "INNER JOIN accounts ON users.user_id = accounts.user_id GROUP BY user_id ORDER BY user_id ASC;";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(showUsersWithAccountsQuery);
        System.out.println("Välj ID för vilken användare du vill radera konto");
        while (resultSet.next()) {
            int userId = resultSet.getInt("user_id");
            String userName = resultSet.getString("name");
            int socialSecurityNumber = resultSet.getInt("social_security_number");
            System.out.println("ID " + userId + ". " + userName + ", personnummer: " + socialSecurityNumber);
        }

        int chosenUserId;
        try {
            chosenUserId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt ID-format. Avbryter...");
            return;
        }

        boolean menu = true;
        while (menu) {
            System.out.println("Välj ID för vilket konto du vill ta bort");
            List<Accounts> accountsList = Accounts.getAllAccounts(connection);
            boolean accountExists = false;
            for (Accounts account : accountsList) {
                if (account.getUser_id() == chosenUserId) {
                    accountExists = true;
                    System.out.println("ID " + account.getAccount_id() + ". " + account.getAccount_name() + ", Balans: " + account.getBalance());
                }
            }

            if (!accountExists) {
                System.out.println("Inga konton hittades för den angivna användaren.");
                break;
            }

            int chosenAccountId;
            try {
                chosenAccountId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt konto-ID-format. Avbryter...");
                return;
            }

            String deleteQuery = "DELETE FROM accounts WHERE account_id = ? AND user_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, chosenAccountId);
            preparedStatement.setInt(2, chosenUserId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Kontot har tagits bort.");
            } else {
                System.out.println("Kunde inte hitta det angivna kontot för den angivna användaren.");
            }

            System.out.println("Vill du ta bort fler konton? Y/N");
            String choice = scanner.nextLine().toUpperCase();
            if (!choice.equals("Y")) {
                menu = false;
            }
        }
    }
}
