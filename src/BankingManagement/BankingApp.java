package BankingManagement;

import jdk.swing.interop.SwingInterOpUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

//    static void main(String[] args) throws ClassNotFoundException, SQLException {

       private    static    final   String url = "jdbc:mysql://localhost:3306/bankingSystem";
        private  static  final     String user = "root";
        private  static  final   String password = "Sonu@1234";





    static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded Successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url,user,password);
            System.out.println("Connection Established Successfully");
            Scanner scanner = new Scanner(System.in);

            Users users = new Users(connection,scanner);
            AccountManager accountManager = new AccountManager(connection,scanner);
            Accounts accounts  = new Accounts(connection,scanner);


            String email;
            long account_number;

            while(true) {
                System.out.println("!--** WELCOME TO L&T BANKING SYSTEM.**--!");
                System.out.println();
                System.out.println("1. Register .... ");
                System.out.println("2. Login .... ");
                System.out.println("3. Exit....");
                System.out.println("Enter your Choice :   ");

                int choice1 = scanner.nextInt();
                scanner.nextLine();
                switch (choice1) {
                    case 1:
                        users.registration();
                        break;
                    case 2:
                        email = users.login();
                        if (email != null) {

                            System.out.println();
                            System.out.println("user logged In ...");
                            if (!accounts.account_exist(email)) {
                                System.out.println("1.  Open a new Bank account...");
                                System.out.println("2. Exit ");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account creation successfully...");
                                    System.out.println("Your Account number is : " + account_number);
                                } else {

                                    break;
                                }

                            }
                                account_number = accounts.getAccount_number(email);
                                int choice2 = 0;
                                while (choice2 != 5) {
                                    System.out.println();
                                    System.out.println("1.Debit Money...");
                                    System.out.println("2. credit Money ...");
                                    System.out.println("3. Transfer Money ...");
                                    System.out.println("4. Check  Money ...");
                                    System.out.println("5. Log out");
                                    System.out.println("Enter Your Choice : -> ");
                                    choice2 = scanner.nextInt();

                                    switch (choice2) {
                                        case 1:
                                            accountManager.debit_money(account_number);
                                            break;

                                        case 2:
                                            accountManager.credit_money(account_number);
                                            break;

                                        case 3:
                                            accountManager.transfer_money(account_number);
                                            break;

                                        case 4:
                                            accountManager.getBalance(account_number);
                                            break;
                                        case 5:
                                            System.out.println("Logged out successfully..");
                                            break;
                                        default:
                                            System.out.println("Enter your choice : -> ");
                                            break;
                                    }

                                }
                            }else {
                            System.out.println("Incorrect Email or password");
                        }


                    case 3:
                        System.out.println("THANK FOR VISITING  MY BANKING SYSTEM ...!");
                        System.out.println("Exiting From Banking System ...!");
                        return;

                    default:
                        System.out.println("Enter the valid Choice ...");
                        break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
