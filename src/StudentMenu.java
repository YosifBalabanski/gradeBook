import java.sql.SQLException;

public class StudentMenu {

    public static void timeTablePersonal(){

    }

    public static void timeTable(){

    }

    public static void reportCard(String studentIdFinder) throws SQLException {
        String id = DB.searchLoginID(studentIdFinder);
        String query = "SELECT g.date,s.name AS subject,g.grade FROM grades g JOIN subjects s ON g.subject_id = s.id WHERE g.student_id = " + id + ";";
        DB.printQuery(query);
    }

    public static void attendanceReport(){

    }

    public static void sendFeedback(String date,String comment, String teacherIdFinder, String studentIdFinder) throws SQLException {
        String studentID = DB.searchLoginID(studentIdFinder);
        String teacherID = DB.searchLoginID(teacherIdFinder);

        String update = "INSERT INTO `dnevnik`.`comments` (`date`,`feedback`,`student_id`,`teacher_id`) VALUES ('" + date + "','" + comment + "','" + studentID + "','" + teacherID + "');";
        DB.doUpdate(update);
    }

    public static void viewFeedback(String studentIdFinder) throws SQLException {
        String id = DB.searchLoginID(studentIdFinder);
        String query = "SELECT c.date,t.full_name AS teacher,c.feedback FROM comments c JOIN teachers t ON c.teacher_id = t.id WHERE c.student_id = " + id + ";";
        DB.printQuery(query);
    }

}
