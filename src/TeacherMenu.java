import java.sql.SQLException;
import java.util.Scanner;

public class TeacherMenu {

    public static void viewTimeTable(){
        int answer = 0;
        Scanner scanner = new Scanner(System.in);
        answer = scanner.nextInt();
        if(answer>0){
            //call view by year
        }else{
            //call view specific class
        }
    }

    public static void grade(){

    }

    public static void takeAttendance(){

    }

    public static void feedbackTS(){

    }

    public static void feedbackST(){

    }

    public static void customQueryE() throws SQLException {
        System.out.println("Enter a query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        DB.printQuery(query);
    }

}
