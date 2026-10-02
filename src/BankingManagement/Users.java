package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Users{

    private Connection connection;
    private Scanner  scanner;


    public  Users(Connection connection , Scanner scanner){
        this.connection = connection;
        this.scanner  =  scanner;
    }


    public void registration () {
        scanner.nextLine();
        System.out.println("Enter the full_name :");
        String full_name = scanner.nextLine();
        //System.out.println();
        System.out.println("Enter your Email id : -> ");
        String email = scanner.nextLine();
        System.out.println("Enter your Password : -> ");
        String password = scanner.nextLine();
        if(user_exist(email)){
            System.out.println(" User already use this email try with another Email .....! ");
            return;
        }
        String registration_query =  "INSERT INTO user(full_name,email,password)VALUES(?,?,?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(registration_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int rowset = preparedStatement.executeUpdate();

            if(rowset > 0){
                System.out.println(" Registration completed successfully....!");
            }else {
                System.out.println(" Registration failed....!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String login() {
        scanner.nextLine();
        System.out.println("Enter your Email Id : -> ");
        String email = scanner.nextLine();
        System.out.println("Enter your Password : -> ");
        String Password = scanner.nextLine();

        String login_query = "SELECT * FROM user  WHERE  email = ? AND password = ? ";


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, Password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return email;
            } else {
                return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
            return null;

    }
    public boolean user_exist(String email) {
        String exist_query = "SELECT * FROM user WHERE email = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(exist_query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return  true;
            }else{

                return  false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  false;

    }
}
