import java.sql.*;

public class DB {
    public static ResultSet doQuery(String query){
        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/dnevnik", "root", "qwerty1234"
            );
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void doUpdate(String update) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/dnevnik", "root", "qwerty1234"
            );
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);  // <-- This is the missing piece!

            // Optional: close resources
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String searchLoginID(String email) throws SQLException {
        String query = "SELECT * FROM login_info;";
        String id = "0";
        ResultSet rs = DB.doQuery(query);
        while (rs.next()){
            if(email.equals(rs.getString("email"))){
                id = rs.getString("id");
            }
        }
        return id;
    }

    public static String searchSubjectID(String subjectName) throws SQLException {
        String query = "SELECT * FROM subjects;";
        String id = "0";
        ResultSet rs = DB.doQuery(query);
        while (rs.next()){
            if(subjectName.equals(rs.getString("name"))){
                id = rs.getString("id");
            }
        }
        return id;
    }

    public static void printQuery(String query) throws SQLException {
        ResultSet rs = DB.doQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData(); // To get column names and count
        int columnCount = rsmd.getColumnCount();
        System.out.println("--------------query results:---------------");
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                String columnValue = rs.getString(i);
                System.out.print(columnName + ": " + columnValue + "  ");
            }
            System.out.println(); // New line after each row
        }
        System.out.println("-------------------------------------------");
    }

    public static void login(String email, String password) throws SQLException {
        String query = "SELECT * FROM login_info";
        ResultSet rs = DB.doQuery(query);
        String role = "none";
        while(rs.next()){
            try {
                if(email.equals(rs.getString("email")) && password.equals(decodePassword(rs.getString("password")))){
                    System.out.println("login successful!");
                    role = rs.getString("role");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(role.equals("none")) System.out.println("login Failed");
        else{
            if(role.equals("student")){
                query = "SELECT * FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = \"student\";";
                rs = DB.doQuery(query);
                while(rs.next()){
                    if(email.equals(rs.getString("email"))){
                        String name = rs.getString("full_name");
                        String fullClass = rs.getString("class_year") + rs.getString("class_letter");
                        System.out.println("Welcome " + name + " from " + fullClass);
                        DBI.studentMenu(name, email, fullClass);
                    }
                }
            }if(role.equals("teacher")){
                query = "SELECT * FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = \"teacher\";";
                rs = DB.doQuery(query);
                while(rs.next()){
                    if(email.equals(rs.getString("email"))){
                        String name = rs.getString("full_name");
                        System.out.println("Welcome " + name);
                        DBI.teacherMenu(name, email);
                    }
                }
            }if(role.equals("director")){
                query = "SELECT * FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = \"director\";";
                rs = DB.doQuery(query);
                while(rs.next()){
                    if(email.equals(rs.getString("email"))){
                        String name = rs.getString("full_name");
                        System.out.println("Welcome " + name);
                        DBI.directorMenu(name, email);
                    }
                }
            }
            if(role.equals("admin")){
                query = "SELECT * FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = \"admin\";";
                rs = DB.doQuery(query);
                while(rs.next()){
                    if(email.equals(rs.getString("email"))){
                        String name = rs.getString("full_name");
                        System.out.println("Welcome " + name);
                        DBI.adminMenu(name, email);
                    }
                }
            }

        }

    }

    public static void register(String vCode, String email, String password, String fullName, String role, int classYear, char classLetter) throws SQLException {
        String id = DB.loginInsert(vCode, email, password, role);
        if(id.equals("0")){
            System.out.println("Something went wrong");
        } else{
            if(role.equals("student")){
                System.out.println("inserting student");
                DB.studentInsert(id, fullName, classYear, classLetter);
            }else{
                if(role.equals("teacher")){
                    System.out.println("inserting teacher");
                    DB.teacherInsert(id, fullName);
                }else if(role.equals("director")) {
                    System.out.println("inserting director");
                    DB.directorInsert(id, fullName);
                }else if(role.equals("admin")) {
                    System.out.println("inserting admin");
                    DB.adminInsert(id, fullName);
                }
            }
        }

    }

    public static String loginInsert(String vCode, String email, String password, String role) throws SQLException {
        password = encodePassword(password);
        String removeVC = "DELETE FROM verification_codes WHERE code = '" + vCode + "';";
        String id = "0", update = "INSERT INTO `dnevnik`.`login_info` (`email`,`password`,`role`) VALUES ('" + email + "', '" + password + "', '" + role + "');";
                                                                                                       //('email', 'password', 'role');
        //check to see if the email is already in the DB
        id = DB.searchLoginID(email);

        if(!id.equals("0")){
            System.out.println("Email already exists");
        }else {
            DB.doUpdate(update);
            id = DB.searchLoginID(email);
            DB.doUpdate(removeVC);
        }

        return id;
    }

    public static void directorInsert(String id, String fullName){
        String update = "INSERT INTO `dnevnik`.`directors` (`id`,`full_name`) VALUES (" + id + ", \"" + fullName + "\")";
        DB.doUpdate(update);
    }

    public static void teacherInsert(String id, String fullName){
        String update = "INSERT INTO `dnevnik`.`teachers` (`id`,`full_name`) VALUES (" + id + ", \"" + fullName + "\")";
        DB.doUpdate(update);
    }

    public static void adminInsert(String id, String fullName){
        String update = "INSERT INTO `dnevnik`.`admins` (`id`,`full_name`) VALUES (" + id + ", \"" + fullName + "\")";
        DB.doUpdate(update);
    }

    public static void studentInsert(String id, String fullName, int classYear, char classLetter) {
        String update = "INSERT INTO `dnevnik`.`students` (`id`,`full_name`,`class_year`,`class_letter`) VALUES (" + id + ",\"" + fullName + "\"," + classYear + ",'" + classLetter + "');";
        DB.doUpdate(update);
    }

    public static String encodePassword(String password){
        return insertCharAt(password, '_', 0);
    }

    public static String decodePassword(String password){
        return removeCharAt(password, 0);
    }

    public static String removeCharAt(String str, int index) {
        if (index < 0 || index >= str.length()) return str;
        return str.substring(0, index) + str.substring(index + 1);
    }

    public static String insertCharAt(String str, char c, int index) {
        if (index < 0 || index > str.length()) return str;
        return str.substring(0, index) + c + str.substring(index);
    }
}


//String query = "SELECT login_info.id, login_info.email, login_info.role, directors.full_name FROM login_info INNER JOIN directors ON login_info.id = directors.id WHERE login_info.role = 'director' UNION ALL SELECT login_info.id, login_info.email, login_info.role, teachers.full_name FROM login_info INNER JOIN teachers ON login_info.id = teachers.id WHERE login_info.role = 'teacher' UNION ALL SELECT login_info.id, login_info.email, login_info.role, students.full_name FROM login_info INNER JOIN students ON login_info.id = students.id WHERE login_info.role = 'student' UNION ALL SELECT login_info.id, login_info.email, login_info.role, admins.full_name FROM login_info INNER JOIN admins ON login_info.id = admins.id WHERE login_info.role = 'admin';";

//        String id;
//
//        // Check if the email already exists in the database
//        String checkQuery = "SELECT COUNT(*) FROM login_info WHERE email = '" + email + "'";
//        ResultSet checkRs = DB.doQuery(checkQuery);
//        checkRs.next();  // Move to the first row of the result set
//        if (checkRs.getInt(1) > 0) {
//            System.out.println("Error: Email already registered.");
//            return "-1"; // Exit early to prevent duplicate insertion
//        }
//
//        String query = "SELECT id FROM dnevnik.login_info WHERE email = '" + email + "';";
//        String update = "INSERT INTO `dnevnik`.`login_info` (`email`,`password`,`role`) VALUES (\"" + email + "\", \"" + password + "\", \"" + role + "\");";
//
//        DB.doUpdate(update);
//
//        // Retrieve the inserted user ID
//        ResultSet rs = DB.doQuery(query);
//        if (rs.next()) {  // ✅ Fix: Ensure the cursor moves to the first row
//            id = rs.getString("id");
//        } else {
//            System.out.println("Error: User ID not found after insertion.");
//            return "0"; // Handle case where no user is found
//        }
//        return id;