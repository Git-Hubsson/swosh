package Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Accounts {
    private int account_id;
    private String account_name;
    private long account_number;
    private float balance;
    private int user_id;

    public Accounts() {

    }

    public static List<Accounts> getAllAccounts(Connection connection) throws SQLException {
        List<Accounts> accountsList = new ArrayList<>();

        String query = "SELECT * FROM accounts";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            Accounts account = new Accounts();
            account.setAccount_id(resultSet.getInt("account_id"));
            account.setAccount_name(resultSet.getString("account_name"));
            account.setAccount_number(resultSet.getLong("account_number"));
            account.setBalance(resultSet.getFloat("balance"));
            account.setUser_id(resultSet.getInt("user_id"));

            accountsList.add(account);
        }

        return accountsList;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public long getAccount_number() {
        return account_number;
    }

    public void setAccount_number(long account_number) {
        this.account_number = account_number;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
