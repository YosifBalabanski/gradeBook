import java.sql.*;
import java.util.Scanner;

public class DBI {
    public static void loginInterface() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String email, password;
        System.out.println("Enter your email: ");
        email = scanner.nextLine();
        System.out.println("Enter your password: ");
        password = scanner.nextLine();

        DB.login(email, password);
    }

    public static void registerInterface() throws SQLException {

        String vCode, role = "default";
        char classLetter = 0;
        int classYear = 0, i = 1;
        boolean verify = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your verification code: ");
        vCode = scanner.nextLine();

        String query = "SELECT * FROM verification_codes";
        ResultSet rs = DB.doQuery(query);

        try{
            while(rs.next()) {
                if(rs.getString("code").equals(vCode)){
                    verify = true;
                    role = rs.getString("role");
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        if(role.equals("student")){
            while(i>0){
                i--;
                System.out.println("Please enter your class year: ");
                classYear = scanner.nextInt();
                scanner.nextLine();
                if(classYear < 1 || classYear > 12){
                    System.out.println("The year has to be between 1 and 12!!!");
                    i++;
                }
            }
            i = 1;
            while(i>0){
                i--;
                System.out.println("Please enter your class: ");
                classLetter = scanner.next().charAt(0);
                scanner.nextLine();
                if((classLetter == 'a') || (classLetter == 'b') || (classLetter == 'c'));
                else {
                    System.out.println("There are only 3 possible answers(a,b or c)");
                    i++;
                }
            }
            i = 1;
        }

        if(verify){
            System.out.println("Enter your full name: ");
            String fullName = scanner.nextLine();
            System.out.println("Enter your email: ");
            String email = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            String passwordConfirm;
            System.out.println("Please confirm your password: ");
            passwordConfirm = scanner.nextLine();
            if(passwordConfirm.equals(password)) System.out.println("password confirmed");
            else {
                while (i > 0) {
                    System.out.println("incorrect password");
                    i--;
                    System.out.println("Please attempt to confirm your password again: ");
                    passwordConfirm = scanner.nextLine();
                    if(!passwordConfirm.equals(password)) i++;
                }
            }
            DB.register(vCode, email, password,fullName,role, classYear, classLetter);
        }else{
            System.out.println("Your verification code is wrong or doesn't exist");
        }

    }

    public static void adminMenu(String name, String email) throws SQLException {
        System.out.println("Hello " + name + " please administrate well");
        boolean condition = true;

        while(condition){
            System.out.println("---menu---");
            System.out.println("To exit the program, Press 0");
            System.out.println("To make a custom query, Press 1: ");
            System.out.println("To edit the database, Press 2: ");
            System.out.println("To add a user, Press 3");
            System.out.println("To delete a user by id, Press 4: ");
            System.out.println("To delete a user through email, Press 5: ");
            System.out.println("To to create a verification code, Press 6: ");
            System.out.println("To generate verification codes, Press 7: ");
            System.out.println("To show all users, Press 8: ");
            Scanner scanner = new Scanner(System.in);
            System.out.print("answer: ");
            int answer = scanner.nextInt();
            scanner.nextLine();
            System.out.println("----------");
            if(answer == 0){
                System.out.println("Exiting the admin menu");
                condition = false;
            } else if(answer == 1){
                AdminMenu.customQueryInterface();

            } else if (answer == 2) {
                AdminMenu.customUpdateInterface();

            } else if (answer == 3) {
                System.out.println("****////--Start--////****");
                AdminMenu.addUser();
                System.out.println("****////---End---////****");

            } else if (answer == 4) {
                System.out.println("Enter the id of the user you would like to remove: ");
                String idToDelete = scanner.nextLine();
                AdminMenu.removeUserByID(idToDelete);

            } else if (answer == 5) {
                System.out.println("Enter the criteria of the user you would like to remove: ");
                String criteria = scanner.nextLine();
                AdminMenu.removeUserByEmail(criteria);

            } else if (answer == 6) {
                AdminMenu.createVC();

            }else if(answer == 7){
                String roleToGen;
                int numberOfVCs, lengthOfVCs;
                System.out.println("Please enter the role: ");
                roleToGen = scanner.nextLine();
                System.out.println("Please enter the number of codes you would like to generate: ");
                numberOfVCs = scanner.nextInt();
                System.out.println("Please enter the length of each verification code: ");
                lengthOfVCs = scanner.nextInt();
                AdminMenu.generateVCs(roleToGen, numberOfVCs, lengthOfVCs);

            } else if (answer == 8) {
                AdminMenu.showUsers();
            }

        }
    }

    public static void directorMenu(String name, String email){
        System.out.println("you have called the director menu");
        Scanner scanner = new Scanner(System.in);
        boolean condition = true;

        while (condition){
            System.out.println("---menu---");
            System.out.println("To exit the program, Press 0");
            System.out.println("To make a custom query, Press 1: ");
            System.out.println("To view a specific timetable, Press 2: ");
            System.out.println("To view a specific class, Press 3: ");
            System.out.println("To grade a student's work, Press 4: ");
            System.out.println("To take attendance, Press 5: ");
            System.out.println("To comment on a student, Press 6: ");
            System.out.print("answer: ");
            int answer = scanner.nextInt();
            scanner.nextLine();

            System.out.println("----------");

            if (answer == 0){
                condition = false;
            } else if (answer == 1) {

            }
        }
    }

    public static void teacherMenu(String name, String email) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean condition = true;

        while(condition){
            System.out.println("---menu---");
            System.out.println("To exit the program, Press 0");
            System.out.println("To make a custom query, Press 1: ");
            System.out.println("To view a specific timetable, Press 2: ");
            System.out.println("To view a specific class, Press 3: ");
            System.out.println("To grade a student's work, Press 4: ");
            System.out.println("To take attendance, Press 5: ");
            System.out.println("To comment on a student, Press 6: ");
            System.out.print("answer: ");

            int answer = scanner.nextInt();
            scanner.nextLine();

            System.out.println("----------");

            if(answer == 0){
                condition = false;

            } else if (answer == 1) {
                TeacherMenu.customQueryE();

            } else if (answer == 2) {
                TeacherMenu.viewTimeTable();

            } else if (answer == 3) {
                System.out.println("Enter a specific class: ");
                String fullClass = scanner.nextLine();
                TeacherMenu.viewClass(fullClass);

            } else if (answer == 4) {
                System.out.println("Enter the year(YYYY): ");
                String dateYear = scanner.nextLine();
                System.out.println("Enter the year(MM): ");
                String dateMonth = scanner.nextLine();
                System.out.println("Enter the year(DD): ");
                String dateDay = scanner.nextLine();
                String date = dateYear + "-" + dateMonth + "-" + dateDay;
                System.out.println("Enter the subject name: ");
                String subjectName = scanner.nextLine();
                System.out.println("Enter the student's id: ");
                String studentID = scanner.nextLine();
                double grade;
                System.out.println("Enter the grade: ");
                grade = scanner.nextDouble();
                scanner.nextLine();
                TeacherMenu.grade(date, grade, subjectName, studentID, email);

            } else if (answer == 5) {
                System.out.println("Enter the year(YYYY): ");
                String dateYear = scanner.nextLine();
                System.out.println("Enter the year(MM): ");
                String dateMonth = scanner.nextLine();
                System.out.println("Enter the year(DD): ");
                String dateDay = scanner.nextLine();
                String date = dateYear + "-" + dateMonth + "-" + dateDay;
                TeacherMenu.takeAttendance(date);

            } else if (answer == 6) {
                System.out.println("Enter the feedback you wish to send: ");
                String comment = scanner.nextLine();
                System.out.println("Enter the id of the student you wish to send it to: ");
                String studentID = scanner.nextLine();
                System.out.println("Enter the year(YYYY): ");
                String dateYear = scanner.nextLine();
                System.out.println("Enter the year(MM): ");
                String dateMonth = scanner.nextLine();
                System.out.println("Enter the year(DD): ");
                String dateDay = scanner.nextLine();
                String date = dateYear + "-" + dateMonth + "-" + dateDay;
                TeacherMenu.sendFeedback(date, comment, email, studentID);
            }
        }
    }

    public static void studentMenu(String name, String email, String fullClass) throws SQLException {
        System.out.println("Welcome " + name);
        Scanner scanner = new Scanner(System.in);
        boolean condition = true;

        while(condition){
            System.out.println("---menu---");
            System.out.println("To exit the program, Press 0");
            System.out.println("To view your timetable, Press 1: ");
            System.out.println("To view a specific timetable, Press 2: ");
            System.out.println("To view your report card, Press 3: ");
            System.out.println("To view any feedback from your teachers, Press 4: ");
            System.out.println("To view your attendance report, Press 5: ");
            System.out.print("answer: ");

            int answer = scanner.nextInt();
            scanner.nextLine();

            System.out.println("----------");

            if(answer == 0){
                condition = false;

            } else if (answer == 1) {
                StudentMenu.timeTablePersonal(fullClass);

            } else if (answer == 2) {
                StudentMenu.timeTable();

            } else if (answer == 3) {
                StudentMenu.reportCard(email);

            } else if (answer == 4) {
                StudentMenu.viewFeedback(email);

            } else if (answer == 5) {
                StudentMenu.attendanceReport(email);

            }
        }


    }

}
