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

        String vCode, role = "default", update;
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
            System.out.println("Enter your name: ");
            String fullName = scanner.nextLine();
            DB.register(email,password,fullName,role, classYear, classLetter);
            String removeVC = "DELETE FROM verification_codes WHERE code = '" + vCode + "';";
            DB.doUpdate(removeVC);
        }else{
            System.out.println("Your verification code is wrong or doesn't exist");
        }

    }

    public static void adminMenu(String name, String email) throws SQLException {
        System.out.println("Hello admin: " + name);
        boolean i = true;
        while(i){
            System.out.println("---menu---");
            System.out.println("To exit the program, Press 0");
            System.out.println("To make a custom query, Press 1: ");
            System.out.println("To edit the database, Press 2: ");
            System.out.println("To delete a user by id, Press 3: ");
            System.out.println("To delete a user through email, Press 4: ");
            System.out.println("To to create a verification code, Press 5: ");
            System.out.println("To generate verification codes, Press 6: ");
            System.out.println("To show all users, Press 7: ");
            Scanner scanner = new Scanner(System.in);
            System.out.print("answer: ");
            int answer = scanner.nextInt();
            System.out.println("----------");
            if(answer == 0){
                System.out.println("Exiting the admin menu");
                i = false;
            } else if(answer == 1){
                AdminMenu.customQueryInterface();

            } else if (answer == 2) {
                AdminMenu.customUpdateInterface();

            } else if (answer == 3) {
                System.out.println("Enter the id of the user you would like to remove: ");
                String idToDelete = scanner.nextLine();
                AdminMenu.removeUserByID(idToDelete);

            } else if (answer == 4) {
                System.out.println("Enter the criteria of the user you would like to remove: ");
                String criteria = scanner.nextLine();
                AdminMenu.removeUserByEmail(criteria);

            } else if (answer == 5) {
                AdminMenu.createVC();

            } else if (answer == 6) {
                String roleToGen;
                int numberOfVCs, lengthOfVCs;
                System.out.println("Please enter the role: ");
                roleToGen = scanner.nextLine();
                System.out.println("Please enter the number of codes you would like to generate: ");
                numberOfVCs = scanner.nextInt();
                System.out.println("Please enter the length of each verification code: ");
                lengthOfVCs = scanner.nextInt();
                AdminMenu.generateVCs(roleToGen, numberOfVCs, lengthOfVCs);

            }else if(answer == 7){
                AdminMenu.showUsers();

            }

        }
    }
    public static void directorMenu(String name, String email){
        System.out.println("you have called the director menu");
    }
    public static void teacherMenu(String name, String email){
        System.out.println("you have called the teacher menu");
    }
    public static void studentMenu(String name, String email, String fullClass){
        System.out.println("you have called the student menu");
    }
}
