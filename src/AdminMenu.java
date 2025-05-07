import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class AdminMenu {

    public static void makeQuery() throws SQLException {
        System.out.println("Enter a query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        Core.printQuery(query);
    }

    public static void makeUpdate(){
        System.out.println("Enter a query: ");
        Scanner scanner = new Scanner(System.in);
        String update = scanner.nextLine();
        Core.doUpdate(update);
    }

    public static void showUsers() throws SQLException {
        String query = "SELECT login_info.id, login_info.email, login_info.role, directors.full_name FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = 'director' UNION ALL SELECT login_info.id, login_info.email, login_info.role, teachers.full_name FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = 'teacher' UNION ALL SELECT login_info.id, login_info.email, login_info.role, students.full_name FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = 'student' UNION ALL SELECT login_info.id, login_info.email, login_info.role, admins.full_name FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = 'admin';";
        Core.printQuery(query);
    }

    public static void addUser() throws SQLException {
        Menus.registerMenu();
    }

    public static void removeUserByID(String id){
        Core.doUpdate("DELETE FROM login_info WHERE id = '" + id + "';");
    }

    public static void removeUserByEmail(String email) throws SQLException {
        removeUserByID(Core.searchLoginID(email));
    }

    public static void createVC(){
        String role, vCode;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a role");
        role = scanner.nextLine();
        System.out.println("Please enter a verification Code");
        vCode = scanner.nextLine();
        Core.doUpdate("INSERT INTO verification_codes (`role`,`code`) VALUES ('" + role + "' , '" + vCode + "');");
    }

    public static void generateVCs(String role, int n, int length){
        String[] vCode = new String[n];
        for (int i = 0; i < n; i++) {
            vCode[i] = generate(length);
        }
        for (int i = 0; i < n; i++) {
            String update = "INSERT INTO verification_codes (`role`,`code`) VALUES ('" + role + "' , '" + vCode[i] + "');";
            Core.doUpdate(update);
        }
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int DEFAULT_LENGTH = 10;

    public static String generate(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
