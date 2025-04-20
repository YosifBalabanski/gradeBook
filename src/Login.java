import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    public static void login(String email, String password) throws SQLException {
        String query = "SELECT * FROM login_info";
        ResultSet rs = DB.doQuery(query);
        String role = "none";
        while (rs.next()) {
            try {
                if (email.equals(rs.getString("email")) && password.equals(decodePassword(rs.getString("password")))) {
                    System.out.println("login successful!");
                    role = rs.getString("role");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (role.equals("none")) System.out.println("login Failed");
        else {
            if (role.equals("student")) {
                query = "SELECT * FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = \"student\";";
                rs = DB.doQuery(query);
                while (rs.next()) {
                    if (email.equals(rs.getString("email"))) {
                        String name = rs.getString("full_name");
                        String fullClass = rs.getString("class_year") + rs.getString("class_letter");
                        DBI.studentMenu(name, email, fullClass);
                    }
                }
            }
            if (role.equals("teacher")) {
                query = "SELECT * FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = \"teacher\";";
                rs = DB.doQuery(query);
                while (rs.next()) {
                    if (email.equals(rs.getString("email"))) {
                        String name = rs.getString("full_name");
                        DBI.teacherMenu(name, email);
                    }
                }
            }
            if (role.equals("director")) {
                query = "SELECT * FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = \"director\";";
                rs = DB.doQuery(query);
                while (rs.next()) {
                    if (email.equals(rs.getString("email"))) {
                        String name = rs.getString("full_name");
                        DBI.directorMenu(name, email);
                    }
                }
            }
            if (role.equals("admin")) {
                query = "SELECT * FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = \"admin\";";
                rs = DB.doQuery(query);
                while (rs.next()) {
                    if (email.equals(rs.getString("email"))) {
                        String name = rs.getString("full_name");
                        DBI.adminMenu(name, email);
                    }
                }
            }

        }

    }

    public static String decodePassword(String password) {
        return removeCharAt(password, 0);
    }

    public static String removeCharAt(String str, int index) {
        if (index < 0 || index >= str.length()) return str;
        return str.substring(0, index) + str.substring(index + 1);
    }
}
