package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/smart_resume_builder",
                    "root",
                    "root123"
            );

            System.out.println("Database Connected Successfully!");
            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}