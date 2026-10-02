package BankingManagement;

import jdk.swing.interop.SwingInterOpUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;
    private Scanner scanner;


    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter  Amount : ->  ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your Security_pin : -> ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String credit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number  = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);

                        int rowaffected = preparedStatement1.executeUpdate();
                        if (rowaffected > 0) {
                            System.out.println("Rs." + amount + " Debited successfully from Account number :  -> " + account_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;

                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance ");
                    }
                } else {
                    System.out.println("Invalid pin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter  Amount : ->  ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your Security_pin : -> ");
       String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  balance  FROM accounts WHERE account_number  = ? AND security_pin = ?");
               // preparedStatement.setDouble(1,amount);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {

                    // double current_balance = resultSet.getDouble("balance");
                    String credit_query = "UPDATE accounts SET  balance = balance + ? WHERE  account_number = ?";

                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                  //  preparedStatement.setString(3,security_pin);

                    int row_affected = preparedStatement1.executeUpdate();

                    if (row_affected > 0) {
                        System.out.println("Rs. : " + amount + " Successfully credited  to  Account_number : -> " + account_number);
                        connection.commit();
                        //connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println(" Not credited amount ..  please try again");
                        connection.rollback();
                    }

                } else {
                    System.out.println("Invalid Account number please check the  account number and  try again ....! ");
                }


            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }finally {
            connection.setAutoCommit(true);

        }

    }


    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter the receiver account number : -> ");
        long receiver_account_number = scanner.nextLong();
        System.out.println("Enter the  amount : -> ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the Security pin : -> ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE accounts  SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE  account_number = ? ";
                        PreparedStatement creditpreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);

                        debitpreparedStatement.setDouble(1, amount);
                        debitpreparedStatement.setLong(2, sender_account_number);


                        creditpreparedStatement.setDouble(1, amount);
                        creditpreparedStatement.setLong(2, receiver_account_number);
//                        debitpreparedStatement.setDouble(1,amount);
//                        debitpreparedStatement.setLong(2,sender_account_number);
                        int rowaffected1 = debitpreparedStatement.executeUpdate();
                        int rowaffected2 = creditpreparedStatement.executeUpdate();
                        if (rowaffected1 > 0 && rowaffected2 > 0) {
                            System.out.println("Transaction successfully ... !");
                            System.out.println("Rs.->" + amount + " Transferred successfully..!");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed ..!");
                            //connection.setAutoCommit(true);
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance-!");
                    }

                } else {
                    System.out.println("Invalid security pin ");
                }
            } else {
                System.out.println("Invalid account number");
            }
        } catch (Exception e) {


            connection.setAutoCommit(true);
            e.printStackTrace();
        }finally {

            connection.setAutoCommit(true);

        }
    }

public void getBalance (long account_number){
        scanner.nextLine();
    System.out.println("Enter the security pin : -> ");
    String security_pin = scanner.nextLine();
    try {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
        preparedStatement.setLong(1,account_number);
        preparedStatement.setString(2,security_pin);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            double balance = resultSet.getDouble("balance");
            System.out.println("Balance : " + balance);
        }else {
            System.out.println("Invalid pin please check the pin and try again ...!");


        }
    } catch (SQLException e) {

        e.printStackTrace();
    }
}

}