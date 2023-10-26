package Services;

import Models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserService {

    public static void printUserSummary(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Ange user-id");
        int userId = Integer.parseInt(scanner.nextLine());

        User user = getUserById(connection, userId);

        if (user == null) {
            System.out.println("Användaren med det angivna ID:t finns inte.");
            return;
        }

        String query = "SELECT users.user_id, users.name, accounts.account_id, accounts.balance, accounts.account_name FROM users " +
                "LEFT JOIN accounts ON users.user_id = accounts.user_id WHERE users.user_id = ? ORDER BY account_id ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        boolean isFirstRow = true;
        int totalAccounts = 0;
        int totalSaldo = 0;
        while (resultSet.next()) {
            if (isFirstRow) {
                userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("name");
                System.out.println("Användarsummering för användare ID: " + userId);
                System.out.println("Namn: " + userName);
                System.out.println("-----------------------");
                isFirstRow = false;
            }

            int accountId = resultSet.getInt("account_id");
            String accountName = resultSet.getString("account_name");
            float balance = resultSet.getFloat("balance");
            System.out.println("Konto ID: " + accountId + ", " + accountName);
            System.out.println("Saldo: " + balance);
            System.out.println("-----------------------");

            totalAccounts ++;
            totalSaldo += balance;
        }
        System.out.println("Totalt finns det " + totalAccounts + " konton med sammanslaget " + totalSaldo + " kronor");
    }

    public static void updateUserInfo(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Välj med ID vilken användare du vill redigera:");
        List<User> userList = User.getAllUsers(connection);
        for (User user : userList) {
            System.out.println("ID " + user.getUser_id() + ". " + user.getName() + ", personnummer: " + user.getSocial_security_number());
        }

        int chosenUserId = Integer.parseInt(scanner.nextLine());

        User selectedUser = getUserById(connection, chosenUserId);
        if (selectedUser == null) {
            System.out.println("Användaren med det angivna ID:t hittades inte.");
            return;
        }

        boolean showEdits = true;
        while (showEdits) {
            System.out.println("Vad vill du redigera?");
            System.out.println("1. Personnummer: " + selectedUser.getSocial_security_number());
            System.out.println("2. Namn: " + selectedUser.getName());
            System.out.println("3. Email: " + selectedUser.getEmail());
            System.out.println("4. Lösenord: " + selectedUser.getPassword());
            System.out.println("5. Telefonnummer: " + selectedUser.getPhone());
            System.out.println("6. Onlinestatus: " + selectedUser.getOnline());
            System.out.println("Välj ett alternativ (1-6):");

            int menuChoice;
            try {
                menuChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt val. Vänligen ange ett giltigt nummer.");
                continue;
            }

            switch (menuChoice) {
                case 1:
                    System.out.println("Ange det nya personnumret:");
                    long newSSN;
                    try {
                        newSSN = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ogiltigt personnummer-format. Avbryter...");
                        continue;
                    }
                    selectedUser.setSocial_security_number(newSSN);
                    break;
                case 2:
                    System.out.println("Ange det nya namnet:");
                    String newName = scanner.nextLine();
                    selectedUser.setName(newName);
                    break;
                case 3:
                    System.out.println("Ange den nya emailadressen:");
                    String newEmail = scanner.nextLine();
                    selectedUser.setEmail(newEmail);
                    break;
                case 4:
                    System.out.println("Ange det nya lösenordet:");
                    String newPassword = scanner.nextLine();
                    selectedUser.setPassword(newPassword);
                    break;
                case 5:
                    System.out.println("Ange det nya telefonnumret:");
                    String newPhone = scanner.nextLine();
                    selectedUser.setPhone(newPhone);
                    break;
                case 6:
                    System.out.println("Ange den nya onlinestatusen (0/1):");
                    int newOnlineStatus;
                    try {
                        newOnlineStatus = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ogiltig onlinestatus. Avbryter...");
                        continue;
                    }
                    selectedUser.setOnline(newOnlineStatus);
                    break;
                default:
                    System.out.println("Ogiltigt val.");
                    continue;
            }

            updateUser(connection, selectedUser);

            System.out.println("Vill du ändra något mer? (Y/N)");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("N")) {
                showEdits = false;
            }
        }
    }

    public static void updateUser(Connection connection, User selectedUser) throws SQLException {
        String sql = "UPDATE users SET social_security_number = ?, name = ?, email = ?, password = ?, phone = ?, online = ? WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, selectedUser.getSocial_security_number());
        statement.setString(2, selectedUser.getName());
        statement.setString(3, selectedUser.getEmail());
        statement.setString(4, selectedUser.getPassword());
        statement.setString(5, selectedUser.getPhone());
        statement.setInt(6, selectedUser.getOnline());
        statement.setInt(7, selectedUser.getUser_id());
        statement.executeUpdate();
        System.out.println("Användarinformationen har uppdaterats.");
    }

    public static User getUserById(Connection connection, int userId) throws SQLException {
        List<User> userList = User.getAllUsers(connection);
        for (User user : userList) {
            if (user.getUser_id() == userId) {
                return user;
            }
        }
        return null;
    }

    public static void deleteUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Välj med ID vilken användare du vill ta bort:");
        List<User> userList = User.getAllUsers(connection);
        for (User user : userList) {
            System.out.println("ID " + user.getUser_id() + ". " + user.getName() + ", personnummer: " + user.getSocial_security_number());
        }

        int chosenUserId = Integer.parseInt(scanner.nextLine());

        User user = getUserById(connection, chosenUserId);

        if (user == null) {
            System.out.println("Användaren med det angivna ID:t finns inte.");
            return;
        }

        String deleteUser = "DELETE FROM users WHERE user_id = ?;";
        String deleteAccounts = "DELETE FROM accounts WHERE user_id = ?;";

        PreparedStatement userStatement = connection.prepareStatement(deleteUser);
        userStatement.setInt(1, chosenUserId);
        PreparedStatement accountsStatement = connection.prepareStatement(deleteAccounts);
        accountsStatement.setInt(1, chosenUserId);

        int userResult = userStatement.executeUpdate();
        int accountsResult = accountsStatement.executeUpdate();

        if (userResult > 0 && accountsResult > 0) {
            System.out.println("Användaren och dess konton har tagits bort.");
        } else if (userResult > 0) {
            System.out.println("Användaren har tagits bort, men det fanns inga konton kopplade till användaren.");
        } else {
            System.out.println("Kunde inte hitta den angivna användaren.");
        }
    }

    public static void addNewUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Personnummer");
        float socialSecurityNumber = Float.parseFloat(scanner.nextLine());
        System.out.println("Namn:");
        String namn = scanner.nextLine();
        System.out.println("Lösenord");
        String password = scanner.nextLine();
        System.out.println("Email:");
        String email = scanner.nextLine();
        LocalDateTime created = LocalDateTime.now();
        int online = 0;
        System.out.println("Telefon");
        String phone = scanner.nextLine();

        String query = "INSERT INTO users (social_security_number, name, email, phone, password, online, created) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setFloat(1, socialSecurityNumber);
        preparedStatement.setString(2, namn);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, phone);
        preparedStatement.setString(5, PasswordManager.hash(password));
        preparedStatement.setInt(6, online);
        preparedStatement.setTimestamp(7, Timestamp.valueOf(created));
        preparedStatement.executeUpdate();
    }

    public static User logInUser(Connection connection, int socialSecurityNumber, String password) {
        String query = "SELECT password FROM users WHERE social_security_number = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, socialSecurityNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                boolean passwordMatch = PasswordManager.verifyPassword(password, hashedPassword);
                if (passwordMatch) {
                    System.out.println("Login successful!");
                    return getLoggedInUserInfo(socialSecurityNumber, hashedPassword, connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getLoggedInUserInfo(int socialSecurityNumber, String hashedPassword, Connection connection) throws
            SQLException {
        String query = "SELECT * FROM users WHERE social_security_number = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, socialSecurityNumber);
        statement.setString(2, hashedPassword);
        ResultSet result = statement.executeQuery();

        int user_id = 0;
        String email = null;
        String name = null;
        String phone = null;
        LocalDateTime created = null;

        while (result.next()) {
            user_id = result.getInt("user_id");
            name = result.getString("name");
            email = result.getString("email");
            phone = result.getString("phone");
            created = result.getTimestamp("created").toLocalDateTime();
        }
        User user = new User();
        if (user_id != 0) {
            user.setUser_id(user_id);
            user.setSocial_security_number(socialSecurityNumber);
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setOnline(1);
            user.setCreated(created);
            user.setPassword(hashedPassword);
            return user;
        } else {
            return null;
        }
    }
}
