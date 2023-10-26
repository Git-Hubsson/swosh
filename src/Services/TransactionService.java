package Services;

import Models.Accounts;
import Models.User;
import Models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static Services.AccountService.getAccountById;
import static Services.UserService.getUserById;

public class TransactionService {

    public static void sendMoneyAsUser(Connection connection, Scanner scanner, User loggedInUser) throws SQLException {
        System.out.println("Välj med ID från vilket konto du ska skicka pengar");
        List<Accounts> accountsList = Accounts.getAllAccounts(connection);
        boolean accountExists = false;
        for (Accounts account : accountsList) {
            if (account.getUser_id() == loggedInUser.getUser_id()) {
                System.out.println("ID " + account.getAccount_id() + ". " + account.getAccount_name() + ", Balans: " + account.getBalance());
                accountExists = true;
            }
        }

        if (!accountExists){
            System.out.println("Du har inga konton");
            return;
        }

        int selectedSenderAccountId = Integer.parseInt(scanner.nextLine());

        Accounts selectedSenderAccount = getAccountById(connection, selectedSenderAccountId);
        if (selectedSenderAccount == null) {
            System.out.println("Inga konton hittades med det angivna ID:t.");
            return;
        }

        System.out.println("Hur mycket vill du skicka? Det finns " + selectedSenderAccount.getBalance() + " pengar på kontot.");

        float amount;
        try {
            amount = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt belopp-format. Avbryter...");
            return;
        }

        if (amount < 0 || amount > selectedSenderAccount.getBalance()) {
            System.out.println("Du kan inte skicka ett negativt belopp eller ett belopp som överstiger kontots saldo.");
            return;
        }

        System.out.println("Knappa in kontonummer på mottagare");
        int accountNumber;
        try {
            accountNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt kontonummer-format. Avbryter...");
            return;
        }

        Accounts receiverAccount = null;
        for (Accounts account : accountsList) {
            if (account.getAccount_number() == accountNumber) {
                receiverAccount = account;
                break;
            }
        }

        if (receiverAccount == null) {
            System.out.println("Kontot finns inte");
            return;
        }

        float newReceiverBalance = receiverAccount.getBalance() + amount;
        System.out.println(newReceiverBalance);
        updateBalance(connection, newReceiverBalance, accountNumber);

        float newSenderBalance = selectedSenderAccount.getBalance() - amount;
        updateBalance(connection, newSenderBalance, selectedSenderAccount.getAccount_number());

        registerTransaction(connection, selectedSenderAccount.getAccount_id(), receiverAccount.getAccount_id(), amount);
        System.out.println("Transaktionen lyckades!");

    }
    public static void sendMoneyAsAdmin(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Välj med ID vilken användare du ska skicka pengar från");
        List<User> userList = User.getAllUsers(connection);
        for (User user : userList) {
            System.out.println("ID " + user.getUser_id() + ". " + user.getName() + ", personnummer: " + user.getSocial_security_number());
        }
        int senderId = Integer.parseInt(scanner.nextLine());

        User user = getUserById(connection, senderId);

        if (user == null) {
            System.out.println("Användaren med det angivna ID:t finns inte.");
            return;
        }

        System.out.println("Välj med ID från vilket konto du ska skicka pengar");
        List<Accounts> accountsList = Accounts.getAllAccounts(connection);
        boolean senderAccountExists = false;
        for (Accounts account : accountsList) {
            if (account.getUser_id() == senderId) {
                senderAccountExists = true;
                System.out.println("ID " + account.getAccount_id() + ". " + account.getAccount_name() + ", Balans: " + account.getBalance());
            }
        }

        if (!senderAccountExists) {
            System.out.println("Inga konton hittades för den angivna användaren.");
            return;
        }

        int selectedSenderAccountId = Integer.parseInt(scanner.nextLine());

        Accounts selectedSenderAccount = getAccountById(connection, selectedSenderAccountId);
        if (selectedSenderAccount == null) {
            System.out.println("Inga konton hittades med det angivna ID:t.");
            return;
        }

        System.out.println("Hur mycket vill du skicka? Det finns " + selectedSenderAccount.getBalance() + " pengar på kontot.");

        float amount;
        try {
            amount = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt belopp-format. Avbryter...");
            return;
        }

        if (amount < 0 || amount > selectedSenderAccount.getBalance()) {
            System.out.println("Du kan inte skicka ett negativt belopp eller ett belopp som överstiger kontots saldo.");
            return;
        }

        System.out.println("Knappa in kontonummer på mottagare");
        int accountNumber;
        try {
            accountNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt kontonummer-format. Avbryter...");
            return;
        }

        Accounts receiverAccount = null;
        for (Accounts account : accountsList) {
            if (account.getAccount_number() == accountNumber) {
                receiverAccount = account;
                break;
            }
        }

        if (receiverAccount == null) {
            System.out.println("Kontot finns inte");
            return;
        }

        float newReceiverBalance = receiverAccount.getBalance() + amount;
        updateBalance(connection, newReceiverBalance, accountNumber);

        float newSenderBalance = selectedSenderAccount.getBalance() - amount;
        updateBalance(connection, newSenderBalance, selectedSenderAccount.getAccount_number());
        registerTransaction(connection, selectedSenderAccount.getAccount_id(), receiverAccount.getAccount_id(), amount);
        System.out.println("Transaktionen lyckades!");
    }

    public static void updateBalance(Connection connection, float newBalance, long accountNumber) {
        try {
            String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, newBalance);
            preparedStatement.setLong(2, accountNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerTransaction(Connection connection, int sender_account_id, int receiver_account_id, float amount) throws SQLException {
        LocalDateTime datetime = LocalDateTime.now();
        Transaction transaction = new Transaction(sender_account_id, receiver_account_id, amount, datetime);
        String query = "INSERT INTO transactions (sender_account_id, receiver_account_id, amount) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, transaction.getSenderAccountId());
        preparedStatement.setInt(2, transaction.getReceiverAccountId());
        preparedStatement.setFloat(3, transaction.getAmount());
        preparedStatement.executeUpdate();
    }

    public static void getTransactionHistory(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Ange konto-ID:");
        int accountId = Integer.parseInt(scanner.nextLine());

        List<Accounts> accountsList = Accounts.getAllAccounts(connection);
        Accounts account = null;
        for (Accounts accounts : accountsList) {
            if (accounts.getAccount_id() == accountId){
                account = accounts;
                break;
            }
        }

        if (account == null) {
            System.out.println("Kontot med det angivna ID:t finns inte.");
            return;
        }

        System.out.println("Ange startdatum (YYYY-MM-DD):");
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Ogiltigt startdatum-format.");
            return;
        }

        System.out.println("Ange slutdatum (YYYY-MM-DD):");
        LocalDate endDate;
        try {
            endDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Ogiltigt slutdatum-format.");
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        String query = "SELECT * FROM transactions WHERE (sender_account_id = ? OR receiver_account_id = ?)"
                 + " AND datetime BETWEEN ? AND ? ORDER BY datetime";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, accountId);
        preparedStatement.setInt(2, accountId);
        preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(startDateTime));
        preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(endDateTime));

        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("Transaktionshistorik för konto med ID " + accountId + " mellan " + startDate + " och " + endDate + ":");

        while (resultSet.next()) {
            int transactionId = resultSet.getInt("transaction_id");
            int senderId = resultSet.getInt("sender_account_id");
            int receiverId = resultSet.getInt("receiver_account_id");
            float amount = resultSet.getFloat("amount");
            LocalDateTime datetime = resultSet.getTimestamp("datetime").toLocalDateTime();

            Transaction transaction = new Transaction(transactionId, senderId, receiverId, amount, datetime);

            System.out.println("Transaktions-ID: " + transaction.getTransactionId());
            System.out.println("Avsändarens konto-ID: " + transaction.getSenderAccountId());
            System.out.println("Mottagarens konto-ID: " + transaction.getReceiverAccountId());
            System.out.println("Belopp: " + transaction.getAmount());
            System.out.println("Datum och tid: " + transaction.getDatetime());
            System.out.println("-----------------------");
        }
    }
}
