package Services;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseManager {
    private static DatabaseManager db;
    private MysqlDataSource dataSource;

    private DatabaseManager() {

    }



    // Konfigurerar kopplingar mot databasen
    private void initializeDatabase() {
        try {
            Properties properties = loadPropertiesFromFile("./src/Services/database.properties");

            dataSource = new MysqlDataSource();
            dataSource.setUser(properties.getProperty("username"));
            dataSource.setPassword(properties.getProperty("password"));
            dataSource.setUrl("jdbc:mysql://" + properties.getProperty("url") + ":" + properties.getProperty("port") + "/" + properties.getProperty("database") +
                    "?serverTimezone=UTC");
            dataSource.setUseSSL(false);
        } catch (IOException e) {
            System.out.println("Failed to load database properties file!");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            printSQLException(e);
            System.exit(0);
        }
    }

    // Skapar en tillfällig koppling till databasen
    private Connection createConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            System.out.println("Failed to connect to database!");
            printSQLException(e);
            System.exit(0);
            return null;
        }
    }

    public static Connection getConnection() {
        if (db == null) {
            db = new DatabaseManager();
            db.initializeDatabase();
        }
        return db.createConnection();
    }

    public static void printSQLException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }

    private Properties loadPropertiesFromFile(String filename) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(filename);
        properties.load(fileInputStream);
        fileInputStream.close();
        return properties;
    }
}