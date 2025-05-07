import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TeacherMenu {


    public static void makeQuery() throws SQLException {
        System.out.println("Enter a query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        DB.printQuery(query);
    }

    public static void viewTimeTable(){
        StudentMenu.timeTable();
    }

    public static void viewClass(String fullClass) throws SQLException {
        int classYear = Integer.parseInt(fullClass.substring(0, fullClass.length() - 1));
        char classLetter = fullClass.charAt(fullClass.length() - 1);
        DB.printQuery("SELECT * FROM students s WHERE s.class_year = '" + classYear + "' AND s.class_letter = '" + classLetter + "';");
    }


    public static void grade(String date, double grade, String subjectName, String studentID, String teacherEmail) throws SQLException {
        String teacherID = DB.searchLoginID(teacherEmail), subjectID = DB.searchSubjectID(subjectName);
        DB.doUpdate("INSERT INTO grades (`date`,`grade`,`subject_id`,`student_id`,`teacher_id`) VALUES ('" + date + "', '" + grade + "', '" + subjectID + "', '" + studentID + "', '" + teacherID + "');");
    }

    public static void takeAttendance(String date) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the class you are having a lesson with: ");
        String fullClass = scanner.nextLine();
        int classYear = Integer.parseInt(fullClass.substring(0, fullClass.length() - 1));
        char classLetter = fullClass.charAt(fullClass.length() - 1);
        DB.printSubjects();
        System.out.println("Enter the subject: ");
        String subjectName = scanner.nextLine();
        String subjectID = DB.searchSubjectID(subjectName);

        // Get the count
        String countQuery = "SELECT COUNT(*) FROM students WHERE class_letter = '" + classLetter + "' AND class_year = " + classYear;
        ResultSet countRs = DB.doQuery(countQuery);
        int count = 0;
        if (countRs.next()) {
            count = countRs.getInt(1);
        }
        if(count > 0){
            // Get the student data
            String studentQuery = "SELECT id, full_name FROM students WHERE class_letter = '" + classLetter + "' AND class_year = " + classYear + " order by id asc";
            ResultSet rs = DB.doQuery(studentQuery);
            int[] idLog = new int[count];
            int i = 0;
            System.out.println("--start--");
            while(rs.next()){
                System.out.println("Is " + rs.getString("full_name") + " with an ID:" + rs.getString("id") + " present? [y/n]: ");
                String answer = scanner.nextLine();
                if(answer.equals("y")){
                    idLog[i] = Integer.parseInt(rs.getString("id"));
                    i++;
                }
            }
            System.out.println("---end---");
            for (int j = 0; j < count; j++) {
                if(idLog[j] != 0){
                    String update = "INSERT INTO attendance (`date`,`student_id`,`subject_id`) VALUES('" + date + "', '" + idLog[j] + "', '" + subjectID + "');";
                    DB.doUpdate(update);
                }
            }
        }

    }

    public static void sendFeedback(String date, String comment, String teacherEmail, String studentID) throws SQLException {
        DB.doUpdate("INSERT INTO comments (`date`,`feedback`,`student_id`,`teacher_id`) VALUES ('" + date + "','" + comment + "','" + studentID + "','" + DB.searchLoginID(teacherEmail) + "');");
    }

    public static void showUsers() throws SQLException {
        String query = "SELECT login_info.id, login_info.email, login_info.role, directors.full_name FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = 'director' UNION ALL SELECT login_info.id, login_info.email, login_info.role, teachers.full_name FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = 'teacher' UNION ALL SELECT login_info.id, login_info.email, login_info.role, students.full_name FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = 'student' UNION ALL SELECT login_info.id, login_info.email, login_info.role, admins.full_name FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = 'admin';";
        DB.printQuery(query);
    }

}
