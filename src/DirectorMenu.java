import java.sql.SQLException;
import java.util.Scanner;

public class DirectorMenu {

    public static void customQueryE() throws SQLException {
        System.out.println("Enter a query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        Core.printQuery(query);
    }

    public static void showUsers() throws SQLException {
        String query = "SELECT login_info.id, login_info.email, login_info.role, directors.full_name FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = 'director' UNION ALL SELECT login_info.id, login_info.email, login_info.role, teachers.full_name FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = 'teacher' UNION ALL SELECT login_info.id, login_info.email, login_info.role, students.full_name FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = 'student' UNION ALL SELECT login_info.id, login_info.email, login_info.role, admins.full_name FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = 'admin';";
        Core.printQuery(query);
    }

    public static void checkFeedback(String searchID) throws SQLException {
        String query = "select c.date, c.feedback, s.full_name as student, t.full_name as teacher from comments c join teachers t on c.teacher_id = t.id join students s on c.student_id = s.id where c.student_id = '" + searchID + "' or c.teacher_id = '" + searchID + "';";
        Core.printQuery(query);
    }

    public static void viewTimeTable(){
        StudentMenu.timeTable();
    }

    public static void removeTimeTableEntry(String ttEntryToRemoveID){
        Core.doUpdate("delete from time_tables where id = '" + ttEntryToRemoveID + "';");
    }

    public static void makeTimeTableEntry() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the specific class:");
        String fullClass = scanner.nextLine();
        int classYear = Integer.parseInt(fullClass.substring(0, fullClass.length() - 1));
        char classLetter = fullClass.charAt(fullClass.length() - 1);
        Core.printQuery("select id, day_name from days order by id asc;");
        System.out.println("Enter the day id: ");
        String dayID = scanner.nextLine();
        System.out.println("Enter the period(1-8): ");
        String period = scanner.nextLine();
        Core.printQuery("select id, subject_name from subjects;");
        System.out.println("Enter the subject ID for this period: ");
        int subjectID = scanner.nextInt();
        scanner.nextInt();
        Core.printQuery("select t.id as tid, t.full_name as name, s.name as subject from teachers t join teacher_subjects ts on ts.teacher_id = t.id join subjects s on s.id = ts.subject_id;");
        System.out.println("Enter the id of the teacher for this lesson: ");
        String teacherID = scanner.nextLine();

        String update = "INSERT INTO time_tables (`period`,`class_year`,`class_letter`,`subject_id`,`day_id`,`teacher_id`) VALUES('" + period + "','" + classYear + "','" + classLetter + "','" + subjectID + "','" + dayID + "','" + teacherID + "');";
        Core.doUpdate(update);
    }

    // Needs polishing
    public static void makeTimeTable() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the specific class:");
        String fullClass = scanner.nextLine();
        int classYear = Integer.parseInt(fullClass.substring(0, fullClass.length() - 1));
        char classLetter = fullClass.charAt(fullClass.length() - 1);
        System.out.println("Enter the number of days they will be attending school:");
        int nOfSchoolDays = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < nOfSchoolDays; i++) {
            System.out.println("Enter the number of periods:");
            int periodCount = scanner.nextInt();
            scanner.nextLine();
            for (int j = 0; j < periodCount; j++) {
                Core.printSubjects();
                System.out.println("Enter the subject ID for this period: ");
                int subjectID = scanner.nextInt();
                scanner.nextLine();
                Core.printQuery("select t.id as tid, t.full_name as name, s.subject_name as subject from teachers t join teacher_subjects ts on ts.teacher_id = t.id join subjects s on s.id = ts.subject_id;");
                System.out.println("Enter the id of the teacher for this lesson: ");
                String teacherID = scanner.nextLine();
                String update = "INSERT INTO time_tables (`period`,`class_year`,`class_letter`,`subject_id`,`day_id`,`teacher_id`) VALUES('" + (j + 1) + "','" + classYear + "','" + classLetter + "','" + subjectID + "','" + (i + 1) + "','" + teacherID + "');";
                Core.doUpdate(update);
            }
        }

    }

    public static void createVC(){
        String role, vCode;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a role");
        role = scanner.nextLine();
        System.out.println("Please enter a verification Code");
        vCode = scanner.nextLine();
        String update = "INSERT INTO verification_codes (`role`,`code`) VALUES ('" + role + ", '" + vCode + "');";
        Core.doUpdate(update);
    }

    public static void assignTeacherToSubject() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the teacher's id: ");
        String teacherID = scanner.nextLine();
        System.out.println("Enter the subject name: ");
        String subjectName = scanner.nextLine();
        String subjectID = Core.searchSubjectID(subjectName);
        String update = "INSERT INTO teacher_subjects (`teacher_id`,`subject_id`) VALUES('" + teacherID + "','" + subjectID + "');";
        Core.doUpdate(update);
    }

}
