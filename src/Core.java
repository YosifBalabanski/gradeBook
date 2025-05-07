import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/GradeBook";
    private static final String USER = "root";
    private static final String PASSWORD = "qwerty1234";
    private static final Logger LOGGER = Logger.getLogger(Core.class.getName());

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet doQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Query failed: " + query, e);
        }
        return null;
    }

    public static void doUpdate(String update) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);

            statement.close();
            connection.close();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Update failed: " + update, e);
        }
    }

    public static String searchLoginID(String email) throws SQLException {
        String query = "SELECT * FROM login_info;";
        String id = "0";
        try (ResultSet rs = doQuery(query)) {
            while (rs.next()) {
                if (email.equals(rs.getString("email"))) {
                    id = rs.getString("id");
                }
            }
        }
        return id;
    }

    public static String searchSubjectID(String subjectName) throws SQLException {
        String query = "SELECT * FROM subjects;";
        String id = "0";
        try (ResultSet rs = doQuery(query)) {
            while (rs.next()) {
                if (subjectName.equals(rs.getString("subject_name"))) {
                    id = rs.getString("id");
                }
            }
        }
        return id;
    }

    public static void printQuery(String query) throws SQLException {
        try (ResultSet rs = doQuery(query)) {
            ResultSetMetaData rsmd = rs.getMetaData(); // To get column names and count
            int columnCount = rsmd.getColumnCount();
            System.out.println("--------------query results:---------------");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnLabel(i); // Use getColumnLabel!
                    String columnValue = rs.getString(i);
                    System.out.print(columnName + ": " + columnValue + "  ");
                }
                System.out.println(); // New line after each row
            }
            System.out.println("-------------------------------------------");
        }

    }

    public static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData(); // To get column names and count
        int columnCount = rsmd.getColumnCount();
        System.out.println("--------------query results:---------------");
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnLabel(i); // Use getColumnLabel!
                String columnValue = rs.getString(i);
                System.out.print(columnName + ": " + columnValue + "  ");
            }
            System.out.println(); // New line after each row
        }
        System.out.println("-------------------------------------------");
    }

    public static void printSubjects() throws SQLException {
        printQuery("SELECT id, subject_name FROM subjects ORDER BY id ASC");
    }

    public static void printTeachers() throws SQLException {
        printQuery("SELECT id, full_name FROM teachers ORDER BY id ASC");
    }





}