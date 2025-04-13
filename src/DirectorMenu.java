import java.sql.SQLException;
import java.util.Scanner;

public class DirectorMenu {

    public static void makeTimeTable(){
        // input for which class
        // loop that goes from monday to friday
        // asks how many lessons per the day
        // then you give it a list by typing a string of numbers
        // it decodes the string of numbers and gives you a complete set of timetable entries
        // at the end it prints the timetable
    }

    public static void customQueryE() throws SQLException {
        System.out.println("Enter a query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        DB.printQuery(query);
    }

    public static void showUsers() throws SQLException {
        String query = "SELECT login_info.id, login_info.email, login_info.role, directors.full_name FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = 'director' UNION ALL SELECT login_info.id, login_info.email, login_info.role, teachers.full_name FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = 'teacher' UNION ALL SELECT login_info.id, login_info.email, login_info.role, students.full_name FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = 'student' UNION ALL SELECT login_info.id, login_info.email, login_info.role, admins.full_name FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = 'admin';";
        DB.printQuery(query);
    }

    public static void createVC(){
        String role, vCode;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a role");
        role = scanner.nextLine();
        System.out.println("Please enter a verification Code");
        vCode = scanner.nextLine();
        String update = "INSERT INTO `dnevnik`.`verification_codes` (`role`,`code`) VALUES ('" + role + ", '" + vCode + "');";
        DB.doUpdate(update);
    }

    public static void checkFeedback(boolean choice){

    }

}
