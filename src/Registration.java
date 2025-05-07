import java.sql.SQLException;

public class Registration {
    public static void register(String vCode, String email, String password, String fullName, String role, int classYear, char classLetter) throws SQLException {
        String id = loginInsert(vCode, email, password, role);
        if (id.equals("0")) {
            System.out.println("Something went wrong");
        } else {
            if (role.equals("student")) {
                System.out.println("inserting student");
                studentInsert(id, fullName, classYear, classLetter);
            } else {
                if (role.equals("teacher")) {
                    System.out.println("inserting teacher");
                    teacherInsert(id, fullName);
                } else if (role.equals("director")) {
                    System.out.println("inserting director");
                    directorInsert(id, fullName);
                } else if (role.equals("admin")) {
                    System.out.println("inserting admin");
                    adminInsert(id, fullName);
                }
            }
        }

    }

    public static String loginInsert(String vCode, String email, String password, String role) throws SQLException {
        password = encodePassword(password);
        String removeVC = "DELETE FROM verification_codes WHERE code = '" + vCode + "';";
        String update = "INSERT INTO login_info (`email`,`password`,`role`) VALUES ('" + email + "', '" + password + "', '" + role + "');";
        String id = Core.searchLoginID(email);

        if (!id.equals("0")) {
            System.out.println("Email already exists");
        } else {
            Core.doUpdate(update);
            id = Core.searchLoginID(email);
            Core.doUpdate(removeVC);
        }

        return id;
    }

    public static void directorInsert(String id, String fullName) {
        String update = "INSERT INTO directors (`id`,`full_name`) VALUES (" + id + ", \"" + fullName + "\")";
        Core.doUpdate(update);
    }

    public static void teacherInsert(String id, String fullName) {
        String update = "INSERT INTO teachers (`id`,`full_name`) VALUES (" + id + ", \"" + fullName + "\")";
        Core.doUpdate(update);
    }

    public static void adminInsert(String id, String fullName) {
        String update = "INSERT INTO admins (`id`,`full_name`) VALUES (" + id + ", \"" + fullName + "\")";
        Core.doUpdate(update);
    }

    public static void studentInsert(String id, String fullName, int classYear, char classLetter) {
        String update = "INSERT INTO students (`id`,`full_name`,`class_year`,`class_letter`) VALUES (" + id + ",\"" + fullName + "\"," + classYear + ",'" + classLetter + "');";
        Core.doUpdate(update);
    }

    public static String encodePassword(String password) {
        return insertCharAt(password, '_', 0);
    }

    public static String insertCharAt(String str, char c, int index) {
        if (index < 0 || index > str.length()) return str;
        return str.substring(0, index) + c + str.substring(index);
    }
}
