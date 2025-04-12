import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int answer = -1;
        boolean flag = true;
        String check = "default";

        while(flag){
            System.out.println("to log in press 0: ");
            System.out.println("to register press 1:");
            System.out.print("answer: ");
            answer = scanner.nextInt();
            scanner.nextLine();
            if(answer == 1){
                DBI.registerInterface();
                flag = false;
            }else if(answer == 0){
                DBI.loginInterface();
                flag = false;
            }else {
                System.out.println("Input can only be one or zero try again!");
            }
        }
    }
}
