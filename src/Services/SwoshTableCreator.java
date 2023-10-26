package Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SwoshTableCreator {
    public static void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // Skapa användartabell
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT PRIMARY KEY AUTO_INCREMENT," +
                "social_security_number BIGINT NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "phone VARCHAR(20) NOT NULL," +
                "online TINYINT NOT NULL," +
                "created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";
        statement.executeUpdate(createUserTableSQL);

        // Skapa kontotabell
        String createAccountTableSQL = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_id INT PRIMARY KEY AUTO_INCREMENT," +
                "user_id INT NOT NULL," +
                "account_number BIGINT NOT NULL," +
                "balance DECIMAL(10, 2) NOT NULL," +
                "account_name varchar(25) NOT NULL" +
                ")";
        statement.executeUpdate(createAccountTableSQL);

        // Skapa transaktionstabell
        String createTransactionTableSQL = "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id INT PRIMARY KEY AUTO_INCREMENT," +
                "sender_account_id INT NOT NULL," +
                "receiver_account_id INT NOT NULL," +
                "amount FLOAT NOT NULL," +
                "datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";
        statement.executeUpdate(createTransactionTableSQL);

        statement.close();
    }
}
