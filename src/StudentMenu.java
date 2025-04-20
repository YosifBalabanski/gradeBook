import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudentMenu {

    public static void timeTablePersonal(String fullClass) {
        try {
            // Extract year and letter
            int classYear = Integer.parseInt(fullClass.substring(0, fullClass.length() - 1));
            char classLetter = fullClass.charAt(fullClass.length() - 1);

            // SQL query with concatenated parameters
            String query = """
            SELECT tt.id, d.day_name, tt.period, s.subject_name
            FROM time_tables tt
            JOIN days d ON tt.day_id = d.id
            JOIN subjects s ON tt.subject_id = s.id
            WHERE tt.class_year = %d AND tt.class_letter = '%s'
            ORDER BY d.id, tt.period;
        """.formatted(classYear, classLetter);

            ResultSet rs = DB.doQuery(query);

            // Use LinkedHashMap to preserve order of days
            Map<String, List<String>> timetable = new LinkedHashMap<>();
            String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            // Initialize empty lists for each day
            for (String day : daysOfWeek) {
                timetable.put(day, new ArrayList<>());
            }

            while (rs.next()) {
                String day = rs.getString("day_name");
                int period = rs.getInt("period");
                String subject = rs.getString("subject_name");

                // Add to the correct day list
                timetable.get(day).add(period + ": " + subject);
            }
            System.out.println("--------------");
            System.out.println("Class: " + fullClass + " TT:");
            System.out.println("--------------");
            // Print the full week
            for (String day : daysOfWeek) {
                System.out.println("-- " + day + " --");
                List<String> entries = timetable.get(day);
                if (entries.isEmpty()) {
                    System.out.println("No classes.");
                } else {
                    for (String line : entries) {
                        System.out.println(line);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void timeTable(){
        System.out.println("Enter the class whose schedule you'd like to see: ");
        System.out.print("answer: ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        timeTablePersonal(answer);
    }

    public static void reportCard(String studentEmail) throws SQLException {
        String id = DB.searchLoginID(studentEmail);
        DB.printQuery("SELECT g.date,s.subject_name AS subject,g.grade FROM grades g JOIN subjects s ON g.subject_id = s.id WHERE g.student_id = " + id + ";");
    }


    public static void viewFeedback(String studentEmail) throws SQLException {
        String id = DB.searchLoginID(studentEmail);
        DB.printQuery("SELECT c.date,t.full_name AS teacher,c.feedback FROM comments c JOIN teachers t ON c.teacher_id = t.id WHERE c.student_id = " + id + ";");
    }

    public static void attendanceReport(String studentEmail) throws SQLException {
        String studentID = DB.searchLoginID(studentEmail), query = "select count(*) from subjects;";
        ResultSet rs = DB.doQuery(query);
        int n = 0, i = 0;
        while(rs.next()){
            n = rs.getInt("count(*)");
        }

        int[] subjectIDs = new int[n];
        String[] subjects = new String[n];
        int[] subjectAttendance = new int[n];

        for (i = 1; i <= n; i++) {
            subjectIDs[i - 1] = i;
        }

        i = 0;
        query = "SELECT * FROM subjects ORDER BY id ASC";
        rs = DB.doQuery(query);
        while(rs.next()){
            subjects[i] = rs.getString("name");
            i++;
        }

        for (i = 0; i < n; i++) {
            query = "SELECT COUNT(*) AS attendance_count FROM attendance WHERE student_id = '" + studentID + "' AND subject_id = '" + subjectIDs[i] + "';";
            rs = DB.doQuery(query);
            while(rs.next()){
                subjectAttendance[i] = rs.getInt("count(*)");
            }
        }

        System.out.println("-----------");
        for (i = 0; i < n; i++) {
            System.out.println(subjects[i] + " - " + subjectAttendance[i]);
        }
        System.out.println("-----------");
    }

}
