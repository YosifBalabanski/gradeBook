
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int answer;
        boolean flag = true;

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

//        int classYear = 12;
//        char classLetter = 'a';
//        String password = "1234";
//        String role = "student";
//        String[] emails = {
//                "example1@gmail.com", "example2@gmail.com", "example3@gmail.com", "example4@gmail.com", "example5@gmail.com", "example6@gmail.com",
//                "example7@gmail.com", "example8@gmail.com", "example9@gmail.com", "example10@gmail.com", "example11@gmail.com", "example12@gmail.com"
//        };
//        String[] vCodes = {
//            "oG7NO", "OwZgL", "eL55L", "bqPKR", "hZZ6x", "76BYL", "z5VMU", "44WpP", "X3HQt", "GohPN", "9nuOg", "qFOCZ"
//        };
//        String[] names = {
//            "Edin Unos","Derik Tus","Lorax Tree","Teren Tetros","Five Guys","Shon Shrekov","Sabrine Sol","Irene Merkel","Nous","Dekan Dekanov","Ori Ori","Yada Yge"
//        };
//
//        for (int i = 0; i < 12; i++) {
//            DB.register(vCodes[i], emails[i], password, names[i], role, classYear, classLetter);
//        }

//        String query = "select * from students where class_year = '" + classYear + "' and class_letter = '" + classLetter + "';";
//        ResultSet rs = DB.doQuery(query), count;
//        query = "select count(*) from students where class_year = '" + classYear + "' and class_letter = '" + classLetter + "';";
//        int n = Integer.parseInt(String.valueOf(count = DB.doQuery(query)));
//        System.out.println(n);
//        String testEmail = "", testPassword = "";
//        DB.login(testEmail, testPassword);