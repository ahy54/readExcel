package org.example.step_definitions;

import java.sql.*;

public class getCHatGPT {


    public static void main(String[] args) throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String password = "jfskdfj&h39";
        String user = "somePostgresUser";
        String url = "jdbc:postgresql://192.168.0.115:5432/dvdrental";

        Connection db;
        try {
            db = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM customer");
        while (rs.next()) {
            System.out.print(rs.getString(1)+": ");
            System.out.println(rs.getString(3));
            db.close();

        }


    }

}
